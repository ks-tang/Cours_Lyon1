#pragma once

#include <vector>
#include <iostream>

#include "mathematics.h"

#include <QtCore/QFile>
#include <QPixmap>
#include <QFileDialog>
#include <QtCore/QSize>

class Heightfield {
    private:
        int width;
        int height;
        std::vector<int> color;
    public:

    Heightfield(const QImage image) {
        this->width = image.width();
        this->height = image.height();
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                color.push_back(qGray(image.pixel(j,i)) /7);
            }
        }
    }

    int getHeight();
    int getWidth();
    int getColor(int);
};
