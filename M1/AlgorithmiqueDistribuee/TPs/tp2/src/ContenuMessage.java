public class ContenuMessage {
    TypeMessage type; // Ces deux attributs sont un exemple de contenu d’un message
    Integer id_candidat;
    Integer numero_phase;
    Integer nb_transmissions;
    boolean unique_id;
    boolean end = false;

    public ContenuMessage(TypeMessage _type, Integer _id_candidat) {
        this.type = _type; 
        this.id_candidat = _id_candidat;
    }

    public ContenuMessage(Integer id_candidat, Integer numero_phase, Integer nb_transmissions, boolean unique_id) {
        this.id_candidat = id_candidat;
        this.numero_phase = numero_phase;
        this.nb_transmissions = nb_transmissions;
        this.unique_id = unique_id;
    }

    public ContenuMessage(Integer id_candidat, Integer numero_phase, Integer nb_transmissions, boolean unique_id, boolean end) {
        this.id_candidat = id_candidat;
        this.numero_phase = numero_phase;
        this.nb_transmissions = nb_transmissions;
        this.unique_id = unique_id;
        this.end = end;
    }
}
