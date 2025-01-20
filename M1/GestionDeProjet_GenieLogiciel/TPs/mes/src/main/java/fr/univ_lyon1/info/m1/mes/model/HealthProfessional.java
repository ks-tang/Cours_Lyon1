package fr.univ_lyon1.info.m1.mes.model;

import java.util.List;
import java.util.ArrayList;

/*!
* \brief Classe HealthProfessionnal
*/
public class HealthProfessional {
    private final String name;
    private final List<String> predefPrescr = new ArrayList<>();

    /*!
    * \brief Constructeur de HealthProfessionnal
    * \param name son nom
    * \param mes MES
    */
    public HealthProfessional(final String name) {
        this.name = name;
        this.predefPrescr.add("Paracetamol");
    }

    /*!
    * \brief Fonction qui récupère le nom d'un professionnel
    */
    public String getName() {
        return name;
    }

    /*!
    * \brief Fonction qui récupère la liste des prescriptions prédéfinies du professionnel
    */
    public List<String> getPredefPrescr() {
        return predefPrescr;
    }

}
