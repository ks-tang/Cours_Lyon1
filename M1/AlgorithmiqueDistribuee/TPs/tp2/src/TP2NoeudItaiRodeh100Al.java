import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

public class TP2NoeudItaiRodeh100Al extends Node {
    public static final int TAILLE_ANNEAU = 10;
    Etat etat;
    Integer id_leader;
    private static int compte = 0;
    private static int compte_term = 0;
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
        compte++;
        Message m = new Message(new ContenuMessage(num_candidat, 1, 1, true, false));
        sendAll(m);
    }

    public void endIteration() {
        System.out.print("Itération " + nb_executions + " : " + compte + " messages envoyés ; " + compte_term + " messages terminaison; " );
        System.out.println(nb_candidats + " candidats.");
        nb_messages_all += compte;
        nb_messages_all += compte_term;
        if (compte +  compte_term > nb_messages_max) nb_messages_max = compte + compte_term;
        if (compte +  compte_term < nb_messages_min) nb_messages_min = compte + compte_term;
        nb_executions++;
        nb_candidats = 0;
        compte = 0;
        compte_term = 0;
        if (nb_executions < 100) {
            startNewIteration();
        } else {
            end = true;
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
        if (!end) {
            if (!contenu.end) {
                if (etat == Etat.NON_CANDIDAT) {
                    compte++;
                    sendAll(new Message(new ContenuMessage(contenu.id_candidat, 
                        contenu.numero_phase, 
                        contenu.nb_transmissions + 1, 
                        contenu.unique_id,
                        false)));
                } else {
                    if (contenu.nb_transmissions == getTopology().getNodes().size()) {
                        System.out.println(contenu.nb_transmissions + " " + getTopology().getNodes().size());
                        if (contenu.unique_id) {
                            compte_term++;
                            sendAll(new Message(new ContenuMessage(
                                num_candidat, 
                                contenu.numero_phase + 1, 
                                1, 
                                true,
                                true)));
                        } else {
                            num_candidat = (int)(Math.random() * 20);
                            compte++;
                            sendAll(new Message(new ContenuMessage(
                                num_candidat, 
                                contenu.numero_phase + 1, 
                                1, 
                                true,
                                false)));
                        }
                    } else {
                        if (num_candidat == contenu.id_candidat && phase == contenu.numero_phase) {
                            System.out.println(num_candidat + " " + contenu.id_candidat + " " + phase + " " +  contenu.numero_phase);
                            compte++;
                            sendAll(new Message(new ContenuMessage(
                                num_candidat, 
                                phase, 
                                contenu.nb_transmissions + 1, 
                                false)));
                        } else if (contenu.numero_phase > phase || (contenu.id_candidat > num_candidat && phase == contenu.numero_phase)) {
                            compte++;
                            sendAll(new Message(new ContenuMessage(
                                contenu.id_candidat, 
                                contenu.numero_phase, 
                                contenu.nb_transmissions + 1, 
                                unique_id,
                                false)));
                        }
                    }
                }
            } else {
                if (num_candidat == contenu.id_candidat) {
                    endIteration();
                } else {
                    compte_term++;
                    sendAll(new Message(new ContenuMessage(contenu.id_candidat, 
                        contenu.numero_phase, 
                        contenu.nb_transmissions, 
                        contenu.unique_id, 
                        true)));
                }
            }
        }
    }
}
