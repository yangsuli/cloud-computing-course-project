#!/bin/bash

rsync -avz *.sh suli@192.168.8.1:/scratch/suli/838/cloud-computing-course-project/hadoop
rsync -avz --include="/src" --include="/conf" hadoop-1.0.4 suli@192.168.8.1:/scratch/suli/838/cloud-computing-course-project/hadoop

