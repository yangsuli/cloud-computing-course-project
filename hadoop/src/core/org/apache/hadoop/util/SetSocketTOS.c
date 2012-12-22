#include <jni.h>
#include "org_apache_hadoop_util_ADGSetSocketTOS.h"

#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <netdb.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <arpa/inet.h>


JNIEXPORT jobject Java_org_apache_hadoop_util_ADGSetSocketTOS_getChannelFromConnector(JNIEnv * env, jclass obj, jobject connector)
{
    JNIEnv e = *env;
    jclass clazz;
    jfieldID fid;
    jobject impl;
    jobject fdesc;

    if (!(clazz = e->GetObjectClass(env,connector)) ||
        !(fid = e->GetFieldID(env,clazz,"_acceptChannel","Ljava/nio/channels/ServerSocketChannel;")) ||
        !(impl = e->GetObjectField(env,impl,fid))) return NULL;

    return impl;

}



JNIEXPORT jobject Java_org_apache_hadoop_util_ADGSetSocketTOS_getSocketFromHttpConnection(JNIEnv * env, jclass obj, jobject connection)
{
    JNIEnv e = *env;
    jclass clazz;
    jfieldID fid;
    jobject impl;
    jobject fdesc;

    if (!(clazz = e->GetObjectClass(env,connection)) ||
        !(fid = e->GetFieldID(env,clazz,"http","Lsun/net/www/http/HttpClient;")) ||
        !(impl = e->GetObjectField(env,connection,fid))) return NULL;

    if (!(clazz = e->GetObjectClass(env,impl)) ||
        !(fid = e->GetFieldID(env,clazz,"serverSocket","Ljava/net/Socket;")) ||
        !(impl = e->GetObjectField(env,impl,fid))) return NULL;

    return impl;

}

JNIEXPORT jobject Java_org_apache_hadoop_util_ADGSetSocketTOS_getConnectorFromResponse(JNIEnv * env, jclass obj, jobject response)
{
    JNIEnv e = *env;
    jclass clazz;
    jfieldID fid;
    jobject impl;
    jobject fdesc;

    if (!(clazz = e->GetObjectClass(env,response)) ||
        !(fid = e->GetFieldID(env,clazz,"_connection","Lorg/mortbay/jetty/HttpConnection;")) ||
        !(impl = e->GetObjectField(env,response,fid))) return NULL;

    if (!(clazz = e->GetObjectClass(env,impl)) ||
        !(fid = e->GetFieldID(env,clazz,"_connector","Lorg/mortbay/jetty/Connector;")) ||
        !(impl = e->GetObjectField(env,impl,fid))) return NULL;

    return impl;

}


JNIEXPORT jobject Java_org_apache_hadoop_util_ADGSetSocketTOS_getImpl(JNIEnv * env, jclass obj, jobject sock)
{
    JNIEnv e = *env;
    jclass clazz;
    jfieldID fid;
    jobject impl;
    jobject fdesc;

    if (!(clazz = e->GetObjectClass(env,sock)) ||
        !(fid = e->GetFieldID(env,clazz,"impl","Ljava/net/SocketImpl;")) ||
        !(impl = e->GetObjectField(env,sock,fid))) return NULL;

    return impl;



}
static int getFd(JNIEnv *env, jobject sock)
{
    JNIEnv e = *env;
    jclass clazz;
    jfieldID fid;
    jobject impl;
    jobject fdesc;

    /* get the SocketImpl from the Socket */
    if (!(clazz = e->GetObjectClass(env,sock)) ||
        //!(fid = e->GetFieldID(env,clazz,"impl","Ljava/net/SocketImpl;")) ||
        !(fid = e->GetFieldID(env,clazz,"sc","Lsun/nio/ch/SocketChannelImpl;")) ||
        !(impl = e->GetObjectField(env,sock,fid))) return -1;


    /* get the FileDescriptor from the SocketImpl */
    if (!(clazz = e->GetObjectClass(env,impl)) ||
        !(fid = e->GetFieldID(env,clazz,"fd","Ljava/io/FileDescriptor;")) ||
        !(fdesc = e->GetObjectField(env,impl,fid))) return -1;


    /* get the fd from the FileDescriptor */
    if (!(clazz = e->GetObjectClass(env,fdesc)) ||
        !(fid = e->GetFieldID(env,clazz,"fd","I"))) return -1;


    /* return the descriptor */
    return e->GetIntField(env,fdesc,fid);
}

