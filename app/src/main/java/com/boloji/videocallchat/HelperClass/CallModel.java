package com.boloji.videocallchat.HelperClass;

public class CallModel {

    public String group_id = "", message = "", message_id = "", sender_id = "", timestamp = "",sender_name = "";

    public CallModel() {
    }

    public CallModel(String group_id, String message, String message_id, String sender_id, String timestamp, String sender_name) {
        this.group_id = group_id;
        this.message = message;
        this.message_id = message_id;
        this.sender_id = sender_id;
        this.timestamp = timestamp;
        this.sender_name = sender_name;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}