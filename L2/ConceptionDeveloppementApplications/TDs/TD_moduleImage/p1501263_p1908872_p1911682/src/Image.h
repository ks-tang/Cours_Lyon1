#ifndef IMAGE
#define IMAGE

#include "Pixel.h"
#include <string>

#include <SDL2/SDL.h>
#include <SDL2/SDL_ttf.h>
#include <SDL2/SDL_image.h>
#include <SDL2/SDL_mixer.h>

/**definition d'une image */
class Image{

 private :

   /** les dimensions de l'image */
   int dimx, dimy;

   /** le tableau 1D de pixel */
   Pixel * tab;    

  /*
   SDL_Window * window;
   SDL_Surface * surface; //image sur CPU
   SDL_Texture * texture; //image sur GPU
  */


 public :

   /** @brief Constructeur par defaut de la classe: initialise dimx et dimy a 0,
    ce constructeur n'alloue pas de pixel */
   Image (); ///constructeur image sans paramètre

   /** @brief Constructeur de la classe: initialise dimx et dimy (apr�s v�rification)
    puis alloue le tableau de pixel dans le tas (image noire)
    @param dimensionX dimension x du tableau @param dimensionY dimension y du tableau */
   Image (const int dimensionX, const int dimensionY); //constructeur image avec paramètres des dimensions x et y */

   /** @brief Destructeur de la classe: d�allocation de la m�moire du tableau de pixels
    et mise � jour des champs dimx et dimy � 0 */
   ~Image ();

   /** @brief Accesseur : r�cup�re le pixel original de coordonn�es (x,y) en v�rifiant leur validit�
    la formule pour passer d'un tab 2D � un tab 1D est tab[y*dimx+x]
    @param x dimension x du tableau de pixels @param y dimension y du tableau de pixels */
   Pixel & getPix ( const int x, const int y) const;

   /** @brief Mutateur : modifie le pixel de coordonn�es (x,y) 
    @param x dimension x du tableau de pixels @param y dimension y du tableau de pixels @param couleur un Pixel de couleur */
   void setPix (const int x, const int y, const Pixel couleur);

   /** @brief Dessine un rectangle plein de la couleur dans l'image (en utilisant setPix, indices en param�tre compris) 
      @param Xmin valeur x d'un coin d'un rectangle @param Ymin valeur y du meme coin d'un rectangle
      @param Xmax valeur x d'un coin d'un rectangle (opposé de Xmin) @param Ymax valeur y du meme coin d'un rectangle (opposé de Ymin) 
      @param couleur un Pixel de couleur */
   void dessinerRectangle (const int Xmin, const int Ymin, const int Xmax, const int Ymax, const Pixel couleur);

   /** @brief Efface l'image en la remplissant de la couleur en param�tre
    (en appelant dessinerRectangle avec le bon rectangle)
    @param couleur un Pixel de couleur */
   void effacer (const Pixel couleur);

   /**  @brief Effectue une s�rie de tests v�rifiant que le module fonctionne et
    que les donn�es membres de l'objet sont conformes */
   static void testRegression ();


   /** @brief sauver une image dans un fichier en paramètres
   @param filename chemin absolu ou relatif vers un fichier */
   void sauver(const std::string & filename);

   /** @brief ouvrir une image depuis un fichier en paramètre
    @param filename chemin absolu ou relatif vers un fichier */
   void ouvrir(const std::string & filename);

   /** @brief afficher les valeurs des pixels sur la console */
   void afficherConsole();


   /** @brief Affiche l'image dans une fenêtre SDL2 */
   void afficher ();


};



#endif
