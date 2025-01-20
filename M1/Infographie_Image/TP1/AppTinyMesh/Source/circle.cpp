#include "circle.h"

Circle::Circle(const Vector v, const double r, const int nbD){
    center = v;
    radius = r;
    nbDiv = nbD;
}

int Circle::getNbDiv() const {
    return this->nbDiv;
}