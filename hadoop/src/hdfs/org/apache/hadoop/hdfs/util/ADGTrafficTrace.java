/*
 * This class contains all the information we use to describle each kind of traffic
 * yangsuli 11/25/2012
 */

package org.apache.hadoop.hdfs.util;

import javax.net.SocketFactory;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.lang.String;


public class ADGTrafficTrace {
	public static class ADGTrafficDesc {
		/*
		 * These two share the same flow
		 * refer to DFSClient.java BlockReader related code
		 */
		public static final byte TRAFFIC_READ_DATA_ACK = (byte) 11;
		public static final byte TRAFFIC_READ_DATA_REQUEST = (byte) 12;

		/*
		 * The following share a socket(flow)
		 */
		public static final byte TRAFFIC_READ_DATA_BLOCK = (byte) 13;

		/*
		 * The following share a socket(flow)
		 */
        public static final byte TRAFFIC_WRITE_DATA_ACK = (byte) 14;


        /*
         * The following share a socket (flow)
         * It is the client-to-first-datanode transfer when client writes a block
         */
        public static final byte TRAFFIC_WRITE_CLIENT_DATA_HEADER = (byte) 15;
        public static final byte TRAFFIC_WRITE_CLIENT_DATA_PACKETS = (byte) 15;

        /*
         * The following share a socket (flow)
         * It is the pipelined data transfer when client writes a block
         */
        public static final byte TRAFFIC_WRITE_PIPELINE_DATA_HEADER = (byte) 17;
        public static final byte TRAFFIC_WRITE_PIPELINE_DATA_PACKETS = (byte) 18;







	        public ADGTrafficDesc(byte type){
			this.type = type;
		}
		private byte type;
	}

        //ADG
        //For the RPC style communication
        //internally, it should aslo call ADGSetSocketTrafficType(sock, desc).
        //Just needs to determine traffic type according to method name
        //SO it is easer for whoever calls it.
        //yangsuli 11/30/2012
    public static boolean ADGSetRPCSendTrafficType(Socket sock, String method){
        //FIXME:
        //implementing it
        return true;
    }

    public static boolean ADGSetRPCResponseTrafficType(Socket sock, String method){
        //FIXME:
        //implementing it
        return true;
    }

	public static boolean ADGSetSocketTrafficType(Socket sock, ADGTrafficDesc desc){
	
		//ADG TODO:
		//call into the ioctl of sock, set the appropriate field
		//yangsuli 11/25/2012
		return true;
	}
}
