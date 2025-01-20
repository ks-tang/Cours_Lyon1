package fr.univlyon1.m1if.m1if03.model.operations;

import fr.univlyon1.m1if.m1if03.exceptions.ForbiddenLoginException;
import fr.univlyon1.m1if.m1if03.model.User;
import jakarta.validation.constraints.NotNull;

import javax.naming.InvalidNameException;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;
import java.io.Serializable;
import java.util.Set;

public interface UserResourceInterface {
    /**
     * Crée un utilisateur et le place dans le DAO.
     *
     * @param login    Login de l'utilisateur à créer
     * @param password Password de l'utilisateur à créer
     * @param name     Nom de l'utilisateur à créer
     * @throws IllegalArgumentException  Si le login est null ou vide ou si le password est null
     * @throws NameAlreadyBoundException Si le login existe déjà
     * @throws ForbiddenLoginException   Si le login est "login" ou "logout" (ce qui mènerait à un conflit d'URLs)
     */
    void create(@NotNull String login, @NotNull String password, String name)
            throws IllegalArgumentException, NameAlreadyBoundException, ForbiddenLoginException;

    /**
     * Renvoie les IDs de tous les utilisateurs présents dans le DAO.
     *
     * @return L'ensemble des IDs sous forme d'un <code>Set&lt;Serializable&gt;</code>
     */
    Set<Serializable> readAll();

    /**
     * Renvoie un utilisateur s'il est présent dans le DAO.
     *
     * @param login Le login de l'utilisateur demandé
     * @return L'instance de <code>User</code> correspondant au login
     * @throws IllegalArgumentException Si le login est null ou vide
     * @throws NameNotFoundException    Si le login ne correspond à aucune entrée dans le DAO
     * @throws InvalidNameException     Ne doit pas arriver car les clés du DAO user sont des strings
     */
    User readOne(@NotNull String login) throws IllegalArgumentException, NameNotFoundException, InvalidNameException;

    /**
     * Met à jour un utilisateur en fonction des paramètres envoyés.<br>
     * Si l'un des paramètres est nul ou vide, le champ correspondant n'est pas mis à jour.
     *
     * @param login    Le login de l'utilisateur à mettre à jour
     * @param password Le password à modifier. Ou pas.
     * @param name     Le nom à modifier. Ou pas.
     * @throws IllegalArgumentException Si le login est null ou vide
     * @throws InvalidNameException     Ne doit pas arriver car les clés du DAO user sont des strings
     * @throws NameNotFoundException    Si le login ne correspond pas à un utilisateur existant
     */
    void update(@NotNull String login, String password, String name) throws IllegalArgumentException, InvalidNameException, NameNotFoundException;

    /**
     * Supprime un utilisateur dans le DAO.
     *
     * @param login Le login de l'utilisateur à supprimer
     * @throws IllegalArgumentException Si le login est null ou vide
     * @throws NameNotFoundException    Si le login ne correspond à aucune entrée dans le DAO
     * @throws InvalidNameException     Ne doit pas arriver car les clés du DAO user sont des strings
     */
    void delete(@NotNull String login) throws IllegalArgumentException, NameNotFoundException, InvalidNameException;
}
