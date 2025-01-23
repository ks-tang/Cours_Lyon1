#include "graphe.hpp"

namespace MMaze {

    /*Constructeur du graphe, avec comme paramètre une tuile, où on ajoute un noeud pour chaque case puis on ajoute le voisin de chaque noeud
     si c'est différent d'une boutique.
    */
    Graphe::Graphe(Tuile t){
        //initialisation des noeuds si la case n'est pas une boutique
        for(int i=0; i<16; i++){
            if(t.tab_sites[i] != MMaze::Site::BOUTIQUE){
                noeud.push_back(Noeud(Case(i)));
            }
        }
        //initialisation du tableau de murs
        for(int i=0; i<24; i++){
            if(t.tab_murs[i]){
                tab_murs[i] = true;
            }
            else tab_murs[i] = false;
        }
        //ajout des voisins pour tous les noeuds.
        for(int j=0; j<noeud.size(); j++){
            Case caseActuelle(noeud[j].index_noeud);
            ajouter_voisin(caseActuelle);
        }
    }
    //fonction permettant d'ajouter les voisins du noeud de la case c.
    Noeud Graphe::ajouter_voisin(Case c){
        for (Direction dir : directions){
            Case actuelle = c;
            Case voisine = c;
            bool murExistant = false;
            try{
                for(int i=1; i<4; i++){
                    voisine = actuelle.voisine(dir);
                    for(int j=0; j<noeud.size(); j++){
                        if(noeud[j].index_noeud == c.index()){
                            //on vérifie qu'il n'y a pas de mur entre la case actuelle et la voisine pour l'ajouter dans les noeuds voisins.
                            Mur m(actuelle,voisine);
                            //si le mur existe, on met le booléen a vrai, ce qui permet de ne pas ajouter les autres voisins plus loin.
                            if(tab_murs[m.index()] == true){
                                murExistant = true;
                            }
                            //s'il n'existe pas, ajoute les voisins.
                            if(murExistant == false){
                                noeud[j].voisins.push_back(Noeud(voisine));
                                noeud[j].arete.push_back(Arete(dir,i));
                            }
                        }
                    }
                    actuelle = voisine;
                }
            }
            catch(std::exception& e){
                
            }
        }
    }

    void Graphe::afficher_graphe(){
        for(int i=0; i<noeud.size(); i++){
            std::cout<<"Noeud = "<<i<<std::endl;
            std::cout<<"index = "<<noeud[i].index_noeud<<std::endl;
            for(int j=0; j<noeud[i].voisins.size(); j++){
                std::cout<<"Voisins "<<j<<" : " <<noeud[i].voisins[j].index_noeud<<std::endl;
                std::cout<<"Longueur : "<<noeud[i].arete[j].longueur<<std::endl;
            }
        }
    }

}