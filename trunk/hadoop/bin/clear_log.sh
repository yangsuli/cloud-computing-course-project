hdp_dir=`pwd`

rm logs/*;

for slave in `cat conf/slaves` 
do
    ssh $slave "rm $hdp_dir/logs/*"
done
