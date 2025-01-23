package fr.univ_lyon1.info.m1.mes.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import fr.univ_lyon1.info.m1.mes.utils.Observable;

/*!
* \brief Classe Patient
*/
public final class Patient extends Observable {
    private List<Prescription> prescriptions = new ArrayList<>();
    private String name;
    private String ssID;

    /*!
    * \brief Constructeur du Patient
    * \param name son nom
    * \param ssID son numéro de Securite Social
    */
    private Patient(final PatientBuilder builder) {
        this.name = builder.name;
        this.ssID = builder.ssID;
    }

    /*!
    * \brief Fonction qui récupère le nom du Patient
    */
    public String getName() {
        return name;
    }

    /*!
    * \brief Fonction qui récupère le numero de Securite Social du Patient
    */
    public String getSSID() {
        return ssID;
    }

    /*!
    * \brief Fonction qui récupère toutes les prescriptions du Patient
    */
    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    /*!
    * \brief Fonction qui récupère les prescriptions du Patient prescrites
    * par un certain HealthProfessionnal
    * \param hp le Patient
    */
    public List<Prescription> getPrescriptions(final HealthProfessional hp) {
        return prescriptions.stream()
                .filter(p -> p.getHealthProfessional() == hp)
                .collect(Collectors.toList());
    }

    /*!
    * \brief Fonction qui ajoute une prescription au Patient
    * \param hp le HealthProfessionnel prescripteur
    * \param p la prescription 
    */
    public void addPrescription(final HealthProfessional hp, final String p) {
        prescriptions.add(new Prescription(hp, p));
        //setChanged();
        notifyObservers();
    }

    /*!
    * \brief Fonction qui enlève une prescription au Patient
    * \param p la prescription
    */
    public void removePrescription(final Prescription p) {
        prescriptions.remove(p);
        //setChanged();
        notifyObservers();
    }

    /*!
    * \brief Fonction qui enlève une prescription au Patient
    * \param str la prescription de type String
    */
    public void removePrescription(final String str) {
        for (Prescription pr : prescriptions) {
            if (pr.getContent().equals(str)) {
                prescriptions.remove(pr);
            }
        }
        //setChanged();
        notifyObservers();
    }
    
    /*!
    * \brief Classe PatientBuilder
    */
    public static class PatientBuilder {
        private String name;
        private String ssID;

        public PatientBuilder(final String name, final String ssid) {
            this.name = name;
            this.ssID = ssid;
        }

        public Patient build() {
            return new Patient(this);
        }
    }

}

