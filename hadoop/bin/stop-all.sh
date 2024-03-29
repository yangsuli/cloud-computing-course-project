#!/usr/bin/env bash

# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.


# Stop all hadoop daemons.  Run this on master node.

bin=`dirname "$0"`
bin=`cd "$bin"; pwd`

if [ -e "$bin/../libexec/hadoop-config.sh" ]; then
  . "$bin"/../libexec/hadoop-config.sh
else
  . "$bin/hadoop-config.sh"
fi

"$bin"/stop-mapred.sh --config $HADOOP_CONF_DIR
"$bin"/stop-dfs.sh --config $HADOOP_CONF_DIR

#Added by yangsuli
#delete all previous logs so that we have a fresh log directory
$bin/clear_log.sh
