package fr.univlyon1.m1if.m1if03.dao;

import fr.univlyon1.m1if.m1if03.model.Salon;
import jakarta.validation.constraints.NotNull;

import javax.naming.NameNotFoundException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Implémentation de l'interface DAO pour la classe <code>Salon</code>.
 *
 * @author Lionel Médini
 */
public class SalonDao extends AbstractMapDao<Salon> {
    //<editor-fold desc="Implémentation de la méthode de la classe générique">
    @Override
    protected Serializable getKeyForElement(@NotNull Salon element) {
        return element.getId();
    }
    //</editor-fold>

    //<editor-fold desc="Méthode métier spécifique au DAO d'utilisateurs">
    /**
     * Renvoie un salon à partir de son nom.<br>
     * renvoie le premier salon trouvé avec le nom demandé<br>

     * @param name le nom à rechercher
     * @return un <code>Salon</code> dont le nom est celui passé en paramètre
     * @throws NameNotFoundException Si le nom du salon à rechercher n'a pas été trouvé
     */
    public Salon findByName(@NotNull String name) throws NameNotFoundException {
        for(Salon salon: this.collection.values()) {
            if(salon.getName().equals(name)) {
                return salon;
            }
        }
        throw new NameNotFoundException(name);
    }

    /**
     * Renvoie les salons dont le propriétaire est celui dont l'id est passé en paramètre.
     * @param idOwner L'ID de l'utilisateur dont on cherche les salons qu'il a créés
     * @return Un <code>Set</code> de <code>Salon</code> créés par l'utilisateur dont l'id est passé en paramètre
     */
    public Set<Salon> findByOwner(@NotNull String idOwner) {
        Set<Salon> result = new HashSet<>();
        for(Salon salon: this.collection.values()) {
            if(salon.getOwner().equals(idOwner)) {
                result.add(salon);
            }
        }
        return result;
    }

    /**
     * Renvoie les salons dont l'utilisateur dont l'id est passé en paramètre est membre.
     * @param idMember L'ID de l'utilisateur dont on cherche les salons dont il est membre
     * @return Un <code>Set</code> de <code>Salon</code> dont l'utilisateur dont l'id est passé en paramètre est membre
     */
    public Set<Salon> findByMember(@NotNull String idMember) {
        Set<Salon> result = new HashSet<>();
        for(Salon salon: this.collection.values()) {
            if(salon.hasMember(idMember)) {
                result.add(salon);
            }
        }
        return result;
    }

    /**
     * Renvoie les salons dont l'utilisateur dont l'id est passé en paramètre n'est pas membre.
     * @param idNonMember L'ID de l'utilisateur dont on cherche les salons dont il n'est pas membre
     * @return Un <code>Set</code> de <code>Salon</code> dont l'utilisateur dont l'id est passé en paramètre n'est pas membre
     */
    public Set<Salon> findByNonMember(@NotNull String idNonMember) {
        Set<Salon> result = new HashSet<>();
        for(Salon salon: this.collection.values()) {
            if(!salon.hasMember(idNonMember)) {
                result.add(salon);
            }
        }
        return result;
    }
    //</editor-fold>
}
