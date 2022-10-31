package com.libraryApp.DAL;

import com.libraryApp.Model.Base;
import com.libraryApp.Model.Item;
import com.libraryApp.Model.User;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class Database {
    protected static final String USERS_FILE = "users.in";
    protected static final String LOGGED_IN_USER_FILE = "logged_in_user.in";
    protected static final String ITEMS_FILE = "items.in";

    public Database() {
        try {
            if (!Files.exists(Paths.get(USERS_FILE)) || loadBase(USERS_FILE).stream().count() == 0) {
                List<Base> users = new ArrayList<>();
                users.add(new User(1, "Admin", "123", "Admin", "Lastname", LocalDate.now()));
                users.add(new User(2, "Joost", "123", "Joost", "Smit", LocalDate.now()));
                insert(users, USERS_FILE);
            }
            if (!Files.exists(Paths.get(ITEMS_FILE)) || loadBase(ITEMS_FILE).stream().count() == 0) {
                List<Base> items = new ArrayList<>();
                items.add(new Item(1, "Test book 1", "Test author 1"));
                items.add(new Item(2, "Test book 2", "Test author 2"));
                items.add(new Item(3, "Test book 3", "Test author 3", LocalDateTime.of(2022, 10, 4, 0, 0)));
                items.add(new Item(4, "Test book 4", "Test author 4", LocalDateTime.of(2022, 10, 1, 0, 0)));
                insert(items, ITEMS_FILE);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    protected static List<Base> loadBase(String fileName) throws Exception {
        List<Base> objects = new ArrayList<>();

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            while (true) objects.add((Base) in.readObject());
        } catch (EOFException ex) {
        } catch (Exception ex) {
            throw new Exception("Not able to load data");
        }

        return objects;
    }

    protected static void insert(List<Base> data, String fileName) throws Exception {
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(Paths.get(fileName)))) {
            data.forEach(d -> {
                try {
                    out.writeObject(d);
                } catch (Exception ex) {
                }
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

    protected static void update(List<Base> data, Base newData, String fileName) throws Exception {
        for (int i = 0; i < data.stream().count(); i++) {
            if (data.get(i).getId() == newData.getId()) {
                data.set(i, newData);
                break;
            }
        }
        insert(data, fileName);
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