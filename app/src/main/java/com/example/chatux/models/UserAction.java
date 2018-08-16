package com.example.chatux.models;

/**
 * Created by Amit kumar karn on 09-11-2017.
 */

public class UserAction {
    public int _id;
    public MessageModel _message;
    public String actionType;
    public String dynamic;
    public NextMessagePath messagePath;
    public String text;

    public static UserAction clone(UserAction userAction) {
        UserAction result = new UserAction();
        result.actionType = userAction.actionType;
        result.text = userAction.text;
        result.messagePath = userAction.messagePath;
        result._id = userAction._id;
        result._message = userAction._message;
        result.dynamic = userAction.dynamic;
        return result;
    }

    public String toString() {
        return "UserAction{actionType='" + this.actionType + '\'' + ", dynamic='" + this.dynamic + '\'' + ", text='" + this.text + '\'' + ", messagePath=" + this.messagePath + '}';
    }
}
