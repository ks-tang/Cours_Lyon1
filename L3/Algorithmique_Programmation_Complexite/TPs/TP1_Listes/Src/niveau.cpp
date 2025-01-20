#include "niveau.hpp"
#include "coordonnees.hpp"

#include <ncurses.h>

Niveau::Niveau(short int hauteur, short int largeur) {
  /* bords horizontaux */
  for(int i = 0; i < largeur; ++i) {
    murs.ajouter_en_tete(encode(0, i)) ;
    murs.ajouter_en_tete(encode(hauteur - 1, i)) ;
  }

  /* bords verticaux */
  for(int i = 0; i < largeur; ++i) {
    murs.ajouter_en_tete(encode(i, 0)) ;
    murs.ajouter_en_tete(encode(i, largeur - 1)) ;
  }
}

void Niveau::dessiner() {
  /* couleur bleue */
  init_pair(1, COLOR_BLUE, COLOR_BLUE) ;
  attron(COLOR_PAIR(1)) ;

  /* dessin des murs */
  Cellule* mur = murs.tete() ;
  while(mur) {
    short int ml = ligne(mur->valeur) ;
    short int mc =  colonne(mur->valeur) ;
    mvaddch(ml, 2*mc, ' ') ;
    mvaddch(ml, 2*mc + 1, ' ') ;
    mur = mur->suivante ;
  }

  /* couleur normale */
  attroff(COLOR_PAIR(1)) ;
}
