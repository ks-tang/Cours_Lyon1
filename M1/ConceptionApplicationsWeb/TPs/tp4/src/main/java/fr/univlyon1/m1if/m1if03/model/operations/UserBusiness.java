package fr.univlyon1.m1if.m1if03.model.operations;

import fr.univlyon1.m1if.m1if03.dao.UserDao;
import fr.univlyon1.m1if.m1if03.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotNull;

import javax.naming.InvalidNameException;
import javax.naming.NameNotFoundException;

/**
 * Réalise les opérations de login et de logout d'un utilisateur.<br>
 * Cette classe est créée et utilisée par le contrôleur délégué <code>UsersOperationsController</code> qui lui injecte le DAO.
 * Cette classe devra être modifiée pour le passage en authentification stateless.
 *
 * @author Lionel Médini
 */
public class UserBusiness {
    private final UserDao userDao;

    /**
     * Constructeur avec une injection du DAO nécessaire aux opérations.
     * @param userDao le DAO d'utilisateurs provenant du contexte applicatif
     */
    public UserBusiness(UserDao userDao) {
        this.userDao = userDao;
    }
    //<editor-fold desc="Méthodes réalisant les opérations métier sur les ressources">

    /**
     * Réalise l'opération de login d'un utilisateur.
     * &Agrave; condition :
     * <ul>
     *     <li>qu'un login soit présent dans la requête</li>
     *     <li>que ce login corresponde à un utilisateur déjà créé par <code>UsersController</code></li>
     *     <li>que le password corresponde à celui de l'utilisateur</li>
     * </ul>
     * Renvoie un code HTTP 204 (No Content) en cas de succès.
     * Sinon, renvoie une erreur HTTP appropriée.
     *
     * @param login    le paramètre "login" de la requête
     * @param password le paramètre "password" de la requête
     * @param request  la requête car il faudra y créer une session uniquement à certaines conditions
     * @return <code>true</code> si les login et password correspondent, <code>false</code> sinon
     * @throws IllegalArgumentException Si le login est null ou vide ou si le password est null
     * @throws InvalidNameException Ne doit pas arriver car les clés du DAO user sont des strings
     * @throws NameNotFoundException Si le login ne correspond pas à un utilisateur existant
     */
    public boolean login(@NotNull String login, String password, HttpServletRequest request)
            throws IllegalArgumentException, InvalidNameException, NameNotFoundException {
        if (login == null || login.equals("")) {
            throw new IllegalArgumentException("Le login ne doit pas être null ou vide.");
        }
        User user = userDao.findOne(login);
        if (user.verifyPassword(password)) {
            // Gestion de la session utilisateur
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Réalise l'opération de logout d'un utilisateur.<br>
     * Renvoie un code HTTP 204 (No Content).<br>
     * En première approximation, ne renvoie pas d'erreur si le client n'était pas logué.
     *
     * @param request  la requête qui contient la session à invalider
     */
    public void logout(HttpServletRequest request) {
        request.getSession().invalidate();
    }
    //</editor-fold>
}
