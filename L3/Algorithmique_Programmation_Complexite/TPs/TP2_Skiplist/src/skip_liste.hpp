#ifndef LIFAP6_SKIP_LISTE_HPP
#define LIFAP6_SKIP_LISTE_HPP

#include "skip_cellule.hpp"

#include <random>

class SkipListe {
  public :

    /* construction, destruction */
    SkipListe() ;
    ~SkipListe() ;

    /* suppression de la copie pour le moment */
    SkipListe(const SkipListe&) = delete ;
    SkipListe& operator=(const SkipListe&) = delete ;

    /* insertion triee */
    void inserer(int v) ;

    /* recherche */
    SkipCellule* chercher(int v) ;

    /* affichage */
    void afficher() ;

    /* tests */
    bool test_tri() ;

    /* ajouter un niveau */
    void ajouter_niveau() ;

  private :

    /* la sentinelle servira a stocker les tetes de chaque niveau */
    SkipCellule* m_sentinelle ;

    /* generateur de nombres aleatoires pour les pile ou face */
    std::default_random_engine m_random ;
    std::bernoulli_distribution m_piece ;
    bool pile_ou_face() ;
} ;

#endif
