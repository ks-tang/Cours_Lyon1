package lifap7_project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Observer;
import PackageModele.*;
import java.util.Observable;

/**
 * @brief: Classe gérant la vue contrôleur grille.
 * 
 * @file: VueControleurGrille.java
 * @author: p1403762 & p1501263
 * @date: 04/12/2021
 */
public class VueControleurGrille extends JFrame implements Observer{
    private static final int PIXEL_PER_SQUARE = 100;
    // tableau de cases : i, j -> case
    private VueCase[][] tabCV;
    // hashmap : case -> i, j
    private HashMap<VueCase, Point> hashmap; // voir (*)
    // currentComponent : par défaut, mouseReleased est exécutée pour le composant (JLabel) sur lequel elle a été enclenchée (mousePressed) même si celui-ci a changé
    // Afin d'accéder au composant sur lequel le bouton de souris est relaché, on le conserve avec la variable currentComponent à
    // chaque entrée dans un composant - voir (**)
    private JComponent currentComponent;
    /**
     * @brief Constructeur de la classe VueControleurGrille
     * @param jeu Jeu Actuel
     */
    public VueControleurGrille(Jeu jeu) {
        setTitle("Flow Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(jeu.getSize() * PIXEL_PER_SQUARE, jeu.getSize() * PIXEL_PER_SQUARE);
        tabCV = new VueCase[jeu.getSize()][jeu.getSize()];
        hashmap = new HashMap<VueCase, Point>();

        JPanel contentPane = new JPanel(new GridLayout(jeu.getSize(), jeu.getSize()));

        for (int i = 0; i < jeu.getSize(); i++) {
            for (int j = 0; j < jeu.getSize(); j++) {

                tabCV[i][j] = new VueCase(i, j, jeu);
                contentPane.add(tabCV[i][j]);

                hashmap.put(tabCV[i][j], new Point(j, i));

                tabCV[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        //Point p = hashmap.get(e.getSource()); // (*) permet de récupérer les coordonnées d'une caseVue


                        VueCase caseActuelle = ((VueCase) e.getSource());
                        jeu.clicCase(caseActuelle.get_x(), caseActuelle.get_y());
                        System.out.println("mousePressed : " + e.getSource());

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        // (**) - voir commentaire currentComponent
                        currentComponent = (JComponent) e.getSource();
                        VueCase caseActuelle = ((VueCase) e.getSource());
                        jeu.entrerCase(caseActuelle.get_x(), caseActuelle.get_y());
                        System.out.println("mouseEntered : " + e.getSource());
                    }


                    @Override
                    public void mouseReleased(MouseEvent e) {
                        // (**) - voir commentaire currentComponent
                        VueCase caseActuelle = ((VueCase) e.getSource());
                        jeu.relacherClic(caseActuelle.get_x(), caseActuelle.get_y());
                        System.out.println("mouseReleased : " + currentComponent);
                    }
                });


            }
        }
        setContentPane(contentPane);

    }

    /**
     * @brief Met à jour l'affichage si on reçoit une notification de l'observable
     */
    @Override
    public void update(Observable o, Object arg){
        repaint();
    }


    public static void main(String[] args) {
        Jeu jeu = new Jeu(1);
        VueMenu menu = new VueMenu(jeu);
        
        VueControleurGrille vue = new VueControleurGrille(jeu);
       
        while(jeu.menuOuvert == true){
           
           
            vue.setVisible(false);
        }
        jeu = new Jeu(menu.getLvl());
        vue = new VueControleurGrille(jeu);
        jeu.addObserver(vue);
        vue.setVisible(true);
            

    }

}
