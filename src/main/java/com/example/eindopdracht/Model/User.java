package com.example.eindopdracht.Model;

import java.time.LocalDate;

public class User implements java.io.Serializable, Base {
    private static final long serialVersionUID = 1L;

    public int id;
    public String username;
    public String password;

    public String firstname;
    public String lastname;
    public LocalDate birthdate;

    public User(int id) {
        setId(id);
    }
    public User(String username, String password) {
        setUsername(username);
        setPassword(password);
    }
    public User(String username, String password, String firstname, String lastname, LocalDate birthdate) {
        setUsername(username);
        setPassword(password);
        setFirstname(firstname);
        setLastname(lastname);
        setBirthdate(birthdate);
    }
    public User(int id, String username, String password, String firstname, String lastname, LocalDate birthdate) {
        setId(id);
        setUsername(username);
        setPassword(password);
        setFirstname(firstname);
        setLastname(lastname);
        setBirthdate(birthdate);
    }


    //Setters
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setId(int id) { this.id = id; }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    //Getters
    public String getPassword() { return password; }
    public String getUsername() { return username; }
    public int getId() { return id; }
    public String getFirstname() {
        return firstname;
    }
    public String getLastname() {
        return lastname;
    }
    public LocalDate getBirthdate() {
        return birthdate;
    }

}