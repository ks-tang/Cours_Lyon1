#include "Snake.h"
#include <iostream>


/*!
** Dessine des cases de couleurs vertes
** pour chaque position du corps du snake
*/
void Snake::draw(SDL_Renderer* renderer)
{
    
	SDL_SetRenderDrawColor(renderer, 40, 120, 40, 255);

	for(int i = 0; i < body_length; i++)
	{
		SDL_Rect r {TAILLE * body[i].x, TAILLE * body[i].y, TAILLE, TAILLE};
		SDL_RenderFillRect(renderer, &r);

		/*
		SurfaceBody = IMG_Load("./data/body.jpg");
    	TextureBody = SDL_CreateTextureFromSurface(renderer, SurfaceBody);
    	SDL_RenderCopy(renderer, TextureBody, NULL, &r);
    	SDL_RenderPresent(renderer);
    	SDL_FreeSurface(SurfaceBody);
		*/
	}

	

	SDL_Rect rect {TAILLE * headLocation.x, TAILLE * headLocation.y, TAILLE, TAILLE};

	//SDL_RenderFillRect(renderer, &rect);
	SurfaceHead = IMG_Load("./data/head.png");
    TextureHead = SDL_CreateTextureFromSurface(renderer, SurfaceHead);
    SDL_RenderCopy(renderer, TextureHead, NULL, &rect);
    //SDL_RenderPresent(renderer);
    SDL_FreeSurface(SurfaceHead);
	
}


/*!
** Mise à jour de position du snake
*/
void Snake::update(unsigned int delta, unsigned int speed)
{
	/// time accumule le temps de delta
	time += delta;

	/// Lorsque time dépasse speed
	/// on met à jour la position du snake
	if (time > speed)
	{
		/// on remet à zero time
		time = 0;

		///La position de la tête du snake se met à jour selon la direction
		headLocation.x = headLocation.x + direction.x;
		headLocation.y = headLocation.y + direction.y;

		/// Pour le corps du snake
		/// chaque partie prend la position de la suivante
		for(int i = 0; i < body_length-1; i++)
		{
			body[i].x = body[i+1].x;
			body[i].y = body[i+1].y;
		}

		/// la dernière partie prend la position de la tête
		body[body_length-1].x = headLocation.x;
		body[body_length-1].y = headLocation.y;
	}

}

/*!
** Si la position de la tête correspond à celle d'une partie du corps
** return true sinon false
*/
bool Snake::checkCollision()
{
	if(body_length > 2)
	{
		for( int i = 0; i < body_length-1; i++)
		{
			if( headLocation.x == body[i].x && headLocation.y == body[i].y)
			{
				return true;
			}
		} 
	}
	
	return false;
}


/*!
** Si la position de la tête dépasse les bords du terrain
** return true sinon false
*/
bool Snake::checkLimits()
{
	if(headLocation.x<0 || headLocation.x>=(WIDTH/TAILLE) )
	{
		return true;
	}
	if(headLocation.y<0 || headLocation.y>=(HEIGHT/TAILLE) )
	{
		return true;
	} 
	return false;
}

/*!
** Si la position de la tête correspond à celle d'une pomme
** return true sinon false
** la taille du snake augmente
*/
bool Snake::eatApple(Apple apple)
{

	if(headLocation.x == apple.x && headLocation.y == apple.y)
	{
		/// Ajoute un élément à la fin du tableau body
		body_length++;
		
		for( int i = body_length; i>0; i--)
		{
			body[i].x = body[i-1].x;
			body[i].y = body[i-1].y;
		}
		return true;
	} else return false;
}

/*!
** Si la position de la tête correspond à celle d'un poison
** return true sinon false
*/
bool Snake::eatPoison(Poison poison)
{

	if (headLocation.x == poison.x && headLocation.y == poison.y)
	{
		return true;
	} else return false;
}

/*!
** Si la position de la tête correspond à celle d'un reducer
** return true sinon false
*/
bool Snake::eatReducer(Reducer reducer)
{
	if ( headLocation.x == reducer.x && headLocation.y == reducer.y)
	{
		body_length--;
		return 1;
	} else return 0;
}

/*!
** Si la position de la tête correspond à celle d'un speedDown
** return true sinon false
*/
bool Snake::eatSpeedDown(SpeedDown speedDown)
{
    if (headLocation.x == speedDown.x && headLocation.y == speedDown.y)
    {
        return 1;
    } else return 0;
}