all: main.dvi main.pdf

main.dvi: main.tex abstract.tex storage.tex compute.tex networks.tex conclusions.tex intro.tex
	@latex main.tex
	@latex main.tex

main.pdf: main.dvi
	@dvipdf main.dvi
