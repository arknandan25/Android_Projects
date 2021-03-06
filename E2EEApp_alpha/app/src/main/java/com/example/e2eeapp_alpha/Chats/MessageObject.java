package com.example.e2eeapp_alpha.Chats;

//public class MessageObject {
////    String msg_id, msg_receiver_id, msg_sender_id, msg_text, msg_timestamp, msg_type;
//    String   msg_receiver_id, msg_sender_id, msg_text, msg_timestamp, msg_type;
//
//    public MessageObject(){}
//
//
//    public MessageObject(  String msg_receiver_id, String msg_sender_id, String msg_text, String msg_timestamp, String msg_type) {
////        this.msg_id = msg_id;
//        this.msg_receiver_id = msg_receiver_id;
//        this.msg_sender_id = msg_sender_id;
//        this.msg_text = msg_text;
//        this.msg_timestamp = msg_timestamp;
//        this.msg_type = msg_type;
//    }
//
////    public String getMsg_id() {
////        return msg_id;
////    }
////
////    public void setMsg_id(String msg_id) {
////        this.msg_id = msg_id;
////    }
//
//    public String getMsg_receiver_id() {
//        return msg_receiver_id;
//    }
//
//    public void setMsg_receiver_id(String msg_receiver_id) {
//        this.msg_receiver_id = msg_receiver_id;
//    }
//
//    public String getMsg_sender_id() {
//        return msg_sender_id;
//    }
//
//    public void setMsg_sender_id(String msg_sender_id) {
//        this.msg_sender_id = msg_sender_id;
//    }
//
//    public String getMsg_text() {
//        return msg_text;
//    }
//
//    public void setMsg_text(String msg_text) {
//        this.msg_text = msg_text;
//    }
//
//    public String getMsg_timestamp() {
//        return msg_timestamp;
//    }
//
//    public void setMsg_timestamp(String msg_timestamp) {
//        this.msg_timestamp = msg_timestamp;
//    }
//
//    public String getMsg_type() {
//        return msg_type;
//    }
//
//    public void setMsg_type(String msg_type) {
//        this.msg_type = msg_type;
//    }
//}


//attampt 2 MVP

public class MessageObject {
    private String from;
    private String to;
    private String message;
    private String type;
    private String timestamp;
    private String ucid;
    private String time;
    private String date;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public String getFrom() {
        return from;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MessageObject(String from, String message, String type, String timestamp, String ucid, String time, String date, String to) {
        this.from = from;
        this.message = message;
        this.type = type;
        this.timestamp = timestamp;
        this.ucid = ucid;
        this.time = time;
        this.date = date;
        this.to = to;
    }

    public MessageObject(){}

}