#ifndef POISON
#define POISON

#include <SDL2/SDL.h>
#include <SDL2/SDL_ttf.h>
#include <SDL2/SDL_image.h>
#include <SDL2/SDL_mixer.h>

/*! Structure Poison
** Lorsque le snake mange cet objet,
** il meurt et donc le jeu s'arrête
*/
struct Poison
{
    /// Coordonnées x et y de l'objet
    int x,y;

    /// Initialisation de la surface et de la texture
    SDL_Surface * SurfacePoison = NULL;
    SDL_Texture * TexturePoison = NULL;

    
    /*!
    ** Procédure qui dessine et affiche le poison
    ** à partir d'une image dans le dossier data
    ** @param renderer 
    */
    void draw(SDL_Renderer * renderer);


    /*!
    ** Procédure qui génère aléatoirement une position (x,y)
    ** pour l'objet Poison
    */
    void genererPoison();
    

};

#endif