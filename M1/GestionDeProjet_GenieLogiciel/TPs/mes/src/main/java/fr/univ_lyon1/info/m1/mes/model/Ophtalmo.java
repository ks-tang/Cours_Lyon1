package fr.univ_lyon1.info.m1.mes.model;

/*!
* \brief Classe Ophtalmo
*/
public class Ophtalmo extends HealthProfessional {
    public Ophtalmo(final String name) {
        super(name);
        super.getPredefPrescr().add("Gouttes pour les yeux");
        super.getPredefPrescr().add("Eviter de regarder les écrans");
    }
}
