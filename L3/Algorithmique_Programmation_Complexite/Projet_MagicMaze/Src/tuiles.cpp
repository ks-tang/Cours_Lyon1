/* ENG Charles P1403762
    TANG Kévin P1501263
*/

#include "tuiles.hpp"



namespace MMaze{

    Tuile::Tuile():uf(16){

        //initialisation des murs
        for(int i=0; i<tab_murs.size(); i++){
            tab_murs[i] = true;
        }
        //initialisation des sites à vide
        for(int i=0; i<tab_sites.size(); i++){
            tab_sites[i] = MMaze::Site::AUCUN;
        }
        //initialisation des cases à vide
        for(int i=0; i<16; i++){
            tab_cases.push_back(Case(i));
        }

        //déclaration du mélangeur de murs
        Melangeur <int> tab_melangeur_murs;
        for(int i=0; i<24; i++){
            tab_melangeur_murs.inserer(i);
        }
        
        //methode d'abattage des murs
        while(tab_melangeur_murs.taille() > 0)
        {   
            if(verif_site()) break;

            //retire un mur du mélangeur au hasard 
            Mur m(tab_melangeur_murs.retirer() );
            //regarde la position du mur
            //std::cout << m.index() << std::endl;
            //recherche les parents des deux cases adjacentes
            int m1 = uf.recherche(m[0].index());
            int m2 = uf.recherche(m[1].index());
            //abat le mur si les deux cases ne sont pas dans la même classe d'équivalence
            uf.unionE(m1,m2);
            tab_murs[m.index()] = false;

            
        }

    }



    Tuile::Tuile(int typeT):uf(16){

        if(typeT == 0) //Tuile de départ 
        {
            //initialisation des murs
            for(int i=0; i<tab_murs.size(); i++){
                tab_murs[i] = true;
            }
            //initialisation des sites a vide
            for(int i=0; i<tab_sites.size(); i++){
                tab_sites[i] = MMaze::Site::AUCUN;
            }
            //initialisation des cases à vide
            for(int i=0; i<16; i++){
                tab_cases.push_back(Case(i));
            }


            //declaration du melangeur du sites
            Melangeur <Site> tab_melangeur_depart;

            //ajout des départs dans le melangeur
            tab_melangeur_depart.inserer(MMaze::Site::DEPART_JAUNE);
            tab_melangeur_depart.inserer(MMaze::Site::DEPART_VERTE);
            tab_melangeur_depart.inserer(MMaze::Site::DEPART_ORANGE);
            tab_melangeur_depart.inserer(MMaze::Site::DEPART_VIOLETTE);
            //tirage des départs du mélangeur
            tab_sites[5] = tab_melangeur_depart.retirer();
            tab_sites[6] = tab_melangeur_depart.retirer();
            tab_sites[9] = tab_melangeur_depart.retirer();
            tab_sites[10] = tab_melangeur_depart.retirer();

            //ajout des portes dans le mélangeur
            tab_melangeur_depart.inserer(MMaze::Site::PORTE_JAUNE);
            tab_melangeur_depart.inserer(MMaze::Site::PORTE_VERTE);
            tab_melangeur_depart.inserer(MMaze::Site::PORTE_ORANGE);
            tab_melangeur_depart.inserer(MMaze::Site::PORTE_VIOLETTE);
            //tirage des portes du mélangeur
            tab_sites[2] = tab_melangeur_depart.retirer();
            tab_sites[4] = tab_melangeur_depart.retirer();
            tab_sites[11] = tab_melangeur_depart.retirer();
            tab_sites[13] = tab_melangeur_depart.retirer();



            //declaration du mélangeur des murs
            Melangeur <int> tab_melangeur_murs;
            //insertion des murs dans le mélangeur
            for(int i=0; i<24; i++){
                tab_melangeur_murs.inserer(i);
            }
            //abat les murs pour relier les sites importants
            while(tab_melangeur_murs.taille() > 0)
            {
                //si tous les sites importants sont reliés à la case 13
                //alors plus besoin d'abattre des murs, on arrete le melangeur
                if(verif_site()) break;

                Mur m(tab_melangeur_murs.retirer() );
                //std::cout << m.index() << std::endl;
                int m1 = uf.recherche(m[0].index());
                int m2 = uf.recherche(m[1].index());

                uf.unionE(m1,m2);
                tab_murs[m.index()] = false;

            }
            //abattre les murs du milieu si ce n'est pas déjà fait
            tab_murs[5] = false;
            tab_murs[6] = false;
            tab_murs[17] = false;
            tab_murs[18] = false;

        }



        if(typeT == 1) //Tuile classique
        {

            //initialisation des murs
            for(int i=0; i<tab_murs.size(); i++){
                tab_murs[i] = true;
            }
            //initialisation des sites a vide
            for(int i=0; i<tab_sites.size(); i++){
                tab_sites[i] = MMaze::Site::AUCUN;
            }
            //initialisation des cases à vide
            for(int i=0; i<16; i++){
                tab_cases.push_back(Case(i));
            }

            //tirage du nombre de porte (entre 1 et 3)
            Melangeur <int> tab_melangeur_nb;
            tab_melangeur_nb.inserer(1);
            tab_melangeur_nb.inserer(2);
            tab_melangeur_nb.inserer(3);
            int nb_portes = tab_melangeur_nb.retirer();

            //ajout des porte dans un mélangeur
            Melangeur <Site> tab_melangeur_portes;
            tab_melangeur_portes.inserer(MMaze::Site::PORTE_JAUNE);
            tab_melangeur_portes.inserer(MMaze::Site::PORTE_VERTE);
            tab_melangeur_portes.inserer(MMaze::Site::PORTE_ORANGE);
            tab_melangeur_portes.inserer(MMaze::Site::PORTE_VIOLETTE);
            //ajout de la position des porte dans un mélangeur
            Melangeur <int> tab_melangeur_position_porte;
            tab_melangeur_position_porte.inserer(2);
            tab_melangeur_position_porte.inserer(4);
            tab_melangeur_position_porte.inserer(11);

            //pour le nombre de portes tiré on affecte une position et une porte 
            tab_sites[13] = MMaze::Site::PORTE;
            for(int i=0; i<nb_portes; i++)
            {
                tab_sites[tab_melangeur_position_porte.retirer()] = tab_melangeur_portes.retirer();
            }


            //declaration du mélangeur des murs
            Melangeur <int> tab_melangeur_murs;
            //insertion des murs dans le mélangeur
            for(int i=0; i<24; i++){
                tab_melangeur_murs.inserer(i);
            }
            //abat les murs pour relier les sites importants
            while(tab_melangeur_murs.taille() > 0)
            {
                //si tous les sites importants sont reliés à la case 13
                //alors plus besoin d'abattre des murs, on arrete le melangeur
                if(verif_site()) break;

                Mur m(tab_melangeur_murs.retirer() );
                //std::cout << m.index() << std::endl;
                int m1 = uf.recherche(m[0].index());
                int m2 = uf.recherche(m[1].index());

                uf.unionE(m1,m2);
                tab_murs[m.index()] = false;
            }

        }

        impasse();
        abattre_murs_m_equivalence();
        
    }

    

