package fr.univ_lyon1.info.m1.mes.model;

/*!
* \brief Classe Prescription
*/
public class Prescription {
    private final HealthProfessional hp;
    private final String content;

    /*!
    * \brief Constructeur de la classe Prescription
    */
    public Prescription(final HealthProfessional hp, final String content) {
        this.hp = hp;
        this.content = content;
    }

    /*!
    * \brief Fonction qui recupere le contenu de la prescription
    */
    public String getContent() {
        return content;
    }

    /*!
    * \brief Fonction qui recupere le HealthProfessional qui a prescrit
    */
    public HealthProfessional getHealthProfessional() {
        return hp;
    }
}
