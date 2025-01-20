#ifndef LIFAP6_LISTE_LISTE_HPP
#define LIFAP6_LISTE_LISTE_HPP

#include "cellule.hpp"

class Liste {

  public :

    /* construction d'une liste vide */
    Liste() ;

    /* construction par copie */
    Liste(const Liste& autre) ;

    /* affectation */
    Liste& operator=(const Liste& autre) ;

    /* destruction */
    ~Liste() ;

    /* ajout en tete */
    void ajouter_en_tete(int valeur) ;

    /* ajout en queue */
    void ajouter_en_queue(int valeur) ;

    /* suppression en tete */
    void supprimer_en_tete() ;

    /* consultation de la tete */
    const Cellule* tete() const ;
    Cellule* tete() ;

    /* consultation de la queue */
    const Cellule* queue() const ;
    Cellule* queue() ;

    /* taille de la liste */
    int taille() const ;

    /* recherche dans la liste */
    const Cellule* recherche(int valeur) const ;
    Cellule* recherche(int valeur) ;

    /* affichage */
    void afficher() const ;

  private :

    /* votre code ici */
    int nb_elements_liste;
    Cellule * premiere_cellule;

} ;

#endif
