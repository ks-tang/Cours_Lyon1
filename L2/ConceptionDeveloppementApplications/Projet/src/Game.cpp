#include "Game.h"
#include <iostream>
#include "time.h"

using namespace std;

/*!
** Appel la fonction init()
*/
Game::Game()
{
    init();
    srand(time(0));
}


/*!
** Initialisation de SDL
** Crée une fenêtre et un renderer
*/
void Game::init()
{
    /// Initialise SDL
    if (SDL_Init(SDL_INIT_VIDEO) < 0)
    {
        cerr << "Initialisation de SDL échouée ! SDL_Error: " << SDL_GetError() << endl;
        exit(EXIT_FAILURE);
    }

    /// Crée une Window
    window = SDL_CreateWindow("Snake Game", SDL_WINDOWPOS_CENTERED, SDL_WINDOWPOS_CENTERED,
        WIDTH, HEIGHT, SDL_WINDOW_SHOWN);

    if (window == NULL)
    {
        cout << "Création de la fenêtre échouée ! SDL_Error: " << SDL_GetError() << endl;
        exit(EXIT_FAILURE);
    }

    /// Crée un renderer
    renderer = SDL_CreateRenderer(window, -1, SDL_RENDERER_ACCELERATED);
    if (renderer == NULL)
    {
        cout << "Création du renderer échouée ! SDL_Error: " << SDL_GetError() << endl;
        exit(EXIT_FAILURE);
    }
}


/*!
** Affichage du background pour le jeu
** fond noir ou image de fond
*/
void Game::renderBackground(SDL_Renderer * renderer)
{
    //SDL_SetRenderDrawColor(renderer, 0,0,0,255); //fond noir
    //SDL_RenderPresent(renderer);
    //SDL_RenderClear(renderer);

    SDL_Rect r{ 0, 0, WIDTH, HEIGHT };
    SurfaceBackground = IMG_Load("./data/sol.png");
    TextureBackground = SDL_CreateTextureFromSurface(renderer, SurfaceBackground);
    SDL_RenderCopy(renderer, TextureBackground, NULL, &r);
    SDL_FreeSurface(SurfaceBackground);
}


/*!
** Détruit le renderer, la fenêtre
** et arrête les fonctions SDL
*/
Game::~Game()
{
    ///Détruit renderer
    SDL_DestroyRenderer( renderer );

    ///Détruit window
    SDL_DestroyWindow( window );

    ///Quite SDL subsystems
    SDL_Quit();
}


/*!
** Boucle principale du jeu
*/
void Game::run(int mode)
{
    
    /// Génère une fois les premiers éléments
    apple.genererApple();
    poison.genererPoison();
    reducer.genererReducer();
    speedDown.genererSpeedDown();

    while(running)
    {


        ///Fonction qui gère les actions de l'utilisateur
        inputs();

        /// Gestion du temps dans le jeu
        before = now;
        now = SDL_GetTicks();
        delta = (now - before);

        /// Variable qui accumule le temps pour la génération des objets
        accumulator += delta;

        /// Déplacement des objets à un certain temps donné
        /// pour le poison, le reducer et le speedDown
        if(accumulator > 5000 )
        {
            poison.genererPoison();
            reducer.genererReducer();
            speedDown.genererSpeedDown();

            accumulator = 0;
        }

        /// Gestion du déplacement du snake
        snake.update(delta, snake.speed);
        if(mode==2)snake2.update(delta, snake2.speed);

        /// Gestion des évènements de rencontre
        Collisions();
        if(mode==2)Collisions2();
        
        /// Affichage des éléments du jeu
        draw(mode);

		SDL_Delay(100);
    }

    SDL_Delay(100);

    /// Affichage d'une image de fin
    EndGame();

    SDL_Delay(2500);

    /// Affichage de fin de jeu et du score dans la console
    std::cout << "Jeu terminé" << std::endl;
    std::cout << "Score Joueur 1 (ZQSD) : " << score << std::endl;
    if(mode==2)std::cout << "Score Joueur 2 (flèches) : " << score2 << std::endl;
}

/*!
** Procédure de gestion des évènements de rencontre
** collision entre les différents éléments du jeu
*/
void Game::Collisions()
{
    /// Si le snake rencontre une pomme
    /// le score du joueur augmente
    /// une nouvelle pomme est générée
    /// la vitesse du snake augmente
    if (snake.eatApple(apple))
    {
        score++;
        apple.genererApple();
        snake.speed -= 5;
    }

    /// Si le snake rencontre un poison
    /// la partie se termine
    if(snake.eatPoison(poison)) running = false;

    /// Si le snake rencontre un reducer
    /// sa taille diminue
    /// un nouveau reducer est généré
    /// si la taille du snake atteint 0, la partie est terminée
    if(snake.eatReducer(reducer))
    {
        reducer.genererReducer();
        if(snake.body_length == 0) running = false;
    }

    /// Si le snake rencontre un speedDown
    /// sa vitesse diminue
    /// un nouveau speedDown est généré
    if(snake.eatSpeedDown(speedDown))
    {
        snake.speed += 5;
        speedDown.genererSpeedDown();
    }


    /// Si le snake rencontre une autre partie de son corps
    /// la partie se termine
    if(snake.checkCollision()) running = false;

    /// Si le snake rencontre un des bords du terrain
    /// la partie se termine
    if(snake.checkLimits()) running = false;

}

