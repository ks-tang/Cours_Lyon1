package fr.univlyon1.m1if.m1if03.classes;

/**
 * Messages échangés dans le chat.
 * Cette classe pourra être améliorée dans la suite de l'UE.
 */
public class Message {
    private final String user;
    private String text;
    private final int idMessage;
    private final String idSalon;

    
    public Message(String user, String text) {
        this.user = user;
        this.text = text;
        this.idMessage = 0;
        this.idSalon = null;
    }

    public Message(String user, String text, int idmessage, String idsalon) {
        this.user = user;
        this.text = text;
        this.idMessage = idmessage;
        this.idSalon = idsalon;
    }

    
    public Message(String user, String text, String idsalon) {
        this.user = user;
        this.text = text;
        this.idMessage = 0;
        this.idSalon = idsalon;
    }

    public String getUser() {
        return user;
    }

    public String getText() {
        return text;
    }
    
    public int getIdMessage() {
        return idMessage;
    }

    public String getIdSalon() {
        return idSalon;
    }

    public void setText(String text) {
        this.text = text;
    }
}
