import dpkt
import sys
import socket

FIN_PACKET = 999

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
	if tcp.dport == 389 or tcp.sport == 389: #LDAP traffic
		return None

	if socket.inet_ntoa(ip.src) == "10.10.101.4" or socket.inet_ntoa(ip.dst) == "10.10.101.4":
		return None
	if socket.inet_ntoa(ip.src) == "10.10.101.2" or socket.inet_ntoa(ip.dst) == "10.10.101.2":
		return None
	if tcp.dport == 22 or tcp.sport ==22:
		# throw out all ssh traffic for now
		# because we have bugs parsing them
		# apprently we lost a lot of ssh traffic
		# yangsuli 12/23/2012
		return None

			
	data_len1 = len(tcp.data)
	data_len2 = int(ip.len - 4*(ip.v_hl & 0x0f) - 4*((tcp.off_x2 & 0xf0) >> 4))
	data_len3 = int(ip.len)

	SYN = tcp.flags & 0x02
	FIN = tcp.flags & 0x01
	if FIN == dpkt.tcp.TH_FIN:
		return (ip.dst, ip.src, tcp.dport, tcp.sport, data_len3, FIN_PACKET)

	#if data_len2 > 1448:
		#print "data length > 1448:", data_len2,  "src IP:", socket.inet_ntoa(ip.src), "src port:", tcp.sport, "dst IP:", socket.inet_ntoa(ip.dst), "dst port:", tcp.dport, "TCP flags:", binB(tcp.flags)

	if ip.tos == 0:
		if SYN == dpkt.tcp.TH_SYN:
			return None #"We don't count tcp setup/tearing down traffic"
		elif FIN == dpkt.tcp.TH_FIN:
			#return (ip.dst, ip.src, tcp.dport, tcp.sport, data_len3, FIN_PACKET)
			raise Exception("Should see FIN here!!!")
		else:
			if tcp.dport != 22 and tcp.sport != 22:
				print "Tos == 0 but not SYN or FIN, with data length:", data_len2, "src IP:", socket.inet_ntoa(ip.src), "src port:", tcp.sport, "dst IP:", socket.inet_ntoa(ip.dst), "dst port:", tcp.dport, "TCP flags:", binB(tcp.flags)
	
	if tcp.dport == 22 or tcp.sport ==22:
		flow_type = 99
		# throw out all ssh traffic for now
		# because we have bugs parsing them
		# apprently we lost a lot of ssh traffic
		# yangsuli 12/23/2012
		return None
	else:
		flow_type = ip.tos

	return (ip.dst, ip.src, tcp.dport, tcp.sport, data_len3, flow_type)
   except Exception as e:
    	print "process exception", e
	return None

class CFlow:
# sIP, dIP, sPort, dPort,
# duration, volume,
# f_size_min, f_size_avg, f_size_max, f_pkt_num, f_inter_min, f_inter_avg, f_inter_max
    def __init__(self, sip, cip, sport, cport, ts, plen, flow_type):
        self.serverIP=sip
        self.clientIP=cip
        self.serverPort=sport
        self.clientPort=cport
        self.forward=[]
        self.backward=[]
        self.flow_type={flow_type:plen}
        self.backward.append( (ts, plen, flow_type ))
    def __str__(self):
        return (socket.inet_ntoa(self.serverIP), socket.inet_ntoa(self.clientIP), self.serverPort, self.clientPort).__str__()+"\n"
    def ts_size_info(self):
        return self.forward.__str__()+"\n"+self.backward.__str__()+"\n"
    def type_info(self):
        return "traffic type: coresponding size "+self.flow_type.__str__()+"\n"
    def GetNewPacket(self, pktTuple):
        srcIP=pktTuple[0]
        dstIP=pktTuple[1]
        times=pktTuple[2]
        ip_len=pktTuple[3]
        flow_type = pktTuple[4]
        if srcIP==self.serverIP:
            self.forward.append( (times, ip_len, flow_type))
        else:
            self.backward.append((times, ip_len, flow_type))
        if flow_type not in self.flow_type:
            self.flow_type[flow_type]=ip_len
        else:
            self.flow_type[flow_type]+=ip_len
    def total_size(self):
	total_size = 0
	for key in self.flow_type:
		total_size += self.flow_type[key]
	return total_size
    def __hash__(self):
        return hash( (serverIP, clientIP, serverPort, clientPort))
    def __eq__(self, other):
        return (self.serverIP, self.clientIP, self.serverPort, self.clientPort) == (other.serverIP, other.clientIP, other.serverPort, other.clientPort) or (self.serverIP, self.clientIP, self.serverPort, self.clientPort) == (other.clientIP, other.serverIP, other.clientPort, other.serverPort)

