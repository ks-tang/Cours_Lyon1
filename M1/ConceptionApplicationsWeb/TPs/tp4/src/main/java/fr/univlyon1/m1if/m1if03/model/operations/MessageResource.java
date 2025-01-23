package fr.univlyon1.m1if.m1if03.model.operations;

import fr.univlyon1.m1if.m1if03.dao.MessageDao;
import fr.univlyon1.m1if.m1if03.model.Message;
import jakarta.validation.constraints.NotNull;

import javax.naming.InvalidNameException;
import javax.naming.NameNotFoundException;
import java.util.List;

/**
 * Réalise les opérations "simples" (CRUD) de gestion des ressources de type <code>Message</code>.<br>
 * Cette classe est créée et utilisée par le contrôleur de ressources <code>MessagesController</code> qui lui injecte le DAO.
 *
 * @author Lionel Médini
 */
public class MessageResource {
    private final MessageDao messageDao;

    /**
     * Constructeur avec une injection du DAO nécessaire aux opérations.
     * @param messageDao le DAO de messages provenant du contexte applicatif
     */
    public MessageResource(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    /**
     * Crée un message et le place dans le DAO.<br>
     * Remarque : on ne vérifie pas que le salon et l'auteur sont valides, parce que c'est déjà fait dans le contrôle d'autorisation.
     * @param salon  Le salon contenant le message à créer
     * @param author Le login de l'auteur du message à créer
     * @param text  Le texte du message à créer
     * @return L'id du message créé
     */
    public Integer createMessage(@NotNull String salon, @NotNull String author, String text) {
        return messageDao.add(new Message(Integer.valueOf(salon), author, text));
    }

    /**
     * Renvoie les IDs de tous les messages présents dans le DAO.
     * @return L'ensemble des IDs sous forme d'un <code>Set&lt;Serializable&gt;</code>
     */
    public List<Integer> readAll() {
        return messageDao.getAllIds();
    }

    /**
     * Renvoie un message s'il est présent dans le DAO.
     * @param key L'id du message demandé
     * @return L'instance de <code>Message</code> correspondant au login
     * @throws IllegalArgumentException Si l'id du message est null ou vide
     * @throws InvalidNameException Si l'id du message n'est pas un Integer correctement formé
     * @throws NameNotFoundException Si l'id ne correspond à aucune entrée dans le DAO
     */
    public Message readOne(@NotNull String key) throws IllegalArgumentException, NameNotFoundException, InvalidNameException {
        if (key == null || key.equals("")) {
            throw new IllegalArgumentException("L'id du message ne doit pas être null ou vide.");
        }
        return messageDao.findOne(Integer.valueOf(key));
    }

    /**
     * Met à jour un message en fonction des paramètres envoyés.<br>
     * Si l'un des paramètres est nul ou vide, le champ correspondant n'est pas mis à jour.
     *
     * @param key L'id de l'objet <code>Message</code> à mettre à jour
     * @param text   Le texte à modifier. Ou pas.
     * @throws IllegalArgumentException Si l'id du message est null ou vide
     * @throws InvalidNameException Si l'id du message n'est pas un Integer correctement formé
     * @throws NameNotFoundException Si l'id ne correspond à aucune entrée dans le DAO
     */
    public void updateMessage(@NotNull String key, String text) throws IllegalArgumentException, InvalidNameException, NameNotFoundException {
        if (key == null || key.equals("")) {
            throw new IllegalArgumentException("L'id du message ne doit pas être null ou vide.");
        }
        Message message = readOne(key);
        if (text != null) {
            message.setText(text);
        }
    }

    /**
     * Supprime un message dans le DAO.
     *
     * @param key L'id du message à supprimer
     * @throws IllegalArgumentException Si l'id du message est null ou vide
     * @throws InvalidNameException Si l'id du message n'est pas un Integer correctement formé
     * @throws NameNotFoundException Si l'id ne correspond à aucune entrée dans le DAO
     */
    public void deleteMessage(@NotNull String key) throws IllegalArgumentException, NameNotFoundException, InvalidNameException {
        if (key == null || key.equals("")) {
            throw new IllegalArgumentException("L'id du message ne doit pas être null ou vide.");
        }
        messageDao.deleteById(Integer.valueOf(key));
    }
}
