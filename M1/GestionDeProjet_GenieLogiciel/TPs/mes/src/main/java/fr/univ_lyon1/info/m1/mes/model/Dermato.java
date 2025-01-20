package fr.univ_lyon1.info.m1.mes.model;

/*!
* \brief Classe Dermato
*/
public class Dermato extends HealthProfessional {
    public Dermato(final String name) {
        super(name);
        super.getPredefPrescr().add("Appliquer une crème apaisante matin et soir");
        super.getPredefPrescr().add("Prendre un bain");
    }
}
