package com.example.chatux.models;

/**
 * Created by Amit kumar karn on 09-11-2017.
 */

public class NextMessagePath {
    public int _id;
    public MessageModel _message;
    public UserAction _userAction;
    public String frameId;
    public String messageId;

    public String toString() {
        return "NextMessagePath{frameId='" + this.frameId + '\'' + ", messageId='" + this.messageId + '\'' + '}';
    }
}