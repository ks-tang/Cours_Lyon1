#include "liste.hpp"

#include <iostream>
#include <cassert>

Liste::Liste() {
  /* votre code ici */
  nb_elements_liste = 0;
  premiere_cellule = nullptr;
}

Liste::Liste(const Liste& autre) {
  /* votre code ici */
  nb_elements_liste = autre.nb_elements_liste;
  premiere_cellule = autre.premiere_cellule;
}

Liste& Liste::operator=(const Liste& autre) {
  /* votre code ici */
  nb_elements_liste = autre.nb_elements_liste;
  premiere_cellule = autre.premiere_cellule;
  return *this ;
}

Liste::~Liste() {
  /* votre code ici */
  
}

void Liste::ajouter_en_tete(int valeur) {
  /* votre code ici */
  Cellule * nouvelle_cell = new Cellule(valeur, premiere_cellule);
  premiere_cellule = nouvelle_cell;

  nb_elements_liste++;
}

void Liste::ajouter_en_queue(int valeur) {
  /* votre code ici */
  Cellule * c = premiere_cellule;
  Cellule * nouvelle_cell = new Cellule(valeur, nullptr);

  while(c->getCelluleSuivante() != nullptr)
  {
    c = c->getCelluleSuivante();
  }

  c->setCelluleSuivante(nouvelle_cell);
  nb_elements_liste++;
  
}

void Liste::supprimer_en_tete() {
  /* votre code ici */
  premiere_cellule = premiere_cellule->getCelluleSuivante();;
  delete premiere_cellule;
}

Cellule* Liste::tete() {
  /* votre code ici */
  return premiere_cellule;
}

const Cellule* Liste::tete() const {
  /* votre code ici */
  return premiere_cellule ;
}

Cellule* Liste::queue() {
  /* votre code ici */
  Cellule * c = premiere_cellule;

  while(c != nullptr)
  {
    c = c->getCelluleSuivante();
  }
  return c ;
}

const Cellule* Liste::queue() const {
  /* votre code ici */
  Cellule * c = premiere_cellule;

  while(c != nullptr)
  {
    c = c->getCelluleSuivante();
  }
  return c ;
}

int Liste::taille() const {
  /* votre code ici */
  return nb_elements_liste;
}

Cellule* Liste::recherche(int valeur) {
  /* votre code ici */
  Cellule * c = premiere_cellule;

  while(c != nullptr)
  {
    if(c->getValeur() == valeur){
      return c;
    } else {
      c = c->getCelluleSuivante();
    }
    
  }
  return nullptr;
}

const Cellule* Liste::recherche(int valeur) const {
  /* votre code ici */
  Cellule * c = premiere_cellule;

  while(c != nullptr)
  {
    if(c->getValeur() == valeur){
      return c;
    } else {
      c = c->getCelluleSuivante();
    }
    
  }
  return nullptr;
}

void Liste::afficher() const {
  /* votre code ici */
  Cellule * c = premiere_cellule;

  std::cout << "[ ";
  while (c != nullptr)
  {
    std::cout << c->getValeur() << " ";
    c = c->getCelluleSuivante();
  }
  std::cout << "]" << std::endl;

}
