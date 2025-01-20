#include "serpent.hpp"
#include "coordonnees.hpp"

#include <stdexcept>
#include <cassert>
#include <ncurses.h>

Serpent::Serpent(short int ligne, short int colonne) {
  direction = HAUT ;
  m_etat = GRANDISSANT ;
  m_jauge = 5 ;
  m_segments.ajouter_en_queue(encode(ligne, colonne)) ;
}

void Serpent::avancer(const Niveau& niveau) {
  /* position de la tete */
  int tl = ligne(m_segments.queue()->valeur) ;
  int tc = colonne(m_segments.queue()->valeur) ;

  /* deplacement de la tete dans la bonne direction */
  switch(direction) {
    case GAUCHE :
      --tc ;
      break ;
    case HAUT :
      --tl ;
      break ;
    case DROITE :
      ++tc ;
      break ;
    case BAS :
      ++tl ;
      break ;
    default :
      assert(0 && "direction inconnue") ;
      break ;
  }

  /* suppression de la queue si stable */
  if(m_etat == STABLE) {
    m_segments.supprimer_en_tete() ;
  } else {
    --m_jauge ;
    if(m_jauge == 0) {
      m_etat = STABLE ;
    }
  }

  /* test de collision */
  int tete = encode(tl, tc) ;
  if(m_segments.recherche(tete)) {
    throw(std::runtime_error("collision")) ;
  }
  if(niveau.murs.recherche(tete)) {
    throw(std::runtime_error("collision")) ;
  }

  /* ajout de la tete deplacee */
  m_segments.ajouter_en_queue(tete) ;
}

void Serpent::dessiner() {
  /* couleur bleue */
  init_pair(1, COLOR_BLUE, COLOR_BLUE) ;
  attron(COLOR_PAIR(1)) ;
  /* itération sur les segments */
  Cellule* segment = m_segments.tete() ;
  while(segment) {
    short int sl = ligne(segment->valeur) ;
    short int sc = colonne(segment->valeur) ;
    mvaddch(sl, 2*sc, ' ') ;
    mvaddch(sl, 2*sc + 1, ' ') ;
    segment = segment->suivante ;
  }
  /* couleur normale */
  attroff(COLOR_PAIR(1)) ;
}

void Serpent::rafraichir() {
  /* couleur bleue */
  init_pair(1, COLOR_BLUE, COLOR_BLUE) ;
  attron(COLOR_PAIR(1)) ;
  /* dessin de la tete */
  short int tl = ligne(m_segments.queue()->valeur) ;
  short int tc = colonne(m_segments.queue()->valeur) ;
  mvaddch(tl, 2*tc, ' ') ;
  mvaddch(tl, 2*tc + 1, ' ') ;
  /* couleur normale */
  attroff(COLOR_PAIR(1)) ;
  if(m_etat == STABLE) {
    /* effacement de la queue */
    short int ql = ligne(m_segments.tete()->valeur) ;
    short int qc = colonne(m_segments.tete()->valeur) ;
    mvaddch(ql, 2*qc, ' ') ;
    mvaddch(ql, 2*qc + 1, ' ') ;
  }
}
