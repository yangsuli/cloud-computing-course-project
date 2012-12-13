hdp_dir="/scratch/838-project/cloud-computing-course-project/hadoop/"
cd $hdp_dir

rm logs/*;

for slave in `cat conf/slaves` 
do
    ssh $slave "rm $hdp_dir/logs/*"
done
