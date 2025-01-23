/* ENG Charles P1403762
    TANG Kévin P1501263
*/

#include "graphe.hpp"

int main(void){
    MMaze::Tuile t(1);
    MMaze::Graphe test(t);
    t.afficher_tuile();

    test.afficher_graphe();
}