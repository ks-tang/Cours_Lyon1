package fr.univlyon1.m1if.m1if03.dto.message;

import fr.univlyon1.m1if.m1if03.dao.MessageDao;
import fr.univlyon1.m1if.m1if03.model.Message;

import jakarta.servlet.ServletContext;

import javax.naming.InvalidNameException;
import javax.naming.NameNotFoundException;

/**
 * Application du pattern DTO.<br>
 * Réalise le mapping pour les différents types de DTO de <code>Message</code>, en entrée et en sortie du serveur.
 *
 * @author Lionel Médini
 */
public class MessageDtoMapper {
    private final MessageDao messageDao;

    /**
     * Initialise le mapper avec une référence au contexte pour qu'il puisse aller y chercher les DAOs.
     * @param context Le contexte applicatif qui contient les DAOs
     */
    public MessageDtoMapper(ServletContext context) {
        this.messageDao = (MessageDao) context.getAttribute("messageDao");
    }

    /**
     * Génère une instance de <code>MessageResponseDto</code> à partir d'un objet métier <code>Message</code>.
     * @param message L'instance de <code>Message</code> dont on veut renvoyer une représentation
     * @return Un <code>MessageResponseDto</code> avec tous les champs positionnés
     */
    public MessageResponseDto toDto(Message message) {
        return new MessageResponseDto(message.getAuthor(), message.getSalon(), message.getText());
    }

    /**
     * Renvoie une instance de <code>Message</code> à partir d'un objet métier <code>MessageRequestDto</code>.
     * Si un objet d'id identique est trouvé dans le DAO, renvoie cet objet, en recopiant dedans les propriétés spécifiées par la requête.
     * Sinon, renvoie une nouvelle instance de l'objet.
     * @param messageRequestDto Une instance de <code>MessageRequestDto</code> construite à partir d'une requête
     * @return Une instance de <code>Message</code> correspondante
     */
    public Message toMessage(MessageRequestDto messageRequestDto) {
        Message message = null;
        try {
            message = messageDao.findOne(messageRequestDto.getId());
            if(messageRequestDto.getText() != null) {
                message.setText(messageRequestDto.getText());
            }
        } catch (NameNotFoundException e) {
            message = new Message(messageRequestDto.getSalon(), messageRequestDto.getAuthor(), messageRequestDto.getText());
        } catch (InvalidNameException ignored) {
            // getId() renvoie un entier, donc le format est nécessairement bon
        }
        return message;
    }
}
