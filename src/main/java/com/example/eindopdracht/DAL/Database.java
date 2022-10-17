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
    private static final String ITEMS_FILE = "items.in";

    public Database() {
        try {
            insert(new User("Admin", "123"), USERS_FILE);
            insert(new User("Admin", "123"), USERS_FILE);

        } catch (Exception ex) {
            System.out.println("error");
            System.out.println(ex.getMessage());
        }
    }

    public static User login(User userInput) throws Exception {
        if (userInput.getUsername() == null || userInput.getUsername().isEmpty()) throw new Exception("User not filled in");
        if (userInput.getPassword() == null || userInput.getPassword().isEmpty()) throw new Exception("Password not filled in");

        Optional<User> user = loadUsers().stream()
                .filter(u -> u.getUsername().equals(userInput.getUsername()))
                .findFirst();

        if (!user.isPresent()) throw new Exception("User does not exist");
        else if (!user.get().getPassword().equals(userInput.getPassword())) throw new Exception("Invalid username/password combination");

        return user.get();
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
        List<Object> objects = new ArrayList<Object>();
        Object object = null;

        ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));

        try {
                object = in.readObject();
            System.out.println(object);

                objects.add(object);

        } catch (EOFException ex) {
        }
        catch (Exception ex) {
            System.out.println("error2");
            System.out.println(ex.getMessage());
        }

        in.close();
        return objects;
    }

    private static void insert(Serializable data, String fileName) throws Exception {
        File file = new File(fileName);
        boolean append = file.exists();
        System.out.println(append);
        if (append) {
            try (FileOutputStream fout = new FileOutputStream(fileName); AppendableObjectOutputStream out = new AppendableObjectOutputStream(fout)) {
                out.writeObject(data);
                out.close();
                fout.close();
            } catch (Exception ex) {
                System.out.println("help");
            }
        }
        else {
            System.out.println("help");
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
                out.writeObject(data);
            } catch (Exception ex) {
            }
        }
    }
}