class Complete_CFlow:
        def __init__(self, cflow, fin_time):
                self.cflow = cflow
                self.fin_time = fin_time
		self.unfinalized = False
                #self.forward_btime = self.cflow.forward[0][0]

		if fin_time is None:
			final_ts = 0
			backward = self.cflow.backward
			if len(backward) > 0:
				final_ts = backward[len(backward) - 1][0]
			forward = self.cflow.forward
			if len(forward) > 0:
				if final_ts > forward[len(forward) - 1][0]:
					final_ts = forward[len(forward) - 1][0]
			self.fin_time = final_ts
			self.unfinalized = True

	def __str__(self):
		if self.unfinalized == False:
			return self.cflow.__str__() + "BEGIN_TIME: " + self.begin_time().__str__() + " FIN TIME: " + self.fin_time.__str__() + "\n"
		else:
			return self.cflow.__str__() + "BEGIN_TIME: " + self.begin_time().__str__() + " (Unfinalized) END TIME: " + self.fin_time.__str__() + "\n"
			
        def __hash__(self):
		return hash(self.cflow)
	def __eq__(self, other):
		return self.cflow == other.cflow
	def begin_time(self):
		begin_ts = 0
		backward = self.cflow.backward
		if len(backward)>0 :
			begin_ts = backward[0][0]
		forward = self.cflow.forward
		if len(forward) > 0:
			if begin_ts > forward[0][0]:
				begin_ts = forward[0][0]
		if begin_ts == 0:
			raise Exception("A flow with no packets!!!!")
		return begin_ts
			


def main():
    flows=[] #"currently active flows"
    finalized_flows=[] #"flows that are finalized"
    total_type = {}
    fi=open(sys.argv[1])
    pcap=dpkt.pcap.Reader(fi)
    try:
        #Note: here I am assuming ts is in strictly increasing order
	#hopefully this is true
	#yangsuli 12/22/2012
        for ts, buf in pcap:
	    packet = processPacket(buf)
	    if packet is None:
	    	continue
	
	    dst = packet[0]
	    src = packet[1]
	    dport = packet[2]
	    sport = packet[3]
	    ip_len = packet[4]
            flow_type=packet[5]

	    if flow_type == FIN_PACKET:
		#print "process one FIN packet"
		# One flow finalized
            	f=CFlow(dst, src, dport, sport, ts, ip_len, flow_type)
		if f not in flows:
			#print "fin packets for flow " + f.__str__() + " not in flows"
			#do nothing here
			#as for one flow we will have tow FIN packets
			#print "Yangsuli"
			#print f.__str__()
			#raise Exception("finalize a flow which we haven't seen before!")
			pass
	        else:
			ind = flows.index(f)
			#print "remove flow: ", flows[ind].__str__(), " from flows"
		        #print "ind = ", ind, "and len(flows) = ", len(flows)
			finalized_flows.append(Complete_CFlow(flows[ind], ts))
		        #print "this point2!"
			# keep the finalized flow in flows pool
			# so that we don't falsely start a new flow when we get the final ACK packet
			#del flows[ind]
			continue

	    if flow_type not in total_type:
		total_type[flow_type] = ip_len
	    else:
                total_type[flow_type] += ip_len


            f=CFlow(dst, src, dport, sport, ts, ip_len, flow_type)
            if f in flows:
                ind=flows.index(f)
                flows[ind].GetNewPacket((src, dst, ts, ip_len, flow_type))
            else:
		#print "add flow to flows: ", f.__str__()
                flows.append(f)
    except Exception as e:
        print "exception", e
    fi.close()
    fi=open("tcp.txt",'w')
    f2=open("ts_size.txt",'w')

    for f in flows:
        #fi.write(f.__str__())
        #fi.write(f.type_info())
        #fi.write("\n")
        #f2.write(f.__str__())
	#f2.write(f.type_info())
        #f2.write(f.ts_size_info())
	#finalized_flows.append(Complete_CFlow(f, None))
	cf = Complete_CFlow(f, None)
	if cf not in finalized_flows:
		print "Unfinalzied Flow!!!"
		print f.__str__()
		finalized_flows.append(Complete_CFlow(f,None))
    #fi.write("total tos: ");
    #fi.write(total_type.__str__())
    #fi.write("\n")

    for f in finalized_flows:
        fi.write(f.__str__())
        fi.write(f.cflow.type_info())
	fi.write("duration time: " + (f.fin_time - f.begin_time()).__str__() + "\n")
	fi.write("total size: " + f.cflow.total_size().__str__() + "\n")
        fi.write("\n")
        f2.write(f.__str__())
	f2.write(f.cflow.type_info())
        f2.write(f.cflow.ts_size_info())
    fi.write("total tos: ");
    fi.write(total_type.__str__())
    fi.close()
    f2.close()
main()

