package com.example.eindopdracht.Model;

public class User implements java.io.Serializable, Base {
    private static final long serialVersionUID = 1L;

    public int id;
    public String username;
    public String password;

    public User(int id) {
        setId(id);
    }
    public User(String username, String password) {
        setUsername(username);
        setPassword(password);
    }
    public User(int id, String username, String password) {
        setId(id);
        setUsername(username);
        setPassword(password);
    }


    //Setters
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setId(int id) { this.id = id; }

    //Getters
    public String getPassword() { return password; }
    public String getUsername() { return username; }
    public int getId() { return id; }


}