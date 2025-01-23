#ifndef NOEUD
#define NOEUD

#include <vector>
#include "arete.hpp"
#include "case.hpp"

namespace MMaze {
    struct Noeud {
        Noeud(Case c);

        std::vector<Noeud> voisins;
        std::vector<Arete> arete;
        int index_noeud;
    };
}
#endif