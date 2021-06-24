package com.example.chatapps.Models;

public class Chat {
    private String sender;
    private String receiver;
    private String message;
    private boolean daxem;
    public Chat(String sender, String receiver, String message,boolean daxem) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.daxem = daxem;
    }
    public Chat()
    {

    }

    public boolean isDaxem() {
        return daxem;
    }

    public void setDaxem(boolean daxem) {
        this.daxem = daxem;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
