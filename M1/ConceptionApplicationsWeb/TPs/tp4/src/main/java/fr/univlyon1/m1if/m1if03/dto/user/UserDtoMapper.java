package fr.univlyon1.m1if.m1if03.dto.user;

import fr.univlyon1.m1if.m1if03.dao.MessageDao;
import fr.univlyon1.m1if.m1if03.dao.SalonDao;
import fr.univlyon1.m1if.m1if03.dao.UserDao;
import fr.univlyon1.m1if.m1if03.model.Salon;
import fr.univlyon1.m1if.m1if03.model.User;

import javax.naming.InvalidNameException;
import javax.naming.NameNotFoundException;

import java.util.List;
import java.util.ArrayList;

import jakarta.servlet.ServletContext;

import static java.util.stream.Collectors.toList;

/**
 * Application du pattern DTO.<br>
 * Réalise le mapping pour les différents types de DTO de <code>User</code>, en entrée et en sortie du serveur.
 *
 * @author Lionel Médini
 */
public class UserDtoMapper {
    private final UserDao userDao;
    private final SalonDao salonDao;
    private final MessageDao messageDao;

    /**
     * Initialise le mapper avec une référence au contexte pour qu'il puisse aller y chercher les DAOs.
     * @param context Le contexte applicatif qui contient les DAOs
     */
    public UserDtoMapper(ServletContext context) {
        this.userDao = (UserDao) context.getAttribute("userDao");
        this.salonDao = (SalonDao) context.getAttribute("salonDao");
        this.messageDao = (MessageDao) context.getAttribute("messageDao");
    }

    /**
     * Génère une instance de <code>UserResponseDto</code> à partir d'un objet métier <code>User</code>.
     * @param user L'instance de <code>User</code> dont on veut renvoyer une représentation
     * @return Un <code>UserResponseDto</code> avec tous les champs positionnés
     */
    public UserResponseDto toDto(User user) {
        List<Integer> ownedSalons =
                salonDao.findByOwner(user.getLogin())
                        .stream()
                        .map(Salon::getId)
                        .collect(toList());

        List<Integer> memberOfSalons =
                salonDao.findByMember(user.getLogin())
                        .stream()
                        .map(Salon::getId)
                        .collect(toList());

        List<Integer> createdMessages;
        try {
            createdMessages = messageDao.findByUser(user.getLogin()).stream().map(messageDao::getId).collect(toList());
        } catch (Exception e) {
            createdMessages = new ArrayList<>();
        }

        return new UserResponseDto(user.getLogin(), user.getName(), ownedSalons, memberOfSalons, createdMessages);
    }

    /**
     * Renvoie une instance de <code>User</code> à partir d'un objet métier <code>UserRequestDto</code>.
     * Si un objet d'id identique est trouvé dans le DAO, renvoie cet objet, en recopiant dedans les propriétés spécifiées par la requête.
     * Sinon, renvoie une nouvelle instance de l'objet.
     * @param userRequestDto Une instance de <code>UserRequestDto</code> construite à partir d'une requête
     * @return Une instance de <code>User</code> correspondante
     */
    public User toUser(UserRequestDto userRequestDto) {
        User user = null;
        try {
            user = userDao.findOne(userRequestDto.getLogin());
            if(userRequestDto.getPassword() != null) {
                user.setPassword(userRequestDto.getPassword());
            }
            if(userRequestDto.getName() != null) {
                user.setName(userRequestDto.getName());
            }
        } catch (NameNotFoundException e) {
            user = new User(userRequestDto.getLogin(), userRequestDto.getPassword(), userRequestDto.getName());
        } catch (InvalidNameException ignored) {
            // getLogin() renvoie un String, donc le format est nécessairement bon
        }
        return user;
    }
}
