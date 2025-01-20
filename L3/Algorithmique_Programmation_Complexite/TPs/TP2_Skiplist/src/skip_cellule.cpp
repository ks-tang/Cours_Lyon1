#include "skip_cellule.hpp"

SkipCellule::SkipCellule(int v) : valeur(v) {
  suivante.push_back(nullptr) ;
}

SkipCellule::~SkipCellule() {
  delete suivante[0] ;
}
