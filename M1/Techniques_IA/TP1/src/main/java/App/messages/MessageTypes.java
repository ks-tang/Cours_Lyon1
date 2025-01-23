package App.messages;

public enum MessageTypes{
    MOVE("move"),;

    private final String value;

    MessageTypes(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}