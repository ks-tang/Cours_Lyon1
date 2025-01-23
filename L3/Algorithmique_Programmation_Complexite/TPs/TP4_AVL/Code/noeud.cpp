#include "noeud.hpp"

Noeud::Noeud(int v) :
  gauche(nullptr),
  droite(nullptr),
  valeur(v)
{}

Noeud::~Noeud() {
  delete gauche ;
  delete droite ;
}
