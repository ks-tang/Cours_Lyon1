package lifap7_project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Observer;
import PackageModele.*;
import java.util.Observable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @brief: Classe gérant la vue du menu du début de jeu.
 * 
 * @file: VueMenu.java
 * @author: p1403762 & p1501263
 * @date: 04/12/2021
 */
public class VueMenu extends JFrame {

    public int lvl;

    /**
     * @brief Constructeur de la classe VueMenu.
     * @param jeu Jeu actuel
     */
    public VueMenu(Jeu jeu){
        JButton bouton1 = new JButton("Facile");
        bouton1.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt){
                lvl = 3;
                jeu.menuOuvert = false;
                dispose();
            }
        });

        JButton bouton2 = new JButton("Moyen");
        bouton2.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt){
                lvl = 5;
                jeu.menuOuvert = false;
                dispose();
            }
        });

        JButton bouton3 = new JButton("Expert");
        bouton3.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt){
                lvl = 9;
                jeu.menuOuvert = false;
                dispose();
            }
        });

        JLabel lvl = new JLabel("Choisissez un niveau !");
        JPanel panneau = new JPanel();
        panneau.setPreferredSize(new Dimension(800,800));

        panneau.setBackground(Color.GREEN);
        panneau.add(lvl);
        panneau.add(bouton1);
        panneau.add(bouton2);
        panneau.add(bouton3);
        setContentPane(panneau);
        setLocation(0,0);
        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * @brief Getter du nombre de cases du lvl
     * @return int
     */
    public int getLvl(){
        return this.lvl;
    }
}
