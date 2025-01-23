#include "heightfield.h"

int Heightfield::getWidth() {
    return this->width;
}

int Heightfield::getHeight() {
    return this->height;
}

int Heightfield::getColor(int i){
    return this->color[i];
}