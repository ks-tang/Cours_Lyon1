package fr.univ_lyon1.info.m1.mes;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.Test;
import fr.univ_lyon1.info.m1.mes.model.HealthProfessional;
import fr.univ_lyon1.info.m1.mes.model.HealthProfessionalFactory;

public class HealthProFactoryTest {
    @Test
    public void CreationTest() {
        HealthProfessionalFactory factory = new HealthProfessionalFactory();
        HealthProfessional hp = factory.createHP("Ophtalmo", "Dr. Lelouch");

        assertThat(hp.getName(), is("Dr. Lelouch"));
    }

    @Test
    public void NotCreationTest() {
        HealthProfessionalFactory factory = new HealthProfessionalFactory();
        HealthProfessional hp = factory.createHP("Ophtalmo", "Dr. Lelouch");

        assertThat(hp.getName(), not(is("Dr. Lelouch Lamperouge")));
    }
}
