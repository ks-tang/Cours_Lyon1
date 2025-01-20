public class MsgProposition {
    Phase phase;
    Integer valeur;
    Integer no_ronde;

    public MsgProposition(Integer valeur, Integer no_ronde) {
        this.phase = Phase.PROPOSITION;
        this.valeur = valeur;
        this.no_ronde = no_ronde;
    }
}
