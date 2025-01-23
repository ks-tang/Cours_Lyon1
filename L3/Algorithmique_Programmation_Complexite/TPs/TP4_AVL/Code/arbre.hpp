#pragma once

#include "noeud.hpp"

class Arbre {

  public :

    Arbre() ;
    ~Arbre() ;

    void inserer(int valeur) ;
    void afficher() ;

  private :

    Noeud* m_racine ;
} ;
