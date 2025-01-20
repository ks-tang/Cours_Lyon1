package fr.univlyon1.m1if.m1if03.classes;

/**
 * Messages échangés dans le chat.
 * Cette classe pourra être améliorée dans la suite de l'UE.
 */
public class Message {
    private final String user;
    private String text;

    public Message(String user, String text) {
        this.user = user;
        this.text = text;
    }

    public String getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
