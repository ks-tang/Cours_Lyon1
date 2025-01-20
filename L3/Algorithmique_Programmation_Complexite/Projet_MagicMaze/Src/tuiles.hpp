/* ENG Charles P1403762
    TANG Kévin P1501263
*/

#ifndef TUILE
#define TUILE

#include "site.hpp"
#include <array>
#include <iostream>
#include "melangeur.hpp"
#include "mur.hpp"
#include "draw.hpp"
#include "unionfind.hpp"
#include "case.hpp"
#include "direction.hpp"

namespace MMaze {


    class Tuile{
        private:
        

        UnionFind uf;

        bool verif_site();

        void impasse();
        void fermer_boutique();
        int compte_nb_boutiques();
        void abattre_murs_m_equivalence();

        //int plus_court_chemin(int depart, int arrivee);


        public:
        Tuile();
        Tuile(int typeT);
        
        std::array<bool, 24> tab_murs;
        std::array<Site, 16> tab_sites;
        std::vector<Case> tab_cases;
        void afficher_tuile();
        
        //int* chemins[50][50];
    };

}
#endif
