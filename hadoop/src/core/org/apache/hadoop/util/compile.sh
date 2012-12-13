#!/usr/bin/bash
pwd=`pwd`
javac ADGSetSocketTOS.java
cd ../../../../
javah -jni org.apache.hadoop.util.ADGSetSocketTOS
cd $pwd
mv ../../../../org_apache_hadoop_util_ADGSetSocketTOS.h org_apache_hadoop_util_ADGSetSocketTOS.h

gcc -shared -I/usr/lib/jvm/java-6-sun-1.6.0.26/include -I/usr/lib/jvm/java-6-sun-1.6.0.26/include/linux SetSocketTOS.c -o libsetsocktos.so 
cp libsetsocktos.so /home/openflow/hadoop-1.0.4/libexec/../lib/native/Linux-i386-32/
