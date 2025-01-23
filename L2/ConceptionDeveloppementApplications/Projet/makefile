#executable
all: bin/snake

bin/snake: obj/main.o obj/Game.o obj/Snake.o obj/Vector2D.o obj/Apple.o obj/Constantes.o obj/Poison.o obj/Reducer.o obj/SpeedDown.o
	g++ -ggdb -Wall obj/main.o obj/Game.o obj/Snake.o obj/Vector2D.o obj/Apple.o obj/Constantes.o obj/Poison.o obj/Reducer.o obj/SpeedDown.o -o bin/snake -lSDL2 -lSDL2_ttf -lSDL2_image -lSDL2_mixer


obj/main.o: src/main.cpp
	g++ -ggdb -Wall -c src/main.cpp -o obj/main.o

obj/Game.o: src/Game.h src/Game.cpp 
	g++ -ggdb -Wall -c src/Game.cpp -o obj/Game.o

obj/Snake.o: src/Snake.h src/Snake.cpp
	g++ -ggdb -Wall -c src/Snake.cpp -o obj/Snake.o

obj/Vector2D.o: src/Vector2D.h src/Vector2D.cpp
	g++ -ggdb -Wall -c src/Vector2D.cpp -o obj/Vector2D.o

obj/Apple.o: src/Apple.h src/Apple.cpp
	g++ -ggdb -Wall -c src/Apple.cpp -o obj/Apple.o

obj/Constantes.o: src/Constantes.h src/Constantes.cpp
	g++ -ggdb -Wall -c src/Constantes.cpp -o obj/Constantes.o

obj/Poison.o: src/Poison.h src/Poison.cpp
	g++ -ggdb -Wall -c src/Poison.cpp -o obj/Poison.o

obj/Reducer.o: src/Reducer.h src/Reducer.cpp
	g++ -ggdb -Wall -c src/Reducer.cpp -o obj/Reducer.o

obj/SpeedDown.o: src/SpeedDown.h src/SpeedDown.cpp
	g++ -ggdb -Wall -c src/SpeedDown.cpp -o obj/SpeedDown.o

clean: 
	rm obj/*.o 
	rm bin/*

veryclean: clean 
