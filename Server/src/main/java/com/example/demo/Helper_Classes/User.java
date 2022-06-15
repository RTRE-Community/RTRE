package com.example.demo.Helper_Classes;

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
    
}
