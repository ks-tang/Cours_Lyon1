#pragma once

#include <vector>
#include <iostream>

#include "mathematics.h"

class Demisphere {
    protected:
    double radius;
    Vector center;
    int nbVertical;
    int nbHorizontal;
    public:
    Demisphere() {};
    Demisphere(const Vector v, const double r, const int nbVertical, const int nbHorizontal);
    ~Demisphere() {};

    double getRadius() const;
    Vector getCenter() const;
    int getNbVertical() const;
    int getNbHorizontal() const;

    // Acces to vertices
    Vector Center() const;
    Vector Vertex(int) const;
    double Radius() const;
    double Length() const;
    int NbVertical() const;
    int NbHorizontal() const;
};

//! Returns the center of the Cylinder.
inline Vector Demisphere::Center() const {
    return center;
}

inline double Demisphere::Radius() const {
    return radius;
}

inline int Demisphere::NbVertical() const {
    return nbVertical;
}

inline int Demisphere::NbHorizontal() const {
    return nbHorizontal;
}
