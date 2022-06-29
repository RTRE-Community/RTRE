package fore.rtre.server.Object;

public class Message {

    private String message;
    private String to;
    private String from;
    private String date;
    private boolean readMessage;



    public Message(String message, String to, String from, String date, Boolean notRead){
        this.message = message;
        this.to = to;
        this.from = from;
        this.date = date;
        this.readMessage = notRead;
    }

    public String getFrom() {
        return from;
    }
    public String getMessage() {
        return message;
    }
    public String getTo() {
        return to;
    }
    public boolean getReadMessage(){
        return readMessage;
    }

    public void readMessage(){
        this.readMessage = true;
    }
    public String getDate() {
        return date;
    }
}
