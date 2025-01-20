package fr.univlyon1.m1if.m1if03.model;

import jakarta.validation.constraints.NotNull;

import javax.naming.NameAlreadyBoundException;
import java.util.HashSet;
import java.util.Set;

/**
 * Représente un salon privé.<br>
 * Un salon est créé par un utilisateur qui a donc un rôle de propriétaire.
 * Les utilisateurs peuvent s'inscrire et se désisncrire au salon y auront un rôle de membre.
 * On pourra attribuer aux autres utilisateurs un rôle de non-membre.
 * Les autres opérations ne seront accessibles qu'au propriétaire du salon.
 * L'ajout d'un message et la récupération des messages sera accessible à tous les membres.
 * La suppression d'un message sera accessible uniquement à l'auteur de ce message et au propriétaire du salon.
 *
 * @author Lionel Médini
 */
public class Salon {
    private final Integer id;
    private String name;
    private final String owner;
    private final Set<String> members = new HashSet<>();

    //<editor-fold desc="Gestion des caractéristiques du salon">
    /**
     * Crée un salon avec le nom spécifié.
     * @param name Nom du salon
     * @param owner Propriétaire du salon
     */
    public Salon(@NotNull String name, @NotNull String owner) {
        this.name = name;
        this.owner = owner;
        this.id = (owner + "/" + name).hashCode();
    }

    /**
     * Renvoie l'id du salon.<br>
     * L'id est généré à la création du salon et est invariable pour une instance donnée.
     * @return l'id du salon
     */
    public Integer getId() {
        return id;
    }

    /**
     * Renvoie le nom du salon.
     * @return le nom du salon
     */
    public String getName() {
        return name;
    }

    /**
     * Modifie le nom du salon.
     * @param name Le nouveau nom du salon
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Renvoie le login du propriétaire du salon.
     * @return Le login du propriétaire du salon
     */
    public String getOwner() {
        return owner;
    }
    //</editor-fold>

    //<editor-fold desc="Gestion des membres">
    /**
     * Ajoute un membre si celui-ci n'est pas déjà membre du salon.
     * @param memberId ID de l'user à rajouter comme membre
     * @throws NameAlreadyBoundException Si la clé utilisée pour ajouter l'élément est déjà existante
     */
    public void addMember(String memberId) throws NameAlreadyBoundException {
        if(this.members.contains(memberId)) {
            throw new NameAlreadyBoundException();
        }
        this.members.add(memberId);
    }

    /**
     * Supprime un membre si celui-ci est membre du salon.
     * @param memberId ID de l'user à supprimer des membres
     * @return un booléen qui indique si l'utilisateur a été supprimé
     */
    public boolean removeMember(String memberId) {
        return this.members.remove(memberId);
    }

    /**
     * Indique si un utilisateur est membre du salon.
     * @param userId ID de l'user à tester
     * @return un booléen répondant à la question
     */
    public boolean hasMember(String userId) {
        return this.members.contains(userId);
    }

    /**
     * Renvoie la liste complète des membres.
     * @return une <code>List&lt;User&gt;</code>
     */
    public Set<String> getMembers() {
        // On renvoie un nouveau Set pour ne pas permettre la modification de la variable d'instance
        return new HashSet<>(this.members);
    }
    //</editor-fold>
}
