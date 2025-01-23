#include "Reducer.h"
#include "Constantes.h"



/*!
** Prend les coordonnées de la position de la pomme,
** ajuste en fonction de la tailles des cases,
** charge et affiche l'image
*/
void Reducer::draw(SDL_Renderer * renderer)
{
    SDL_Rect r{ TAILLE * x, TAILLE * y, TAILLE, TAILLE };
    //SDL_SetRenderDrawColor(renderer, 0, 0, 255, 255);
    //SDL_RenderFillRect(renderer, &r);

    SurfaceReduce = IMG_Load("./data/reducer.png");
    TextureReduce = SDL_CreateTextureFromSurface(renderer, SurfaceReduce);
    SDL_RenderCopy(renderer, TextureReduce, NULL, &r);
    SDL_FreeSurface(SurfaceReduce);
}


/*!
** Génère les coordonnées x et y
** avec la fonction rand()
*/
void Reducer::genererReducer()
{
    x = rand() % (WIDTH / TAILLE);
    y = rand() % (HEIGHT / TAILLE);
}