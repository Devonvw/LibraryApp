package com.libraryApp.DAL;

import com.libraryApp.Model.Base;
import com.libraryApp.Model.Item;
import com.libraryApp.Model.ToLateException;
import com.libraryApp.Model.User;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemDAO extends Database {
    public static void lendItem(int itemId, int memberId) throws Exception {
        List<Item> items = loadItems();

        Item item = items.stream().filter(i -> i.getId() == itemId).findFirst().orElse(null);

        User user = UserDAO.loadUsers().stream().filter(i -> i.getId() == memberId).findFirst().orElse(null);

        if (item == null) throw new Exception("Item does not exist");
        if (user == null) throw new Exception("User does not exist");
        if (!item.available) throw new Exception("Item is already lent");

        item.lend(user);

        List<Base> itemsInput = new ArrayList<>();
        for (Item i : items) itemsInput.add(i);

        update(itemsInput, item, ITEMS_FILE);
    }

    public static void receiveItem(int itemId, boolean isLate) throws Exception {
        List<Item> items = loadItems();

        Item item = items.stream().filter(i -> i.getId() == itemId).findFirst().orElse(null);

        if (item == null) throw new Exception("Item does not exist");
        if (item.getAvailable()) throw new Exception("Item is already received");
        if (!isLate && item.getLendDate().isBefore(LocalDateTime.now().minusDays(21))) {
            int daysLate = (Period.between(item.getLendDate().toLocalDate(), LocalDateTime.now().minusDays(21).toLocalDate())).getDays();
            throw new ToLateException("Item is late by: " + daysLate + " days" + "\nTotal fine: €" + String.format("%.2f",daysLate * 0.1));
        }

        item.receive();

        List<Base> itemsInput = new ArrayList<>();
        for (Item i : items) itemsInput.add(i);

        update(itemsInput, item, ITEMS_FILE);
    }

    public static List<Item> loadItems() throws Exception {
        List<Item> items = new ArrayList<Item>();

        loadBase(ITEMS_FILE).forEach((object) -> items.add((Item) object));

        return items;
    }

    public static void insertItem(List<Item> newData) throws Exception {
        List<Base> dataBase = new ArrayList<>(loadItems());
        List<Integer> ids = dataBase.stream().map(item -> item.getId()).toList();

        for (Item item: newData) {
             if (ids.contains(item.getId())) throw new Exception("Item id " + item.getId() + " already exists!");
        };

        dataBase.addAll(newData);

        insert(dataBase, ITEMS_FILE);
    }

    public static void updateItem(List<Item> data, Item newData) throws Exception {
        List<Base> dataBase = new ArrayList<>(data);

        update(dataBase, newData, ITEMS_FILE);
    }

    public static void deleteItem(List<Item> data, int id) throws Exception {
        List<Base> dataBase = new ArrayList<>(data);

        delete(dataBase, id, ITEMS_FILE);
    }

    public static void insertItemWithoutId(List<Item> data, Item newData) throws Exception {
        List<Base> dataBase = new ArrayList<>(data);

        insertNewWithoutId(dataBase, newData, ITEMS_FILE);
    }
}
