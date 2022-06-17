package com.example.demo.Object;

public class Message {

    private String message;
    private boolean read;
    private Long to;
    private Long from;
    private String date;


    public Message(String message, Long to, Long from, String date){
        this.message = message;
        this.to = to;
        this.from = from;
        this.read = false;
        this.date = date;


    }

    public Long getFrom() {
        return from;
    }
    public String getMessage() {
        return message;
    }
    public Long getTo() {
        return to;
    }

    public void readMessage(){
        this.read = true;
    }
    public String getDate() {
        return date;
    }
}
