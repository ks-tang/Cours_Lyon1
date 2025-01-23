package fr.univlyon1.m1if.m1if03.model.operations;

import fr.univlyon1.m1if.m1if03.dao.UserDao;
import fr.univlyon1.m1if.m1if03.exceptions.ForbiddenLoginException;
import fr.univlyon1.m1if.m1if03.model.User;
import jakarta.validation.constraints.NotNull;

import javax.naming.InvalidNameException;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;
import java.io.Serializable;
import java.util.Set;

/**
 * Réalise les opérations "simples" (CRUD) de gestion des ressources de type <code>User</code>.<br>
 * Cette classe est créée et utilisée par le contrôleur de ressources <code>UsersController</code> qui lui injecte le DAO.
 *
 * @author Lionel Médini
 */
public class UserResource implements UserResourceInterface {
    private final UserDao userDao;

    /**
     * Constructeur avec une injection du DAO nécessaire aux opérations.
     * @param userDao le DAO d'utilisateurs provenant du contexte applicatif
     */
    public UserResource(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Crée un utilisateur et le place dans le DAO.
     *
     * @param login    Login de l'utilisateur à créer
     * @param password Password de l'utilisateur à créer
     * @param name      Nom de l'utilisateur à créer
     * @throws IllegalArgumentException Si le login est null ou vide ou si le password est null
     * @throws NameAlreadyBoundException Si le login existe déjà
     * @throws ForbiddenLoginException Si le login est "login" ou "logout" (ce qui mènerait à un conflit d'URLs)
     */
    @Override
    public void create(@NotNull String login, @NotNull String password, String name)
            throws IllegalArgumentException, NameAlreadyBoundException, ForbiddenLoginException {
        if (login == null || login.equals("")) {
            throw new IllegalArgumentException("Le login ne doit pas être null ou vide.");
        }
        if (password == null) {
            throw new IllegalArgumentException("Le password ne doit pas être null.");
        }
        // Protection contre les valeurs de login qui poseraient problème au niveau des URLs
        if (login.equals("login") || login.equals("logout")) {
            throw new ForbiddenLoginException();
        }
        userDao.add(new User(login, password, name));
    }

    /**
     * Renvoie les IDs de tous les utilisateurs présents dans le DAO.
     *
     * @return L'ensemble des IDs sous forme d'un <code>Set&lt;Serializable&gt;</code>
     */
    @Override
    public Set<Serializable> readAll() {
        return userDao.getAllIds();
    }

    /**
     * Renvoie un utilisateur s'il est présent dans le DAO.
     *
     * @param login Le login de l'utilisateur demandé
     * @return L'instance de <code>User</code> correspondant au login
     * @throws IllegalArgumentException Si le login est null ou vide
     * @throws NameNotFoundException Si le login ne correspond à aucune entrée dans le DAO
     * @throws InvalidNameException Ne doit pas arriver car les clés du DAO user sont des strings
     */
    @Override
    public User readOne(@NotNull String login) throws IllegalArgumentException, NameNotFoundException, InvalidNameException {
        if (login == null || login.equals("")) {
            throw new IllegalArgumentException("Le login ne doit pas être null ou vide.");
        }
        return userDao.findOne(login);
    }

    /**
     * Met à jour un utilisateur en fonction des paramètres envoyés.<br>
     * Si l'un des paramètres est nul ou vide, le champ correspondant n'est pas mis à jour.
     *
     * @param login     Le login de l'utilisateur à mettre à jour
     * @param password Le password à modifier. Ou pas.
     * @param name      Le nom à modifier. Ou pas.
     * @throws IllegalArgumentException Si le login est null ou vide
     * @throws InvalidNameException Ne doit pas arriver car les clés du DAO user sont des strings
     * @throws NameNotFoundException Si le login ne correspond pas à un utilisateur existant
     */
    @Override
    public void update(@NotNull String login, String password, String name) throws IllegalArgumentException, InvalidNameException, NameNotFoundException {
        User user = readOne(login);
        if (password != null && !password.equals("")) {
            user.setPassword(password);
        }
        if (name != null && !name.equals("")) {
            user.setName(name);
        }
    }

    /**
     * Supprime un utilisateur dans le DAO.
     *
     * @param login Le login de l'utilisateur à supprimer
     * @throws IllegalArgumentException Si le login est null ou vide
     * @throws NameNotFoundException Si le login ne correspond à aucune entrée dans le DAO
     * @throws InvalidNameException Ne doit pas arriver car les clés du DAO user sont des strings
     */
    @Override
    public void delete(@NotNull String login) throws IllegalArgumentException, NameNotFoundException, InvalidNameException {
        if (login == null || login.equals("")) {
            throw new IllegalArgumentException("Le login ne doit pas être null ou vide.");
        }
        userDao.deleteById(login);
    }
}
