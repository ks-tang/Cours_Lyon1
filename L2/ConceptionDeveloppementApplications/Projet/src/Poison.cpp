#include "Poison.h"
#include "Constantes.h"



/*!
** Prend les coordonnées de la position de la pomme,
** ajuste en fonction de la tailles des cases,
** charge et affiche l'image
*/
void Poison::draw(SDL_Renderer * renderer)
{
    SDL_Rect r{ TAILLE * x, TAILLE * y, TAILLE, TAILLE };
    //SDL_SetRenderDrawColor(renderer, 255, 0, 255, 255);
    //SDL_RenderFillRect(renderer, &r);

    
    SurfacePoison = IMG_Load("./data/poison.png");
    TexturePoison = SDL_CreateTextureFromSurface(renderer, SurfacePoison);
    SDL_RenderCopy(renderer, TexturePoison, NULL, &r);
    SDL_FreeSurface(SurfacePoison);
}


/*!
** Génère les coordonnées x et y
** avec la fonction rand()
*/
void Poison::genererPoison()
{

    x = rand() % (WIDTH / TAILLE);
    y = rand() % (HEIGHT / TAILLE);
}