package App.messages;

import org.json.JSONException;
import org.json.JSONObject;

import App.movement.Directions;
import App.movement.Position;

import java.util.Date;

public class Message {

    private int sender;
    private int receiver;
    private Date date;
    private JSONObject content;

    public Message(int sender, int receiver, Date date) {
        this.sender = sender;
        this.receiver = receiver;
        this.date = date;
    }

    public void setContent(MessageTypes type, Directions move, Position position, Position senderpos) {
        content = new JSONObject();
        try {
            content.put("type", type.getValue());
            content.put("move", move);
            content.put("position", position);
            content.put("senderpos", senderpos);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Position getPosition() {
        try {
            return (Position) this.content.get("position");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Position getSenderPosition() {
        try {
            return (Position) this.content.get("senderpos");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getType() {
        try {
            return this.content.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "Message{" +
                "sender=" + sender +
                ", receiver=" + receiver +
                ", date=" + date +
                ", content='" + content + '\'' +
                '}';
    }

    public int getReceiver() {
        return receiver;
    }

    public int getSender() {
        return sender;
    }
}
