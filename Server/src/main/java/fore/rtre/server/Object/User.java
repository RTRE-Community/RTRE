package fore.rtre.server.Object;

import java.util.ArrayList;

public class User {

    private Long Id;
    private String queryName;

    public User(){}

    public User(Long Id, String queryName){
        this.Id = Id;
        this.queryName = queryName;

    }

    public Long getId() {
        return Id;
    }
    public String getQueryName() {
        return queryName;
    }



    
}
