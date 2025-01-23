package fr.univ_lyon1.info.m1.mes;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.Test;
import fr.univ_lyon1.info.m1.mes.model.Homeopath;
import fr.univ_lyon1.info.m1.mes.model.HealthProfessional;
import java.util.List;

public class HomeopathTest {
    @Test
    public void NameTest(){
        HealthProfessional h = new Homeopath("Dr. Hahnemann");

        String name = h.getName();

        assertThat(name, is("Dr. Hahnemann"));
    }

    @Test
    public void NotNameTest(){
        HealthProfessional h = new Homeopath("Dr. Hahnemann");

        String name = h.getName();

        assertThat(name, not(is("Dr. Strange")));
    }

    @Test
    public void GetPredefPrescTest() {
        HealthProfessional h = new Homeopath("Dr. Hahnemann");

        List<String> list = h.getPredefPrescr();

        assertThat(list, hasItem("Natrum Muriaticum 30CH"));
        assertThat(list, hasItem("Sucre 200K"));
        assertThat(list, hasItem("Paracetamol"));
    }

    @Test
    public void GetNotPredefPrescTest() {
        HealthProfessional h = new Homeopath("Dr. Hahnemann");

        List<String> list = h.getPredefPrescr();

        assertThat(list, not(hasItem("Do some sport")));
    }
}
