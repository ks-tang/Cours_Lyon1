/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PackageModele;

import java.sql.CallableStatement;
import java.util.Observable;

/**
 * @brief: Classe gérant le jeu
 * 
 * @file: Jeu.java
 * @author: p1403762 & p1501263
 * @date: 04/12/2021
 */
public class Jeu extends Observable{
    private final int size;
    public ModeleCase[][] _tabCaseM;

    public Chemin[] tabChemin;
    boolean clicPressed = false;
    int indCheminActuel;
    public boolean victoire;
    public boolean menuOuvert;
    
    /**
     * @brief Getter de l'attribut size de la classe Jeu.
     * @return int
     */
    public int getSize() {
        return size;
    }
    /**
     * @brief Vérifie que la case est un symbole.
     * @param caseActuelle Case où on effectue la vérification.
     * @return true si la case est un symbole, faux sinon.
     */
    public boolean estSymbole(ModeleCase caseActuelle){
        if((caseActuelle.type == CaseType.S1) || (caseActuelle.type == CaseType.S2) || (caseActuelle.type == CaseType.S3) 
        || (caseActuelle.type == CaseType.S4) || (caseActuelle.type == CaseType.S5) || (caseActuelle.type == CaseType.S6) 
        || (caseActuelle.type == CaseType.S7) || (caseActuelle.type == CaseType.S8 || (caseActuelle.type == CaseType.S9))){
            return true;
        }
        else return false;
    }
    
    /**
     * @brief Vérifie que la case d'avant est un voisin du haut.
     * @param caseAvant La case avant.
     * @param caseActuelle La case actuelle.
     * @return true si la case avant est un voisin du haut de la case actuelle, faux sinon.
     */
    public boolean voisinHaut(ModeleCase caseAvant, ModeleCase caseActuelle){
        if(caseAvant.getY() == caseActuelle.getY() &&
                caseAvant.getX() == (caseActuelle.getX()-1)){  
            return true;
        }
        else return false;
    }
    
     /**
     * @brief Vérifie que la case d'avant est un voisin du bas.
     * @param caseAvant La case avant.
     * @param caseActuelle La case actuelle.
     * @return true si la case avant est un voisin du bas de la case actuelle, faux sinon.
     */
    public boolean voisinBas(ModeleCase caseAvant, ModeleCase caseActuelle){
        if(caseAvant.getY() == caseActuelle.getY() &&
                caseAvant.getX() == (caseActuelle.getX()+1)){  
            return true;
        }
        else return false;
    }
    
     /**
     * @brief Vérifie que la case d'avant est un voisin de gauche.
     * @param caseAvant La case avant.
     * @param caseActuelle La case actuelle.
     * @return true si la case avant est un voisin de gauche de la case actuelle, faux sinon.
     */
    public boolean voisinGauche(ModeleCase caseAvant, ModeleCase caseActuelle){
        if(caseAvant.getY() == (caseActuelle.getY()-1) && 
                caseAvant.getX() == caseActuelle.getX()){  
            return true;
        }
        else return false;
    }
    
     /**
     * @brief Vérifie que la case d'avant est un voisin de droite.
     * @param caseAvant La case avant.
     * @param caseActuelle La case actuelle.
     * @return true si la case avant est un voisin de droite de la case actuelle, faux sinon.
     */
    public boolean voisinDroite(ModeleCase caseAvant, ModeleCase caseActuelle){
        if(caseAvant.getY() == (caseActuelle.getY()+1) && 
                caseAvant.getX() == caseActuelle.getX()){  
            return true;
        }
        else return false;
    }

     /**
     * @brief Vérifie que la case d'avant est un voisin.
     * @param caseAvant La case avant.
     * @param caseActuelle La case actuelle.
     * @return true si la case est un voisin, faux sinon.
     */
    public boolean voisin(ModeleCase caseAvant, ModeleCase caseActuelle){
        if(voisinHaut(caseAvant, caseActuelle) || voisinBas(caseAvant, caseActuelle) || voisinGauche(caseAvant, caseActuelle) || voisinDroite(caseAvant, caseActuelle)){
            return true;
        }
        else return false;
    }
    
