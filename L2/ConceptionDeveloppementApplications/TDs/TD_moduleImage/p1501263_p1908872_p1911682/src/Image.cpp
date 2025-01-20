#include "Image.h"
#include <iostream>
#include <cassert>
#include <stdlib.h>
#include <fstream>

///constructeurs

 ///construction d'une image de dimension 0 et tab nul
Image::Image()
{
    dimx=0; dimy=0;
    tab = NULL;
}

 ///construction d'une image de dimension donnée et alloue un tableau de pixels correspondant 
Image::Image(const int dimensionX, const int dimensionY)
{
    assert(dimensionX>0);
    assert(dimensionY>0);

    dimx=dimensionX;
    dimy=dimensionY;

    tab = new Pixel[dimensionX*dimensionY];
}

///destructeur
 ///détruit le tableau de pixels sur le tas et met les dimensions à 0
Image::~Image()
{
    if(tab != NULL)
    {
        delete [] tab;
    }
    dimx=0; dimy=0;
}


// get/set
 ///pour obtenir le pixel corrrespondant aux données en paramètres
Pixel & Image::getPix ( const int x, const  int y) const
{
    return tab[y*dimx+x];
}

 ///modifie la couleur du pixel en paramètre
void Image::setPix (const int x, const int y, const Pixel couleur)
{
    //std::cout << x << " " << y << " " << dimx << " " << dimy << std::endl;
    tab[y*dimx+x]=couleur;
}


/// fonctions/procédures
 ///dessine un rectangle de couleur avec les coordonnées des coins données
void Image::dessinerRectangle (const int Xmin, const int Ymin, const int Xmax, const int Ymax, const Pixel couleur)
{
    assert(Xmin<=Xmax && Ymin<=Ymax);

    for (int i=Xmin; i<Xmax; i++){
        for (int j=Ymin; j<Ymax; j++)
        {
            setPix(i,j,couleur);
        }
    }
}

 ///met l'image dans une même couleur
void Image::effacer (const Pixel couleur)
{
    dessinerRectangle(0, 0, dimx, dimy, couleur);
}

 ///test de bon fonctionnement des fonctions
void Image::testRegression()
{

    std::cout << "Création d'une image..." << std::endl;
    int x=10;
    int y=10;
    Image Im(x,y);

    Pixel pix0 (0,0,0);
    Pixel pix10(10,10,10);

    std::cout << "Coloration de l'image..." << std::endl;
    Im.setPix(0,0,pix0);

    std::cout << "Test de la coloration 1...";
    assert(Im.getPix(0,0).getRouge()==0);
    assert(Im.getPix(0,0).getVert()==0);
    assert(Im.getPix(0,0).getBleu()==0);
    std::cout << "OK" << std::endl;

    /*
    std::cout << Im.tab[0].getRouge() << " ";
    std::cout << Im.tab[0].getVert() << " ";
    std::cout << Im.tab[0].getBleu() << std::endl;  */

    std::cout << "Test de la coloration 2...";
    Im.setPix(0,0,pix10);
    assert(Im.getPix(0,0).getRouge()==10);
    assert(Im.getPix(0,0).getVert()==10);
    assert(Im.getPix(0,0).getBleu()==10);
    std::cout << "OK" << std::endl;

    /*
    std::cout << Im.tab[0].getRouge() << " ";
    std::cout << Im.tab[0].getVert() << " ";
    std::cout << Im.tab[0].getBleu() << std::endl;  */

    std::cout << "On dessine un rectangle...";
    Pixel pix20(20,20,20);
    Im.dessinerRectangle(0,0, 2,2, pix20);

    assert(Im.getPix(0,0).getRouge()==20);
    assert(Im.getPix(0,0).getVert()==20);
    assert(Im.getPix(0,0).getBleu()==20);
    std::cout << "OK" << std::endl;

    /*
    std::cout << Im.getPix(0,0).getRouge() << " ";
    std::cout << Im.getPix(0,0).getVert() << " ";
    std::cout << Im.getPix(0,0).getBleu() << std::endl;
    */

    std::cout << "On efface...";
    Im.effacer(pix0);

    assert(Im.getPix(0,0).getRouge()==0);
    assert(Im.getPix(0,0).getVert()==0);
    assert(Im.getPix(0,0).getBleu()==0);
    std::cout << "OK" << std::endl;

    /*
    std::cout << Im.tab[0].getRouge() << " ";
    std::cout << Im.tab[0].getVert() << " ";
    std::cout << Im.tab[0].getBleu() << std::endl;  
    */

    std::cout << "Vérification de la sauvegarde..." << std::endl;
    Im.sauver("./data/test.ppm");
    
    std::cout << "Vérification de la lecture..." << std::endl;
    Im.ouvrir("./data/test.ppm");

    std::cout << "Vérification de l'image...(tous les pixels à 0)" << std::endl;
    assert(Im.getPix(0,0).getRouge()==0);
    assert(Im.getPix(0,0).getVert()==0);
    assert(Im.getPix(0,0).getBleu()==0);
    Im.afficherConsole();

    std::cout << "OK, tout est nickel !" << std::endl;

    remove("./data/test.ppm");

}







 ///sauvegarde l'image dans un fichier
