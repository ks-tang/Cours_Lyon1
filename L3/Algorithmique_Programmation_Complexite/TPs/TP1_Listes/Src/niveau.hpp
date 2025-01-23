#ifndef LIFAP6_LISTES_NIVEAU_HPP
#define LIFAP6_LISTES_NIVEAU_HPP

#include "liste.hpp"

class Niveau {

  public :

    /* construction */
    Niveau(short int hauteur, short int largeur) ;

    /* murs */
    Liste murs ;

    /* affichage */
    void dessiner() ;
} ;

#endif
