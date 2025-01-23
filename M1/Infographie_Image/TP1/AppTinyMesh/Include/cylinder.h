#pragma once

#include <vector>
#include <iostream>

#include "mathematics.h"

class Cylinder {
    protected:
    double radius;
    Vector center;
    double length;
    int nbDiv;
    public:
    Cylinder() {};
    Cylinder(const Vector v, const double r, const double l, const int nbD);
    ~Cylinder() {};

    //accesseur
    int getNbDiv() const;
    double getLength() const;

    // Acces to vertices
    Vector Center() const;
    Vector Vertex(int) const;
    double Radius() const;
    double Length() const;
    int NbDiv() const;
};

//! Returns the center of the Cylinder.
inline Vector Cylinder::Center() const {
    return center;
}

inline double Cylinder::Radius() const {
    return radius;
}

inline double Cylinder::Length() const {
    return length;
}

inline int Cylinder::NbDiv() const {
    return nbDiv;
}
