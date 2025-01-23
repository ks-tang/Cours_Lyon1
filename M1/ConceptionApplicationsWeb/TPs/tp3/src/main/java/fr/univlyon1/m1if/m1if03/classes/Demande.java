package fr.univlyon1.m1if.m1if03.classes;

/**
 * Représente une demande faite par un utilisateur à un autre utilisateur au sujet d'une ressource.<br>
 * Dans un premier temps, les instances seront uniquement des demandes d'adhésion à des salons.
 * Plus tard, ce pourront être des demandes de modification / suppression de ressources.
 */
public class Demande {
    private final int idDemande;
    private final String user;
    private final String destinataire;
    private final String salon;
    private final String action;
    private enum State {
        pending,
        accepted,
        refused
    };
    private State state;

    /**
     * Crée une nouvelle demande.
     * @param idDemande Id de la demande
     * @param user Utilisateur à l'origine de la demande
     * @param destinataire Utilisateur à la destination de la demande
     * @param salon Salon concerné par la demande
     * @param action Action demandée par l'utilisateur ; pour un salon, c'est soit une adhésion (register), soit une désinscription (quit)
     */
    public Demande(int idDemande, String user, String destinataire, String salon, String action) {
        this.idDemande = idDemande;
        this.user = user;
        this.destinataire = destinataire;
        this.salon = salon;
        this.action = action;
        this.state = State.pending;
    }

    /**
     * Renvoie l'id de la demande.
     * @return L'id de la demande
     */
    public int getId() {
        return idDemande;
    }

    /**
     * Renvoie le login de l'utilisateur à l'origine de la demande.
     * @return Le login de l'utilisateur à l'origine de la demande
     */
    public String getUser() {
        return user;
    }

    /**
     * Renvoie le login de l'utilisateur à la destination de la demande.
     * @return Le login de l'utilisateur à la destination de la demande
     */
    public String getDestinataire() {
        return destinataire;
    }

    /**
     * Renvoie le salon concerné par la demande.
     * @return Le salon concerné par la demande
     */
    public String getSalon() {
        return salon;
    }

    /**
     * Renvoie l'action demandée par l'utilisateur.
     * @return L'action demandée par l'utilisateur (<code>register</code> ou <code>quit</code>)
     */
    public String getAction() {
        return action;
    }

    /**
     * Renvoie l'état courant de la demande.
     * @return L'état courant de la demande
     */
    public String getState() {
        return state == State.pending ? "En cours" : (state == State.accepted ? "Acceptée" : "Refusée");
    }

    /**
     * Accepte la demande.
     * L'état de la demande est modifié pour <code>accepted</code>
     */
    public void accept() {
        this.state = State.accepted;
    }

    /**
     * Refuse la demande.
     * L'état de la demande est modifié pour <code>refused</code>
     */
    public void refuse() {
        this.state = State.refused;
    }
}
