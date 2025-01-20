/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PackageModele;
import lifap7_project.*;
import java.util.*;   
/**
 * @brief: Classe gérant le chemin
 * 
 * @file: Chemin.java
 * @author: p1403762 & p1501263
 * @date: 04/12/2021
 */
public class Chemin {
    public List<ModeleCase> _chemin;

    /**
     * @brief Constructeur de la classe Chemin
     */
    public Chemin(){
        this._chemin = new ArrayList();
    }
    
    /**
     * @brief Ajoute une case à la liste chemin en dernière position.
     * @param caseActuelle La case à ajouter au chemin.
     */
    public void ajouterCase(ModeleCase caseActuelle){
        if(!_chemin.contains(caseActuelle))
            _chemin.add(caseActuelle);
    }
    
    /**
     * @brief Vide la liste chemin
     */
    public void viderChemin(){
        _chemin.clear();
    }
    /**
     * @brief Vérifie que le chemin est bien valide et suis les règles affectées.
     * @return boolean
     */
    public boolean validerChemin(){
        if(_chemin.get(0).type == _chemin.get(_chemin.size()-1).type && _chemin.get(0) != _chemin.get(_chemin.size()-1)){
            return true;
        }
        else return false;
    }
    
    
}
