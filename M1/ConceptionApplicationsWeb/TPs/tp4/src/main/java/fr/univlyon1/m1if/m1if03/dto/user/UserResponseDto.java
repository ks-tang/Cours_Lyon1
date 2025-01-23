package fr.univlyon1.m1if.m1if03.dto.user;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

/**
 * DTO qui contient la totalité des données d'un utilisateur renvoyé par une vue du serveur.
 * @author Lionel Médini
 */
@JacksonXmlRootElement(localName = "user")
public class UserResponseDto {
    private final String login;
    private final String name;
    private final List<Integer> ownedSalons;
    private final List<Integer> memberOfSalons;
    private final List<Integer> createdMessages;

    /**
     * Crée un <code>UserResponseDto</code> à templater dans la réponse.
     * @param login Le login de l'utilisateur
     * @param name Le nom du salon
     * @param ownedSalons La liste des salons dont l'utilisateur est propriétaire
     * @param memberOfSalons La liste des salons dont l'utilisateur est membre
     * @param createdMessages La liste des messages dont l'utilisateur est auteur
     */
    public UserResponseDto(String login, String name, List<Integer> ownedSalons, List<Integer> memberOfSalons, List<Integer> createdMessages) {
        this.login = login;
        this.name = name;
        this.ownedSalons = ownedSalons;
        this.memberOfSalons = memberOfSalons;
        this.createdMessages = createdMessages;
    }

    /**
     * Renvoie le login de l'utilisateur.
     * @return Le login de l'utilisateur
     */
    public String getLogin() {
        return login;
    }

    /**
     * Renvoie le nom de l'utilisateur.
     * @return Le nom de l'utilisateur
     */
    public String getName() {
        return name;
    }

    /**
     * Renvoie la liste des salons dont l'utilisateur est propriétaire.
     * @return La liste des salons dont l'utilisateur est propriétaire
     */
    public List<Integer> getOwnedSalons() {
        return ownedSalons;
    }

    /**
     * Renvoie la liste des salons dont l'utilisateur est membre.
     * @return La liste des salons dont l'utilisateur est membre
     */
    public List<Integer> getMemberOfSalons() {
        return memberOfSalons;
    }

    /**
     * Renvoie la liste des messages dont l'utilisateur est auteur.
     * @return La liste des messages dont l'utilisateur est auteur
     */
    public List<Integer> getCreatedMessages() {
        return createdMessages;
    }
}
