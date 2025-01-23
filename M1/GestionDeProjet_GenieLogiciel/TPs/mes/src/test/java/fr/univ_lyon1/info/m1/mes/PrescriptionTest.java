package fr.univ_lyon1.info.m1.mes;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.Test;
import fr.univ_lyon1.info.m1.mes.model.Prescription;
import fr.univ_lyon1.info.m1.mes.model.HealthProfessional;

public class PrescriptionTest {
    @Test
    public void ContentTest() {
        HealthProfessional hp = new HealthProfessional("Dr. Griffiths");
        Prescription pr = new Prescription(hp, "Se laver les mains avec du gel hydroalcoolique");

        assertThat(pr.getContent(), is("Se laver les mains avec du gel hydroalcoolique"));
    }

    @Test
    public void NotContentTest() {
        HealthProfessional hp = new HealthProfessional("Dr. Griffiths");
        Prescription pr = new Prescription(hp, "Se laver les mains avec du gel hydroalcoolique");

        assertThat(pr.getContent(), not(is("Se gratter les fesses")));
    }

    @Test
    public void GetHealthProTest() {
        HealthProfessional hp = new HealthProfessional("Dr. Griffiths");
        Prescription pr = new Prescription(hp, "Se laver les mains avec du gel hydroalcoolique");

        String name = pr.getHealthProfessional().getName();

        assertThat(name, is("Dr. Griffiths"));
    }

    @Test
    public void GetNotHealthProTest() {
        HealthProfessional hp = new HealthProfessional("Dr. Griffiths");
        Prescription pr = new Prescription(hp, "Se laver les mains avec du gel hydroalcoolique");

        String name = pr.getHealthProfessional().getName();

        assertThat(name, not(is("Dr. Pittet")));
    }
}
