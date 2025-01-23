public class MessageCoordinateur {
    Phase phase;
    Integer no_ronde;
    Integer valeur;
    boolean accepte;
    Integer no_ronde_propose;
    Integer no_ronde_envoye;

    /**
     * Message ACK
     * @param no_ronde
     * @param accepte
     */
    public MessageCoordinateur(Integer no_ronde, boolean accepte) {
        this.phase = Phase.ACK;
        this.no_ronde = no_ronde;
        this.accepte = accepte;
    }

    /**
     * Message DECISION
     */
    public MessageCoordinateur(Integer valeur) {
        this.phase = Phase.DECISION;
        this.valeur = valeur;
    }    

    /**
     * Messgae ESTIMATION
     * @param valeur
     * @param no_ronde_propose
     * @param no_ronde_envoye
     */
    public MessageCoordinateur(Integer valeur, Integer no_ronde_propose, Integer no_ronde_envoye) {
        this.phase = Phase.ESTIMATION;
        this.valeur = valeur;
        this.no_ronde_propose = no_ronde_propose;
        this.no_ronde_envoye = no_ronde_envoye;
    }

    /**
     * Message PROPOSITION
     * @param valeur
     * @param no_ronde
     */
    public MessageCoordinateur(Integer valeur, Integer no_ronde) {
        this.phase = Phase.PROPOSITION;
        this.valeur = valeur;
        this.no_ronde = no_ronde;
    }
}
