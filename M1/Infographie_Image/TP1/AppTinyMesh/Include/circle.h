#pragma once

#include <vector>
#include <iostream>

#include "mathematics.h"

class Circle {
    protected:
    double radius;
    Vector center;
    int nbDiv;
    public:
    Circle() {};
    Circle(const Vector v, const double r, const int nbD);
    ~Circle() {};

    int getNbDiv() const;

    // Acces to vertices
    Vector Center() const;
    Vector Vertex(int) const;
    double Radius() const;
    int NbDiv() const;
};

//! Returns the center of the circle.
inline Vector Circle::Center() const {
    return center;
}

inline double Circle::Radius() const {
    return radius;
}

inline int Circle::NbDiv() const {
    return nbDiv;
}