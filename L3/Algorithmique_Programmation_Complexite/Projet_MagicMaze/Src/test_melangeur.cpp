#include "melangeur.hpp"

#include <iostream>
#include <cassert>
#include <cstdlib>

using namespace MMaze ;

void test_taille() {
  //test des tailles : ce test verifie que l'evolution de la taille du
  //melangeur est correcte au fur et a mesure des insertions et retraits
  Melangeur<int> m ;
  unsigned int size = 1000 ;
  for(unsigned int i = 0; i < size; ++i) {
    m.inserer(i) ;
    assert(m.taille() == i+1) ;
  }
  int iterations = 1000000 ;
  for(int i = 0; i < iterations; ++i) {
    unsigned int t = m.taille() ;
    if(t > 0 && (rand() % 2)) {
      m.retirer() ;
      assert(m.taille() == t - 1) ;
    } else {
      m.inserer(i) ;
      assert(m.taille() == t + 1) ;
    }
  }
}

void test_melange() {
  //le test suivant compte combien de fois l'element 0 sort avant l'element
  //size-1 dans les melanges. Le test est correct si le ratio obtenu a la fin
  //est proche de 0.5 car les deux elements ont autant de chances d'etre dans un
  //ordre que dans l'autre.
  Melangeur<int> m ;
  int size = 1000 ;
  int iterations = 10000 ;
  int permutation_count = 0 ;
  int elem ;
  for(int iter = 0; iter < iterations; ++iter) {
    for(int i = 0; i < size; ++i) {
      m.inserer(i) ;
    }
    bool notfound = true ;
    for(int i = 0; i < size; ++i) {
      elem = m.retirer() ;
      if(notfound && elem == 0) {
        ++ permutation_count ;
      }
      if(elem == size - 1) {
        notfound = false ;
      }
    }
  }

  std::cout 
    << "ratio de melange : " 
    << (double) permutation_count / (double) iterations 
    << std::endl ;
}

void test_permutation() {
  //le test suivant verifie que tous les elements fournis en entree ressortent
  //si on les retire tous. un message d'erreur s'affichera si certains elements
  //sortants du melangeur ne sont pas dans la bonne plage de valeurs ()
  Melangeur<int> m ;
  const int size = 1000 ;
  bool found[size] = {0} ;
  int elem ;
  for(int i = 0; i < size; ++i) {
    m.inserer(i) ;
  }
  for(int i = 0; i < size; ++i) {
    elem = m.retirer() ;
    assert(elem >= 0 && elem < size) ;
    found[elem] = true ;
  }
  for(int i = 0; i < size; ++i) {
    if(!found[i]) {
      std::cout 
        << "erreur : le melange n'a pas retourne toutes les valeurs" 
        << std::endl ;
    }
  }
}

void test_insertion_retrait() {
  //le test verifie que chaque element present dans le melangeur a autant de
  //chances de sortir, meme apres des insertions et retraits. Normalement a la
  //fin, tous les elements sont sortis a peu pres autant de fois les uns que
  //les autres, et le minimum et maximum d'apparitions sont proches de
  //iterations / size ;
  Melangeur<int> m ;
  const int size = 1000 ;
  int iterations = 1000000 ;
  int out_counts[size] ;
  int elem ;
  for(int i = 0; i < size; ++i) {
    m.inserer(i) ;
    out_counts[i] = 0 ;
  }
  for(int iter = 0; iter < iterations; ++iter) {
    elem = m.retirer() ;
    m.inserer(elem) ;
    assert(elem >= 0 && elem < size) ;
    ++out_counts[elem] ;
  }
  int min = out_counts[0] ;
  int max = min ;
  for(int i = 1; i < size; ++i) {
    min = out_counts[i] < min ? out_counts[i] : min ;
    max = out_counts[i] > max ? out_counts[i] : max ;
  }

  std::cout
    << "minimum d'apparitions : " << min << std::endl
    << "maximum d'apparitions : " << max << std::endl
    << "nombre d'apparitions souhaitable : " 
    << (double) iterations / (double) size 
    << std::endl ;
}

int main() {

  MelangeurOptions::imprevisible() ;

  test_taille() ;
  test_melange() ;
  test_permutation() ;
  test_insertion_retrait() ;

  return 0 ;
}
