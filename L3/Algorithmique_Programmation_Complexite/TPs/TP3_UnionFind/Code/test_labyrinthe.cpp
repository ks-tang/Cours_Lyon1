#include "labyrinthe.hpp"
#include "unionfind.hpp"

int main()
{
  Labyrinthe lab(20, 20) ;
  std::cout << lab << std::endl ;
/*
  lab.abattre_tous_les_murs() ;
  std::cout << lab << std::endl ;

  lab.monter_tous_les_murs() ;
  std::cout << lab << std::endl ;

  lab.abattre_mur(0,0,0,1) ;
  lab.abattre_mur(0,1,1,1) ;
  std::cout << lab << std::endl ;

  lab.monter_mur(0,0,0,1) ;
  std::cout << lab << std::endl ;

  ////////////////////////
  std::cout << lab.tab_murs.taille() << std::endl;
  for(int i=0; i<100; i++)
  { 
    Mur m = lab.tab_murs.retirer();
    lab.abattre_mur(m.l1, m.c1, m.l2, m.c2);
  }
  std::cout << lab.tab_murs.taille() << std::endl;
  std::cout << lab << std::endl ;

  */




  return 0 ;
}
