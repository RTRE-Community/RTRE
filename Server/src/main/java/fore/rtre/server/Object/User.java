package fore.rtre.server.Object;

import java.util.ArrayList;

public class User {

    private ArrayList<Message> incoming;
    private ArrayList<Message> outgoing;
    private Long Id;

    public User(){}

    public User(Long Id){
        this.Id = Id;
    

    }


    public boolean addOutgoing(){

        return true;

    }

    public boolean addIncoming(){

        return true;

    }



    
}
