#ifndef GAME
#define GAME


#include <SDL2/SDL.h>
#include <SDL2/SDL_ttf.h>
#include <SDL2/SDL_image.h>
#include <SDL2/SDL_mixer.h>


#include "Snake.h"
#include "Apple.h"
#include "Poison.h"
#include "Reducer.h"
#include "SpeedDown.h"

#include "Constantes.h"




class Game
{
    private:
    
    /// Initialisation des variables
    /// window, renderer et events
    SDL_Window * window = nullptr;
    SDL_Renderer * renderer = nullptr;
    SDL_Event events;

    /// Entier pour le score du joueur
    unsigned int score = 0, score2 = 0;

    /// Eléments du jeu
    Snake snake;
    Apple apple;
    Poison poison;
    Reducer reducer;
    SpeedDown speedDown;

    Snake snake2;

    /// Initialisation surface et texture pour le background
    SDL_Surface * SurfaceBackground = NULL;
    SDL_Texture * TextureBackground = NULL;

    /// Initialisation surface et texture pour l'image de fin du jeu
    SDL_Surface * SurfaceEnd = NULL;
    SDL_Texture * TextureEnd = NULL;

    /// Booléen pour la boucle principale du jeu
    bool running = true;

    /// Entiers pour la gestion du temps dans le jeu
    unsigned int before = 0, now = 0, delta = 0, accumulator = 0;

    /*!
    ** Procédure d'initialisation de SDL
    */
    void init();

    /*!
    ** Procédure de gestion des actions de l'utilisateur
    */
    void inputs();

    /*!
    ** Procédure qui affiche un fond (background)
    ** @param renderer 
    */
    void renderBackground(SDL_Renderer * renderer);

    /*!
    ** Procédure d'affichage du jeu
    */
    void draw(int mode);

    /*!
    ** Procédure de gestion des collisions entre les éléments du jeu
    */
    void Collisions();
    void Collisions2();

    

    /*!
    ** Procédure d'affichage de l'image de fin de jeu
    */
    void EndGame();
    

    public:

    /*!
    ** Constructeur de la classe Game
    */
    Game();

    /*!
    ** Destructeur de la classe Game
    */
    ~Game();


    /*!
    ** Procédure qui lance le jeu et le met à jour
    ** Boucle principale du jeu
    */
    void run(int mode);

};





#endif