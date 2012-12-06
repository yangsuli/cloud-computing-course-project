/*
 * This class contains all the information we use to describle each kind of traffic
 * yangsuli 11/25/2012
 */

package org.apache.hadoop.util;

import javax.net.SocketFactory;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.lang.String;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class ADGTrafficTrace {

     public static final Log LOG = LogFactory.getLog(ADGTrafficTrace.class);

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
    public static boolean ADGSetRPCSendTrafficType(Socket sock, String method, String tag){
        //FIXME:
        //implementing it
        LOG.info("ADG RPC Request Method (Tag: " + tag + "): "+method);        
        return true;
    }

    public static boolean ADGSetRPCResponseTrafficType(Socket sock, String method, String tag){
        //FIXME:
        //implementing it
        LOG.info("ADG RPC Respond Method (Tag: " + tag + "): "+method);        
        return true;
    }

	public static boolean ADGSetSocketTrafficType(Socket sock, ADGTrafficDesc desc){
	
        return ADGSetSocketTrafficType(sock, desc, "DefaultSetTrafficTAG");
		//ADG TODO:
		//call into the ioctl of sock, set the appropriate field
        //TODO:
        //invoke ioctl system call on sock
        //in tcp_ioctl function implementation
        //you need to add a SET_SOCKET_TYPE command
        //and inside that command, what you need to do is to set a field in the struct sock
        //then you need to modify tcp_transmit_skb (tcp layer) or ip_queue_xmit (ip layer) in their header construting process
        //You need consult the correspoinding sock struct's field, 
		//yangsuli 11/25/2012
	}

	public static boolean ADGSetSocketTrafficType(Socket sock, ADGTrafficDesc desc, String tag){
        LOG.info("ADG Method (Tag: " + tag + "):  ADGSetSocketTrafficType(Socket sock, ADGTrafficDesc desc)");        
        return true;
    }
}
