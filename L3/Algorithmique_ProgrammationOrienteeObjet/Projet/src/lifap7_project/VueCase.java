

package lifap7_project;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Random;
import PackageModele.*;

// TODO : redéfinir la fonction hashValue() et equals(Object) si vous souhaitez utiliser la hashmap de VueControleurGrille avec VueCase en clef
/**
 * @brief: Classe gérant la vue d'une case.
 * 
 * @file: VueCase.java
 * @author: p1403762 & p1501263
 * @date: 04/12/2021
 */
public class VueCase extends JPanel {
    private int x, y;
    private Jeu jeu;
    /**
     * @brief Constructeur de la classe VueCase.
     * 
     * @param _x Ordonnée de la case
     * @param _y Abscisse de la case
     * @param j Jeu actuel
     */
    public VueCase(int _x, int _y, Jeu j) {
        x = _x;
        y = _y;
        jeu = j;
    }
    /**
     * @brief Getter de l'ordonnée x 
     * @return int
     */
    public int get_x(){
        return x;
    }
    /**
     * @brief Getter de l'abscisse y
     * @return int
     */
    public int get_y(){
        return y;
    }
    /**
     * @brief Dessine le trait correspondant à "midi"
     * @param g
     */
    private void drawNoon(Graphics g) {
        g.fillRect(getWidth()/2-getWidth()/8, 0, getWidth()/4, getHeight()/2);
    }
    /**
     * @brief Dessine le trait correspondant à "9h"
     * @param g
     */
    private void drawNine(Graphics g) {
        g.fillRect(0, getHeight()/2-getHeight()/8, getWidth()/2, getHeight()/4);
    }
    /**
     * @brief Dessine le trait correspondant à "6h"
     * @param g
     */
    private void drawSix(Graphics g) {
        g.fillRect(getWidth()/2-getWidth()/8, getHeight()/2, getWidth()/4, getHeight()/2);
    }
    /**
     * @brief Dessine le trait correspondant à "3h"
     * @param g
     */
    private void drawThree(Graphics g) {
        g.fillRect(getWidth()/2, getHeight()/2-getHeight()/8, getWidth()/2, getHeight()/4);
    }

    /**
     * @brief Peint les éléments graphiques.
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());

        g.drawRoundRect(getWidth()/4, getHeight()/4, getWidth()/2, getHeight()/2, 5, 5);

        Rectangle2D deltaText =  g.getFont().getStringBounds("0", g.getFontMetrics().getFontRenderContext()); // "0" utilisé pour gabarit

        Image img1 = Toolkit.getDefaultToolkit().getImage("img/meme1.png");
        Image img2 = Toolkit.getDefaultToolkit().getImage("img/meme.png");
        Image img3 = Toolkit.getDefaultToolkit().getImage("img/meme2.jpg");
        Image img4 = Toolkit.getDefaultToolkit().getImage("img/meme3.png");
        Image img5 = Toolkit.getDefaultToolkit().getImage("img/meme4.jpg");
        Image img6 = Toolkit.getDefaultToolkit().getImage("img/meme5.jpg");
        Image img7 = Toolkit.getDefaultToolkit().getImage("img/meme6.jpg");
        Image img8 = Toolkit.getDefaultToolkit().getImage("img/meme7.jpg");
        Image img9 = Toolkit.getDefaultToolkit().getImage("img/meme8.jpg");
        switch(jeu._tabCaseM[x][y].type) {
            case S1 :
                g.drawImage(img1,0,0, this.getWidth(), this.getHeight(), this);
                break;
            case S2 :
                g.drawImage(img2,0,0, this.getWidth(), this.getHeight(), this);
                break;
            case S3 :
                g.drawImage(img3,0,0, this.getWidth(), this.getHeight(), this);
                break;
            case S4 :
                g.drawImage(img4,0,0, this.getWidth(), this.getHeight(), this);
                break;
            case S5 :
                g.drawImage(img5,0,0, this.getWidth(), this.getHeight(), this);
                break;
            case S6 :
                g.drawImage(img6,0,0, this.getWidth(), this.getHeight(), this);
                break;
            case S7 :
                g.drawImage(img7,0,0, this.getWidth(), this.getHeight(), this);
                break;
            case S8 :
                g.drawImage(img8,0,0, this.getWidth(), this.getHeight(), this);
                break;
            case S9 :
                g.drawImage(img9,0,0, this.getWidth(), this.getHeight(), this);
                break;
            case h0v0 :
                drawNine(g);
                drawNoon(g);
                break;
            case h0v1 :
                drawNine(g);
                drawSix(g);
                break;
            case h1v0:
                drawThree(g);
                drawNoon(g);
                break;
            case h1v1 :
                drawThree(g);
                drawSix(g);
                break;
            case h0h1:
                drawThree(g);
                drawNine(g);
                break;
            case v0v1:
                drawNoon(g);
                drawSix(g);
                break;
            case cross:
                drawNoon(g);
                drawSix(g);
                drawThree(g);
                drawNine(g);
                break;
            case empty:


        }
    }
    public String toString() {
        return x + ", " + y;

    }

}
