package fr.univlyon1.m1if.m1if03.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageTest {
    private final Message message = new Message(1, "toto", "test message");

    @Test
    void getSalon() {
        assertEquals(message.getSalon(), 1);
    }
    @Test
    void getUser() {
        assertEquals(message.getAuthor(), "toto");
    }

    @Test
    void getText() {
        assertEquals(message.getText(), "test message");
    }

    @Test
    void setText() {
        message.setText("test message 2");
        assertEquals(message.getText(), "test message 2");
    }
}
