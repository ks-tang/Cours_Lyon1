#ifndef APPLE
#define APPLE

#include <SDL2/SDL.h>
#include <SDL2/SDL_ttf.h>
#include <SDL2/SDL_image.h>
#include <SDL2/SDL_mixer.h>

/*! Structure Apple
** Lorsque le snake mange cet objet,
** le score du joueur augmente
** la taille du snake augmente
** la vitesse du snake augmente
*/
struct Apple
{
    /// Coordonnées x et y de l'objet
    int x,y;

    /// Initialisation de la surface et de la texture
    SDL_Surface * SurfaceApple = NULL;
    SDL_Texture * TextureApple = NULL;

    
    /*!
    ** Procédure qui dessine et affiche la pomme
    ** à partir d'une image dans le dossier data
    ** @param renderer 
    */
    void draw(SDL_Renderer * renderer);


    /*!
    ** Procédure qui génère aléatoirement une position (x,y)
    ** pour l'objet Apple
    */
    void genererApple();
    

};

#endif