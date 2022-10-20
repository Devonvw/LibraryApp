package com.example.eindopdracht.DAL;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.eindopdracht.Model.*;


public class Database {
    private static final String USERS_FILE = "users.in";
    private static final String LOGGED_IN_USER_FILE = "logged_in_user.in";
    private static final String ITEMS_FILE = "items.in";

    public Database() {
        try {
            List<Base> users = new ArrayList<>();
            List<Base> items = new ArrayList<>();

            users.add(new User(1,"Admin", "123", "Admin", "Lastname", LocalDate.now()));
            users.add(new User(2,"Joost", "123", "Joost", "Smit", LocalDate.now()));

            items.add(new Item(1, "Test book 1", "Test author 1"));
            items.add(new Item(2, "Test book 2", "Test author 2"));

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
                .filter(i -> i.getId() == itemId)
                .findFirst().orElse(null);

        User user = loadUsers().stream()
                .filter(i -> i.getId() == memberId)
                .findFirst().orElse(null);

        if (item == null) throw new Exception("Item does not exist");
        if (user == null) throw new Exception("User does not exist");
        if (!item.available) throw new Exception("Item is already lent");

        item.lend(user);

        List<Base> itemsInput = new ArrayList<>();
        for(Item i: items) itemsInput.add(i);

        update(itemsInput, item, ITEMS_FILE);
    }

    public static void recieveItem(int itemId) throws Exception {
        List<Item> items = loadItems();

        Item item = items.stream()
                .filter(i -> i.getId() == itemId)
                .findFirst().orElse(null);

        if (item == null) throw new Exception("Item does not exist");
        if (item.available) throw new Exception("Item is already recieved");
        if (item.lendDate.isBefore(LocalDateTime.now().minusDays(21))) throw new Exception("Item is received too late");

        item.recieve();

        List<Base> itemsInput = new ArrayList<>();
        for(Item i: items) itemsInput.add(i);

        update(itemsInput, item, ITEMS_FILE);
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
            System.out.println(ex.getMessage());
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
    public static void updateItem(List<Item> data, Item newData) throws Exception {
        List<Base> dataBase = new ArrayList<>(data);

        update(dataBase, newData, ITEMS_FILE);
    }
    public static void updateUser(List<User> data, User newData) throws Exception {
        List<Base> dataBase = new ArrayList<>(data);

        update(dataBase, newData, USERS_FILE);
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

    public static void insertItemWithoutId(List<Item> data, Item newData) throws Exception {
        List<Base> dataBase = new ArrayList<>(data);

        insertNewWithoutId(dataBase, newData, ITEMS_FILE);
    }
    public static void insertUserWithoutId(List<User> data, User newData) throws Exception {
        List<Base> dataBase = new ArrayList<>(data);

        insertNewWithoutId(dataBase, newData, USERS_FILE);
    }
    public static void insertNewWithoutId(List<Base> data, Base newData, String fileName) throws Exception {
        int highestId = 0;
        for (int i = 0; i < data.stream().count(); i++) {
            if (data.get(i).getId() > highestId) {
                highestId = data.get(i).getId();
            }
        }
        newData.setId(highestId + 1);
        data.add(newData);

        insert(data, fileName);
    }

    public static void deleteItem(List<Item> data, int id) throws Exception {
        List<Base> dataBase = new ArrayList<>(data);

        delete(dataBase, id, ITEMS_FILE);
    }
    public static void deleteUser(List<User> data, int id) throws Exception {
        List<Base> dataBase = new ArrayList<>(data);

        delete(dataBase, id, USERS_FILE);
    }
    public static void delete(List<Base> data, int id, String fileName) throws Exception {
        for (int i = 0; i < data.stream().count(); i++) {
            if (data.get(i).getId() == id) {
                data.remove(i);
                break;
            }
        }
        insert(data, fileName);
    }
}