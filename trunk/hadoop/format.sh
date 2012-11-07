#!/bin/bash

rm -fr /home/openflow/hadoop-tmp/*
rsync -avz --delete /home/openflow/hadoop-tmp/ openflow@192.168.1.128:/home/openflow/hadoop-tmp
/home/openflow/hadoop-1.0.4/bin/hadoop namenode -format
