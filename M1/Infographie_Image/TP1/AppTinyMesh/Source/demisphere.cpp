#include "demisphere.h"

Demisphere::Demisphere(const Vector v, const double r, const int nbV, const int nbH){
    center = v;
    radius = r;
    nbVertical = nbV;
    nbHorizontal = nbH;
}

double Demisphere::getRadius() const {
    return this->radius;
}

Vector Demisphere::getCenter() const {
    return this->center;
}

int Demisphere::getNbHorizontal() const {
    return this->nbHorizontal;
}

int Demisphere::getNbVertical() const {
    return this->nbVertical;
}