static int getFdFromServerSocket(JNIEnv *env, jobject sock)
{
    JNIEnv e = *env;
    jclass clazz;
    jfieldID fid;
    jobject impl;
    jobject fdesc;

    /* get the SocketImpl from the Socket */
    if (!(clazz = e->GetObjectClass(env,sock)) ||
        !(fid = e->GetFieldID(env,clazz,"impl","Ljava/net/SocketImpl;")) ||
        !(impl = e->GetObjectField(env,sock,fid))) return -1;


    /* get the FileDescriptor from the SocketImpl */
    if (!(clazz = e->GetObjectClass(env,impl)) ||
        !(fid = e->GetFieldID(env,clazz,"fd","Ljava/io/FileDescriptor;")) ||
        !(fdesc = e->GetObjectField(env,impl,fid))) return -1;


    /* get the fd from the FileDescriptor */
    if (!(clazz = e->GetObjectClass(env,fdesc)) ||
        !(fid = e->GetFieldID(env,clazz,"fd","I"))) return -1;


    /* return the descriptor */
    return e->GetIntField(env,fdesc,fid);
}

//note the type of return value is jint instead of int
JNIEXPORT jint Java_org_apache_hadoop_util_ADGSetSocketTOS_setSocketTOS(JNIEnv *env, jclass obj, jobject socket, jint flow_type)
{

    static jclass channel_cls = NULL;
    jclass cls;
    jfieldID fid;


    //    printf("fid : %d\n",getFd(env, socket));

    int sockfd = getFd(env, socket);



    //   int tos_local = flow_type;
    int tos_local;
    switch (flow_type) {
        case 1: tos_local = 0x01; //000001
                break;
        case 2: tos_local = 0x02; //000010
                break;
        case 3: tos_local = 0x03; //000011
                break;
        case 4: tos_local = 0x04; //000100
                break;
        case 5: tos_local = 0x05; //000101
                break;
        case 6: tos_local = 0x06; //000110
                break;
        case 7: tos_local = 0x07; //000111
                break;
        case 8: tos_local = 0x08; //001000
                printf("datanode pipeline flow!");
                break;
        case 9: tos_local = 0x09; //001001
                break;
        case 10: tos_local = 0x0a; //001010
                break;
        case 11: tos_local = 0x0b; //001011
                break;
        case 12: tos_local = 0x0c; //001100
                break;
        case 13: tos_local = 0x0d; //001011
                break;
        case 14: tos_local = 0x0e; //001100
                break;
        default:
                printf("unkown flow type %d\n", flow_type);
                return -2;
    }


    if (setsockopt(sockfd, IPPROTO_IP, IP_TOS,  &tos_local, sizeof(tos_local))) {
        printf("setsockopt syscall error to set tos to %d for socket %d \n", tos_local, sockfd);
        return -1;
    }
    else
    {   
         //  printf ("TOS filed has been set to = %d\n",tos_local);
           
    }

    return 0;


}


JNIEXPORT jint Java_org_apache_hadoop_util_ADGSetSocketTOS_setServerSocketTOS(JNIEnv *env, jclass obj, jobject socket, jint flow_type)
{

    static jclass channel_cls = NULL;
    jclass cls;
    jfieldID fid;


    //    printf("fid : %d\n",getFd(env, socket));

    int sockfd = getFdFromServerSocket(env, socket);



    //   int tos_local = flow_type;
    int tos_local;
    switch (flow_type) {
        case 15: tos_local = 0x0f; //001111
                break;
        case 16: tos_local = 0x10; //010000
                 break;
        default:
                printf("unkown http flow type %d\n", flow_type);
                return -2;
    }


    if (setsockopt(sockfd, IPPROTO_IP, IP_TOS,  &tos_local, sizeof(tos_local))) {
        printf("setsockopt syscall error to set tos to %d for socket %d \n", tos_local, sockfd);
        return -1;
    }
    else
    {   
         //  printf ("TOS filed has been set to = %d\n",tos_local);
           
    }

    return 0;


}




