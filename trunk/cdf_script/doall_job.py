#!/usr/bin/python

import sys
import cdf
import numpy
import pdb


L = []
f = open(sys.argv[1], "r")
for line in f:
	try:
	 	 num = float(line.rstrip())
		 L.append(num)
	except:
		continue	
f.close()
cdf.calc_cdf(numpy.array(L), 1000, filename="test_data_for_plot")