    /**
     * @brief Fonction appelée lorsque le joueur clique sur une case
     * @details Crée un chemin si la case est un symbole n'ayant pas de chemin, si le symbole a déjà un chemin, le supprime. Supprime aussi le chemin si on clique sur une case appartenant à un chemin, sinon ne fais rien si la case est vide.
     * @param x Coordonnée x de la case
     * @param y Coordonnée y de la case
     */
    public void clicCase(int x, int y){
            
            for(int j=tabChemin.length-1 ; j>=0; j--){
                if(tabChemin[j] == null){
                    indCheminActuel = j;
                }
            }
            
            for(int i=0; i<tabChemin.length-1 ; i++){
                if(tabChemin[i]!= null && tabChemin[i]._chemin.contains(this._tabCaseM[x][y])){
                    supprimerChemin(i);
                }
            }
            
            if(indCheminActuel>=0){
                if(estSymbole(this._tabCaseM[x][y])){
                   tabChemin[indCheminActuel] = debutChemin(this._tabCaseM[x][y]);
                }
            }
            this.clicPressed = true;
    }
    /**
     * @brief Fonction appelée lorsqu'on entre dans une case
     * @details Ajoute la case si le joueur est en train de maintenir le clic et la case est valide (n'appartient pas à un autre chemin et est voisine de la case d'avant). Si on entre dans une case symbole différente de celle du début du chemin, n'ajoute pas la case.
     * @param x Coordonnée x de la case
     * @param y Coordonnée y de la case
     */
    public void entrerCase(int x,int y){
        if(clicPressed){
            if(indCheminActuel>=0 && this.tabChemin[indCheminActuel] != null){
                if(estSymbole(this._tabCaseM[x][y]) && 
                   this._tabCaseM[x][y].type == this.tabChemin[indCheminActuel]._chemin.get(0).type){
                        this.tabChemin[indCheminActuel].ajouterCase(this._tabCaseM[x][y]);
                }
                else if(this._tabCaseM[x][y].type == CaseType.empty && 
                        voisin(this.tabChemin[indCheminActuel]._chemin.get(tabChemin[indCheminActuel]._chemin.size()-1),this._tabCaseM[x][y])){
                    this.tabChemin[indCheminActuel].ajouterCase(this._tabCaseM[x][y]);
                }
                else if(this.tabChemin[indCheminActuel]._chemin != null){
                    supprimerChemin(indCheminActuel);
                }
            }
        }
    }
    
    /**
     * @brief Fonction appelée lorsqu'on relâche le clic de la souris.
     * @details Vérifie que le chemin ajouté est valide, puis si c'est le cas, change l'état des cases modèles, puis vérifie si le joueur a gagné ou non. A la fin, notifie l'observer des modifications de l'état des cases modèles.
     * @param x Coordonnée x de la case
     * @param y Coordonnée y de la case
     */
    public void relacherClic(int x, int y){
            if(indCheminActuel>=0){
                if(estSymbole(this._tabCaseM[x][y]) ){
                    if(tabChemin[indCheminActuel] != null){
                       if(!tabChemin[indCheminActuel].validerChemin()){
                            supprimerChemin(indCheminActuel);
                        }
                    }
                }
                else if(this._tabCaseM[x][y].type == CaseType.empty){
                    if(tabChemin[indCheminActuel] != null){
                        supprimerChemin(indCheminActuel);
                    }
                }
                else {
                    if(tabChemin[indCheminActuel] != null){
                        supprimerChemin(indCheminActuel);
                    }
                }
            }
            this.clicPressed = false;
            changerEtatChemin();
            this.victoire = verifierVictoire();
            if(victoire){
                System.out.println("Félicitations, vous avez gagné !");
            }
            setChanged();
            notifyObservers();
    }

