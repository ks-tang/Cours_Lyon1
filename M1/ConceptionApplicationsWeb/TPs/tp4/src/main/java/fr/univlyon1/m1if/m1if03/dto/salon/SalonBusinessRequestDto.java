package fr.univlyon1.m1if.m1if03.dto.salon;

/**
 * Modélise les données que peut recevoir le serveur pour créer ou mettre à jour un salon.
 *
 * @author Lionel Médini
 */
public class SalonBusinessRequestDto {
    private String user;

    //<editor-fold desc="Constructeurs">
    /**
     * Crée un <code>SalonRequestDto</code> vide.<br>
     * Appelé par Jackson dans <code>ContentNegotiationHelper</code>, cf. :
     * <code><a href="http://fasterxml.github.io/jackson-core/javadoc/2.13/com/fasterxml/jackson/core/ObjectCodec.html">ObjectCodec</a>.readValue()</code>.
     */
    public SalonBusinessRequestDto() {
        super();
    }

    /**
     * Crée un <code>SalonRequestDto</code> à l'aide des paramètres de la requête.<br>
     * Appelé "à la main" par <code>ContentNegotiationHelper.applicationSpecificProcessing()</code>.
     * @param user Le login de l'utilisateur créateur du salon
     */
    public SalonBusinessRequestDto(String user) {
        this.user = user;
    }
    //</editor-fold>

    //<editor-fold desc="Setters">
    /**
     * Positionne le login du propriétaire du salon (pour utilisation par un <code>CodecMapper</code> Jackson).
     * @param user Le login du propriétaire du salon présent dans la requête
     */
    public void setUser(String user) {
        this.user = user;
    }
    //</editor-fold>

    //<editor-fold desc="Getters">
    /**
     * Renvoie le login du créateur du salon passé dans la requête.
     * @return Le login du créateur du salon passé dans la requête
     */
    public String getUser() {
        return user;
    }
    //</editor-fold>
}
