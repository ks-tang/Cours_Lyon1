#include "sphere.h"

Sphere::Sphere(const Vector v, const double r, const int nbV, const int nbH){
    center = v;
    radius = r;
    nbVertical = nbV;
    nbHorizontal = nbH;
}

double Sphere::getRadius() const {
    return this->radius;
}

Vector Sphere::getCenter() const {
    return this->center;
}

int Sphere::getNbHorizontal() const {
    return this->nbHorizontal;
}

int Sphere::getNbVertical() const {
    return this->nbVertical;
}
