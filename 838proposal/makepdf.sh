latex main.tex
bibtex main
latex main.tex
latex main.tex
bash fixdvipsgnuplot.sh main
ps2pdf main.ps
evince main.pdf

