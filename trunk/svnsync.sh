#!/bin/bash

rsync -avz openflow@192.168.8.133:/home/openflow/*.sh /scratch/suli/838/cloud-computing-course-project/hadoop
rsync -avz openflow@192.168.8.133:/home/openflow/hadoop-1.0.4/src /scratch/suli/838/cloud-computing-course-project/hadoop
rsync -avz openflow@192.168.8.133:/home/openflow/hadoop-1.0.4/conf /scratch/suli/838/cloud-computing-course-project/hadoop
rsync -avz openflow@192.168.8.133:/home/openflow/hadoop-1.0.4/bin /scratch/suli/838/cloud-computing-course-project/hadoop

#openflow@192.168.8.1:/scratch/suli/838/cloud-computing-course-project/hadoop
#rsync -avz --include="/src" --include="/conf" hadoop-1.0.4 suli@192.168.8.1:/scratch/suli/838/cloud-computing-course-project/hadoop

ssh -t openflow@192.168.8.133 "rsync -avz --exclude=conf/hadoop-env.sh /home/openflow/hadoop-1.0.4/ openflow@192.168.8.134:/home/openflow/hadoop-1.0.4; cd /home/openflow/hadoop-1.0.4/src/core/org/apache/hadoop/util/; bash compile.sh"
