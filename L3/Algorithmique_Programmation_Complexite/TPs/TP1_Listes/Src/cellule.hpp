#ifndef LIFAP6_LISTE_CELLULE_HPP
#define LIFAP6_LISTE_CELLULE_HPP

class Cellule {

  /* votre code ici */
  private:
  int valeur;
  Cellule * cellule_suivante; 

  public:
  Cellule();
  Cellule(int val, Cellule * cellule_suiv);
  ~Cellule();

  Cellule * getCelluleSuivante();
  void setCelluleSuivante(Cellule * cellule);
  int getValeur();
  void setValeur(int val);


} ;

#endif
