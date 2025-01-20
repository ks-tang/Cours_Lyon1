package fr.univ_lyon1.info.m1.mes;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.Test;
import fr.univ_lyon1.info.m1.mes.model.MES;
import fr.univ_lyon1.info.m1.mes.model.HealthProfessional;
import fr.univ_lyon1.info.m1.mes.model.Patient;
import fr.univ_lyon1.info.m1.mes.model.Prescription;
import java.util.List;

public class PatientTest {
    MES model = new MES();

    @Test
    /**
     * A simple test, purposely broken so that students can see what happens for
     * test failures.
     */
    public void PatientNameTest() {
        //Creation
        Patient p = model.createPatient("Cecilia", "001");

        //Get
        String name = p.getName();

        //Verification
        assertThat(name, is("Cecilia"));
    }

    @Test
    public void PatientNotNameTest() {
        //Creation
        Patient p = model.createPatient("Cecilia", "001");

        //Get
        String name = p.getName();

        //Verification
        assertThat(name, not(is("Kevin")));
    }

    @Test
    public void PatientSSIDTest() {
        //Creation
        Patient p = model.createPatient("Cecilia", "001");

        //Get
        String ssid = p.getSSID();

        //Verification
        assertThat(ssid, is("001"));
    }

    @Test
    public void AddPrescriptionTest() {
        HealthProfessional hp = new HealthProfessional("Dr. Smith");
        Patient p = model.createPatient("Alice", "20123456789012");
        p.addPrescription(hp, "Do some sport");
        p.addPrescription(hp, "Eat vegetables");

        List<Prescription> prescriptions = model.getPatient("20123456789012").getPrescriptions();

        assertThat(prescriptions, hasItem(
                hasProperty("content", equalTo("Do some sport"))));
        assertThat(prescriptions, hasItem(
                hasProperty("content", equalTo("Eat vegetables"))));
    }

    @Test
    /**
     * Test addPrescription, and demonstrate advanced Hamcrest assertions.
     */
     
    public void GetPrescriptionTest() {
        // Given
        HealthProfessional hp = new HealthProfessional("Dr. Smith");
        Patient p = model.createPatient("Alice", "20123456789012");
        p.addPrescription(hp, "Do some sport");

        // When
        List<Prescription> prescriptions = model.getPatient("20123456789012").getPrescriptions();

        // Then
        assertThat(prescriptions, hasItem(
                hasProperty("content", equalTo("Do some sport"))));
    }

    @Test
    public void GetNotPrescriptionTest() {
        // Given
        HealthProfessional hp = new HealthProfessional("Dr. Smith");
        Patient p = model.createPatient("Alice", "20123456789012");
        p.addPrescription(hp, "Eat fruits");

        // When
        List<Prescription> prescriptions = model.getPatient("20123456789012").getPrescriptions();

        // Then
        assertThat(prescriptions, not(
                hasItem(
                        hasProperty("content", equalTo("Do some sport")))));
    }

    @Test
    public void RemovePrescriptionTest() {
        HealthProfessional hp = new HealthProfessional("Dr. Smith");
        Patient p = model.createPatient("Alice", "20123456789012");
        p.addPrescription(hp, "Do some sport");
        p.addPrescription(hp, "Eat vegetables");
        p.removePrescription("Do some sport");

        List<Prescription> prescriptions = model.getPatient("20123456789012").getPrescriptions();

        assertThat(prescriptions, not( hasItem(
                hasProperty("content", equalTo("Do some sport")))));
        assertThat(prescriptions, hasItem(
                hasProperty("content", equalTo("Eat vegetables"))));
    }
    
}
