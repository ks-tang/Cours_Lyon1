import java.util.ArrayList;
import java.util.List;

import io.jbotsim.core.Color;
import io.jbotsim.core.Link;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

public class ArbreCouvrant extends Node {
    private Node pere;
    private List<Node> enfants;
    private Integer nb_reponses;
    private boolean is_root;

    private static int compte_construction = 0;
    private static int compte_bcast = 0;
    private static int nb_noeuds_traites = 0;

    public void onStart() {
        pere = null;
        enfants = new ArrayList<>();
        nb_reponses = 0;
        is_root = false;
    }
    
    public void onSelection() {
        is_root = true;
        setColor(Color.YELLOW);
        System.out.println("Vous avez sélectionné le noeud numéro " + getID() + "!");
        Message m = new Message(TypeMessage.JOIN);
        sendAll(m);
        compte_construction+=getNeighbors().size();
    }

    public void onMessage(Message m) {
        System.out.println("Le noeud " + getID() + " a reçu le message suivant du noeud "
        + m.getSender() + ": \"" + m.getContent() + "\"");
        if (m.getContent().equals(TypeMessage.JOIN)) {
            //compte_construction++;
            if(pere == null) {
                pere = m.getSender();
                setColor(Color.YELLOW);
                Link link = getCommonLinkWith(pere);
                link.setColor(Color.YELLOW);

                if (getNeighbors().size() > 1) { // S'il n'a qu'un seul voisin, c'est son père
                    for (Integer i = 0; i < getNeighbors().size(); i++) {
                        if (getNeighbors().get(i).getID() != m.getSender().getID()) { // On envoie à tout le monde auf au père
                            Message m_join = new Message(TypeMessage.JOIN);
                            send(getNeighbors().get(i), m_join);
                            compte_construction++;
                        }
                    }
                } else {
                    Message m_back = new Message(TypeMessage.BACK);
                    send(pere, m_back);
                    setColor(Color.GREEN);
                    compte_construction++;
                }

            } else { // envoie BACKNO car a déjà join
                Message m_back_no = new Message(TypeMessage.BACK_NO);
                send(m.getSender(), m_back_no);
                compte_construction++;
            }
            checkEnd();
        } else if (m.getContent().equals(TypeMessage.BACK) || (m.getContent().equals(TypeMessage.BACK_NO))) {
            //compte_construction++;
            if (m.getContent().equals(TypeMessage.BACK)) {
                enfants.add(m.getSender());
            }
            nb_reponses++;
            checkEnd();
        } else if (m.getContent().equals(TypeMessage.BCAST)) {
            setColor(Color.RED);
            diffuse();
            nb_noeuds_traites++;
            if (nb_noeuds_traites >= getTopology().getNodes().size()-1) {
                System.out.println();
                System.out.println("Nombre de noeuds traités : " + nb_noeuds_traites);
                System.out.println("Nombre de messages construction de l'arbre couvrant : " + compte_construction);
                System.out.println("Nombre de messages diffusion : " + compte_bcast);
            }
        }
    }

    public void checkEnd() {
        if (nb_reponses >= getNeighbors().size()-1 && !is_root) {
            setColor(Color.GREEN);
            Message m = new Message(TypeMessage.BACK);
            send(pere, m);
            compte_construction++;
        } else if (is_root && nb_reponses >= getNeighbors().size()) {
            System.out.println(nb_reponses + " " + getNeighbors().size());
            setColor(Color.PINK);
            System.out.println("Fin !");
            diffuse();
        }
    }

    public void diffuse() {
        for (Integer i = 0; i < enfants.size(); i++) {
            send(enfants.get(i), new Message(TypeMessage.BCAST));
            compte_bcast++;
        }
    }
}
