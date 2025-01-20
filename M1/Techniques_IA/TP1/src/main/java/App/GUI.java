package App;


import javax.swing.*;

import App.model.Agent;
import App.model.Grid;

import java.awt.*;

public class GUI {
    JFrame f;
    JFrame f1;
    JFrame f2;
    JButton[][] buttons;
    JButton[][] buttonsInitial;
    JButton[][] buttonsFinal;

    public GUI(int width, int height, Grid grid) {
        f = new JFrame("Grid");
        f1 = new JFrame("Initial Grid");
        f2 = new JFrame("Final Grid");
        buttons = new JButton[width][height];
        buttonsInitial = new JButton[width][height];
        buttonsFinal = new JButton[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int gridValue = grid.getGrid()[i][j];
                String buttonText = "";
                JButton b;
                JButton b1;
                JButton b2;
                if (gridValue != -1) {
                    buttonText = Grid.getAgents().get(gridValue).getLetter();
                }

                b = new JButton();
                buttons[i][j] = b;
                buttons[i][j].setBackground(Color.white);
                buttons[i][j].setText(buttonText);
                b1 = new JButton();
                buttonsInitial[i][j] = b1;
                buttonsInitial[i][j].setBackground(Color.white);
                buttonsInitial[i][j].setText(buttonText);
                b2 = new JButton();
                buttonsFinal[i][j] = b2;
                buttonsInitial[i][j].setBackground(Color.white);
                buttonsInitial[i][j].setText(buttonText);
                f.add(b);
                f1.add(b1);
                f2.add(b2);
            }
        }

        f.setLayout(new GridLayout(width, height));
        f1.setLayout(new GridLayout(width, height));
        f2.setLayout(new GridLayout(width, height));

        f.setSize(600, 600);
        f1.setSize(300,300);
        f2.setSize(300,300);
        f.setLocationRelativeTo(null);
        f1.setLocationRelativeTo(null);
        f2.setLocationRelativeTo(null);
        f.setVisible(true);
        f1.setVisible(true);
        f2.setVisible(true);
    }

    public synchronized void move(int x, int y, Agent agent) {
        this.buttons[x][y].setText(agent.getLetter());
        if (agent.isToFinalState())
            this.buttons[x][y].setBackground(Color.green);
    }

    public synchronized void init(int x, int y, Agent agent) {
        this.buttonsInitial[x][y].setText(agent.getLetter());
    }

    public synchronized void finale(int x, int y, Agent agent) {
        this.buttonsFinal[x][y].setText(agent.getLetter());
    }

    public synchronized void remove(int x, int y) {
        this.buttons[x][y].setText("");
        this.buttons[x][y].setBackground(Color.white);
    }

}
