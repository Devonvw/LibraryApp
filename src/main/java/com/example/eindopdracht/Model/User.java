package com.example.eindopdracht.Model;

public class User implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    public String username;
    public String password;

    public User(String username, String password) {
        setUsername(username);
        setPassword(password);
    }

    //Setters
    public void setUsername(String username) { this.username = username; }

    public void setPassword(String password) { this.password = password; }

    //Getters
    public String getPassword() { return password; }

    public String getUsername() { return username; }
}