def ParsePkts(line):
    time_stamps = []
    total_size = 0
    line = line.replace('[', ' ')
    line = line.replace('(', ' ')
    line = line.replace(',', ' ')
    line = line.replace(']', ' ')
    line = line.strip()
    packets = line.split(')')
    for packet in packets:
        ts_size = packet.split()
        if len(ts_size) != 2:
            break
        size = int (ts_size[1])
        if size == 0:
            continue
        total_size += size
        #when calculating flow duration time
        #we do not count in those packets which has 0 payload size
        #thus we have some error as not counting flow setting up and tearing down (as SYN packets etc. have 0 payload size
        time_stamps.append(float(ts_size[0]))
    return [total_size, time_stamps]




def main():
    fi = open("ts_size.txt")
    while True:
        line=fi.readline()
        if line=="":
            break
        print line.strip()
        #forward packets line
        forward_flow = fi.readline()
        forward_size_ts = ParsePkts(forward_flow)
        #backward packets line
        backward_flow = fi.readline()
        backward_size_ts = ParsePkts(backward_flow)
        total_size = forward_size_ts[0] + backward_size_ts[0]
        print "flow total size: ", total_size
        if total_size == 0:
            continue
        merged_ts = forward_size_ts[1] + backward_size_ts[1]
        merged_ts.sort()
        #print "time stamp list", merged_ts
        print "flow duration time: ", merged_ts[len(merged_ts) - 1] - merged_ts[0]
        print "\n"
main()
