import dpkt
import sys
import socket
class CFlow:
# sIP, dIP, sPort, dPort,
# duration, volume,
# f_size_min, f_size_avg, f_size_max, f_pkt_num, f_inter_min, f_inter_avg, f_inter_max
    def __init__(self, sip, cip, sport, cport, ts, plen, tos):
        self.serverIP=sip
        self.clientIP=cip
        self.serverPort=sport
        self.clientPort=cport
        self.forward=[]
        self.backward=[]
        self.tos={tos:plen}
        self.backward.append( (ts, plen, tos ))
    def __str__(self):
        return (socket.inet_ntoa(self.serverIP), socket.inet_ntoa(self.clientIP), self.serverPort, self.clientPort).__str__()+"\n"
    def ts_size_info(self):
        return self.forward.__str__()+"\n"+self.backward.__str__()+"\n"
    def tos_info(self):
        return "tos filed: coresponding size "+self.tos.__str__()+"\n"
    def GetNewPacket(self, pktTuple):
        srcIP=pktTuple[0]
        dstIP=pktTuple[1]
        times=pktTuple[2]
        payl=pktTuple[3]
        tos=pktTuple[4]
        if srcIP==self.serverIP:
            self.forward.append( (times, payl))
        else:
            self.backward.append((times, payl))
        if tos not in self.tos:
            self.tos[tos]=payl
        else:
            self.tos[tos]+=payl
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
            eth=dpkt.ethernet.Ethernet(buf)
            if type(eth.data)!=dpkt.ip.IP:
                continue
            ip=eth.data
            if type(ip.data)!=dpkt.tcp.TCP:
                print "not tcp"
                continue
            tcp=ip.data
            tos=ip.tos
            total_tos[tos] += len(tcp.data)
            #ignore 0 payload size packet
            #I am not quite sure whether I should do that though...
            if len(tcp.data) == 0:
                continue
            #ignore jobtracker traffic
            #FXIME: we really need a way to eliminate jobtracker traffic...
            if tcp.dport == 54311 or tcp.sport == 54311 :
                continue
            f=CFlow(ip.dst,ip.src,tcp.dport, tcp.sport, ts, len(tcp.data), tos)
            if f in flows:
                ind=flows.index(f)
                flows[ind].GetNewPacket((ip.src, ip.dst, ts, len(tcp.data), tos))
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

