#ifndef SNAKE
#define SNAKE

#include "Vector2D.h"
#include <vector>
#include "Apple.h"
#include "Constantes.h"
#include "Poison.h"
#include "Reducer.h"
#include "SpeedDown.h"

#include <SDL2/SDL.h>
#include <SDL2/SDL_ttf.h>
#include <SDL2/SDL_image.h>
#include <SDL2/SDL_mixer.h>

/// Constante pour l'allocation de la mémoire
/// pour la taille du snake
const int TAILLE_MAX = 500;


/*!
** Structure Snake
*/
struct Snake
{
    /// Position initiale du snake
    Vector2D headLocation = {0,0};

    ///Direction initiale du snake
    Vector2D direction = {0,1};

    /// Tableau de vecteurs
    /// qui définie le corps du snake
    Vector2D body[TAILLE_MAX];

    /// Variable qui gère la taille du snake
    int body_length = 1;

    /// Variables de gestion du temps 
    unsigned int time;
    unsigned int speed = 100;

    SDL_Surface * SurfaceHead = NULL;
    SDL_Texture * TextureHead = NULL;

    SDL_Surface * SurfaceBody = NULL;
    SDL_Texture * TextureBody = NULL;
    
    /*!
    ** Procédure d'affichage du snake
    ** @param renderer
    */
    void draw(SDL_Renderer* renderer);

    /*!
    ** Procédure de mise à jour du snake
    ** selon sa position et sa direction
    ** pour toutes les parties du corps du snake
    ** @param delta temps de jeu entre deux parcours de boucles 
    ** @param speed temps de rafraichissement minimum 
    */
    void update(unsigned int delta, unsigned int speed);

    /*!
    ** Fonction qui vérifie si le snake atteint une partie de son corps
    ** @return boolean
    */
    bool checkCollision();

    /*!
    ** Fonction qui vérifie si le snake atteint un des bords du terrain
    ** @return boolean
    */
    bool checkLimits();

    /*!
    ** Fonction qui vérifie si le snake mange une pomme
    ** @param apple un objet Apple
    ** @return boolean
    */
    bool eatApple(Apple apple);

    /*!
    ** Fonction qui vérifie si le snake mange du poison
    ** @param poison un objet Poison
    ** @return boolean
    */
    bool eatPoison(Poison poison);

    /*!
    ** Fonction qui vérifie si le snake atteint un reducer
    ** @param reducer un objet Reducer
    ** @return boolean
    */
    bool eatReducer(Reducer reducer);

    /*!
    ** Fonction qui vérifie si le snake atteint un speedDown
    ** @param speedDown un objet SpeedDown
    ** @return boolean
    */
    bool eatSpeedDown(SpeedDown speedDown);
    
};



#endif