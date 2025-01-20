package fr.univlyon1.m1if.m1if03.dto.message;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * DTO qui contient la totalité des données d'un utilisateur renvoyé par une vue du serveur.
 *
 * @author Lionel Médini
 */
@JacksonXmlRootElement(localName = "message")
public class MessageResponseDto {
    private final String author;
    private final Integer salon;
    private final String text;

    /**
     * Crée un <code>MessageResponseDto</code> à templater dans la réponse.
     * @param author Le login de l'utilisateur créateur du message
     * @param salon Le salon contenant le message
     * @param text Le texte du message
     */
    public MessageResponseDto(String author, Integer salon, String text) {
        this.author = author;
        this.salon = salon;
        this.text = text;
    }

    /**
     * Renvoie le login du créateur du message.
     * @return Le login du créateur du message
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Renvoie l'id du salon contenant le message.
     * @return L'id du salon contenant le message
     */
    public Integer getSalon() {
        return salon;
    }

    /**
     * Renvoie le texte du message.
     * @return Le texte du message
     */
    public String getText() {
        return text;
    }
}
