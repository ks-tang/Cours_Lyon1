package fr.univ_lyon1.info.m1.mes.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import fr.univ_lyon1.info.m1.mes.utils.Observable;

/*!
* \brief Classe MES
*/
public class MES extends Observable {
    private final List<HealthProfessional> healthProfessionals = new ArrayList<>();
    private final Map<String, Patient> registry = new HashMap<>();

    /*!
    * \brief Recupere le registre des patients
    */
    public List<Patient> getPatients() {
        return new ArrayList<>(registry.values());
    }

    /*!
    * \brief Recupere la liste des HealthProfessional
    */
    public List<HealthProfessional> getHealthProfessional() {
        return healthProfessionals;
    }

    /*!
    * \brief Recupere un Patient selon son ssid
    * \param ssID un String
    */
    public Patient getPatient(final String ssID) {
        return registry.get(ssID);
    }

    /*!
    * \brief Recupere la liste des prescriptions d'un patient prescrites par un certain HealthPro
    * \param id le ssid du patient
    * \param hp le HealthProfessional
    */
    public List<Prescription> getPrescriptionBySSID(final String id, final HealthProfessional hp) {
        Patient p = getPatient(id);
        return p.getPrescriptions(hp);
    }

    /*!
    * \brief Recupere le nom d'un patient avec son ssid
    * \param name le nom du Patient
    */
    public String getSSIDbyName(final String name) {
        for (Map.Entry<String, Patient> entry : registry.entrySet()) {
            if (entry.getValue().getName().equals(name)) {
                return entry.getKey();
            }
        }
        return "";
    }

    /*!
    * \brief Crée un patient
    * \param name le nom du Patient a creer
    * \param ssID le ssid du Patient a creer
    */
    public Patient createPatient(final String name, final String ssID) {
        final Patient p = new Patient.PatientBuilder(name, ssID).build();
        registry.put(ssID, p);
        notifyObservers();
        return p;
    }

    /*!
    * \brief Crée un HealthProfessional
    * \param type le type de HealthPro a creer
    * \param name le nom du HealthPro a creer
    */
    public HealthProfessional createHealthProfessional(final String type, final String name) {
        HealthProfessionalFactory factory = new HealthProfessionalFactory();
        final HealthProfessional hp = factory.createHP(type, name);
        healthProfessionals.add(hp);
        return hp;
    }

    /*!
    * \brief Crée une configuration de base
    */
    public void createExampleConfiguration() {
        final Patient a = createPatient("Alice Foo", "299010212345678");
        final Patient b = createPatient("Bob Bar", "199010212345678");
        final Patient c = createPatient("Charles Boz", "102020212345678");
        //final HealthProfessional w = new HealthProfessional("Dr. Who", this);
        final HealthProfessional w = createHealthProfessional("HealthProfessional", "Dr. Who");
        //final HealthProfessional s = new Dentist("Dr. Strange", this);
        final HealthProfessional s = createHealthProfessional("Dentist", "Dr. Strange");
        //final HealthProfessional g = new Ophtalmo("Dr. Gero", this);
        final HealthProfessional g = createHealthProfessional("Ophtlamo", "Dr. Gero");
        //final HealthProfessional h = new Homeopath("Dr. Hahnemann", this);
        final HealthProfessional h = createHealthProfessional("Homeopath", "Dr. Hahnemann");
        a.addPrescription(w, "One apple a day");
        a.addPrescription(w, "Sport twice a week");
        b.addPrescription(w, "Whatever placebo, you're not sick");
        b.addPrescription(s, "Snake oil");
        c.addPrescription(g, "Carrot");
        c.addPrescription(h, "Tomato");
    }
}
