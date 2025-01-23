package fr.univlyon1.m1if.m1if03.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.NameAlreadyBoundException;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class SalonTest {
    private final User user = new User("toto", "password", "Xyz");
    private Salon salon;

    @BeforeEach
    void setUp() {
        salon = new Salon("testSalon", user.getLogin());
    }

    @Test
    void getId() {
        assertInstanceOf(Integer.class, salon.getId());
    }

    @Test
    void getNom() {
        assertEquals(salon.getName(), "testSalon");
    }

    @Test
    void setNom() {
        salon.setName("retestSalon");
        assertEquals(salon.getName(), "retestSalon");
    }

    @Test
    void getOwner() {
        assertEquals(salon.getOwner(), "toto");
    }

    @Test
    void addMembre() {
        try {
            salon.addMember("toto");
        } catch (NameAlreadyBoundException e) {
            fail("Exception thrown:" + e.getMessage());
        }
    }

    @Test
    void removeMembre() {
        try {
            salon.addMember("toto");
            assertTrue(salon.removeMember("toto"));
        } catch (NameAlreadyBoundException e) {
            fail("Exception thrown:" + e.getMessage());
        }
    }

    @Test
    void hasMembre() {
        try {
            salon.addMember("toto");
            assertTrue(salon.hasMember("toto"));
        } catch (NameAlreadyBoundException e) {
            fail("Exception thrown:" + e.getMessage());
        }
    }

    @Test
    void getAllMembres() {
        try {
            salon.addMember("toto");
            Set<String> membres = salon.getMembers();
            assertEquals(membres.size(), 1);
            assertEquals(membres.toArray()[0], "toto");
        } catch (NameAlreadyBoundException e) {
            fail("Exception thrown:" + e.getMessage());
        }
    }
}
