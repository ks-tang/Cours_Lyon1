#pragma once

#include <vector>
#include <iostream>
#include "circle.h"

#include "mathematics.h"

class Tore {
    protected:
    double radius;
    Circle skeleton;
    int nbDiv;
    public:
    Tore() {};
    Tore(const Circle skel, const double r, const int nbD);

    Circle getSkeleton() const;
    double getRadius() const;
    int getNbDiv() const;

    // Acces to vertices
    Vector Vertex(int) const;
    double Radius() const;
    Circle Skeleton() const;
    int NbDiv() const;

};

inline double Tore::Radius() const {
    return radius;
}

inline Circle Tore::Skeleton() const {
    return skeleton;
}

inline int Tore::NbDiv() const {
    return nbDiv;
}