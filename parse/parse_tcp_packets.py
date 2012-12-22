import dpkt
import sys
import socket


#8 bits
def binB(x): 
	return ''.join(x & (1 << i) and '1' or '0' for i in	range(7,-1,-1))


#16 bits
def binH(x): 
	return ''.join(x & (1 << i) and '1' or '0' for i in	range(15,-1,-1))

def processPacket(buf):
   try:
	eth = dpkt.ethernet.Ethernet(buf)
	if eth.type == dpkt.ethernet.ETH_TYPE_IP:
		pass
	else:
		print "Ethernet type is not IP: 0x%x"%(eth.type)
		return None

	ip = eth.data
	if ip.p == dpkt.ip.IP_PROTO_TCP:
		pass
	else:
		print "protocol is not TCP:", ip.p
		return None

	if (ip.off & 0x1fff) != 0: #"Frag. offset"
		print "Frag. offset not 0:", ip.off & 0x1ffff

	tcp = ip.data

        #ignore jobtracker traffic
        #FXIME: we really need a way to eliminate jobtracker traffic...
	if tcp.dport == 54311 or tcp.sport == 54311: #"Hadoop Job Traffic, not HDFS traffic"
		return None

	data_len1 = len(tcp.data)
	data_len2 = int(ip.len - 4*(ip.v_hl & 0x0f) - 4*((tcp.off_x2 & 0xf0) >> 4))
	data_len3 = int(ip.len)

	if data_len2 > 1448:
		print "data length > 1448:", data_len2,  "src IP:", socket.inet_ntoa(ip.src), "src port:", tcp.sport, "dst IP:", socket.inet_ntoa(ip.dst), "dst port:", tcp.dport, "TCP flags:", binB(tcp.flags)

	if ip.tos == 0:
		SYN = tcp.flags & 0x02
		FIN = tcp.flags & 0x01
		if SYN == dpkt.tcp.TH_SYN:
			return None #"We don't count tcp setup/tearing down traffic"
		elif FIN == dpkt.tcp.TH_FIN:
			return None
		else:
			if tcp.dport != 22 and tcp.sport != 22:
				print "Tos == 0 but not SYN or FIN, with data length:", data_len2, "src IP:", socket.inet_ntoa(ip.src), "src port:", tcp.sport, "dst IP:", socket.inet_ntoa(ip.dst), "dst port:", tcp.dport, "TCP flags:", binB(tcp.flags)
	
	if tcp.dport == 22 or tcp.sport ==22:
		type = 99
	else:
		type = ip.tos

	return (ip.dst, ip.src, tcp.dport, tcp.sport, data_len3, type)
   except Exception as e:
    	print "exception", e

class CFlow:
# sIP, dIP, sPort, dPort,
# duration, volume,
# f_size_min, f_size_avg, f_size_max, f_pkt_num, f_inter_min, f_inter_avg, f_inter_max
    def __init__(self, sip, cip, sport, cport, ts, plen, type):
        self.serverIP=sip
        self.clientIP=cip
        self.serverPort=sport
        self.clientPort=cport
        self.forward=[]
        self.backward=[]
        self.type={type:plen}
        self.backward.append( (ts, plen, type ))
    def __str__(self):
        return (socket.inet_ntoa(self.serverIP), socket.inet_ntoa(self.clientIP), self.serverPort, self.clientPort).__str__()+"\n"
    def ts_size_info(self):
        return self.forward.__str__()+"\n"+self.backward.__str__()+"\n"
    def type_info(self):
        return "packet size: coresponding size "+self.type.__str__()+"\n"
    def GetNewPacket(self, pktTuple):
        srcIP=pktTuple[0]
        dstIP=pktTuple[1]
        times=pktTuple[2]
        ip_len=pktTuple[3]
        type=pktTuple[4]
        if srcIP==self.serverIP:
            self.forward.append( (times, ip_len))
        else:
            self.backward.append((times, ip_len))
        if type not in self.type:
            self.tos[tos]=ip_len
        else:
            self.tos[tos]+=ip_len
    def __hash__(self):
        return hash( (serverIP, clientIP, serverPort, clientPort))
    def __eq__(self, other):
        return (self.serverIP, self.clientIP, self.serverPort, self.clientPort) == (other.serverIP, other.clientIP, other.serverPort, other.clientPort) or (self.serverIP, self.clientIP, self.serverPort, self.clientPort) == (other.clientIP, other.serverIP, other.clientPort, other.serverPort)


def main():
    flows=[]
    total_tos = {}
    fi=open(sys.argv[1])
    pcap=dpkt.pcap.Reader(fi)
    try:
        for ts, buf in pcap:
	    packet = processPacket(buf)
	    if packet is None:
	    	continue
	
	    dst = packet[0]
	    src = packet[1]
	    dport = packet[2]
	    sport = packet[3]
	    ip_len = packet[4]
            type=packet[5]
            total_tos[type] += ip_len

            f=CFlow(dst, src, dport, sport, ts, ip_len, type)
            if f in flows:
                ind=flows.index(f)
                flows[ind].GetNewPacket((src, dst, ts, ip_len, type))
            else:
                flows.append(f)
    except Exception as e:
        print "exception", e
    fi.close()
    fi=open("tcp.txt",'w')
    f2=open("ts_size.txt",'w')

    for f in flows:
        fi.write(f.__str__())
        fi.write(f.tos_info())
        fi.write("\n")
        f2.write(f.__str__())
        f2.write(f.ts_size_info())
    fi.write("total tos: ");
    fi.write(total_tos.__str__())
    fi.close()
    f2.close()
main()

