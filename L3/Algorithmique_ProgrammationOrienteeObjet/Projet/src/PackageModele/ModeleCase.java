/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package PackageModele;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Random;
/**
 * @brief: Classe gérant les cases modèles
 * 
 * @file: ModeleCase.java
 * @author: p1403762 & p1501263
 * @date: 04/12/2021
 */
public class ModeleCase{
    private int x;
    private int y;
    public CaseType type;

    /**
     * @brief Getter de la coordonnée x
     * @return int
     */
    public int getX() {
        return x;
    }
    /**
     * @brief Getter de la coordonnée y
     * @return int
     */
    public int getY() {
        return y;
    }

    /**
     * @brief Constructeur de la classe ModeleCase
     * @param _x Coordonnée x de la case
     * @param _y Coordonnée y de la case
     * @param type Type de la case
     */
    public ModeleCase(int _x, int _y, CaseType type) {
        this.x = _x;
        this.y = _y;
        this.type = type;
    }
    
    /**
     * @brief Constructeur de la classe ModeleCase
     * @param _x Coordonnée x de la case.
     * @param _y Coordonnée y de la case.
     */
    public ModeleCase(int _x, int _y) {
        this.x = _x;
        this.y = _y;
        this.type = CaseType.empty;
    }
    
}
