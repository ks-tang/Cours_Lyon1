package fr.univ_lyon1.info.m1.mes;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.Test;
import fr.univ_lyon1.info.m1.mes.model.Dermato;
import fr.univ_lyon1.info.m1.mes.model.HealthProfessional;
import java.util.List;

public class DermatoTest {
    @Test
    public void NameTest(){
        HealthProfessional d = new Dermato("Dr. Pimple Popper");

        String name = d.getName();

        assertThat(name, is("Dr. Pimple Popper"));
    }

    @Test
    public void NotNameTest(){
        HealthProfessional d = new Dermato("Dr. Pimple Popper");

        String name = d.getName();

        assertThat(name, not(is("Dr. Strange")));
    }

    @Test
    public void GetPredefPrescTest() {
        HealthProfessional d = new Dermato("Dr. Pimple Popper");

        List<String> list = d.getPredefPrescr();

        assertThat(list, hasItem("Appliquer une crème apaisante matin et soir"));
        assertThat(list, hasItem("Paracetamol"));
    }

    @Test
    public void GetNotPredefPrescTest() {
        HealthProfessional d = new Dermato("Dr. Pimple Popper");

        List<String> list = d.getPredefPrescr();

        assertThat(list, not(hasItem("Do some sport")));
    }
}
