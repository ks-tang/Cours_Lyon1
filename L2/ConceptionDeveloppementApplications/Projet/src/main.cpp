#include "Game.h"
#include <iostream>

int main (int argc, char ** argv)
{
    

    int mode;
    do{
        std::cout << "Choisissez le nombre de joueurs (1 ou 2)" << std::endl;
        std::cin >> mode;
    } while(mode <=0 || mode > 2);
    
    Game game;

    game.run(mode);
    

    return 0;
}