package fr.univlyon1.m1if.m1if03.dto.salon;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;
import java.util.Set;

/**
 * DTO qui contient la totalité des données d'un salon renvoyé par une vue du serveur.
 *
 * @author Lionel Médini
 */
@JacksonXmlRootElement(localName = "salon")
public class SalonResponseDto {
    private final String name;
    private final String owner;
    private final Set<String> members;
    private final List<Integer> messages;

    /**
     * Crée un <code>SalonResponseDto</code> à templater dans la réponse.
     * @param name Le nom du salon
     * @param owner Le login de l'utilisateur créateur du salon
     * @param members L'ensemble des membres du salon
     * @param messages La liste des messages du salon
     */
    public SalonResponseDto(String name, String owner, Set<String> members, List<Integer> messages) {
        this.name = name;
        this.owner = owner;
        this.members = members;
        this.messages = messages;
    }

    /**
     * Renvoie le nom du salon.
     * @return Le nom du salon
     */
    public String getName() {
        return name;
    }

    /**
     * Renvoie le login du créateur du salon.
     * @return Le login du créateur du salon
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Renvoie l'ensemble des membres du salon.
     * @return l'ensemble des membres du salon
     */
    public Set<String> getMembers() {
        return members;
    }

    /**
     * Renvoie la liste des messages du salon.
     * @return La liste des messages du salon
     */
    public List<Integer> getMessages() {
        return messages;
    }
}
