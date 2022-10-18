package com.example.eindopdracht.DAL;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.eindopdracht.Model.*;


public class Database {
    private static final String USERS_FILE = "users.in";
    private static final String LOGGED_IN_USER_FILE = "logged_in_user.in";
    private static final String ITEMS_FILE = "items.in";

    public Database() {
        try {
            List<Base> users = new ArrayList<>();
            List<Base> items = new ArrayList<>();

            users.add(new User(1,"Admin", "123"));
            users.add(new User(2,"Joost", "123"));

            items.add(new Item(1, "Test book 1", "Test author 1"));
            items.add(new Item(2, "Test book 1", "Test author 1"));

            insert(users, USERS_FILE);
            insert(items, ITEMS_FILE);
        } catch (Exception ex) {
            System.out.println("error");
            System.out.println(ex.getMessage());
        }
    }

    public static void lendItem(int itemId, int memberId) throws Exception {
        List<Item> items = loadItems();

        Item item = items.stream()
                .peek(i -> {
                    System.out.println(i.getId());
                    System.out.println(i.getTitle());
                })
                .filter(i -> i.getId() == itemId)
                .findFirst().orElse(null);

        User user = loadUsers().stream()
                .filter(i -> i.getId() == memberId)
                .findFirst().orElse(null);

        if (item == null) throw new Exception("Item does not exist");
        if (user == null) throw new Exception("User does not exist");

        item.lend(user);

        List<Base> itemsInput = new ArrayList<>();
        for(Item i: items) itemsInput.add(i);

        update(itemsInput, item, ITEMS_FILE);

        System.out.println(item.getId());
        System.out.println("Gelukt");
    }

    public static void recieveItem(int itemId) throws Exception {
        List<Item> items = loadItems();

        Optional<Item> item = items.stream()
                .peek(i -> {
                    System.out.println(i.getId());
                    System.out.println(i.getTitle());
                })
                .filter(i -> i.getId() == itemId)
                .findFirst();

        if (!item.isPresent()) throw new Exception("Item does not exist");

        Item updatedItem = new Item(item.get());
        updatedItem.recieve();

        List<Base> itemsInput = new ArrayList<>();
        for(Item i: items) itemsInput.add(i);

        update(itemsInput, updatedItem, ITEMS_FILE);
    }

    public static User getLoggedInUser() throws Exception {
        List<Object> objects = loadObjects(LOGGED_IN_USER_FILE);

        if (!objects.isEmpty()) return (User)objects.get(0);
        throw new Exception("No logged in user found");
    }

    public static void login(User userInput) throws Exception {
        if (userInput.getUsername() == null || userInput.getUsername().isEmpty()) throw new Exception("User not filled in");
        if (userInput.getPassword() == null || userInput.getPassword().isEmpty()) throw new Exception("Password not filled in");

        Optional<User> user = loadUsers().stream()
                .filter(u -> u.getUsername().equals(userInput.getUsername()))
                .findFirst();

        if (!user.isPresent()) throw new Exception("User does not exist");
        else if (!user.get().getPassword().equals(userInput.getPassword())) throw new Exception("Invalid username/password combination");


        List<Base> loggedInUser = new ArrayList<>();
        loggedInUser.add(user.get());

        insert(loggedInUser, LOGGED_IN_USER_FILE);
    }

    public static List<User> loadUsers() throws Exception {
        List<User> users = new ArrayList<User>();

        loadObjects(USERS_FILE).forEach((object) -> users.add((User)object));

        return users;
    }

    public static List<Item> loadItems() throws Exception {
        List<Item> items = new ArrayList<Item>();

        loadObjects(ITEMS_FILE).forEach((object) -> items.add((Item)object));

        return items;
    }

    private static List<Object> loadObjects(String fileName) throws Exception {
        List<Object> objects = new ArrayList<>();

        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(Paths.get(fileName)))) {
            while (true) objects.add(in.readObject());
        } catch (EOFException ex) {}
        catch (Exception ex) {
            throw new Exception("Not able to load objects");
        }

        return objects;
    }

    private static List<Base> loadBase(String fileName) throws Exception {
        List<Base> objects = new ArrayList<>();

        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(Paths.get(fileName)))) {
            while (true) objects.add((Base)in.readObject());
        } catch (EOFException ex) {}
        catch (Exception ex) {
            throw new Exception("Not able to load objects");
        }

        return objects;
    }

    private static void insert(List<Base> data, String fileName) throws Exception {
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(Paths.get(fileName)))) {
            data.forEach(d -> {
                try {
                    out.writeObject(d);
                } catch (Exception ex) {}
            });
        }
    }

    private static void insert(List<Base> data, List<Base> oldData, String fileName) throws Exception {
        int highestId = data.get(data.size() - 1).getId();
        data.forEach(d -> {
            d.setId(highestId + 1);
            oldData.add(d);
        });

        insert(oldData, fileName);
    }

    private static void update(List<Base> data, Base newData, String fileName) throws Exception {
        for (int i = 0; i < data.stream().count(); i++) {
            if (data.get(i).getId() == newData.getId()) {
                data.set(i, newData);
                break;
            }
        }

        insert(data, fileName);
    }
}