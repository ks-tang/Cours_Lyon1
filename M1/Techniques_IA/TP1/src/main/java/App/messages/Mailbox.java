package App.messages;

import java.util.HashMap;
import java.util.LinkedList;

import App.model.Agent;

public class Mailbox {

    private static HashMap<Integer, LinkedList<Message>> mailboxes = new HashMap<>();

    public synchronized static void addMailbox(Agent agent) {
        mailboxes.put(agent.getId(), new LinkedList<>());
    }

    public synchronized static void dropAMessage(Message message) {
        mailboxes.get(message.getReceiver()).add(message);
    }

    public synchronized static Message consumeMessage(int index) {
        return mailboxes.getOrDefault(index, new LinkedList<>()).pop();
    }

    public synchronized static Message getMessage(int index) {
        return mailboxes.getOrDefault(index, new LinkedList<>()).getFirst();
    }

    public synchronized static void deleteMessage(int index) {
        mailboxes.getOrDefault(index, new LinkedList<>()).removeFirst();
    }

    public synchronized static boolean containMessages(int index) {
        return mailboxes.getOrDefault(index, new LinkedList<>()).size() > 0;
    }

    synchronized static LinkedList<Message> getAllMessage(int index) {
        return mailboxes.get(index);
    }
}