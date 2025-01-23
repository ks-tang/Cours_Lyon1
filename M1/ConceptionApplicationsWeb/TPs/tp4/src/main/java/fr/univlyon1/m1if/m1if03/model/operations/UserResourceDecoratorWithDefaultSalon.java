package fr.univlyon1.m1if.m1if03.model.operations;

import fr.univlyon1.m1if.m1if03.dao.SalonDao;
import fr.univlyon1.m1if.m1if03.dao.UserDao;
import fr.univlyon1.m1if.m1if03.exceptions.ForbiddenLoginException;
import fr.univlyon1.m1if.m1if03.model.Salon;
import fr.univlyon1.m1if.m1if03.model.User;
import jakarta.validation.constraints.NotNull;

import javax.naming.InvalidNameException;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;
import java.io.Serializable;
import java.util.Set;

/**
 * <strong>Décorateur de la classe <code>UserResource</code></strong>.
 * Réalise les opérations "simples" (CRUD) de gestion des ressources de type <code>User</code>.<br>
 * <strong>En plus de <code>UserResource</code> : cette classe crée un salon par défaut (possédé par un user "system"),
 * dans lequel tous les utilisateur sont ajoutés à leur création et enlevés à leur suppression.</strong><br>
 * Cette classe est créée et utilisée par le contrôleur de ressources <code>UsersController</code> qui lui injecte les DAO.
 *
 * @author Lionel Médini
 */
public class UserResourceDecoratorWithDefaultSalon implements UserResourceInterface {
    public static final String DEFAULT_SALON_NAME = "Salon par défaut";
    private final UserResourceInterface userResource;
    private final Salon defaultSalon;

    /**
     * Constructeur avec une injection du DAO nécessaire aux opérations.<br>
     * <strong>Crée un user "system", un salon par défaut et les place dans les DAO.</strong>
     * @param userDao le DAO d'utilisateurs provenant du contexte applicatif
     * @param salonDao le DAO de salons pour pouvoir y ajouter le salon par défaut
     */
    public UserResourceDecoratorWithDefaultSalon(UserDao userDao, SalonDao salonDao) {
        this.userResource = new UserResource(userDao);

        this.defaultSalon = new Salon(DEFAULT_SALON_NAME, "system");
        try {
            userDao.add(new User("system", "passwordSystem", "System"));
            salonDao.add(defaultSalon);
        } catch (NameAlreadyBoundException ignored) {}
    }

    /**
     * Crée un utilisateur et le place dans le DAO.
     * @param login    Login de l'utilisateur à créer
     * @param password Password de l'utilisateur à créer
     * @param name      Nom de l'utilisateur à créer
     * @throws IllegalArgumentException Si le login est null ou vide ou si le password est null
     * @throws NameAlreadyBoundException Si le login existe déjà
     * @throws ForbiddenLoginException Si le login est "login" ou "logout" (ce qui mènerait à un conflit d'URLs)
     */
    public void create(@NotNull String login, @NotNull String password, String name)
            throws IllegalArgumentException, NameAlreadyBoundException, ForbiddenLoginException {
        userResource.create(login, password, name);
        defaultSalon.addMember(login);
    }

    /**
     * Renvoie les IDs de tous les utilisateurs présents dans le DAO.
     * @return L'ensemble des IDs sous forme d'un <code>Set&lt;Serializable&gt;</code>
     */
    public Set<Serializable> readAll() {
        return userResource.readAll();
    }

    /**
     * Renvoie un utilisateur s'il est présent dans le DAO.
     * @param login Le login de l'utilisateur demandé
     * @return L'instance de <code>User</code> correspondant au login
     * @throws IllegalArgumentException Si le login est null ou vide
     * @throws NameNotFoundException Si le login ne correspond à aucune entrée dans le DAO
     * @throws InvalidNameException Ne doit pas arriver car les clés du DAO user sont des strings
     */
    public User readOne(@NotNull String login) throws IllegalArgumentException, NameNotFoundException, InvalidNameException {
        return userResource.readOne(login);
    }

    /**
     * Met à jour un utilisateur en fonction des paramètres envoyés.<br>
     * Si l'un des paramètres est nul ou vide, le champ correspondant n'est pas mis à jour.
     * @param login     Le login de l'utilisateur à mettre à jour
     * @param password Le password à modifier. Ou pas.
     * @param name      Le nom à modifier. Ou pas.
     * @throws IllegalArgumentException Si le login est null ou vide
     * @throws InvalidNameException Ne doit pas arriver car les clés du DAO user sont des strings
     * @throws NameNotFoundException Si le login ne correspond pas à un utilisateur existant
     */
    public void update(@NotNull String login, String password, String name) throws IllegalArgumentException, InvalidNameException, NameNotFoundException {
        userResource.update(login, password, name);
    }

    /**
     * Supprime un utilisateur dans le DAO <strong>et dans le salon par défaut</strong>.
     * @param login Le login de l'utilisateur à supprimer
     * @throws IllegalArgumentException Si le login est null ou vide
     * @throws NameNotFoundException Si le login ne correspond à aucune entrée dans le DAO
     * @throws InvalidNameException Ne doit pas arriver car les clés du DAO user sont des strings
     */
    public void delete(@NotNull String login) throws IllegalArgumentException, NameNotFoundException, InvalidNameException {
        userResource.delete(login);
        defaultSalon.removeMember(login);
    }
}
