package fr.univ_lyon1.info.m1.mes;


import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.Test;
import fr.univ_lyon1.info.m1.mes.controller.ControllerHealthProfessional;
import fr.univ_lyon1.info.m1.mes.model.HealthProfessional;
import fr.univ_lyon1.info.m1.mes.model.Patient;
import fr.univ_lyon1.info.m1.mes.model.MES;

public class ControllerHealthProTest {
    MES model = new MES();
    ControllerHealthProfessional controller = new ControllerHealthProfessional(model);

    @Test
    public void GetPatientNameTest() {
        Patient p = model.createPatient("Alice Foo", "299010212345678");

        String name = controller.controllerGetName(p);

        assertThat(name, is("Alice Foo"));
    }

    @Test
    public void GetNotPatientNameTest() {
        Patient p = model.createPatient("Alice Foo", "299010212345678");

        String name = controller.controllerGetName(p);

        assertThat(name, not(is("Alice Food")));
    }

    @Test
    public void GetPatientBySSIDTest() {
        Patient p = model.createPatient("Alice Foo", "299010212345678");

        Patient p2 = controller.controllerGetPatient("299010212345678");

        assertThat(p, is(p2));
    }

    @Test
    public void GetHealthProNameTest() {
        HealthProfessional hp = model.createHealthProfessional("Ophtalmo", "Alice Foo");

        String name = controller.controllerGetName(hp);

        assertThat(name, is("Alice Foo"));
    }

    @Test
    public void GetNotHealthProNameTest() {
        HealthProfessional hp = model.createHealthProfessional("Ophtalmo", "Alice Foo");

        String name = controller.controllerGetName(hp);

        assertThat(name, not(is("Alice Food")));
    }

}
