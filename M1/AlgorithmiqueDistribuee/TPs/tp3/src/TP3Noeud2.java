import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.jbotsim.core.Color;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

public class TP3Noeud2 extends Node {
    public static final int TAILLE_ANNEAU = 5;
    // Etat etat;
    // Integer id_leader;
    protected boolean alive;
    protected Set<Node> trusted;
    private int nb_ticks;
    private Map<Node, Integer> last_Message;

    public void onStart() {
        // etat = Etat.CANDIDAT;
        // id_leader = null;
        // id_leader = getID();
        setColor(Color.PINK);
        alive = true;
        trusted = new HashSet<>();
        // Au départ, on trust tout le monde
        for (int i = 0; i < getNeighbors().size(); i++) {
            trusted.add(getNeighbors().get(i));
        }
        nb_ticks = 0;
        last_Message = new HashMap<>();
    }

    public void onSelection() {
        System.out.println("select du noeud " + getID());        
        setColor(Color.RED);
        alive = false;
    }

    public boolean trust() {
        for (Map.Entry<Node, Integer> entry : last_Message.entrySet()) {
            Node key = entry.getKey();
            Integer value = entry.getValue();
            if ((nb_ticks - value) < 20) {
                if (!trusted.contains(key)) {
                    trusted.add(key);
                } 
            } else {
                if (trusted.contains(key)) {
                    System.out.println("Noeud " + getID() + " " + key.getID() + " : " + value + " ; Nb_ticks " + nb_ticks);
                    System.out.println("Le noeud " + key + " n'est plus fait confiance par le noeud " + getID());
                    trusted.remove(key);
                }
            }
        }
        return true;
    }

    public void onClock() {
        if (alive) {
            if ((nb_ticks % 10) == 0) {
                sendAll(new Message(Phase.HEARTBEAT));
            }
            if ((nb_ticks % 20) == 0) {
                trust();
            }
        }
        nb_ticks++;
    }

    public void onMessage(Message m) {
        if (alive) {
            if (m.getContent() == Phase.HEARTBEAT) {
                last_Message.put(m.getSender(), nb_ticks);
            }
        }
    }
}
