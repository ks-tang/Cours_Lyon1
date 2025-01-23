#include "tore.h"

Tore::Tore( const Circle skel, const double r, const int nbD){
    skeleton = skel;
    radius = r;
    nbDiv = nbD;
}

double Tore::getRadius() const {
    return this->radius;
}

Circle Tore::getSkeleton() const {
    return this->skeleton;
}

int Tore::getNbDiv() const {
    return this->nbDiv;
}