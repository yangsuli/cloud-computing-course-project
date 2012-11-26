/*
 * This class contains all the information we use to describle each kind of traffic
 * yangsuli 11/25/2012
 */

package org.apache.hadoop.hdfs.util;

import javax.net.SocketFactory;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;


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







	        public ADGTrafficDesc(byte type){
			this.type = type;
		}
		private byte type;
	}


	public static boolean ADGSetSocketTrafficType(Socket sock, ADGTrafficDesc desc){
	
		//ADG TODO:
		//call into the ioctl of sock, set the appropriate field
		//yangsuli 11/25/2012
		return true;
	}
}
