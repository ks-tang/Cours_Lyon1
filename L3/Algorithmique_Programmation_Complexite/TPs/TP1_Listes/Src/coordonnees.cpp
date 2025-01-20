#include "coordonnees.hpp"

int encode(short int ligne, short int colonne) {
  return ((int) ligne & 0xffff) | ((int) colonne << 16) ;
}

short int ligne(int segment) {
  return segment & 0xffff ;
}

short int colonne(int segment) {
  return segment >> 16 ;
}
