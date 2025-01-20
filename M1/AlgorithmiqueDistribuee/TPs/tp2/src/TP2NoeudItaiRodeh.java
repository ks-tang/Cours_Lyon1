import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

public class TP2NoeudItaiRodeh extends Node {
    public static final int TAILLE_ANNEAU = 10;
    Etat etat;
    Integer id_leader;
    private static int compte = 0;
    public static int nb_executions;
    private static int nb_messages_all = 0;
    private static int nb_messages_min = 100;
    private static int nb_messages_max = 0;
    private static int nb_candidats = 0;

    private Integer num_candidat;
    private Integer phase;
    private Integer nb_transmissions;
    private boolean unique_id;

    public static boolean end = false;

    public void onStart() {
        if (getID() == 0 || Math.random() <= 0.2) {
            etat = Etat.CANDIDAT;
            phase = 1;
            num_candidat = (int)(Math.random() * 20);
            nb_candidats++;
            unique_id = true;
            nb_transmissions = 1;
            candidate();
        }
        else 
            etat = Etat.NON_CANDIDAT;
            id_leader = null;
    }

    public void candidate() {
        System.out.println("Candidat : " + getID() + " new id : " + num_candidat);
        Message m = new Message(new ContenuMessage(num_candidat, 1, 1, true));
        sendAll(m);
        compte++;
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
        getTopology().restart();
    }

    public void onMessage(Message m) {
        ContenuMessage contenu = (ContenuMessage)m.getContent();
        System.out.println("Message de " + contenu.id_candidat + " à " + num_candidat + " phase : " + contenu.numero_phase + 
            " nb_transmissions : " + contenu.nb_transmissions +
            " unique_id : " + contenu.unique_id);
        if (!end) {
        if (etat == Etat.NON_CANDIDAT) {
            sendAll(new Message(new ContenuMessage(contenu.id_candidat, 
                contenu.numero_phase, 
                contenu.nb_transmissions + 1, 
                contenu.unique_id)));
            compte++;
        } else {
            if (contenu.nb_transmissions == getTopology().getNodes().size()) {
                if (contenu.unique_id) {
                    end = true;
                    System.out.println("fin " + compte);
                } else {
                    num_candidat = (int)(Math.random() * 20);
                    sendAll(new Message(new ContenuMessage(
                        num_candidat, 
                        contenu.numero_phase + 1, 
                        1, 
                        true)));
                    compte++;
                }
            } else {
                if (num_candidat == contenu.id_candidat && phase == contenu.numero_phase) {
                    System.out.println(num_candidat + " " + contenu.id_candidat + " " + phase + " " +  contenu.numero_phase);
                    sendAll(new Message(new ContenuMessage(
                        num_candidat, 
                        phase, 
                        contenu.nb_transmissions + 1, 
                        false)));
                    compte++;
                } else if (contenu.id_candidat > num_candidat && phase == contenu.numero_phase) {
                    sendAll(new Message(new ContenuMessage(
                        contenu.id_candidat, 
                        contenu.numero_phase, 
                        contenu.nb_transmissions + 1, 
                        unique_id)));
                    compte++;
                }
            }
        }
    }
    }
}
