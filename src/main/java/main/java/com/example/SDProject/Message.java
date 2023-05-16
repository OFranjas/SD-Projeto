package main.java.com.example.SDProject;

public class Message {

    private String text;

    public Message() {
    }

    public Message(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String message) {
        this.text = message;
    }

}