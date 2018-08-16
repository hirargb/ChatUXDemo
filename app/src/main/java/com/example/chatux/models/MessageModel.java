package com.example.chatux.models;

/**
 * Created by Amit kumar karn on 09-11-2017.
 */

public class MessageModel {
    public int messageType;
    public String message;
    private int messageIcon;

    public MessageModel() {
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMessageIcon() {
        return messageIcon;
    }

    public void setMessageIcon(int messageIcon) {
        this.messageIcon = messageIcon;
    }
}
