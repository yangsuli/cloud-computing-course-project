pwd=`pwd`

#duration cdf 
cd $1
cat *tcp.txt|grep duration|cut -d' ' -f3 > duration1.dat
awk '$1 ~ /e/ {$1=0} {print}' duration1.dat > duration.dat
rm duration1.dat
mv duration.dat $pwd
cd $pwd
mv duration.dat $2
cd $2
cp cdf_duration.gp cdf.gp
bash plotcdf.sh duration.dat
cp cdf.eps $pwd/flow_duration.eps
cp cdf.eps flow_duration.eps
cd $pwd
cp flow_duration.eps $1/flow_duration.eps

#size cdf
cd $1
cat *tcp.txt|grep total|grep -v tos|cut -d' ' -f3 > size1.dat
awk '$1  {$1=$1/1048576.0} {print}' size1.dat > size.dat 
rm size1.dat
mv size.dat $pwd
cd $pwd
mv size.dat $2
cd $2
cp cdf_size.gp cdf.gp
bash plotcdf.sh size.dat
cp cdf.eps $pwd/flow_size.eps
cp cdf.eps flow_size.eps
cd $pwd
cp flow_size.eps $1/flow_size.eps

bash typeflow.sh
