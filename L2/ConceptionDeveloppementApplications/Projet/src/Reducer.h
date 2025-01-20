#ifndef REDUCER
#define REDUCER

#include <SDL2/SDL.h>
#include <SDL2/SDL_ttf.h>
#include <SDL2/SDL_image.h>
#include <SDL2/SDL_mixer.h>

/*! Structure Reducer
** Lorsque le snake mange cet objet,
** sa taille diminue
*/
struct Reducer
{
    /// Coordonnées x et y de l'objet
    int x,y;

    /// Initialisation de la surface et de la texture
    SDL_Surface * SurfaceReduce = NULL;
    SDL_Texture * TextureReduce = NULL;

    
    /*!
    ** Procédure qui dessine et affiche le reducer
    ** à partir d'une image dans le dossier data
    ** @param renderer 
    */
    void draw(SDL_Renderer * renderer);


    /*!
    ** Procédure qui génère aléatoirement une position (x,y)
    ** pour l'objet Reducer
    */
    void genererReducer();
    

};


#endif