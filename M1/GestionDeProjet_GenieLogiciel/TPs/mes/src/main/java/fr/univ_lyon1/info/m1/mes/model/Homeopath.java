package fr.univ_lyon1.info.m1.mes.model;

/*!
* \brief Classe Homeopath
*/
public class Homeopath extends HealthProfessional {
    public Homeopath(final String name) {
        super(name);
        super.getPredefPrescr().add("Natrum Muriaticum 30CH");
        super.getPredefPrescr().add("Sucre 200K");
    }
}
