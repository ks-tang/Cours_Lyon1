package App.movement;

import javafx.util.Pair;

import java.util.LinkedList;

public class Movements {

    private int SIZE = 6;
    private LinkedList<Pair<Position, Position>> movements = new LinkedList<>();

    //Ajoute le movement à la liste.
    public void add(Position from, Position to){
        if(movements.size() == SIZE){
            movements.pop();
        }
        movements.push(new Pair<>(from, to));
    }

    //Détecte s'il y a une boucle dans la liste des mouvements.
    public boolean detectLoop(){
        for (int startPos = 0; startPos < movements.size(); startPos++) {

            for (int sequenceLength = 2; sequenceLength <= (movements.size() - startPos) / 2; sequenceLength++) {


                boolean sequencesAreEqual = true;
                for (int i = 0; i < sequenceLength; i++) {
                    boolean sameDepart = pointAreEquals(movements.get(startPos + i).getKey(),movements.get(startPos + sequenceLength + i).getKey());
                    boolean sameDest = pointAreEquals(movements.get(startPos + i).getValue(),movements.get(startPos + sequenceLength + i).getValue());
                    if (!sameDepart && !sameDest) {
                        sequencesAreEqual = false;
                        break;
                    }
                }
                if (sequencesAreEqual) {
                    return true;
                }
            }
        }

        return false;
    }

    //Vérifie si les 2 positions sont égales.
    private boolean pointAreEquals(Position a, Position b){
        return b.getX() == a.getX() && b.getY() == a.getY();
    }

}
