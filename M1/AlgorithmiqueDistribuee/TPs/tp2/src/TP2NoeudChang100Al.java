import java.util.ArrayList;
import java.util.List;

import io.jbotsim.core.Color;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

public class TP2NoeudChang100Al extends Node {
    public static final int TAILLE_ANNEAU = 10;
    Etat etat;
    Integer id_leader;
    private static int compte = 0;
    public static int nb_executions;
    private static int nb_messages_all = 0;
    private static int nb_messages_min = Integer.MAX_VALUE;
    private static int nb_messages_max = 0;
    private static int nb_candidats = 0;

    public void onStart() {
        if (getID() == 0 || Math.random() <= 0.2) {
            etat = Etat.CANDIDAT;
            nb_candidats++;
            candidate();
        }
        else {
            etat = Etat.NON_CANDIDAT;
            id_leader = null;
        }
    }

    public void candidate() {
        id_leader = getID();
        Message m = new Message(new ContenuMessage(TypeMessage.ELEC, id_leader));
        compte++;
        sendAll(m);
    }

    public void endIteration() {
        System.out.print("Itération " + nb_executions + " : " + compte + " messages envoyés ; ");
        System.out.println(nb_candidats + " candidats.");
        nb_messages_all += compte;
        if (compte > nb_messages_max) nb_messages_max = compte;
        if (compte < nb_messages_min) nb_messages_min = compte;
        nb_executions++;
        nb_candidats = 0;
        compte = 0;
        if (nb_executions < 100) {
            startNewIteration();
        } else {
            System.out.println("Nombre de messages moyen : " + (nb_messages_all / 100));
            System.out.println("Nombre minimal de messages : " + nb_messages_min);
            System.out.println("Nombre maximal de messages : " + nb_messages_max);
        }
    }
    public void startNewIteration() {
        List<Integer> melangeur = new ArrayList<>();
        for (Integer i = 0; i < TAILLE_ANNEAU; i++) {
            melangeur.add(i);
        }
        for (Integer i = 0; i < getTopology().getNodes().size(); i++) {
            double rand = Math.random() * melangeur.size();
            getTopology().getNodes().get(i).setID(melangeur.get((int)rand));
            melangeur.remove((int)rand);
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        getTopology().restart();
    }

    public void onMessage(Message m) {
        ContenuMessage contenu = (ContenuMessage)m.getContent();
        if (contenu.type == TypeMessage.ELEC) {
            if (getID() > contenu.id_candidat) {
                if (etat == Etat.NON_CANDIDAT) {
                    etat = Etat.CANDIDAT;
                    id_leader = getID();
                    compte++;
                    sendAll(new Message(new ContenuMessage(TypeMessage.ELEC, id_leader)));
                    setColor(Color.getColorAt(id_leader));
                }
            } else if (contenu.id_candidat > getID()) {
                etat = Etat.PERDU;
                id_leader = contenu.id_candidat;
                compte++;
                sendAll(new Message(new ContenuMessage(TypeMessage.ELEC, id_leader)));
                setColor(Color.getColorAt(id_leader));
            } else if (contenu.id_candidat == getID()) {
                etat = Etat.ELU;
                id_leader = contenu.id_candidat;
                compte++;
                sendAll(new Message(new ContenuMessage(TypeMessage.LEAD, id_leader)));
            }
        } else if (contenu.type == TypeMessage.LEAD) {
            if (getID() == contenu.id_candidat) {
                endIteration();
            } else {
                id_leader = contenu.id_candidat;
                compte++;
                sendAll(new Message(new ContenuMessage(TypeMessage.LEAD, id_leader)));
            }
        }
    }
}
