public class MsgAck {
    Phase phase;
    Integer no_ronde;
    boolean accepte;

    public MsgAck(Integer no_ronde, boolean accepte) {
        this.phase = Phase.ACK;
        this.no_ronde = no_ronde;
        this.accepte = accepte;
    }
}