    /**
     * @brief Modifie l'état des cases modèles appartenant à un chemin.
     */
    public void changerEtatChemin(){
        
        for(int i=0; i<tabChemin.length-1 ; i++){
            if(tabChemin[i] != null && tabChemin[i]._chemin.size()>2){
                for(int j=1;j<tabChemin[i]._chemin.size()-1; j++){
                    if(voisinDroite(tabChemin[i]._chemin.get(j-1), tabChemin[i]._chemin.get(j))){
                        if(voisinDroite(tabChemin[i]._chemin.get(j), tabChemin[i]._chemin.get(j+1))){
                            tabChemin[i]._chemin.get(j).type = CaseType.h0h1;
                        }
                        if(voisinHaut(tabChemin[i]._chemin.get(j), tabChemin[i]._chemin.get(j+1))){
                            tabChemin[i]._chemin.get(j).type = CaseType.h1v1;
                        }
                        if(voisinBas(tabChemin[i]._chemin.get(j), tabChemin[i]._chemin.get(j+1))){
                            tabChemin[i]._chemin.get(j).type = CaseType.h1v0;
                        }
                    }
                    if(voisinGauche(tabChemin[i]._chemin.get(j-1), tabChemin[i]._chemin.get(j))){
                        if(voisinGauche(tabChemin[i]._chemin.get(j), tabChemin[i]._chemin.get(j+1))){
                            tabChemin[i]._chemin.get(j).type = CaseType.h0h1;
                        }
                        if(voisinHaut(tabChemin[i]._chemin.get(j), tabChemin[i]._chemin.get(j+1))){
                            tabChemin[i]._chemin.get(j).type = CaseType.h0v1;
                        }
                        if(voisinBas(tabChemin[i]._chemin.get(j), tabChemin[i]._chemin.get(j+1))){
                            tabChemin[i]._chemin.get(j).type = CaseType.h0v0;
                        }
                    }
                    if(voisinBas(tabChemin[i]._chemin.get(j-1), tabChemin[i]._chemin.get(j))){
                        if(voisinDroite(tabChemin[i]._chemin.get(j), tabChemin[i]._chemin.get(j+1))){
                            tabChemin[i]._chemin.get(j).type = CaseType.h0v1;
                        }
                        if(voisinGauche(tabChemin[i]._chemin.get(j), tabChemin[i]._chemin.get(j+1))){
                            tabChemin[i]._chemin.get(j).type = CaseType.h1v1;
                        }
                        if(voisinBas(tabChemin[i]._chemin.get(j), tabChemin[i]._chemin.get(j+1))){
                            tabChemin[i]._chemin.get(j).type = CaseType.v0v1;
                        }
                    }
                    if(voisinHaut(tabChemin[i]._chemin.get(j-1), tabChemin[i]._chemin.get(j))){
                        if(voisinDroite(tabChemin[i]._chemin.get(j), tabChemin[i]._chemin.get(j+1))){
                            tabChemin[i]._chemin.get(j).type = CaseType.h0v0;
                        }
                        if(voisinHaut(tabChemin[i]._chemin.get(j), tabChemin[i]._chemin.get(j+1))){
                            tabChemin[i]._chemin.get(j).type = CaseType.v0v1;
                        }
                        if(voisinGauche(tabChemin[i]._chemin.get(j), tabChemin[i]._chemin.get(j+1))){
                            tabChemin[i]._chemin.get(j).type = CaseType.h1v0;
                        }
                    }
                }
            }
        }
       
       
    }
    
    /**
     * @brief Débute un chemin.
     * @param caseActuelle La 1ère case à ajouter au chemin construit.
     * @return Le chemin construit.
     */
    public Chemin debutChemin(ModeleCase caseActuelle){
        Chemin chemin = new Chemin();
        chemin.ajouterCase(caseActuelle);
        return chemin;
    }
    
