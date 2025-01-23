package fr.univlyon1.m1if.m1if03.dto.salon;

import fr.univlyon1.m1if.m1if03.dao.MessageDao;
import fr.univlyon1.m1if.m1if03.dao.SalonDao;
import fr.univlyon1.m1if.m1if03.model.Salon;

import javax.naming.InvalidNameException;
import javax.naming.NameNotFoundException;

import java.util.List;
import jakarta.servlet.ServletContext;

import static java.util.stream.Collectors.toList;

/**
 * Application du pattern DTO.<br>
 * Réalise le mapping pour les différents types de DTO de <code>Salon</code>, en entrée et en sortie du serveur.
 *
 * @author Lionel Médini
 */
public class SalonDtoMapper {
    private final SalonDao salonDao;
    private final MessageDao messageDao;

    /**
     * Initialise le mapper avec une référence au contexte pour qu'il puisse aller y chercher les DAOs.
     * @param context Le contexte applicatif qui contient les DAOs
     */
    public SalonDtoMapper(ServletContext context) {
        this.salonDao = (SalonDao) context.getAttribute("salonDao");
        this.messageDao = (MessageDao) context.getAttribute("messageDao");
    }

    /**
     * Génère une instance de <code>SalonResponseDto</code> à partir d'un objet métier <code>Salon</code>.
     *
     * @param salon L'instance de <code>Salon</code> dont on veut renvoyer une représentation
     * @return Un <code>SalonResponseDto</code> avec tous les champs positionnés
     */
    public SalonResponseDto toDto(Salon salon) {
        List<Integer> salons = messageDao.findBySalon(salon.getId())
                .stream()
                .map(messageDao::getId)
                .collect(toList());
        return new SalonResponseDto(salon.getName(), salon.getOwner(), salon.getMembers(), salons);
    }

    /**
     * Renvoie une instance de <code>Salon</code> à partir d'un objet métier <code>SalonRequestDto</code>.<br>
     * Si un objet d'id identique est trouvé dans le DAO, renvoie cet objet, en recopiant dedans les propriétés spécifiées par la requête.
     * Sinon, renvoie une nouvelle instance de l'objet.
     * @param salonRequestDto Une instance de <code>SalonRequestDto</code> construite à partir d'une requête
     * @return Une instance de <code>Salon</code> correspondante
     */
    public Salon toSalon(SalonRequestDto salonRequestDto) {
        Salon salon = null;
        try {
            salon = salonDao.findOne(salonRequestDto.getId());
            if(salonRequestDto.getName() != null) {
                salon.setName(salonRequestDto.getName());
            }
        } catch (NameNotFoundException e) {
            salon = new Salon(salonRequestDto.getName(), salonRequestDto.getOwner());
        } catch (InvalidNameException ignored) {
            // getId() renvoie un entier, donc le format est nécessairement bon
        }
        return salon;
    }
}
