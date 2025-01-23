package fr.univ_lyon1.info.m1.mes;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.Test;
import fr.univ_lyon1.info.m1.mes.model.MES;
import fr.univ_lyon1.info.m1.mes.model.HealthProfessional;
import fr.univ_lyon1.info.m1.mes.model.Patient;
import fr.univ_lyon1.info.m1.mes.model.Prescription;
import java.util.List;

public class MESTest {
    MES model = new MES();

    @Test
    public void GetPatientsTest() {
        //Creation
        model.createPatient("Cecilia", "001");
        model.createPatient("Kevin", "002");

        //Get
        List<Patient> list = model.getPatients();

        //Verification
        assertThat(list, hasItem(
            hasProperty("name", equalTo("Cecilia"))));
    }

    @Test
    public void GetPatientBySSIDTest() {
        //Creation
        model.createPatient("Cecilia", "001");

        //Get
        Patient p = model.getPatient("001");

        //Verification
        assertThat(p.getName(), is("Cecilia"));
    }

    @Test
    public void GetHealthProTest() {
        //Creation
        model.createHealthProfessional("Ophtalmo", "Kevin");

        //Get
        List<HealthProfessional> hps = model.getHealthProfessional();

        //Verification
        assertThat(hps, hasItem(
                hasProperty("name", equalTo("Kevin"))));
    }

    @Test
    public void GetPrescriptionBySSIDTest() {
        HealthProfessional hp = model.createHealthProfessional("Ophtalmo", "Kevin");
        Patient p = model.createPatient("Cecilia", "001");
        p.addPrescription(hp, "Do some sport");

        List<Prescription> list = model.getPrescriptionBySSID("001", hp);

        assertThat(list, hasItem(
                hasProperty("content", equalTo("Do some sport"))));
    }

    @Test
    public void GetSSIDbyNameTest() {
        model.createPatient("Cecilia", "001");
        model.createPatient("Kevin", "002");

        String ssid = model.getSSIDbyName("Cecilia");

        assertThat(ssid, is("001"));
    }

    @Test
    public void CreatePatientTest() {
        //Creation
        model.createPatient("Alice", "20123456789012");

        //Get
        Patient p = model.getPatient("20123456789012");

        //Verification
        assertThat(p.getName(), is("Alice"));
    }

    @Test
    public void CreateHealthProTest() {
        model.createHealthProfessional("Ophtalmo", "Alice");

        List<HealthProfessional> hps = model.getHealthProfessional();

        assertThat(hps, hasItem(
                hasProperty("name", equalTo("Alice"))));
    }

    @Test
    public void CreateExampleConfigurationTest() {
        model.createExampleConfiguration();

        List<Patient> patients = model.getPatients();
        List<HealthProfessional> healthProfessionals = model.getHealthProfessional();
        List<Prescription> prescriptions = model.getPatient("299010212345678").getPrescriptions();

        assertThat(patients, hasItem(
            hasProperty("name", equalTo("Bob Bar"))));

        assertThat(patients, hasItem(
            hasProperty("SSID", equalTo("102020212345678"))));

        assertThat(healthProfessionals, hasItem(
            hasProperty("name", equalTo("Dr. Strange"))));

        assertThat(prescriptions, hasItem(
            hasProperty("content", equalTo("One apple a day"))));
        assertThat(prescriptions, not(hasItem(
            hasProperty("content", equalTo("Do some sport")))));
    }

}
