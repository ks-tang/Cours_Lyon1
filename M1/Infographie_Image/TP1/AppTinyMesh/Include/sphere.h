#pragma once

#include <vector>
#include <iostream>

#include "mathematics.h"

class Sphere {
    protected:
    double radius;
    Vector center;
    int nbVertical;
    int nbHorizontal;
    public:
    Sphere() {};
    Sphere(const Vector v, const double r, const int nbVertical, const int nbHorizontal);
    ~Sphere() {};

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
inline Vector Sphere::Center() const {
    return center;
}

inline double Sphere::Radius() const {
    return radius;
}

inline int Sphere::NbVertical() const {
    return nbVertical;
}

inline int Sphere::NbHorizontal() const {
    return nbHorizontal;
}
