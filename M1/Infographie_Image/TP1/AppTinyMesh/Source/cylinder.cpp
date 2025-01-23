#include "cylinder.h"

Cylinder::Cylinder(const Vector v, const double r, const double l, const int nbD ){
    center = v;
    radius = r;
    length = l;
    nbDiv = nbD;
}

int Cylinder::getNbDiv() const {
    return this->nbDiv;
}

double Cylinder::getLength() const {
    return this->length;
}