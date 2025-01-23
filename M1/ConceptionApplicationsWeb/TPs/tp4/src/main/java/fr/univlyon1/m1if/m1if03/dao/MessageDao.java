package fr.univlyon1.m1if.m1if03.dao;

import fr.univlyon1.m1if.m1if03.model.Message;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation de l'interface DAO pour la classe <code>Message</code>.
 *
 * @author Lionel Médini
 */
public class MessageDao extends AbstractListDao<Message> {

    /**
     * Renvoie les messages dont l'utilisateur dont l'id est passé en paramètre est l'auteur.
     * @param idUser L'ID de l'utilisateur dont on cherche les messages
     * @return La <code>List</code> de <code>Message</code> dont l'utilisateur dont l'id est passé en paramètre est auteur
     */
    public List<Message> findByUser(@NotNull String idUser) {
        List<Message> result = new ArrayList<>();
        for(Message message: this.collection) {
            if(message.getAuthor().equals(idUser)) {
                result.add(message);
            }
        }
        return result;
    }

    /**
     * Renvoie les messages d'un salon.
     * @param idSalon L'ID du salon dont on cherche les messages
     * @return La <code>List</code> de <code>Message</code> dont l'id du salon est passé en paramètre
     */
    public List<Message> findBySalon(@NotNull Integer idSalon) {
        List<Message> result = new ArrayList<>();
        for(Message message: this.collection) {
            if(message.getSalon().equals(idSalon)) {
                result.add(message);
            }
        }
        return result;
    }
}
