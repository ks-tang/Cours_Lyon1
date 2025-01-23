package fr.univ_lyon1.info.m1.mes;


import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.Test;
import fr.univ_lyon1.info.m1.mes.model.Dentist;
import fr.univ_lyon1.info.m1.mes.model.HealthProfessional;
import java.util.List;

public class DentistTest {
    @Test
    public void NameTest(){
        HealthProfessional d = new Dentist("Dr. Guedj");

        String name = d.getName();

        assertThat(name, is("Dr. Guedj"));
    }

    @Test
    public void NotNameTest(){
        HealthProfessional d = new Dentist("Dr. Guedj");

        String name = d.getName();

        assertThat(name, not(is("Dr. Strange")));
    }

    @Test
    public void GetPredefPrescTest() {
        HealthProfessional d = new Dentist("Dr. Guedj");

        List<String> list = d.getPredefPrescr();

        assertThat(list, hasItem("Ne pas manger pendant une heure"));
        assertThat(list, hasItem("Paracetamol"));
    }

    @Test
    public void GetNotPredefPrescTest() {
        HealthProfessional d = new Dentist("Dr. Guedj");

        List<String> list = d.getPredefPrescr();

        assertThat(list, not(hasItem("Do some sport")));
    }
}
