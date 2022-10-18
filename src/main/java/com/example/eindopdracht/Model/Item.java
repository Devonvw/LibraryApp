package com.example.eindopdracht.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class Item implements java.io.Serializable, Base {
    private static final long serialVersionUID = 1L;

    public int id;
    public Boolean available;
    public String title;
    public String author;
    public LocalDateTime lendDate;

    public User lendUser;

    public Item(Item item) {
        item.setId(getId());
        item.setTitle(getTitle());
        item.setAuthor(getAuthor());
        item.setAvailable(getAvailable());
        item.setLendDate(getLendDate());
        item.setLendUser(getLendUser());
    }
    public Item(int id) {
        setId(id);
    }
    public Item(String title, String author) {
        setId(0);
        setTitle(title);
        setAuthor(author);
        setAvailable(true);
    }
    public Item(int id, String title, String author) {
        setId(id);
        setTitle(title);
        setAuthor(author);
        setAvailable(true);
    }

    public void lend(User user) {
        setLendUser(user);
        setLendDate(LocalDateTime.now());
        setAvailable(false);
    }
    public void recieve() {
        setLendUser(null);
        setLendDate(null);
        setAvailable(true);
    }

    //Setters
    public void setAvailable(Boolean available) { this.available = available; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setLendDate(LocalDateTime lendDate) { this.lendDate = lendDate; }
    public void setLendUser(User user) { this.lendUser = user; }
    public void setId(int id) { this.id = id; }

    //Getters
    public Boolean getAvailable() { return available; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public LocalDateTime getLendDate() { return lendDate; }
    public User getLendUser() { return lendUser; }
    public int getId() { return id; }
}