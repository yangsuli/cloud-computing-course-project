#!/usr/bin/bash
pwd=`pwd`
#javac ADGSetSocketTOS.java
#cd ../../../../
cd /home/openflow/hadoop-1.0.4/bin/
. ./hadoop-env
cd /home/openflow/hadoop-1.0.4/build/classes/
javah -classpath $CLASSPATH -jni org.apache.hadoop.util.ADGSetSocketTOS 
cd $pwd
#mv ../../../../org_apache_hadoop_util_ADGSetSocketTOS.h org_apache_hadoop_util_ADGSetSocketTOS.h
mv /home/openflow/hadoop-1.0.4/build/classes/org_apache_hadoop_util_ADGSetSocketTOS.h org_apache_hadoop_util_ADGSetSocketTOS.h

if [[ X"" = X$JAVA_HOME ]];
then
    gcc -shared -fPIC -I/usr/lib/jvm/java-6-sun-1.6.0.26/include -I/usr/lib/jvm/java-6-sun-1.6.0.26/include/linux SetSocketTOS.c -o libsetsocktos.so
else
gcc -shared -fPIC -I$JAVA_HOME/include -I$JAVA_HOME/include/linux SetSocketTOS.c -o libsetsocktos.so 
fi
cp libsetsocktos.so /home/openflow/hadoop-1.0.4/libexec/../lib/native/Linux-i386-32/
cp libsetsocktos.so /scratch/838-project/hadoop-1.0.4/lib/native/Linux-amd64-64/

