package App.model;

import java.util.Random;

public class LetterGenerator {
    private Grid grid;

    public LetterGenerator(Grid g) {
        grid = g;
    }

    //Récupère un caractère aléatoire dans la table ASCII de '!' à '~' (95 symboles possibles) qui n'est pas dans la grille
    public String rand() {
        boolean letterInGrid;
        String s;
        do {
            Random r = new Random();
            char c = (char)(r.nextInt('~' - '!') + '!');
            s = String.valueOf(c);
            letterInGrid = grid.isAgentLetter(s);
        } while (letterInGrid);
        
        return s;
    }
}
