pwd=`pwd`
cdf_dir="../../cdf_script"

#python type_flow.py `ls *tcp.txt`

echo `pwd`
for file in `ls total_type| grep -v txt`
do
	echo yangsuli`pwd`
	echo $file
	cd total_type
	cat $file | grep duration | cut -d' ' -f3 > ${file}_duration1.dat
	awk '$1 ~ /e/ {$1=0} {print}' ${file}_duration1.dat > ${file}_duration.dat
	rm ${file}_duration1.dat
	dur_dat=${file}_duration.dat
	mv $dur_dat $cdf_dir
	cd $cdf_dir
	cp cdf_duration.gp cdf.gp
	bash plotcdf.sh $dur_dat
	cp cdf.eps $pwd/${file}_flow_duration.eps
	cd $pwd
done

echo `pwd`
for file in `ls total_type| grep -v txt`
do
	echo yangsuli`pwd`
	echo $file
	cd total_type
	cat $file | grep total | grep -v tos| cut -d' ' -f3 > ${file}_size1.dat
	awk '$1 {$1=$1/1048576.0} {print}' ${file}_size1.dat > ${file}_size.dat
	rm ${file}_size1.dat
	size_dat=${file}_size.dat
	mv $size_dat $cdf_dir
	cd $cdf_dir
	cp cdf_size.gp cdf.gp
	bash plotcdf.sh $size_dat
	cp cdf.eps $pwd/${file}_flow_size.eps
	cd $pwd
done
