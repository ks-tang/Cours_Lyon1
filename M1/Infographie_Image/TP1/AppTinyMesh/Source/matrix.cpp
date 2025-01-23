#include "matrix.h"
#include <iostream>

using namespace std;

Matrix::Matrix(){
   for(int i=0; i<3; i++) {
        for (int j=0; j<3; j++) {
            tab[i][j]=0;
        }
   }
}

Matrix::~Matrix(){}

void Matrix::draw() {
   for (int i=0; i<3; i++){
        for (int j=0; j<3; j++){
            std::cout << tab[i][j] << " ";
        }
        cout << endl;
   }
}

Matrix& Matrix::operator=(const Matrix &m) {
    for(int i=0; i<3; i++) {
        for (int j=0; j<3; j++) {
            this->tab[i][j]=m.tab[i][j];
        }
   }
   return *this;
}

Matrix& Matrix::operator+(const Matrix &m){
    for (int i=0; i<3; i++){
        for(int j=0; j<3; j++){
            this->tab[i][j] = this->tab[i][j]+m.tab[i][j];
        }
    }
    return *this;
}


Matrix& Matrix::operator*(const Matrix &m){
    Matrix tmp;
    for (int i=0; i<3; i++){
        for(int j=0; j<3; j++){
            for(int k=0; k<3; k++) {
                tmp.tab[i][j] += this->tab[i][k]*m.tab[k][j];
            }
        }
    }
    return tmp;
}

Vector Matrix::operator*(const Vector &v){
    Vector res;
    res[0] = v[0]*tab[0][0] + v[1] * tab[0][1] + v[2] * tab[0][2];
    res[1] = v[0]*tab[1][0] + v[1] * tab[1][1] + v[2] * tab[1][2];
    res[2] = v[0]*tab[2][0] + v[1] * tab[2][1] + v[2] * tab[2][2];
    return res;
}

Matrix& Matrix::operator-(const Matrix &m){
    for (int i=0; i<3; i++){
        for(int j=0; j<3; j++){
            this->tab[i][j] = this->tab[i][j]-m.tab[i][j];
        }
    }
    return *this;
}

void Matrix::setMatrix(int i, int j, double v) {
    tab[i][j] = v;
}

Matrix Matrix::rotationX(const double radius) {
    double rad = radius*M_PI/180;
    Matrix rX;

    rX.setMatrix(0,0,1);
    rX.setMatrix(0,1,0);
    rX.setMatrix(0,2,0);
    rX.setMatrix(1,0,0);
    rX.setMatrix(1,1,cos(rad));
    rX.setMatrix(1,2,-sin(rad));
    rX.setMatrix(2,0,0);
    rX.setMatrix(2,1,sin(rad));
    rX.setMatrix(2,2,cos(rad));

    return rX;
}

Matrix Matrix::rotationY(const double radius) {
    double rad = radius*M_PI/180;
    Matrix rY;

    rY.setMatrix(0,0,cos(rad));
    rY.setMatrix(0,1,0);
    rY.setMatrix(0,2,sin(rad));
    rY.setMatrix(1,0,0);
    rY.setMatrix(1,1,1);
    rY.setMatrix(1,2,0);
    rY.setMatrix(2,0,-sin(rad));
    rY.setMatrix(2,1,0);
    rY.setMatrix(2,2,cos(rad));

    return rY;
}

Matrix Matrix::rotationZ(const double radius) {
    double rad = radius*M_PI/180;
    Matrix rZ;

    rZ.setMatrix(0,0,cos(rad));
    rZ.setMatrix(0,1,-sin(rad));
    rZ.setMatrix(0,2,0);
    rZ.setMatrix(1,0,sin(rad));
    rZ.setMatrix(1,1,cos(rad));
    rZ.setMatrix(1,2,0);
    rZ.setMatrix(2,0,0);
    rZ.setMatrix(2,1,0);
    rZ.setMatrix(2,2,1);

    return rZ;
}

void Matrix::rotateX(const double radius) {
    double rad = radius*M_PI/180;
    this->setMatrix(0,0,1);
    this->setMatrix(0,1,0);
    this->setMatrix(0,2,0);
    this->setMatrix(1,0,0);
    this->setMatrix(1,1,cos(rad));
    this->setMatrix(1,2,-sin(rad));
    this->setMatrix(2,0,0);
    this->setMatrix(2,1,sin(rad));
    this->setMatrix(2,2,cos(rad));
}

void Matrix::rotateY(const double radius) {
    double rad = radius*M_PI/180;
    this->setMatrix(0,0,cos(rad));
    this->setMatrix(0,1,0);
    this->setMatrix(0,2,sin(rad));
    this->setMatrix(1,0,0);
    this->setMatrix(1,1,1);
    this->setMatrix(1,2,0);
    this->setMatrix(2,0,-sin(rad));
    this->setMatrix(2,1,0);
    this->setMatrix(2,2,cos(rad));
}

void Matrix::rotateZ(const double radius) {
    double rad = radius*M_PI/180;
    this->setMatrix(0,0,cos(rad));
    this->setMatrix(0,1,-sin(rad));
    this->setMatrix(0,2,0);
    this->setMatrix(1,0,sin(rad));
    this->setMatrix(1,1,cos(rad));
    this->setMatrix(1,2,0);
    this->setMatrix(2,0,0);
    this->setMatrix(2,1,0);
    this->setMatrix(2,2,1);
}

Matrix Matrix::homothetie(const double lambda) {
    Matrix transf;
    for(int i=0; i<3; i++) {
        transf.setMatrix(i,i, lambda);
    }
    return (*this*transf);
}

Matrix Matrix::transpose() {
    Matrix tmp;
    for(int i=0; i<3; i++) {
        for (int j=0; j<3; j++){
            tmp.setMatrix(i,j, *this->tab[j,i]);
        }
    }
    return tmp;
}

Matrix Matrix::inverse()
{
	Matrix tmp;
	for(int i=0; i<2; i++)
    {	
	    for(int j=0 ; j<2; j++)
		{
		 	tmp.setMatrix(i, i, 1/(this->tab[i][i]));
		 		if(j!=i)
	    		{
                    tmp.setMatrix(i, j, tmp.tab[i][j] - (this->tab[i][j]/this->tab[i][i]));
				}
		 		for(int k=0; k<2; k++)
		 		{
					if(k!=i)
					{
                        tmp.setMatrix(k, i, (this->tab[k][i]/this->tab[i][i]));
					}
					if(j!=i &&k!=i)
					{
                        tmp.setMatrix(k, j, this->tab[k][j] - this->tab[i][j] * this->tab[k][i] / this->tab[i][i]);
                    }		
				}
		}
		for(int i=0;i<2;i++)
    	{
            for(int j=0;j<2;j++)
            {
                this->setMatrix(i, j, tmp.tab[i][j]);
            }
       }
	}

    return *this;
}

double Matrix::getMatrix(int i, int j) {
    return tab[i][j];
}