    int Tuile::compte_nb_boutiques(){
        int compteur_boutique = 0;

        for(int i=0; i<tab_sites.size(); i++)
        {
            if(tab_sites[i] == MMaze::Site::BOUTIQUE)
            {
                compteur_boutique++;
            }
        }

        return compteur_boutique;
    }


    void Tuile::impasse(){
        
        //variable de récursion
        bool nouvelle_boutique;

        //compteur de parcours des cases voisines
        int compteur;

        do{
            nouvelle_boutique = false;

            //compter le nombre de murs autour de chaque case
            for(int i=0; i<tab_cases.size(); i++)
            {
                compteur = 0;
                
                Case c = tab_cases[i];
                //std::cout << "La case " << c.index() << std::endl;

                if(tab_sites[i] == MMaze::Site::AUCUN)
                {
                    for(int j=0; j<4; j++)
                    {
                        try{
                            Direction d = directions[j];
                            Case v = c.voisine(d);
                            //std::cout << "La case voisine " << v.index() << std::endl;
                            Mur m(c,v);

                            if(tab_murs[m.index()])
                            {
                                compteur++;
                                //std::cout << "compteur : " << compteur << std::endl;
                            }

                        } catch(std::exception &e){
                            compteur++;
                        }
                    }

                    //application du compteur
                    if(compteur >= 3)
                    {
                        tab_sites[i] = MMaze::Site::BOUTIQUE;
                        nouvelle_boutique = true;

                        //fermer les boutiques
                        for(int k=0; k<4; k++)
                        {
                            try {
                                Direction d2 = directions[k];
                                Case v2 = c.voisine(d2);
                                Mur m2(c,v2);
                                tab_murs[m2.index()] = 1;
                            } catch(std::exception &e){
                                //std::cout << "Bord de la tuile" << std::endl;
                            }
                        }
                    }

                }
            }
        } while (nouvelle_boutique);
        

    }
   

