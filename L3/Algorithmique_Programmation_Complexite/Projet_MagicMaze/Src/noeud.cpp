#include "noeud.hpp"
#include <stdlib.h>

namespace MMaze {

    //constructeur de base d'un noeud
    Noeud::Noeud(Case c){
        index_noeud = c.index();
    }
}