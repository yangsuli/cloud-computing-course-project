
package org.apache.hadoop.util;

import java.net.*;
import java.io.*; 


public class ADGSetSocketTOS{ 

    static {
        System.loadLibrary("setsocktos");
    }

    //JNI
    public static native int setSocketTOS(Socket socket, int flow_type);
    public static native int setServerSocketTOS(ServerSocket socket, int flow_type);
    public static native SocketImpl getImpl(Socket socket);
    public static native org.mortbay.jetty.Connector getConnectorFromResponse(javax.servlet.http.HttpServletResponse response);
    public static native java.nio.channels.ServerSocketChannel getChannelFromConnector(org.mortbay.jetty.nio.SelectChannelConnector connector);
    public static native Socket getSocketFromHttpConnection(sun.net.www.protocol.http.HttpURLConnection connection);

}