    //fonction utilisée pour abattre les murs entre deux cases boutiques
    //les deux cases ont le type de site : boutique
    //mais pas encore la meme classe d'equivalence
    void Tuile::abattre_murs_m_equivalence(){
        
        Melangeur <int> melangeur_murs;
        for(int i=0; i<tab_murs.size(); i++)
        {
            melangeur_murs.inserer(i);
        }

        while(melangeur_murs.taille() > 0)
        {   
            //retire un mur du mélangeur au hasard 
            Mur m(melangeur_murs.retirer() );

            //recherche les parents des deux cases adjacentes
            int c1 = uf.recherche( m[0].index() );
            int c2 = uf.recherche( m[1].index() );

            //abattre le mur si boutique
            if(tab_sites[ m[0].index() ] == tab_sites[ m[1].index() ] && tab_sites[ m[0].index() ] == MMaze::Site::BOUTIQUE)
            {
                uf.unionE(c1,c2);
                tab_murs[m.index()] = false;
            }
        }

    }



    void Tuile::afficher_tuile(){
        PadPlateau pad;
        pad.ajouter_tuile(0,0);
        MelangeurOptions::imprevisible();

        for (int i=0; i<16; i++){
            pad.ajouter_site(0,0, Case(i), tab_sites[i]);
        }

        for (int i=0; i<24; i++){
            if(tab_murs[i] == 1){
                pad.ajouter_mur(0,0, Mur(i));
            }     
        }
    
        std::cout << pad << std::endl;
        pad.dessiner("test1.png");
    }


    //pour toutes les cases qui contiennent un site important
    //vérifie que ces cases sont dans la même classe d'équivalence que la case 13
    bool Tuile::verif_site(){
        
        int racine_case13 = uf.recherche(13);

        for(int i = 0; i<16; i++)
        {
            if( tab_sites[i] != MMaze::Site::AUCUN && tab_sites[i] != MMaze::Site::BOUTIQUE )
            {
                if(uf.recherche(i) != racine_case13)
                {
                    /*
                    std::cout << i << " n'est pas dans la classe d'équivalence de 13" << std::endl;
                    std::cout << "Classe d'équivalence de " << i << " : " << uf.recherche(i) << std::endl;
                    std::cout << "Classe d'équivalence de 13 : " << racine_case13 << std::endl;
                    */
                    return false;
                }
            }
        }

        return true;
        
    }


    

