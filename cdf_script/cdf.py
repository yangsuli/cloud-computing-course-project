#!/usr/bin/python

import numpy

def calc_cdf(arr, bins, bin_range=None, filename=None):
	assert arr.size > 0
	[hist_arr, bin_arr] = numpy.histogram(arr, bins=bins, range=bin_range)
	hist_arr = hist_arr.cumsum() 
	assert hist_arr[-1] == arr.size
	hist_arr = hist_arr * 1.0/hist_arr[-1]
	if filename:
		numpy.savetxt(filename, numpy.hstack((bin_arr[:-1].reshape(-1, 1), hist_arr.reshape(-1, 1))), fmt='%g %g')
	return (hist_arr, bin_arr[:-1])
