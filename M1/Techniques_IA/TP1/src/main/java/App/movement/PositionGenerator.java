package App.movement;


import java.util.ArrayList;
import java.util.Random;

import App.model.Grid;

public class PositionGenerator {
    private Grid grid;
    private int minX;
    private int minY;
    private int maxX;
    private int maxY;
    private static final int TRIES = 100;

    public PositionGenerator(Grid g) {
        minX = 0;
        minY = 0;
        maxX = g.getWidth();
        maxY = g.getHeight();
        grid = g;
    }

    //Génère une position aléatoire en fonction du booléen isStart. Retourne une case vide si nous voulons une case de départ, et retourne une case qui n'est pas la case de départ si nous voulons une position finale.
    public Position rand(boolean isStart) {
        boolean isEmpty;
        boolean isFinalState;
        Random random = new Random();
        Position pos;

        do {
            int x = random.nextInt(maxX - minX) + minX;
            int y = random.nextInt(maxY - minY) + minY;
            pos = new Position(x, y);
            isEmpty = grid.isCaseFree(x,y);
            isFinalState = grid.isAgentFinalState(x,y);
        } while ((isStart && !isEmpty) || (!isStart && isFinalState));

        return pos;
    }

    //Génère une position aléatoire à côté de la position donnée qui n'est pas bloquée ou hors de la grille.
    public Position generateRandomMovement(Position pos) {
        int tries = 0;
        int nextx;
        int nexty;
        do {
            tries++;
            double r = Math.round(Math.random()) * 2 - 1;
            double xory = Math.round(Math.random());
            nextx = (int) (pos.getX() + ((xory == 0) ? r : 0));
            nexty = (int) (pos.getY() + ((xory == 1) ? r : 0));
        } while (tries <= TRIES && (grid.isOutOfMap(nextx, nexty) || grid.isBlocked(nextx, nexty)));

        if(tries <= TRIES) {
            return new Position(nextx, nexty);
        } else {
            return null;
        }
    }

    //Génère une position aléatoire à côté de la position donnée qui n'est pas hors de la carte et qui n'est pas dans la liste "blacklist" donnée en paramètre.
    public Position generateRandomNotFreeMovement(Position pos, ArrayList<Position> blacklist) {
        int tries = 0;
        int nextx;
        int nexty;
        do {
            tries++;
            double r = Math.round(Math.random()) * 2 - 1;
            double xory = Math.round(Math.random());
            nextx = (int) (pos.getX() + ((xory == 0) ? r : 0));
            nexty = (int) (pos.getY() + ((xory == 1) ? r : 0)); 
        } while (tries <= TRIES && ((grid.isOutOfMap(nextx, nexty)) || blacklist.contains(new Position(nextx, nexty))));

        if(tries <= TRIES) {
            return new Position(nextx, nexty);
        } else {
            return null;
        }
    }
}
