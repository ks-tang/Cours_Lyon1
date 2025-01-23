import io.jbotsim.core.Color;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

public class TP2Noeud extends Node {
    public static final int TAILLE_ANNEAU = 10;
    Etat etat;
    Integer id_leader;
    private static int compte = 0;
    private static int compte_lead = 0;

    public void onStart() {
        compte = 0;
        etat = Etat.CANDIDAT;
        id_leader = null;
        id_leader = getID();
        Message m = new Message(new ContenuMessage(TypeMessage.ELEC, id_leader));
        sendAll(m);
        compte++;
        // nb_noeuds_traites++;
        setColor(Color.getColorAt(id_leader));
    }

    public void sendMessage(TypeMessage tm, Integer id_lead) {

    }

    public void onSelection() {
        System.out.println("select du noeud " + getID());
    }

    public void onMessage(Message m) {
        ContenuMessage contenu = (ContenuMessage)m.getContent();
        System.out.println("Message pour " + getID() + " de " + m.getSender().getID() +
            " : " + contenu.type.toString() + " " + contenu.id_candidat);
        if (contenu.type == TypeMessage.ELEC) {
            System.out.println("cannnnnnnnnnnnnnnnddddddidat");
            if (getID() > contenu.id_candidat) {
                if (etat == Etat.CANDIDAT) {
                    id_leader = getID();
                    sendAll(new Message(new ContenuMessage(TypeMessage.ELEC, id_leader)));
                    compte++;
                    setColor(Color.getColorAt(id_leader));
                }
            } if (contenu.id_candidat > getID()) {
                etat = Etat.PERDU;
                id_leader = contenu.id_candidat;
                sendAll(new Message(new ContenuMessage(TypeMessage.ELEC, id_leader)));
                compte++;
                setColor(Color.getColorAt(id_leader));
            } else if (contenu.id_candidat == getID()) {
                etat = Etat.ELU;
                id_leader = contenu.id_candidat;
                sendAll(new Message(new ContenuMessage(TypeMessage.LEAD, id_leader)));
                compte_lead++;
            }
        } else if (contenu.type == TypeMessage.LEAD) {
            System.out.println("lead message " + getID() + " " + contenu.id_candidat);
            if (getID() == contenu.id_candidat) {
                System.out.print("Fin de l'exécution  : " + compte +  " messages candidatures, ");
                System.out.println(compte_lead + " messages d'élections.");
            } else {
                System.out.println("heeeeeeeeeeeeeeeeeeeeeeeeeeeeere");
                id_leader = contenu.id_candidat;
                sendAll(new Message(new ContenuMessage(TypeMessage.LEAD, id_leader)));
                compte_lead++;
            }
        }
    }
}
