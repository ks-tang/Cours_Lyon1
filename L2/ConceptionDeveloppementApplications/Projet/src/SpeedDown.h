#ifndef SPEEDDOWN
#define SPEEDDOWN

#include <SDL2/SDL.h>
#include <SDL2/SDL_ttf.h>
#include <SDL2/SDL_image.h>
#include <SDL2/SDL_mixer.h>

/*! Structure SpeedDown
** Lorsque le snake mange cet objet,
** sa vitesse diminue
*/
struct SpeedDown
{
    /// Coordonnées x et y de l'objet
    int x,y;

    /// Initialisation de la surface et de la texture
    SDL_Surface * SurfaceSlow = NULL;
    SDL_Texture * TextureSlow = NULL;

    
    /*!
    ** Procédure qui dessine et affiche l'objet SpeedDown
    ** à partir d'une image dans le dossier data
    ** @param renderer 
    */
    void draw(SDL_Renderer * renderer);


    /*!
    ** Procédure qui génère aléatoirement une position (x,y)
    ** pour l'objet SpeedDown
    */
    void genererSpeedDown();
    

};

#endif