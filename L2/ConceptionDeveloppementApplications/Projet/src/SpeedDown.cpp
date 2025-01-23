#include "SpeedDown.h"
#include "Constantes.h"


/*!
** Prend les coordonnées de la position de la pomme,
** ajuste en fonction de la tailles des cases,
** charge et affiche l'image
*/
void SpeedDown::draw(SDL_Renderer * renderer)
{
    SDL_Rect r{ TAILLE * x, TAILLE * y, TAILLE, TAILLE };
    //SDL_SetRenderDrawColor(renderer, 255, 255, 0, 255);
    //SDL_RenderFillRect(renderer, &r);

    SurfaceSlow = IMG_Load("./data/slow.png");
    TextureSlow = SDL_CreateTextureFromSurface(renderer, SurfaceSlow);
    SDL_RenderCopy(renderer, TextureSlow, NULL, &r);
    SDL_FreeSurface(SurfaceSlow);
}


/*!
** Génère les coordonnées x et y
** avec la fonction rand()
*/
void SpeedDown::genererSpeedDown()
{
    x = rand() % (WIDTH / TAILLE);
    y = rand() % (HEIGHT / TAILLE);
}