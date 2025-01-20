package fr.univ_lyon1.info.m1;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;


public class CharManipulatorTest {

    private CharManipulator manipulator;

    @BeforeEach
    public void setUp() {
        manipulator = new CharManipulator();
    }

    @Test
    public void orderNormalString() {
        assertEquals("A", manipulator.invertOrder("A"));
        assertEquals("DCBA", manipulator.invertOrder("ABCD"));
        assertEquals("321DCBA", manipulator.invertOrder("ABCD123"));
    }

    @Test
    public void orderEmptyString() {
        assertEquals("", manipulator.invertOrder(""));
    }

    @Test
    public void caseNormalString() {
        assertEquals("A", manipulator.invertCase("a"));
        assertEquals("abCD", manipulator.invertCase("ABcd"));
        assertEquals("123abcd", manipulator.invertCase("123ABCD"));
    }

    @Test
    public void caseEmptyString() {
        assertEquals("", manipulator.invertOrder(""));
    }

    @Test
    public void removePatternTest() {
        assertEquals("cc", manipulator.removePattern("coucou", "ou"));
        assertEquals("", manipulator.removePattern("aabb", "ab"));
        assertEquals("az", manipulator.removePattern("akkkkkz", "k"));
    }

}
