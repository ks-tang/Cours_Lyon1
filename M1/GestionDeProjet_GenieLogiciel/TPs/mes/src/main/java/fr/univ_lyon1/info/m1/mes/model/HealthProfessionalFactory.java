package fr.univ_lyon1.info.m1.mes.model;

/*!
* \brief Classe Factory pour HealthProfessional
*/
public class HealthProfessionalFactory {

    public HealthProfessional createHP(final String type, final String name) {
        switch (type) {
            case "Dentist":
                return new Dentist(name);
            case "Dermato":
                return new Dermato(name);
            case "Homeopath":
                return new Homeopath(name);
            case "Ophtalmo":
                return new Ophtalmo(name);
            default:
                return new HealthProfessional(name);
        }
    }
}
