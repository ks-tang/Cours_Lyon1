package fr.univ_lyon1.info.m1.mes;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.Test;
import fr.univ_lyon1.info.m1.mes.controller.ControllerJfxView;
import fr.univ_lyon1.info.m1.mes.model.HealthProfessional;
import fr.univ_lyon1.info.m1.mes.model.Patient;
import fr.univ_lyon1.info.m1.mes.model.MES;
import java.util.List;

public class ControllerJfxViewTest {
    MES model = new MES();
    ControllerJfxView controller = new ControllerJfxView(model);

    @Test
    public void GetPatientByIdTest() {
        model.createPatient("Alice Foo", "299010212345678");

        Patient p = controller.controllerGetPatientById("299010212345678");

        assertThat(p.getName(), is("Alice Foo"));
    }

    @Test
    public void GetNotPatientByIdTest() {
        model.createPatient("Alice Foo", "299010212345678");

        Patient p = controller.controllerGetPatientById("299010212345678");

        assertThat(p.getName(), not(is("Alice Food")));
    }

    @Test
    public void GetPatientsTest() {
        model.createExampleConfiguration();

        List<Patient> list = controller.controllerGetPatients();

        assertThat(list, hasItem(
                hasProperty("name", equalTo("Alice Foo"))));
    }

    @Test
    public void GetNotPatientsTest() {
        model.createExampleConfiguration();

        List<Patient> list = controller.controllerGetPatients();

        assertThat(list, not(hasItem(
                hasProperty("name", equalTo("Alice Food")))));
    }

    @Test
    public void GetHealthProsTest() {
        model.createExampleConfiguration();

        List<HealthProfessional> list = controller.controllerGetHPs();

        assertThat(list, hasItem(
                hasProperty("name", equalTo("Dr. Gero"))));
    }

    @Test
    public void GetNotHealthProsTest() {
        model.createExampleConfiguration();

        List<HealthProfessional> list = controller.controllerGetHPs();

        assertThat(list, not(hasItem(
                hasProperty("name", equalTo("Dr. Zero")))));
    }
}
