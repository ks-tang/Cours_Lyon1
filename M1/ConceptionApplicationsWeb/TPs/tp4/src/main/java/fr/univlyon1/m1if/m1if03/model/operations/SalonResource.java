package fr.univlyon1.m1if.m1if03.model.operations;

import fr.univlyon1.m1if.m1if03.dao.SalonDao;
import fr.univlyon1.m1if.m1if03.dao.UserDao;
import fr.univlyon1.m1if.m1if03.model.Salon;
import jakarta.validation.constraints.NotNull;

import javax.naming.InvalidNameException;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;
import java.io.Serializable;
import java.util.Set;

/**
 * Réalise les opérations "simples" (CRUD) de gestion des ressources de type <code>Salon</code>.<br>
 * Cette classe est créée et utilisée par le contrôleur de ressources <code>SalonsController</code> qui lui injecte le DAO.
 *
 * @author Lionel Médini
 */
public class SalonResource {
    private final UserDao userDao;
    private final SalonDao salonDao;

    /**
     * Constructeur avec une injection des DAOs nécessaires aux opérations.
     * @param salonDao le DAO de salons provenant du contexte applicatif
     * @param userDao le DAO d'utilisateurs provenant du contexte applicatif
     */
    public SalonResource(SalonDao salonDao, UserDao userDao) {
        this.salonDao = salonDao;
        this.userDao = userDao;
    }

    /**
     * Crée un salon et le place dans le DAO.
     * @param name Nom du salon à créer
     * @param ownerLogin Login du propriétaire du salon à créer
     * @return L'id du salon créé
     * @throws InvalidNameException Ne doit pas arriver car les clés du DAO user sont des strings (potentiellement levée par <code>userDao.readOne()</code>)
     * @throws IllegalArgumentException Si le login est null ou vide
     * @throws NameNotFoundException Si le login du propriétaire ne correspond à aucune entrée dans le DAO d'utilisateurs
     * @throws NameAlreadyBoundException Si le nom du salon existe déjà
     */
    public Integer create(@NotNull String name, @NotNull String ownerLogin)
            throws IllegalArgumentException, InvalidNameException, NameNotFoundException, NameAlreadyBoundException {
        if (name == null || name.equals("")) {
            throw new IllegalArgumentException("Le nom du salon ne doit pas être null ou vide.");
        }
        if (ownerLogin == null) {
            throw new IllegalArgumentException("Le login du propriétaire ne doit pas être null.");
        }
        String validLogin = userDao.findOne(ownerLogin).getLogin();
        Salon salon = new Salon(name, validLogin);
        salonDao.add(salon);
        return salon.getId();
    }

    /**
     * Renvoie les IDs de tous les salons présents dans le DAO.
     * @return L'ensemble des IDs sous forme d'un <code>Set&lt;Serializable&gt;</code>
     */
    public Set<Serializable> readAll() {
        return salonDao.getAllIds();
    }

    /**
     * Renvoie un salon s'il est présent dans le DAO.
     * @param key L'id du salon demandé
     * @return L'instance de <code>Salon</code> correspondant au login
     * @throws IllegalArgumentException Si l'id du salon est null ou vide
     * @throws InvalidNameException Si l'id du salon n'est pas un Integer correctement formé
     * @throws NameNotFoundException Si l'id du salon ne correspond à aucune entrée dans le DAO
     */
    public Salon readOne(@NotNull String key) throws IllegalArgumentException, InvalidNameException, NameNotFoundException {
        if (key == null || key.equals("")) {
            throw new IllegalArgumentException("L'id du salon ne doit pas être null ou vide.");
        }
        return salonDao.findOne(Integer.valueOf(key));
    }

    /**
     * Met à jour un salon en fonction des paramètres envoyés.<br>
     * Si l'un des paramètres est nul ou vide, le champ correspondant n'est pas mis à jour.
     * @param key L'id de l'objet <code>Salon</code> à mettre à jour
     * @param name Le nom à modifier. Ou pas.
     * @throws IllegalArgumentException Si l'id du salon est null ou vide
     * @throws InvalidNameException Si l'id du salon n'est pas un Integer correctement formé
     * @throws NameNotFoundException Si l'id du salon ne correspond à aucune entrée dans le DAO
     */
    public void update(@NotNull String key, String name) throws IllegalArgumentException, InvalidNameException, NameNotFoundException {
        if (key == null || key.equals("")) {
            throw new IllegalArgumentException("L'id du salon ne doit pas être null ou vide.");
        }
        Salon salon = readOne(key);
        if (name != null && !name.equals("")) {
            salon.setName(name);
        }
    }

    /**
     * Supprime un salon dans le DAO.
     * @param key Le login du salon à supprimer
     * @throws IllegalArgumentException Si l'id du salon est null ou vide
     * @throws InvalidNameException Ne doit pas arriver car les clés du DAO user sont des strings
     * @throws NameNotFoundException Si l'id du salon ne correspond à aucune entrée dans le DAO
     */
    public void delete(@NotNull String key) throws IllegalArgumentException, InvalidNameException, NameNotFoundException {
        if (key == null || key.equals("")) {
            throw new IllegalArgumentException("L'id du salon ne doit pas être null ou vide.");
        }
        salonDao.deleteById(Integer.valueOf(key));
    }
}
