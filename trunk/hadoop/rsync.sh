#!/bin/bash

rm -fr hadoop-1.0.4/logs
rsync -avz --delete --exclude="/conf/hadoop-env.sh" hadoop-1.0.4 openflow@192.168.1.128
