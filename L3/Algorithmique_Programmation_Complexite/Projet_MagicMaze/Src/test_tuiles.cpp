/* ENG Charles P1403762
    TANG Kévin P1501263
*/

#include "tuiles.hpp"

int main(void){

    MMaze::PadPlateau pad;

    //tuile test
    MMaze::Tuile t;
    std::cout << "Test tuile :" << std::endl;
    t.afficher_tuile();


    //tuile départ
    MMaze::Tuile t0(0);
    std::cout << "Test tuile de départ :" << std::endl;
    t0.afficher_tuile();


    //tuile classique
    MMaze::Tuile t1(1);
    std::cout << "Test tuile classique :" << std::endl;
    t1.afficher_tuile();
    

    return 0;
}
