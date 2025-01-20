package fr.univlyon1.m1if.m1if03.dto.message;

/**
 * Modélise les données que peut recevoir le serveur pour créer ou mettre à jour un message.
 *
 * @author Lionel Médini
 */
public class MessageRequestDto {
    private Integer id;
    private String author;
    private Integer salon;
    private String text;

    //<editor-fold desc="Constructeurs">
    /**
     * Crée un <code>MessageRequestDto</code> vide.<br>
     * Appelé par Jackson dans <code>ContentNegotiationHelper</code>, cf. :
     * <code><a href="http://fasterxml.github.io/jackson-core/javadoc/2.13/com/fasterxml/jackson/core/ObjectCodec.html">ObjectCodec</a>.readValue()</code>.
     */
    public MessageRequestDto() {
        super();
    }

    /**
     * Crée un <code>MessageRequestDto</code> à l'aide des paramètres de la requête.<br>
     * Appelé "à la main" par <code>ContentNegotiationHelper.applicationSpecificProcessing()</code>.
     * @param id L'id du message
     * @param author Le login de l'utilisateur créateur du message
     * @param salon Le salon contenant le message
     * @param text Le texte du message
     */
    public MessageRequestDto(Integer id, String author, Integer salon, String text) {
        this.id = id;
        this.author = author;
        this.salon = salon;
        this.text = text;
    }
    //</editor-fold>

    //<editor-fold desc="Setters">
    /**
     * Positionne l'id du message (pour utilisation par un <code>CodecMapper</code> Jackson).
     * @param id L'id du message présent dans la requête
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Positionne le login de l'auteur du message (pour utilisation par un <code>CodecMapper</code> Jackson).
     * @param author Le login de l'auteur du message présent dans la requête
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Positionne l'id du salon contenant le message (pour utilisation par un <code>CodecMapper</code> Jackson).
     * @param salon L'id du salon contenant le message présent dans la requête
     */
    public void setSalon(Integer salon) {
        this.salon = salon;
    }

    /**
     * Positionne le texte du message (pour utilisation par un <code>CodecMapper</code> Jackson).
     * @param text Le texte du message présent dans la requête
     */
    public void setText(String text) {
        this.text = text;
    }
    //</editor-fold>

    //<editor-fold desc="Getters">
    /**
     * Renvoie l'id du message.
     * @return L'id du message
     */
    public Integer getId() {
        return id;
    }

    /**
     * Renvoie le login du créateur du message passé dans la requête.
     * @return Le login du créateur du message passé dans la requête
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Renvoie l'id du salon contenant le message passé dans la requête.
     * @return L'id du salon contenant le message passé dans la requête
     */
    public Integer getSalon() {
        return salon;
    }

    /**
     * Renvoie le texte du message passé dans la requête.
     * @return Le texte du message passé dans la requête
     */
    public String getText() {
        return text;
    }
    //</editor-fold>
}
