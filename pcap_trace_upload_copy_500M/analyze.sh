for file in `ls *pcap`
do
	#python check_ToS.py $file
	python ../parse/parse_tcp_packets.py $file > ${file}_missed_packet.txt
	mv tcp.txt ${file}_tcp.txt
	mv ts_size.txt ${file}_ts_size.txt
done
