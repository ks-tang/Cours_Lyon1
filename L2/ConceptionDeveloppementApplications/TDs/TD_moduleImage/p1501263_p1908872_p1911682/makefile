
#les 3 exécutables
all: bin/exemple bin/affichage bin/test


# executable test
bin/test: obj/test.o obj/Image.o obj/Pixel.o
	g++ -ggdb -Wall obj/test.o obj/Image.o obj/Pixel.o -o bin/test -lSDL2 -lSDL2_image -lSDL2_ttf -lSDL2_mixer

obj/test.o: src/mainTest.cpp src/Image.h
	g++ -ggdb -Wall -c src/mainTest.cpp -o obj/test.o



#executable exemple
bin/exemple: obj/exemple.o obj/Image.o obj/Pixel.o
	g++ -ggdb -Wall obj/exemple.o obj/Image.o obj/Pixel.o -o bin/exemple -lSDL2 -lSDL2_image -lSDL2_ttf -lSDL2_mixer

obj/exemple.o: src/mainExemple.cpp src/Image.h
	g++ -ggdb -Wall  -c src/mainExemple.cpp -o obj/exemple.o 



#executable affichage
bin/affichage: obj/affichage.o obj/Image.o obj/Pixel.o 
	g++ -ggdb -Wall obj/affichage.o obj/Image.o obj/Pixel.o -o bin/affichage -lSDL2 -lSDL2_image -lSDL2_ttf -lSDL2_mixer 

obj/affichage.o: src/mainAffichage.cpp src/Image.h 
	g++ -ggdb -Wall  -c src/mainAffichage.cpp -o obj/affichage.o 





#Image et Pixel
obj/Image.o: src/Image.h src/Image.cpp 
	g++ -ggdb -Wall -c src/Image.cpp -o obj/Image.o

obj/Pixel.o: src/Pixel.h src/Pixel.cpp
	g++ -ggdb -Wall -c src/Pixel.cpp -o obj/Pixel.o	







clean: 
	rm obj/*.o 
	rm bin/*

veryclean: clean 

