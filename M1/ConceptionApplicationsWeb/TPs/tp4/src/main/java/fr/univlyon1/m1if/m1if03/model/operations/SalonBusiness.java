package fr.univlyon1.m1if.m1if03.model.operations;

import fr.univlyon1.m1if.m1if03.dao.MessageDao;
import fr.univlyon1.m1if.m1if03.dao.UserDao;
import fr.univlyon1.m1if.m1if03.model.Message;
import fr.univlyon1.m1if.m1if03.model.Salon;

import javax.naming.InvalidNameException;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;
import java.util.List;

/**
 * Réalise les opérations de d'ajout et de suppression d'un membre, et de calcul du contenu complet d'un salon.<br>
 * Cette classe est créée et utilisée par le contrôleur délégué <code>SalonsBusinessController</code> qui lui injecte les DAOs.
 *
 * @author Lionel Médini
 */
public class SalonBusiness {
    private final UserDao userDao;
    private final MessageDao messageDao;

    /**
     * Constructeur avec une injection des DAOs nécessaires aux opérations.
     * @param userDao le DAO d'utilisateurs provenant du contexte applicatif
     * @param messageDao le DAO de messages provenant du contexte applicatif
     */
    public SalonBusiness(UserDao userDao, MessageDao messageDao) {
        this.userDao = userDao;
        this.messageDao = messageDao;
    }

    /**
     * Ajoute un membre à un salon.<br>
     * Le salon est supposé existant, on ne fait donc pas de vérification dessus.
     * Il faut cependant vérifier que le login en paramètre correspond à un utilisateur valide et qu'il n'est pas déjà membre du salon.
     * @param salon Le salon auquel le membre doit être ajouté
     * @param user Le login du membre à ajouter au salon
     * @throws IllegalArgumentException Si le login est null ou vide
     * @throws InvalidNameException Ne doit pas arriver car les clés du DAO user sont des strings
     * @throws NameNotFoundException Si le login ne correspond à aucune entrée dans le DAO
     * @throws NameAlreadyBoundException Si l'utilisateur est déjà membre du salon
     */
    public void addMember(Salon salon, String user) throws IllegalArgumentException, InvalidNameException, NameNotFoundException, NameAlreadyBoundException {
        if (user == null || user.equals("")) {
            throw new IllegalArgumentException("Le login ne doit pas être null ou vide.");
        }
        String validUser = userDao.findOne(user).getLogin();
        salon.addMember(validUser);
    }

    /**
     * Supprime un membre d'un salon.<br>
     * Le salon est supposé existant, on ne fait donc pas de vérification dessus.
     * Il faut cependant vérifier que le login en paramètre correspond à un utilisateur valide.
     * @param salon Le salon dont le membre doit être supprimé
     * @param user Le login du membre à supprimer du salon
     * @return <code>true</code> si le membre a été supprimé, <code>false</code> sinon (il n'était pas membre du salon)
     * @throws IllegalArgumentException Si le login du membre est null ou vide
     */
    public boolean removeMember(Salon salon, String user) throws IllegalArgumentException {
        if (user == null || user.equals("")) {
            throw new IllegalArgumentException("Le login ne doit pas être null ou vide.");
        }
        return salon.removeMember(user);
    }

    /**
     * Récupère la liste complète (auteurs et textes) des messages d'un salon (au lieu d'une liste contenant simplement des URLs).<br>
     * Le salon est supposé existant, on ne fait donc pas de vérification dessus.
     * @param salon Le salon dont on veut le contenu
     * @return Une <code>List&gt;Message&lt;</code> contenant tous les messages (non supprimés) d'un salon
     */
    public List<Message> getSalonMessages(Salon salon) {
        return messageDao.findBySalon(salon.getId());
    }
}
