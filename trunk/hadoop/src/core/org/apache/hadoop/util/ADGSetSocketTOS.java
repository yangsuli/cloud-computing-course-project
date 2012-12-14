
package org.apache.hadoop.util;

import java.net.*;
import java.io.*; 


public class ADGSetSocketTOS{ 

    static {
        System.loadLibrary("setsocktos");
    }

    //JNI
    public static native int setSocketTOS(Socket socket, int flow_type);
    public static native SocketImpl getImpl(Socket socket);

}
