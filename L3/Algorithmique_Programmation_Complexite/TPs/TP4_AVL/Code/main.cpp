#include "arbre.hpp"

#include <random>

int main() {
  Arbre a ;

  std::mt19937 alea ;

#ifndef DEBUG
  std::random_device vrai_alea ;
  alea.seed(vrai_alea()) ;
#endif

  std::uniform_int_distribution<int> nombre(0, 100) ;

  for(int i = 0; i < 20; ++i) {
    a.inserer(nombre(alea)) ;
  }

  a.afficher() ;

  return 0 ;
}
