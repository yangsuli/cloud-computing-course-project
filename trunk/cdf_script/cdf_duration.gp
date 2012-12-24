set terminal postscript eps enhanced color
set output 'cdf.eps'

set xlabel "Flow Duration (s)"
set ylabel "Cumulative Fraction" offset 1,0

#set logscale x

#for ytic grid
set style line 1 lc rgb '#B5B5B5' lt 1 lw 4

set xtic font "Helvetica, 17"
set ytic font "Helvetica, 17"
#legend
#set key font "Helvetica, 15"

set ylabel font "Helvetica, 17"

set xrange [0:]
set yrange [0:1]

#set xtics 100
set ytics 0.1


set ytics nomirror
set grid ytics ls 1

set key top left



plot "test_data_for_plot" using 1:2 title "Total Flow Duration CDF" w l linetype 1 linecolor rgb '#DC143C' lw 4
#"quincy_rate_output" using 1:2 title "Quincy" w l linetype 2 linecolor rgb '#00C957' lw 4,\
#"naps_rate_output" using 1:2 title "Naps" w l linetype 8 linecolor rgb '#0000FF' lw 4

