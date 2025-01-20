import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.jbotsim.core.Color;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

public class TP3NoeudCooTourn extends TP3Noeud2 {
    private Integer value;
    private Integer ts;
    private Integer ronde;
    private Phase phase;
    private Map<Integer,Set<Message>> estimationsReceived;
    private Map<Integer, Message> propsReceived;
    private Set<Message> acksReceived;

    public void onStart() {
        super.onStart(); 
        value = (int) Math.round(Math.random() * 100);
        ts = 0;
        ronde = 0;
        phase = Phase.DECISION; 
        estimationsReceived = new HashMap<>();
        propsReceived = new HashMap<>();
        acksReceived = new HashSet<>();
        nouvelleRonde();       
    }

    private Node coordinateur(int ronde) {
        int id = ronde % getTopology().getNodes().size();
        return getTopology().findNodeById(id);
    }

    public void nouvelleRonde() {
        ronde++;
        phase = Phase.ESTIMATION;
        send(coordinateur(ronde), new Message(new MessageCoordinateur(value, ts, ronde)));
        System.out.println("Le noeud " + getID() +  " passe à la ronde " + ronde + ".");

        if (propsReceived.containsKey(ronde)) {
            MessageCoordinateur mc = (MessageCoordinateur)propsReceived.get(ronde).getContent();
            phase = Phase.PROPOSITION;
            if (!trusted.contains(coordinateur(ronde))) {
                send(coordinateur(ts), new Message(new MessageCoordinateur(ts, false)));
            } else {
                value = mc.valeur;
                ts = mc.no_ronde;
                send(coordinateur(ts), new Message(new MessageCoordinateur(ts, true)));
            }
            nouvelleRonde();
        }
    }

    public void onClock() {
        if (alive) {
            super.onClock();    
            if (!trusted.contains(coordinateur(ronde))) {
                nouvelleRonde();
            }
        } else {
            setColor(Color.RED);
        }
    }
    public void onMessage(Message m) {
        if (alive) {
            if (m.getContent() == Phase.HEARTBEAT) {
                super.onMessage(m); 
            } else {
                MessageCoordinateur mc = (MessageCoordinateur)m.getContent();
                if (mc.phase == Phase.ESTIMATION) {
                    System.out.println("ESTIMATION");
                    // Phase phase;
                    // Integer valeur;
                    // Integer no_ronde_propose;
                    // Integer no_ronde_envoye;
                
                    if (mc.no_ronde_envoye >= ronde) {
                        if (estimationsReceived.get(mc.no_ronde_envoye) == null)
                            estimationsReceived.put(mc.no_ronde_envoye, new HashSet<>());
                        estimationsReceived.get(mc.no_ronde_envoye).add(m);
                        
                        if (estimationsReceived.get(ronde) != null) {
                            if (estimationsReceived.get(ronde).size() >= (TAILLE_ANNEAU / 2)) {
                                System.out.println("Moi avoir reçu majorité messages.");
                                for (Message msg : estimationsReceived.get(ronde)) {
                                    MessageCoordinateur msgCo = (MessageCoordinateur)msg.getContent();
                                    
                                    if (msgCo.no_ronde_propose > ts) {
                                        ts = msgCo.no_ronde_propose;
                                        value = msgCo.valeur;
                                    }
                                }

                                phase = Phase.PROPOSITION;
                                acksReceived.clear();

                                sendAll(new Message(new MessageCoordinateur(value, ronde)));
                            }
                        }
                    }
                } else if (mc.phase == Phase.PROPOSITION) {
                    if (mc.no_ronde == ronde) {
                        value = mc.valeur;
                        ts = mc.no_ronde;

                        send(coordinateur(ts), new Message(new MessageCoordinateur(ts, true)));
                        nouvelleRonde();
                    } else if (mc.no_ronde < ronde) {
                        send(coordinateur(ts), new Message(new MessageCoordinateur(mc.no_ronde, false)));
                    } else {
                        propsReceived.put(mc.no_ronde, m);
                    }
                } else if (mc.phase == Phase.ACK) {
                    // Phase phase;
                    // Integer no_ronde;
                    // boolean accepte;
                    if (mc.no_ronde < ronde) {

                    } else  if (mc.no_ronde == ronde) {
                        if (!acksReceived.contains(m))
                            acksReceived.add(m);
                        if (acksReceived.size() >= (TAILLE_ANNEAU / 2)) {
                            System.out.println("J'avoir reçu majorité acks.");
                            boolean ok = true;

                            for (Message msg : acksReceived) {
                                MessageCoordinateur msgCo = (MessageCoordinateur)msg.getContent();
                                // Traitez chaque élément ici
                                if (!msgCo.accepte) {
                                    ok = false;
                                }
                            }
                            if (ok) {
                                phase = Phase.DECISION;
                                sendAll(new Message(new MessageCoordinateur(value)));
                            } else {
                                nouvelleRonde();
                            }
                            acksReceived.clear();
                        }
                    } else {

                    }

                } else if (mc.phase == Phase.DECISION) {
                    // Phase phase;
                    // Integer valeur;
                    System.out.println("Moi " + getID() + ", décide de la valeur " + mc.valeur + ".");
                    
                    sendAll(new Message(new MessageCoordinateur(value)));

                    alive = false;
                }
            }
        }
    }
}