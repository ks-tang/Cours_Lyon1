public class MsgEstimation {
    Phase phase;
    Integer valeur;
    Integer no_ronde_propose;
    Integer no_ronde_envoye;

    public MsgEstimation(Integer valeur, Integer no_ronde_propose, Integer no_ronde_envoye) {
        this.phase = Phase.ESTIMATION;
        this.valeur = valeur;
        this.no_ronde_propose = no_ronde_propose;
        this.no_ronde_envoye = no_ronde_envoye;
    }
}
