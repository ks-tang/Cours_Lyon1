package fr.univ_lyon1.info.m1.mes.model;

/*!
* \brief Classe Dentist
*/
public class Dentist extends HealthProfessional {
    public Dentist(final String name) {
        super(name);
        super.getPredefPrescr().add("Ne pas manger pendant une heure");
        super.getPredefPrescr().add("Utiliser du bain de bouche");
    }
}
