#pragma once

struct Noeud {
  Noeud(int valeur) ;
  ~Noeud() ;

  Noeud* gauche ;
  Noeud* droite ;
  int valeur ;
} ;