void Image::sauver(const std::string & filename)  {
    std::ofstream fichier (filename.c_str());
    assert(fichier.is_open());
    fichier << "P3" << std::endl;
    fichier << dimx << " " << dimy << std::endl;
    fichier << "255" << std::endl;
    for(int y=0; y<dimy; ++y)
        for(int x=0; x<dimx; ++x) {
            Pixel& pix = getPix(x,y);
            fichier << +pix.getRouge() << " " << +pix.getVert() << " " << +pix.getBleu() << " ";
        }
    std::cout << "Sauvegarde de l'image " << filename << " ... OK\n";
    fichier.close();
}

 ///ouvre un fichier 
void Image::ouvrir(const std::string & filename) {
    std::ifstream fichier (filename.c_str());
    assert(fichier.is_open());
	unsigned int r,g,b;
	std::string mot;
	dimx = dimy = 0;
	fichier >> mot >> dimx >> dimy >> mot;
	assert(dimx > 0 && dimy > 0);
	if (tab != NULL) delete [] tab;
	tab = new Pixel [dimx*dimy];
    for(int y=0; y<dimy; ++y)
        for(int x=0; x<dimx; ++x) {
            fichier >> r >> g >> b;
            getPix(x,y).setRouge(r);
            getPix(x,y).setVert(g);
            getPix(x,y).setBleu(b);
        }
    fichier.close();
    std::cout << "Lecture de l'image " << filename << " ... OK\n";
}

///affiche les valeurs de chaque pixel dans la console
void Image::afficherConsole(){
    std::cout << dimx << " " << dimy << std::endl;
    for(int y=0; y<dimy; ++y) {
        for(int x=0; x<dimx; ++x) {
            Pixel& pix = getPix(x,y);
            std::cout << +pix.getRouge() << " " << +pix.getVert() << " " << +pix.getBleu() << " ";
        }
        std::cout << std::endl;
    }
}


void Image::afficher()
{
    
    /// Initialisation de la SDL
    if (SDL_Init(SDL_INIT_VIDEO) < 0) {
        std::cout << "Erreur lors de l'initialisation de la SDL : " << SDL_GetError() << std::endl;
        SDL_Quit();
        exit(1);
    }

    if (TTF_Init() != 0) {
        std::cout << "Erreur lors de l'initialisation de la SDL_ttf : " << TTF_GetError() << std::endl;
        SDL_Quit();
        exit(1);
    }

    int imgFlags = IMG_INIT_PNG | IMG_INIT_JPG;
    if( !(IMG_Init(imgFlags) & imgFlags)) {
        std::cout << "SDL_image could not initialize! SDL_image Error: " << IMG_GetError() << std::endl;
        SDL_Quit();
        exit(1);
    }



    /// Creation de la fenetre
    SDL_Window * window = SDL_CreateWindow("Module_Image", SDL_WINDOWPOS_CENTERED, SDL_WINDOWPOS_CENTERED, 200, 200, SDL_WINDOW_SHOWN | SDL_WINDOW_RESIZABLE);   //paramètres 2 et 3 définissent le coin haut gauche de la fenetre
    if (window == NULL) {                                                                                                                                          // 4 et 5 la dimension / puis fenetre visible et déformable
        std::cout << "Erreur lors de la creation de la fenetre : " << SDL_GetError() << std::endl; 
        SDL_Quit(); 
        exit(1);
    }

    ///Création du renderer
    SDL_Renderer * renderer = NULL;
    renderer = SDL_CreateRenderer(window,-1,SDL_RENDERER_ACCELERATED);

    
    ///////Création de la page //background
    SDL_Color gris = {180,180,180,255};

    SDL_SetRenderDrawColor(renderer, gris.r, gris.g, gris.b, gris.a); //ajoute la couleur au renderer
    SDL_RenderClear(renderer); //clean le renderer
    SDL_RenderPresent(renderer); //met à jour l'affichage


    ///Création de la surface et de la texture
    SDL_Surface * surface = NULL;
    SDL_Texture * texture = NULL;


    surface = IMG_Load("./data/testaffichage.ppm"); //load une image dans la surface
    surface = SDL_ConvertSurfaceFormat(surface, SDL_PIXELFORMAT_ARGB8888, 0); //conversion du format des pixels

    texture = SDL_CreateTextureFromSurface(renderer,surface); //charge la surface dans la texture

    
    //position de l'image finale
    SDL_Rect r; 
    r.x=0; r.y=0; r.w=200; r.h=200;
    

    /// Gestion des évèvenements
    bool quit = false;
    SDL_Event events;


    while (!quit)
    { 

         while (SDL_PollEvent (&events))
        {

            if (events.type == SDL_KEYDOWN)
            {

                switch (events.key.keysym.scancode)
                {
                    case SDL_SCANCODE_T:
                      r.x -= 10;
                      r.y -= 10;
                      r.w += 20;
                      r.h += 20;
                      break;

                   case SDL_SCANCODE_G:
                      r.x += 10;
                      r.y += 10;
                      r.w -= 20;
                      r.h -= 20;
                      break;     

                    case SDL_SCANCODE_ESCAPE:
                         quit = true;
                         break;

                    default: break;
              }
            } 

            ///affichage à l'écran 
            SDL_RenderClear(renderer); //remet à zero le renderer
            SDL_RenderCopy(renderer, texture, NULL, &r); //affichage avec le renderer et la texture
            SDL_RenderPresent(renderer); //met à jour l'affichage

        }
    }


    ///Destroy window
    SDL_DestroyWindow( window );


    ///Quit SDL subsystems
    SDL_Quit();
    
}


