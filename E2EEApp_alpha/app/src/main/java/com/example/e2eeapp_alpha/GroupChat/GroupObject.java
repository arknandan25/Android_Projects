package com.example.e2eeapp_alpha.GroupChat;

public class GroupObject {
    private String date, groupName, message , messageKey, senderName, senderUid, time;

    public GroupObject(){}

    public GroupObject(String date, String groupName, String message, String messageKey, String senderName, String senderUid, String time) {
        this.date = date;
        this.groupName = groupName;
        this.message = message;
        this.messageKey = messageKey;
        this.senderName = senderName;
        this.senderUid = senderUid;
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