    /*
    void Tuile::impasse(){
        //compte le nombre de boutique dans la tuile
        int nb_boutiques_depart = compte_nb_boutiques();

        //initialisation d'un tableau de compteurs
        int tab_compteur[16];
        for(int n=0; n<16; n++)
        {
            tab_compteur[n] = 0;
        }
        
        ///pour les lignes
        //pour toutes les cases entre 0 et 11, vérifie le mur du bas
        for(int i=0; i<12; i++)
        {
            if(tab_murs[i])
            {
                tab_compteur[i]++;
            }
        }
        //pour toutes les cases entre 4 et 15, vérifie le mur du haut
        for(int j=4; j<16; j++)
        {
            if(tab_murs[j-4])
            {
                tab_compteur[j]++;
            }
        }

        ///pour les colonnes
        //premiere colonne
        for(int c1=0; c1<=12; c1+=4)
        {
            if(tab_murs[12+(c1/4)])
            {
                tab_compteur[c1]++;
                tab_compteur[c1+1]++;
            }
        }
        //deuxieme colonne
        int suite = 0;
        for(int c2=1; c2<=13; c2+=4)
        {
            if(tab_murs[16+suite])
            {
                tab_compteur[c2]++;
                tab_compteur[c2+1]++;
            }
            suite++;
        }
        //troisieme colonne
        suite = 0;
        for(int c3=2; c3<=14; c3+=4)
        {
            if(tab_murs[20+suite])
            {
                tab_compteur[c3]++;
                tab_compteur[c3+1]++;
            }
            suite++;
        }

        //application tab_compteur
        if(tab_compteur[0]>=1 && tab_sites[0]==MMaze::Site::AUCUN) tab_sites[0]= MMaze::Site::BOUTIQUE;
        if(tab_compteur[3]>=1 && tab_sites[3]==MMaze::Site::AUCUN) tab_sites[3]= MMaze::Site::BOUTIQUE;
        if(tab_compteur[12]>=1 && tab_sites[12]==MMaze::Site::AUCUN) tab_sites[12]= MMaze::Site::BOUTIQUE;
        if(tab_compteur[15]>=1 && tab_sites[15]==MMaze::Site::AUCUN) tab_sites[15]= MMaze::Site::BOUTIQUE;

        if(tab_compteur[1]>=2 && tab_sites[1]==MMaze::Site::AUCUN) tab_sites[1]= MMaze::Site::BOUTIQUE;
        if(tab_compteur[2]>=2 && tab_sites[2]==MMaze::Site::AUCUN) tab_sites[2]= MMaze::Site::BOUTIQUE;
        if(tab_compteur[7]>=2 && tab_sites[7]==MMaze::Site::AUCUN) tab_sites[7]= MMaze::Site::BOUTIQUE;
        if(tab_compteur[11]>=2 && tab_sites[11]==MMaze::Site::AUCUN) tab_sites[11]= MMaze::Site::BOUTIQUE;
        if(tab_compteur[14]>=2 && tab_sites[14]==MMaze::Site::AUCUN) tab_sites[14]= MMaze::Site::BOUTIQUE;
        if(tab_compteur[13]>=2 && tab_sites[13]==MMaze::Site::AUCUN) tab_sites[13]= MMaze::Site::BOUTIQUE;
        if(tab_compteur[8]>=2 && tab_sites[8]==MMaze::Site::AUCUN) tab_sites[8]= MMaze::Site::BOUTIQUE;
        if(tab_compteur[4]>=2 && tab_sites[4]==MMaze::Site::AUCUN) tab_sites[4]= MMaze::Site::BOUTIQUE;

        if(tab_compteur[5]>=3 && tab_sites[5]==MMaze::Site::AUCUN) tab_sites[5]= MMaze::Site::BOUTIQUE;
        if(tab_compteur[6]>=3 && tab_sites[6]==MMaze::Site::AUCUN) tab_sites[6]= MMaze::Site::BOUTIQUE;
        if(tab_compteur[9]>=3 && tab_sites[9]==MMaze::Site::AUCUN) tab_sites[9]= MMaze::Site::BOUTIQUE;
        if(tab_compteur[10]>=3 && tab_sites[10]==MMaze::Site::AUCUN) tab_sites[10]= MMaze::Site::BOUTIQUE;
        
        //appel de la procedure fermer_boutique
        fermer_boutique(); 

        //compte le nombre de boutique dans la tuile
        int nb_boutiques_fin = compte_nb_boutiques();
        //condition de récursion
        if(nb_boutiques_fin > nb_boutiques_depart)
        {
            impasse();
        }

        

    }
    

    //fermer les murs autour des boutiques
    void Tuile::fermer_boutique(){

        //mur du bas
        for(int i=0; i<12; i++)
        {
            if(tab_sites[i] == MMaze::Site::BOUTIQUE)
            {
                tab_murs[i] = true;
            }
        }
        //mur du haut
        for(int j=4; j<16; j++)
        {
            if(tab_sites[j] == MMaze::Site::BOUTIQUE)
            {
                tab_murs[j-4] = true;
            }
        }
        //premiere colonne
        int suite=0;
        for(int c1=0; c1<=12; c1+=4)
        {
            if(tab_sites[c1] == MMaze::Site::BOUTIQUE)
            {
                tab_murs[12+suite] = true;
            }
            suite++;
        }
        //deuxieme colonne
        suite = 0;
        for(int c2=1; c2<=13; c2+=4)
        {
            if(tab_sites[c2] == MMaze::Site::BOUTIQUE)
            {
                tab_murs[12+suite] = true;
                tab_murs[16+suite] = true;
            }
            suite++;
        }
        //troisieme colonne
        suite = 0;
        for(int c3=2; c3<=14; c3+=4)
        {
            if(tab_sites[c3] == MMaze::Site::BOUTIQUE)
            {
                tab_murs[16+suite] = true;
                tab_murs[20+suite] = true;
            }
            suite++;
        }
        //quatrieme colonne
        suite = 0;
        for(int c4=3; c4<=15; c4+=4)
        {
            if(tab_sites[c4] == MMaze::Site::BOUTIQUE)
            {
                tab_murs[20+suite] = true;
            }
            suite++;
        }
    }
    */



}
