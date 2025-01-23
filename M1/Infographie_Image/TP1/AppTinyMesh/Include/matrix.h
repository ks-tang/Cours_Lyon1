//MATRIX

#pragma once

#include <math.h>
#include <ostream>
#include "mathematics.h"

class Matrix
{
    private:
        double tab[3][3];

    public:
        Matrix();
        ~Matrix();
        void draw();

        Matrix &operator=(const Matrix &m);
        Matrix &operator+(const Matrix &m);
        Matrix &operator-(const Matrix &m);
        Matrix &operator*(const Matrix &m);

        Vector operator*(const Vector &v);

        //
        Matrix rotationX(const double radius);
        Matrix rotationY(const double radius);
        Matrix rotationZ(const double radius);
        void rotateX(const double radius);
        void rotateY(const double radius);
        void rotateZ(const double radius);
        Matrix homothetie(const double lambda);
        Matrix transpose();
        Matrix comatrice();
        Matrix inverse();

        void setMatrix(int i, int j, double v);
        double getMatrix(int i, int j);
};