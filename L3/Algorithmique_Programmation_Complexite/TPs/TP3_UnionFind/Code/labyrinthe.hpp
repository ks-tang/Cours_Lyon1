#ifndef LIFAP6_LABYRINTHE_HPP_
#define LIFAP6_LABYRINTHE_HPP_

#include <iostream>
#include <vector>
#include "unionfind.hpp"
#include "melangeur.hpp"

using Tuile = char ;

struct Mur
{
    int l1, c1;
    int l2, c2;

};

class Labyrinthe {

  public :

    Melangeur<Mur> tab_murs;

    /*** Construction ***/

    //construction d'un nouveau labyrinthe
    //largeur et hauteur sont les dimensions du labyrinthe
    Labyrinthe(int largeur, int hauteur) ;

    /*** Gestion des murs ***/

    //creer un mur entre les cases (l1,c1) et (l2,c2)
    void monter_mur(int l1, int c1, int l2, int c2) ;

    //monter tous les murs du labyrinthe
    void monter_tous_les_murs() ;

    //abattre le mur entre les cases (l1,c1) et (l2,c2)
    void abattre_mur(int l1, int c1, int l2, int c2) ;

    //abattre tous les murs
    void abattre_tous_les_murs() ;

    //consulter la presence d'un mur entre les cases (l1,c1) et (l2,c2)
    bool mur(int l1, int c1, int l2, int c2) const ;

    /*** Affichage ***/

    //cette fonction pourra acceder aux membres prives
    friend std::ostream& operator<<(std::ostream&, const Labyrinthe&) ;

    //mélanger le tableau de murs
    void melanger_tab_murs();

    void doUF();

  private :

    //utilitaire pour recuperer la tuile (l,c) et verifier les bornes
    Tuile tuile(int l, int c) const ;

    int m_largeur ;
    int m_hauteur ;
    std::vector<Tuile> m_tuiles ;
    UnionFind uf;

    
} ;

std::ostream& operator<<(std::ostream&, const Labyrinthe&) ;



#endif
