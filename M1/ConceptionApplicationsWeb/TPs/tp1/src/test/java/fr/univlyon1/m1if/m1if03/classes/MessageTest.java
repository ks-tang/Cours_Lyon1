package fr.univlyon1.m1if.m1if03.classes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageTest {
    private final Message message = new Message("toto", "test message");

    @Test
    void getUser() {
        assertEquals(message.getUser(), "toto");
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
