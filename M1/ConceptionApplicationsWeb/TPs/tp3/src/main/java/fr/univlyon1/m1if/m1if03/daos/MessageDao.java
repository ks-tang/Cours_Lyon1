package fr.univlyon1.m1if.m1if03.daos;

import fr.univlyon1.m1if.m1if03.classes.Message;
import fr.univlyon1.m1if.m1if03.classes.Salon;
import fr.univlyon1.m1if.m1if03.classes.User;
import java.io.Serializable;
import javax.naming.NameAlreadyBoundException;
import java.util.List;
import java.util.ArrayList;

public class MessageDao extends AbstractMapDao<Message> {
    private static int entier = 0;
    
    @Override
    protected Serializable getKeyForElement(Message element) {
        return element.getIdMessage();
    }

    public void createMessage(String user, String text, String idsalon) {
        try {
            this.add(new Message(user, text, entier, idsalon));
            entier++;
        } catch(NameAlreadyBoundException e) {
            System.out.println(e);
        }
    }

    public void createMessage(Message m) {
        try {
            Message message = new Message(m.getUser(), m.getText(), entier, m.getIdSalon());
            entier++;
            this.add(message);
        } catch(NameAlreadyBoundException e) {
            System.out.println(e);
        }
    }

    public List<Message> getAllMessagesBySalon(Salon salon) {
        return salon.getAllMessages();
    }

    public List<Message> getAllMessagesByUser(User user) {
        List<Message> messagesFromUser = new ArrayList<>();
        for(Message message : findAll()) {
            if(message.getUser().equals(user.getLogin())) {
                messagesFromUser.add(message);
            }
        }
        return messagesFromUser;
    }

}
