#ifndef LIFAP6_LISTE_SERPENT_HPP
#define LIFAP6_LISTE_SERPENT_HPP

#include "liste.hpp"
#include "niveau.hpp"

class Serpent {

  public :

    /* creation a une position donnee */
    Serpent(short int ligne, short int colonne) ;

    /* direction courante de deplacement */
    enum Direction {
      DROITE,
      HAUT,
      GAUCHE,
      BAS
    } ;

    Direction direction ;

    /* deplacement */
    void avancer(const Niveau& niveau) ;

    /* dessin */
    void dessiner() ;
    void rafraichir() ;

  private :

    /* etat du serpent */
    enum Etat {
      STABLE,
      GRANDISSANT
    } ;

    Etat m_etat ;
    int m_jauge ;

    /* morceaux de serpent */
    Liste m_segments ;

} ;

#endif
