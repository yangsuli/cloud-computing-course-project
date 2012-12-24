#!/usr/bin/bash

python doall_job.py "$1"

first_x=$(head -n 1 test_data_for_plot | cut -d ' ' -f 1)
sed -i "1i $first_x 0" test_data_for_plot
sed -i '1i 0 0' test_data_for_plot

gnuplot cdf.gp
