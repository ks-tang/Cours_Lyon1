#ifndef PIXEL
#define PIXEL

/** définition d'un pixel */
class Pixel {

 private :

  /**  les composantes du pixel, unsigned char en C++  (0..255) */
   unsigned char r,g,b; 

 public :

   /** @brief Constructeur par d�faut de la classe: initialise le pixel � la couleur noire */
   Pixel ();

   /** @brief Constructeur de la classe: initialise r,g,b avec les param�tres
    @param nr couleur rouge @param ng couleur verte @param nb couleur bleue */
   Pixel (const unsigned char nr, const unsigned char ng, const unsigned char nb);

   /** @brief Accesseur : r�cup�re la composante rouge du pixel */
   unsigned char getRouge() const;

   /** @brief Accesseur : r�cup�re la composante verte du pixel */
   unsigned char getVert() const;

   /** @brief Accesseur : r�cup�re la composante bleue du pixel */
   unsigned char getBleu() const;

   /** @brief Mutateur : modifie la composante rouge du pixel
    @param nr couleur rouge */
   void setRouge(const unsigned int nr);

   /** @brief Mutateur : modifie la composante verte du pixel 
    @param ng couleur verte */
   void setVert(const unsigned int ng);

   /** @brief Mutateur : modifie la composante bleue du pixel 
    @param nb couleur bleue */
   void setBleu(const unsigned int nb);



};

#endif
