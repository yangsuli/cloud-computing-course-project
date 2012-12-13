
package org.apache.hadoop.util;

import java.net.*;
import java.io.*; 


public class ADGSetSocketTOS{ 

    static {
        System.loadLibrary("setsocktos");
    }

    //JNI
    public static native int setSocketTOS(Socket socket);

    /*
       public static void main(String[ ] args){ 
       try{ 
       InetAddress acceptorHost = InetAddress.getByName(args[0]); 
       int serverPortNum = Integer.parseInt(args[1]);        
       InetSocketAddress sSA = new InetSocketAddress(acceptorHost, serverPortNum);


       byte[] a = new byte[]{127, 0, 0, 1};
       InetAddress cIA = InetAddress.getByAddress(a);
    //System.out.println("client IP: "+cIA.getHostAddress());
    InetSocketAddress cSA = new InetSocketAddress(cIA, 0);




    Socket clientSocket = new Socket();
    clientSocket.bind(cSA);

    try
    {
        clientSocket.setTrafficClass(8);

        //JNI
        int fid = new connectionClient().setSocketTOS(clientSocket);
        System.out.println("fid returned is: "+fid);


    }
    catch(SocketException exp) {

        System.out.println(exp);

    }
    //Socket clientSocket = new Socket(acceptorHost, serverPortNum, cIA, 9999); 
    clientSocket.connect(sSA);

    InetAddress clientIA = clientSocket.getLocalAddress();
    String clientIP = clientIA.getHostAddress();

    System.out.println("client IP: "+clientIP);

    BufferedReader br = new BufferedReader(new 
            InputStreamReader(clientSocket.getInputStream( ))); 
    System.out.println(br.readLine( )); 


    System.out.println("ToS:"+clientSocket.getTrafficClass());

    clientSocket.close();        
       } 
catch(Exception e){ 
    e.printStackTrace( ); 
} 
       }
       */
}
