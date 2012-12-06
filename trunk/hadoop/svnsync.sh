#!/bin/bash

rsync -avz *.sh suli@192.168.8.1:/scratch/suli/838/cloud-computing-course-project/hadoop
rsync -avz hadoop-1.0.4/src suli@192.168.8.1:/scratch/suli/838/cloud-computing-course-project/hadoop
rsync -avz hadoop-1.0.4/conf suli@192.168.8.1:/scratch/suli/838/cloud-computing-course-project/hadoop
ssh suli@192.168.8.1 "cd /scratch/suli/838/cloud-computing-course-project/; svn ci"
rsync -avz *.sh suli@192.168.8.134:/home/openflow/hadoop-1.0.4/
rsync -avz hadoop-1.0.4/src suli@192.168.8.134:/home/openflow/hadoop-1.0.4/
rsync -avz hadoop-1.0.4/conf suli@192.168.8.134:/home/openflow/hadoop-1.0.4/
ssh openflow@192.168.8.134 "cd hadoop-1.0.4; ant"

