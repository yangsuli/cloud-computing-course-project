#include<jni.h>
#include "connectionClient.h"

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


static int getFd(JNIEnv *env, jobject sock)
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
JNIEXPORT jint JNICALL Java_connectionClient_setSocketTOS(JNIEnv *env, jobject obj, jobject socket)
{
//	printf("Brisk hello world!\n");
//	return;
	
	
	
	 	static jclass channel_cls = NULL;
    jclass cls;
    jfieldID fid;
    
    
//    printf("fid : %d\n",getFd(env, socket));
    
    int sockfd = getFd(env, socket);
    
    
    printf("fid : %d\n",sockfd);

        int tos_local = 0x34;//last two bits check -- no use

        if (setsockopt(sockfd, IPPROTO_IP, IP_TOS,  &tos_local, sizeof(tos_local))) {
                  error("error at socket option");

                  int tos=0;
                        int toslen=sizeof(tos); //that line here

                  if (getsockopt(sockfd, IPPROTO_IP, IP_TOS,  &tos, &toslen) < 0) {
                          printf ("error to get option");
                  }else {
                          printf ("changing tos opt = %d\n",tos);
                  }
        }
        else
        {
                  int tos=0;
                        int toslen=sizeof(tos); //that line here

                  if (getsockopt(sockfd, IPPROTO_IP, IP_TOS,  &tos, &toslen) < 0) {
                          printf ("error to get option");
                  }else {
                          printf ("changing tos opt = %d\n",tos);
                  }
        }




   
   	return 0;
   	
/*   	
   
    if (channel_cls == NULL) {
        cls = (*env)->FindClass (env, "java/nio/channels/SelectableChannel");
//        assert (cls);
        channel_cls = (jclass) (*env)->NewGlobalRef (env, cls);
        (*env)->DeleteLocalRef (env, cls);
//        assert (channel_cls);
    }
    if (!(*env)->IsInstanceOf (env, socket, channel_cls)) 
        return -2;

    cls = (*env)->GetObjectClass(env, socket);
//    assert (cls);

    fid = (*env)->GetFieldID(env, cls, "fdVal", "I");
    
//    env->DeleteLocalRef (cls);

    if (fid == NULL) {
    	printf("fid == NULL\n");
      return -1;
		}
		
    /* return the descriptor *//* 
    int fd = (*env)->GetIntField(env, socket, fid);
		
		printf("fid != null\n");
    return fd;
    */

}




