package fr.univlyon1.m1if.m1if03.model;

/**
 * Message échangé sur un salon.
 *
 * @author Lionel Médini
 */
public class Message {
    private final String author;
    private final Integer salon;
    private String text;

    /**
     * Crée un nouveau message.
     * @param salon L'id du salon auquel le message appartient
     * @param author L'id de l'utilisateur auteur du message
     * @param text Le texte du message
     */
    public Message(Integer salon, String author, String text) {
        this.salon = salon;
        this.author = author;
        this.text = text;
    }

    /**
     * Renvoie l'ID du salon auquel le message appartient.
     * @return L'ID du salon auquel le message appartient
     */
    public Integer getSalon() {
        return salon;
    }

    /**
     * Renvoie l'ID de l'auteur du message.
     * @return L'ID de l'auteur du message
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Renvoie le texte du message.
     * @return Le texte du message
     */
    public String getText() {
        return text;
    }

    /**
     * Modifie le texte du message.
     * @param text Le nouveau texte du message
     */
    public void setText(String text) {
        this.text = text;
    }
}