    /**
     * @brief Supprime le chemin d'indice donné.
     * @param ind Indice du chemin à supprimer.
     */
    public void supprimerChemin(int ind){
        for(int i=1; i<tabChemin[ind]._chemin.size()-1; i++){
            if(!estSymbole(tabChemin[ind]._chemin.get(i)))
                tabChemin[ind]._chemin.get(i).type = CaseType.empty;
        }
        tabChemin[ind].viderChemin();
        tabChemin[ind] = null;
    }
    /**
     * @brief Vérifie la victoire du joueur.
     * @details Pour vérifier si le joueur a gagné, vérifie que la taille de tous les chemins = la taille de la grille du jeu.
     */
    public boolean verifierVictoire(){
        int tailleGrille = this.size*this.size;
        int nbCaseRemplie = 0;
        
        for(int i=0; i<tabChemin.length-1; i++)
        {
            if(tabChemin[i] != null){
            for(int j=0; j<tabChemin[i]._chemin.size(); j++){
                nbCaseRemplie++;
            }
        }}
        if(nbCaseRemplie == tailleGrille){
            return true;
        } else return false;
    }
    
    /**
     * @brief Constructeur de la classe jeu.
     * @param size taille de la grille de jeu.
     */
    public Jeu(int _size){
        this.size = _size;
        this.indCheminActuel = 0;
        this.tabChemin = new Chemin[10];
        this._tabCaseM = new ModeleCase[size][size];
        this.victoire = false;
        this.menuOuvert = true;

        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                this._tabCaseM[i][j] = new ModeleCase(i,j);
            }
        }
        if(size == 3){
            this._tabCaseM[0][1] = new ModeleCase(0,1,CaseType.S1);
            this._tabCaseM[2][0] = new ModeleCase(2,0,CaseType.S1);
            this._tabCaseM[0][2] = new ModeleCase(0,2,CaseType.S2);
            this._tabCaseM[2][2] = new ModeleCase(2,2,CaseType.S2);
        }

        if(size == 5){
            this._tabCaseM[0][3] = new ModeleCase(0,3,CaseType.S1);
            this._tabCaseM[2][0] = new ModeleCase(2,0,CaseType.S1);
            this._tabCaseM[1][2] = new ModeleCase(1,2,CaseType.S2);
            this._tabCaseM[3][1] = new ModeleCase(3,1,CaseType.S2);
            this._tabCaseM[3][0] = new ModeleCase(3,0,CaseType.S3);
            this._tabCaseM[3][4] = new ModeleCase(3,4,CaseType.S3);
            this._tabCaseM[0][4] = new ModeleCase(0,4,CaseType.S4);
            this._tabCaseM[1][3] = new ModeleCase(1,3,CaseType.S4);
            this._tabCaseM[3][3] = new ModeleCase(3,3,CaseType.S5);
            this._tabCaseM[4][4] = new ModeleCase(4,4,CaseType.S5);
        }
        if(size == 9){
            this._tabCaseM[5][0] = new ModeleCase(5,0,CaseType.S1);
            this._tabCaseM[6][2] = new ModeleCase(6,2,CaseType.S1);
            this._tabCaseM[1][1] = new ModeleCase(1,1,CaseType.S2);
            this._tabCaseM[4][4] = new ModeleCase(4,4,CaseType.S2);
            this._tabCaseM[1][2] = new ModeleCase(1,2,CaseType.S3);
            this._tabCaseM[2][3] = new ModeleCase(2,3,CaseType.S3);
            this._tabCaseM[1][3] = new ModeleCase(1,3,CaseType.S4);
            this._tabCaseM[2][7] = new ModeleCase(2,7,CaseType.S4);
            this._tabCaseM[2][4] = new ModeleCase(2,4,CaseType.S5);
            this._tabCaseM[2][6] = new ModeleCase(2,6,CaseType.S5);
            this._tabCaseM[5][7] = new ModeleCase(5,7,CaseType.S6);
            this._tabCaseM[7][1] = new ModeleCase(7,1,CaseType.S6);
            this._tabCaseM[5][1] = new ModeleCase(5,1,CaseType.S7);
            this._tabCaseM[3][7] = new ModeleCase(3,7,CaseType.S7);
            this._tabCaseM[6][1] = new ModeleCase(6,1,CaseType.S8);
            this._tabCaseM[5][8] = new ModeleCase(5,8,CaseType.S8);
            this._tabCaseM[4][1] = new ModeleCase(4,1,CaseType.S9);
            this._tabCaseM[4][3] = new ModeleCase(4,3,CaseType.S9);
        }
    }
}
