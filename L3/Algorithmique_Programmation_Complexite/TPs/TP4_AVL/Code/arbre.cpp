#include "arbre.hpp"

#include <iostream>

Arbre::Arbre() : 
  m_racine(nullptr)
{}

Arbre::~Arbre() {
  delete m_racine ;
}

static Noeud* inserer_noeud(Noeud* n, int v) {
  if(n) {
    if(v > n->valeur) {
      n->droite = inserer_noeud(n->droite, v) ;
    } else {
      n->gauche = inserer_noeud(n->gauche, v) ;
    }
    return n ;
  } else {
    return new Noeud(v) ;
  }
}

void Arbre::inserer(int v) {
  m_racine = inserer_noeud(m_racine, v) ;
}

#ifndef AFFICHAGE_SIMPLE

static const char* SPLIT = "\xe2\x94\xa4" ;
static const char* V_BRANCH = "\xe2\x94\x82" ;
static const char* H_BRANCH = "\xe2\x94\x80" ;
static const char* UPPER_BRANCH = "\xe2\x95\xad" ;
static const char* LOWER_BRANCH = "\xe2\x95\xb0" ;

#else

static const char* SPLIT = "+" ;
static const char* V_BRANCH = "|" ;
static const char* H_BRANCH = "-" ;
static const char* UPPER_BRANCH = "+" ;
static const char* LOWER_BRANCH = "+" ;

#endif

static void afficher_noeud(Noeud* n, int profondeur, int code) {
  int largeur = 5 ;
  if(n) {
    afficher_noeud(n->droite, profondeur+1, code*2+1) ;
    int i ;
    for(i = 0; i < profondeur-1; ++i) {
      if(((code >> (profondeur-i-1)) & 1 ) != ((code >> (profondeur-i-2)) & 1)) {
        std::cout << V_BRANCH ;
      } else {
        std::cout << " " ;
      }
      for(int l = 0; l < largeur; ++l) {
        std::cout << " " ;
      }
    }
    if(code%2) {
      std::cout << UPPER_BRANCH ;
    } else {
      if(profondeur) {
        std::cout << LOWER_BRANCH ;
      }
    }
    if(profondeur) {
      for(int l = 0; l < largeur; ++l) {
        std::cout << H_BRANCH ;
      }
    }
    std::cout << SPLIT << n->valeur /*<< " -- " << n->hauteur*/ << std::endl ;
    afficher_noeud(n->gauche, profondeur+1, code*2) ;
  }
}

void Arbre::afficher() {
    afficher_noeud(m_racine, 0, 0) ;
}
