package com.libraryApp.DAL;

import com.libraryApp.Model.Base;
import com.libraryApp.Model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO extends Database {
    public UserDAO() {
        super();
    }

    public static User getLoggedInUser() throws Exception {
        List<Base> users = loadBase(LOGGED_IN_USER_FILE);

        if (!users.isEmpty()) return (User) users.get(0);
        throw new Exception("No logged in user found");
    }

    public static void login(User userInput) throws Exception {
        if (userInput.getUsername() == null || userInput.getUsername().isEmpty())
            throw new Exception("User not filled in");
        if (userInput.getPassword() == null || userInput.getPassword().isEmpty())
            throw new Exception("Password not filled in");

        Optional<User> user = loadUsers().stream().filter(u -> u.getUsername().equals(userInput.getUsername())).findFirst();

        if (!user.isPresent()) throw new Exception("User does not exist");
        else if (!user.get().getPassword().equals(userInput.getPassword()))
            throw new Exception("Invalid username/password combination");


        List<Base> loggedInUser = new ArrayList<>();
        loggedInUser.add(user.get());

        insert(loggedInUser, LOGGED_IN_USER_FILE);
    }

    public static List<User> loadUsers() throws Exception {
        List<User> users = new ArrayList<User>();

        loadBase(USERS_FILE).forEach((object) -> users.add((User) object));

        return users;
    }

    public static void updateUser(List<User> data, User newData) throws Exception {
        List<Base> dataBase = new ArrayList<>(data);

        update(dataBase, newData, USERS_FILE);
    }

    public static void deleteUser(List<User> data, int id) throws Exception {
        List<Base> dataBase = new ArrayList<>(data);

        delete(dataBase, id, USERS_FILE);
    }

    public static void insertUserWithoutId(List<User> data, User newData) throws Exception {
        List<Base> dataBase = new ArrayList<>(data);

        insertNewWithoutId(dataBase, newData, USERS_FILE);
    }
}
