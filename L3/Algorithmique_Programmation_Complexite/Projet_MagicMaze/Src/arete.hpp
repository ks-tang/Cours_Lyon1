#ifndef ARETE
#define ARETE

#include "direction.hpp"
namespace MMaze{
    
    struct Arete {
        Arete(Direction d, int l);
        Direction dir;
        int longueur;
    };
}

#endif