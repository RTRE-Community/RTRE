package fore.rtre.server.Helper_Classes;

public class User {
    private String name;
    private String username;
    private Long oid;

    public User(String name, String username, Long oid){
        this.name = name;
        this.username = username;
        this.oid = oid;

    }

    public String getName() {
        return this.name;
    }
    public Long getOid() {
        return this.oid;
    }
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        if(obj instanceof User)
        {
            User temp = (User) obj;
            if(this.name.equals(temp.name) && this.username.equals(temp.username) && this.oid==temp.oid){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        
        return (this.name.hashCode() + this.username.hashCode() + this.oid.hashCode());        
    }
    
}
