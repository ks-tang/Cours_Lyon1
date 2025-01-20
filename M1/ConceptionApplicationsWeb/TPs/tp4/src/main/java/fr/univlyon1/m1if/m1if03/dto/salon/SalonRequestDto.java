package fr.univlyon1.m1if.m1if03.dto.salon;

/**
 * Modélise les données que peut recevoir le serveur pour créer ou mettre à jour un salon.
 *
 * @author Lionel Médini
 */
public class SalonRequestDto {
    private Integer id;
    private String name;
    private String owner;

    //<editor-fold desc="Constructeurs">
    /**
     * Crée un <code>SalonRequestDto</code> vide.<br>
     * Appelé par Jackson dans <code>ContentNegotiationHelper</code>, cf. :
     * <code><a href="http://fasterxml.github.io/jackson-core/javadoc/2.13/com/fasterxml/jackson/core/ObjectCodec.html">ObjectCodec</a>.readValue()</code>.
     */
    public SalonRequestDto() {
        super();
    }

    /**
     * Crée un <code>SalonRequestDto</code> à l'aide des paramètres de la requête.<br>
     * Appelé "à la main" par <code>ContentNegotiationHelper.applicationSpecificProcessing()</code>.
     * @param id L'id du salon
     * @param name Le nom du salon
     * @param owner Le login de l'utilisateur créateur du salon
     */
    public SalonRequestDto(Integer id, String name, String owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
    }
    //</editor-fold>

    //<editor-fold desc="Setters">
    /**
     * Positionne l'id du salon (pour utilisation par un <code>CodecMapper</code> Jackson).
     * @param id L'id du salon présent dans la requête
     */
    public void setId(Integer id) {
        this.id = id;
    }


    /**
     * Positionne le nom du salon (pour utilisation par un <code>CodecMapper</code> Jackson).
     * @param name Le nom du salon présent dans la requête
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * Positionne le login du propriétaire du salon (pour utilisation par un <code>CodecMapper</code> Jackson).
     * @param owner Le login du propriétaire du salon présent dans la requête
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }
    //</editor-fold>

    //<editor-fold desc="Getters">
    /**
     * Renvoie l'id du salon passé dans la requête.
     * @return L'id du salon passé dans la requête
     */
    public Integer getId() {
        return id;
    }

    /**
     * Renvoie le nom du salon passé dans la requête.
     * @return Le nom du salon passé dans la requête
     */
    public String getName() {
        return name;
    } // TODO : corrigé (was protected)

    /**
     * Renvoie le login du créateur du salon passé dans la requête.
     * @return Le login du créateur du salon passé dans la requête
     */
    public String getOwner() {
        return owner;
    }
    //</editor-fold>
}
