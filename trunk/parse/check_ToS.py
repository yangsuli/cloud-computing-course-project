#!/usr/bin/python

import dpkt
import sys
import socket

#8 bits
def binB(x): 
	return ''.join(x & (1 << i) and '1' or '0' for i in	range(7,-1,-1))

#16 bits
def binH(x): 
	return ''.join(x & (1 << i) and '1' or '0' for i in	range(15,-1,-1))
	
if __name__ == '__main__':
	f = open(sys.argv[1])
	pcap = dpkt.pcap.Reader(f)

	for ts, buf in pcap:
			eth = dpkt.ethernet.Ethernet(buf)
			
#			print "Ethernet type:0x%x"%(eth.type), "0x%x"%(dpkt.ethernet.ETH_TYPE_IP)
			if eth.type == dpkt.ethernet.ETH_TYPE_IP:
				pass
			else:
				print "Ethernet type is not IP: 0x%x"%(eth.type)
				continue
			
			ip = eth.data
			
			if ip.p == dpkt.ip.IP_PROTO_TCP:
				pass
			else:
				print "protocol is not TCP:", ip.p
				continue
				
			if (ip.off & 0x1fff) != 0: #"Frag. offset"
				print "Frag. offset not 0:", ip.off & 0x1fff
			
			tcp = ip.data
			
			if tcp.dport == 54311 or tcp.sport == 54311:
				continue			
				
			data_len1 = len(tcp.data)
			data_len2 = int(ip.len - 4*(ip.v_hl & 0x0f) - 4*((tcp.off_x2 & 0xf0) >> 4))
			
			'''
			if data_len1 != data_len2:
				print "data length computation error:", len(tcp.data), "!=", int(ip.len - 4*(ip.v_hl & 0x0f) - 4*((tcp.off_x2 & 0xf0) >> 4)), "$", ip.len , 4*(ip.v_hl & 0x0f) , 4*((tcp.off_x2 & 0xf0) >> 4)
				break
			'''
			
			if data_len2 > 1448:
				print "data length > 1448:", data_len2, "ts:", ts, "src IP:", socket.inet_ntoa(ip.src), "src port:", tcp.sport, "dst IP:", socket.inet_ntoa(ip.dst), "dst port:", tcp.dport, "TCP flags:", binB(tcp.flags)
				break
			
			if data_len2 == 0:
				continue
			
			if ip.tos == 0:
				SYN = tcp.flags & 0x02
				FIN = tcp.flags & 0x01

				if SYN == dpkt.tcp.TH_SYN:
					continue
				elif FIN == dpkt.tcp.TH_FIN:
					continue
				else:
					print "Tos == 0 but not SYN or FIN, with data length:", data_len2, "src IP:", socket.inet_ntoa(ip.src), "src port:", tcp.sport, "dst IP:", socket.inet_ntoa(ip.dst), "dst port:", tcp.dport, "TCP flags:", binB(tcp.flags)

	f.close()