/*!
** Procédure d'affichage des éléments du jeu
*/
void Game::draw(int mode)
{
    ///Affiche le fond
    renderBackground(renderer);

    /// Affichage des différents éléments du jeu
    /// snake, apple, poison, reducer, speedDown
    snake.draw(renderer);
    if(mode==2)snake2.draw(renderer);
    poison.draw(renderer);
    reducer.draw(renderer);
    speedDown.draw(renderer);
    apple.draw(renderer);

    SDL_RenderPresent(renderer);
    SDL_RenderClear(renderer);
}

void Game::inputs()
{

    while (SDL_PollEvent(&events))
		{
            /// Si l'utilisateur appuie sur la croix de fermeture de la fenêtre
            /// le jeu s'arrête
			if (events.type == SDL_QUIT)
			{
				running = false;
			}
			else if (events.type == SDL_KEYDOWN)
            {
                /// Si le joueur appuie sur un bouton pour diriger le snake
                /// le snake change de direction
                /// on vérifie que le snake ne va pas dans la direction opposée
                /// (demi-tour impossible)

                switch(events.key.keysym.scancode)
                {
                    case SDL_SCANCODE_W:
                        if(snake.direction.y != 1)
                        {
                            snake.direction = {0,-1};
                        }
                        break;
                    case SDL_SCANCODE_S:
                        if(snake.direction.y != -1)
                        {
                            snake.direction = {0,1};
                        }
                        break;
                    case SDL_SCANCODE_A:
                        if(snake.direction.x != 1)
                        {
                            snake.direction = {-1,0};
                        }
                        break;
                    case SDL_SCANCODE_D:
                        if(snake.direction.x != -1)
                        {
                            snake.direction = {1,0};
                        }
                        break;


                    
                    case SDL_SCANCODE_RIGHT:
                        if(snake2.direction.x != -1)
                        {
                            snake2.direction = {1,0};
                        }
                        break;
                    case SDL_SCANCODE_LEFT:
                        if(snake2.direction.x != 1)
                        {
                            snake2.direction = {-1,0};
                        }
                        break;
                    case SDL_SCANCODE_UP:
                        if(snake2.direction.y != 1)
                        {
                            snake2.direction = {0,-1};
                        }
                        break;
                    case SDL_SCANCODE_DOWN:
                        if(snake2.direction.y != -1)
                        {
                            snake2.direction = {0,1};
                        }
                        break;
                    
            
                    default:
                        break;

                }
            }
		}

}

void Game::EndGame()
{
    //Affichage d'une image de fin "You Lose"
    SurfaceEnd = IMG_Load("./data/lose.jpg");
    TextureEnd = SDL_CreateTextureFromSurface(renderer, SurfaceEnd);
    SDL_Rect rectangle{0,0,WIDTH, HEIGHT};
    SDL_RenderCopy(renderer, TextureEnd, NULL, &rectangle);
    SDL_RenderPresent(renderer);
    SDL_RenderClear(renderer);
    SDL_FreeSurface(SurfaceEnd);
}



/*!
** Procédure de gestion des évènements de rencontre
** collision entre les différents éléments du jeu
*/
void Game::Collisions2()
{
    /// Si le snake rencontre une pomme
    /// le score du joueur augmente
    /// une nouvelle pomme est générée
    /// la vitesse du snake augmente
    if (snake2.eatApple(apple))
    {
        score2++;
        apple.genererApple();
        snake2.speed -= 5;
    }

    /// Si le snake rencontre un poison
    /// la partie se termine
    if(snake2.eatPoison(poison)) running = false;

    /// Si le snake rencontre un reducer
    /// sa taille diminue
    /// un nouveau reducer est généré
    /// si la taille du snake atteint 0, la partie est terminée
    if(snake2.eatReducer(reducer))
    {
        reducer.genererReducer();
        if(snake2.body_length == 0) running = false;
    }

    /// Si le snake rencontre un speedDown
    /// sa vitesse diminue
    /// un nouveau speedDown est généré
    if(snake2.eatSpeedDown(speedDown))
    {
        snake2.speed += 5;
        speedDown.genererSpeedDown();
    }


    /// Si le snake rencontre une autre partie de son corps
    /// la partie se termine
    if(snake2.checkCollision()) running = false;

    /// Si le snake rencontre un des bords du terrain
    /// la partie se termine
    if(snake2.checkLimits()) running = false;

}