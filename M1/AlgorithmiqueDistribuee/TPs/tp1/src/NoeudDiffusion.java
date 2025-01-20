import io.jbotsim.core.Color;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

public class NoeudDiffusion extends Node {
    private boolean hasReceivedBroadcast; 
    private static int compte = 0;
    private static int nb_noeuds_traites = 0;

    public void onStart() {
        hasReceivedBroadcast = false;
    }

    public void onSelection() {
        hasReceivedBroadcast = true;
        System.out.println("Vous avez sélectionné le noeud numéro " + getID() + "!");
        Message m = new Message("Bonjour voisin");
        sendAll(m);
        compte += getNeighbors().size();
        nb_noeuds_traites++;
        setColor(Color.PINK);
    }

    public void onMessage(Message m) {
        if (!hasReceivedBroadcast) {
            System.out.println("Le noeud " + getID() + " a reçu le message suivant du noeud "
            + m.getSender() + ": \"" + m.getContent() + "\"");
            hasReceivedBroadcast = true;
            setColor(Color.PINK);
            nb_noeuds_traites++;

            if (nb_noeuds_traites >= getTopology().getNodes().size()) { // si fin on n'envoie pas le message
                System.out.println();
                System.out.println("> Nombre de nœuds traités : " + nb_noeuds_traites);
                System.out.println("> Nombre de messages envoyés : " + compte);
            } else { // sinon on envoie à nos voisins
                Message m_next = new Message("Bonjour voisin");
                sendAll(m_next);
                compte += getNeighbors().size();
            }
        }
    }
}
