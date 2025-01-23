#include "cellule.hpp"


Cellule::Cellule()
{
    valeur = 0;
    cellule_suivante = nullptr;
}

Cellule::Cellule(int val, Cellule * cellule_suiv)
{
    valeur = val;
    cellule_suivante = cellule_suiv;
}

Cellule::~Cellule()
{

}

Cellule * Cellule::getCelluleSuivante()
{
    return cellule_suivante;
}

void Cellule::setCelluleSuivante(Cellule * cellule)
{
    this->cellule_suivante = cellule;
}

int Cellule::getValeur()
{
    return valeur;
}

void Cellule::setValeur(int val)
{
    this->valeur = val;
}