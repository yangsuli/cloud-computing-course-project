#!/usr/bin/python

import numpy
#import scipy.stats
import pdb
import subprocess

def bin_arr(arr, bin_limits):
	"""Apply binning based on arr.
	   return bin_list and arr_list.
	   bin_list = a list of (start, end)  
	   index_list = a list of row no
	   for the last bin, [start, end]
	   Note: 1) arr is NOT assumed to be sorted in any way.
	   	     2) For an empty bin, the list entry is empty.
			 3) The order of the indices is based on the original order.
			 4) If bin_limits = [1, 2, 3], the calculated bins are [1,2), [2,3]
	"""
	assert isinstance(arr, numpy.ndarray) and isinstance(bin_limits, numpy.ndarray)
	bin_list = [ (bin_limits[no], bin_limits[no+1]) for no in range(bin_limits.size-1) ] 
	index_list = [ [] for i in range(bin_limits.size+1) ] 
	for (bin_num, index) in zip(numpy.digitize(arr, bin_limits), range(arr.size)):  
		index_list[bin_num].append(index)
	del index_list[0]
	del index_list[-1]
	# Have to append the last elements equal to the last bin
	for index in numpy.where(arr == bin_limits[-1])[0]:
		index_list[-1].append(index)
	index_list[-1].sort()  # make the indices sorted - not the values!
	assert len(bin_list) == len(index_list)
	return bin_list, index_list

def find_contiguous_regions(condition):
	""" Calculate the length of subarrs with the same value.
		Note: it's upto the caller to ensure the binned values satisfy certain conditions, .e.g true or >0.
	Args:
		condition: whether an input array 
	Returns:
		 an array of (start_ind, end_ind, length) where [start_ind, end_ind)
	"""
	assert isinstance(condition, numpy.ndarray) 
	assert condition.size > 0
	
	if condition.size == 1:
		return numpy.array([0, 1, 1], ndmin=2)

	d = numpy.diff(condition)
	idx, = d.nonzero() 

# We need to start things after the change in "condition". Therefore, 
# we'll shift the index by 1 to the right.
	idx += 1
	idx = numpy.r_[0, idx]
	idx = numpy.r_[idx, condition.size]
	result_arr = numpy.vstack((idx[:-1], idx[1:], numpy.diff(idx))).T
	return result_arr

#def calc_dist(t1, t2):
#	"""The distance between two location tuples in meters."""
#	assert len(t1) == 2 and len(t2) == 2
#	RADIUS = 6371.  # Radius of the earth
#	dist = 1000.*RADIUS * math.arccos(math.cos(math.radians(t1[0]))*math.cos(math.radians(t2[0]))*math.cos(math.radians(t2[1]-t1[1]))+math.sin(math.radians(t1[0]))*math.sin(math.radians(t2[0])))
#	return dist

def calc_dist(t1, t2):
	"""The distance between two location tuples in meters."""
	assert isinstance(t1, numpy.ndarray) and isinstance(t2, numpy.ndarray)
	assert t1.shape[1] == 2 and t2.shape[1] == 2  # Should have two columns for lat and long respectively.
	RADIUS = 6371.   # Radius of the earth
	dist = 1000.*RADIUS * numpy.arccos(numpy.cos(numpy.radians(t1[:,0]))*numpy.cos(numpy.radians(t2[:,0]))*numpy.cos(numpy.radians(t2[:,1]-t1[:,1]))+numpy.sin(numpy.radians(t1[:,0]))*numpy.sin(numpy.radians(t2[:,0])))
	return dist

def add_fields(arr, descr, arr_append=None):
	""" arr is a numpy structure array and descr is a list of tuples,
		e.g. [('score', float)]
		arr_append is the data to be appended. 
	"""
	if arr.dtype.fields is None:
		raise ValueError, "arr must be a structured numpy array"
	arr_new = numpy.zeros(arr.shape, dtype=arr.dtype.descr + descr)
	for name in arr.dtype.names:
		arr_new[name] = arr[name]
	if arr_append is not None:
		assert arr_append.ndim == 2 and arr_append.shape[1] == len(descr)
		for no, pair in enumerate(descr):
			arr_new[pair[0]] = arr_append[:, no]
	return arr_new

def calc_cdf(arr, bins, bin_range=None, filename=None):
	assert arr.size > 0
	[hist_arr, bin_arr] = numpy.histogram(arr, bins=bins, range=bin_range)
	hist_arr = hist_arr.cumsum() 
	assert hist_arr[-1] == arr.size
	hist_arr = hist_arr * 1.0/hist_arr[-1]
	if filename:
		numpy.savetxt(filename, numpy.hstack((bin_arr[1:].reshape(-1, 1), hist_arr.reshape(-1, 1))), fmt='%g %g')
#		numpy.savetxt(filename, numpy.hstack((bin_arr[:-1].reshape(-1, 1), hist_arr.reshape(-1, 1))), fmt='%g %g')
	return (hist_arr, bin_arr[:-1])

def calc_pdf(arr, bins, bin_range=None, filename=None, divide=True):
	[hist_arr, bin_arr] = numpy.histogram(arr, bins=bins, range=bin_range)
	assert hist_arr.sum() == arr.size
	if divide:
		if arr.size > 0:
			hist_arr = hist_arr * 1.0/arr.size  # else return raw numbers
	if filename:
		numpy.savetxt(filename, numpy.hstack((bin_arr[:-1].reshape(-1, 1), hist_arr.reshape(-1, 1))), fmt='%g %g')
	return (hist_arr, bin_arr[:-1])

def is_float_equal(x, y):
	assert isinstance(x, float) and isinstance(y, float)
	RATIO = 0.000001
	return numpy.fabs(x-y) <= RATIO * numpy.fabs(x)

#def prune_arr_value(arr, low_percent, high_percent):
#	"""Take the input numpy array and return the index arr within low_percent and high_percent.
#	   (low_percent, high_percent)
#	Args: 
#		input array of values.
#		low_percent: lower percentile.
#		high_percent: higher percentile.
#	Returns:
#		index_arr
#	"""	
#	low_val = scipy.stats.scoreatpercentile(arr, low_percent)
#	high_val = scipy.stats.scoreatpercentile(arr, high_percent)
#	return numpy.where(numpy.logical_and(arr>low_val, arr<high_val))[0]

def prune_arr_quartile(arr, low_percent, high_percent):
	""" Only prune out (low_percent+high_percent)% points.
		(low_percent, high_percent)"""
	assert isinstance(arr, numpy.ndarray) and len(arr.shape) == 1  # Make sure 1d array
	if low_percent == 0 and high_percent == 100:
		return arr
	arr_sort = numpy.sort(arr)
	low_start = numpy.ceil(arr_sort.size * low_percent / 100.)
	high_start = numpy.ceil(arr_sort.size * (100-high_percent) / 100.)
	return arr_sort[low_start:-1*high_start]

def enum(**enums):
	return type('Enum', (), enums)

#def calc_low_median_high_percentile(arr, low_percent, high_percent):
#	assert isinstance(arr, numpy.ndarray) and arr.size > 0
#	low = scipy.stats.scoreatpercentile(arr, low_percent)
#	median = scipy.stats.scoreatpercentile(arr, 50)
#	high = scipy.stats.scoreatpercentile(arr, high_percent)
#	return (low, median, high)

def remove_file(filename):
	subprocess.call("rm -f %s" % filename, shell=True)
