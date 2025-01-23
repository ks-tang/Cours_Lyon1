package fr.univ_lyon1.info.m1.mes;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.Test;
import fr.univ_lyon1.info.m1.mes.model.Ophtalmo;
import fr.univ_lyon1.info.m1.mes.model.HealthProfessional;
import java.util.List;

public class OphtalmoTest {
    @Test
    public void NameTest(){
        HealthProfessional o = new Ophtalmo("Dr. Monoyer");

        String name = o.getName();

        assertThat(name, is("Dr. Monoyer"));
    }

    @Test
    public void NotNameTest(){
        HealthProfessional o = new Ophtalmo("Dr. Monoyer");

        String name = o.getName();

        assertThat(name, not(is("Dr. Strange")));
    }

    @Test
    public void GetPredefPrescTest() {
        HealthProfessional o = new Ophtalmo("Dr. Monoyer");

        List<String> list = o.getPredefPrescr();

        assertThat(list, hasItem("Gouttes pour les yeux"));
        assertThat(list, hasItem("Paracetamol"));
    }

    @Test
    public void GetNotPredefPrescTest() {
        HealthProfessional o = new Ophtalmo("Dr. Monoyer");

        List<String> list = o.getPredefPrescr();

        assertThat(list, not(hasItem("Do some sport")));
    }
}
