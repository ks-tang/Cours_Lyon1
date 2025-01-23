#include "Pixel.h"
#include <cassert>


//constructeurs
/// construit un pixel avec les paramètres à 0 (pixel noir)
Pixel::Pixel(){r=0; g=0; b=0;}

/// construit un pixel avec les paramètres donnés
Pixel::Pixel(const unsigned char nr, const unsigned char ng, const unsigned char nb)
{
    assert(nr<256);
    assert(ng<256);
    assert(nb<256);
    r=nr;
    g=ng;
    b=nb;
}

//gets
/// retourne la couleur du pixel rouge
unsigned char Pixel::getRouge() const {return r;} 

/// retourne la couleur du pixel vert
unsigned char Pixel::getVert() const {return g;}

/// retourne la couleur du pixel bleu
unsigned char Pixel::getBleu() const {return b;}

//sets
/// modifie la couleur du pixel rouge
void Pixel::setRouge(const unsigned int nr)
{
    assert(nr<256);
    r=nr;
}

/// modifie la couleur du vert
void Pixel::setVert(const unsigned int ng)
{
    assert(ng<256);
    g=ng;
}

/// modifie la couleur du pixel bleu
void Pixel::setBleu(const unsigned int nb)
{
    assert(nb<256);
    b=nb;
}



