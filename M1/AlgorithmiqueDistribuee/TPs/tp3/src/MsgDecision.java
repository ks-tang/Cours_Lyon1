public class MsgDecision {
    Phase phase;
    Integer valeur;

    public MsgDecision(Integer valeur) {
        this.phase = Phase.DECISION;
        this.valeur = valeur;
    }
}
