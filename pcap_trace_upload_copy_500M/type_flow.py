import re
import sys
import os
import subprocess



#str = "traffic type: coresponding size {0: 104, 32: 67802403, 36: 358095"
#all_type1 = re.findall(r'(\d+):', str)
#all_type = [int(d) for d in all_type1 if (d != "0")]
#print all_type


def processOneFile(tcp_file):
	subprocess.call(['rm', '-rf', tcp_file+'_type'])
	subprocess.call(['mkdir', tcp_file+'_type'])
	flow_pool = {}
	fi = open(tcp_file)
	print "open ", tcp_file
	while True:
		line=fi.readline()
		if not line:
			break
		if not line.strip():
			continue
		if "total" in line:
			continue
		#process a flow:
		flow_des = line
		flow_time = fi.readline()
		flow_type_size = fi.readline()
		flow_dur_time = fi.readline()
		flow_total_size = fi.readline()
		flow_type1 = re.findall(r'(\d+):', flow_type_size)
		flow_type = tuple([int(d) for d in flow_type1 if (d != "0")])
		if flow_type in flow_pool:
			flow_pool[flow_type].append((flow_des, flow_time, flow_type_size, flow_dur_time, flow_total_size))
		else:
			flow_pool[flow_type] = [(flow_des, flow_time, flow_type_size, flow_dur_time, flow_total_size)]
	for key in flow_pool:
		fi = open(tcp_file + '_type/' + str(key[0]) + '_' + str(key[1]), 'w')
		#fi.write(key.__str__())
		#fi.write("\n") 
		#fi.write("\n")
		for flow in flow_pool[key]:
			for line in flow:
				fi.write(line)
			fi.write("\n")
		fi.close()
	return flow_pool

def main():
	subprocess.call(['rm', '-rf', 'total_type'])
	subprocess.call(['mkdir', 'total_type'])
	file_list = sys.argv
	del file_list[0]
	total_pool = {}
	for tcp_file in file_list:
		one_pool = processOneFile(tcp_file)
		if len(one_pool) == 0:
			continue
		for key in one_pool:
			print "yangsuli: " , tcp_file, key
			if key in total_pool:
				total_pool[key].extend(one_pool[key])
			else:
				total_pool[key] = one_pool[key]

	for key in total_pool:
		fi = open("total_type/" + str(key[0]) + '_' + str(key[1]),'w')
		for flow in total_pool[key]:
			for line in flow:
				print line
				fi.write(line)
			fi.write("\n")
		fi.close()
main()
		
