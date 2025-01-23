#include "Apple.h"
#include "Constantes.h"



/*!
** Prend les coordonnées de la position de la pomme,
** ajuste en fonction de la tailles des cases,
** charge et affiche l'image
*/
void Apple::draw(SDL_Renderer * renderer)
{
    SDL_Rect r{ TAILLE * x, TAILLE * y, TAILLE, TAILLE };

    //SDL_SetRenderDrawColor(renderer, 255, 0, 0, 255);
    //SDL_RenderFillRect(renderer, &r);

    SurfaceApple = IMG_Load("./data/apple.png");
    TextureApple = SDL_CreateTextureFromSurface(renderer, SurfaceApple);
    SDL_RenderCopy(renderer, TextureApple, NULL, &r);
    SDL_FreeSurface(SurfaceApple);
}


/*!
** Génère les coordonnées x et y
** avec la fonction rand()
*/
void Apple::genererApple()
{
    x = rand() % (WIDTH / TAILLE);
    y = rand() % (HEIGHT / TAILLE);
}