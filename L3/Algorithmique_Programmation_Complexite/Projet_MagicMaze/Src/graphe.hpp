#ifndef GRAPHE
#define GRAPHE

#include "tuiles.hpp"
#include "case.hpp"
#include "direction.hpp"
#include "noeud.hpp"
#include <vector>

namespace MMaze {

    class Graphe {
        public:
        Graphe(Tuile t);
        void afficher_graphe();
        std::array<bool, 24> tab_murs;
        private:
        std::vector<Noeud> noeud;
        
        Noeud ajouter_voisin(Case c);
        
    };

}

#endif