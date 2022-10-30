package com.libraryApp.DAL;

import com.libraryApp.Model.Base;
import com.libraryApp.Model.Item;
import com.libraryApp.Model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public static void receiveItem(int itemId) throws Exception {
        List<Item> items = loadItems();

        Item item = items.stream().filter(i -> i.getId() == itemId).findFirst().orElse(null);

        if (item == null) throw new Exception("Item does not exist");
        if (item.available) throw new Exception("Item is already received");

        LocalDateTime lendDate = item.lendDate;

        item.receive();

        List<Base> itemsInput = new ArrayList<>();
        for (Item i : items) itemsInput.add(i);

        update(itemsInput, item, ITEMS_FILE);

        if (lendDate.isBefore(LocalDateTime.now().minusDays(21)))
            throw new IllegalArgumentException("Item is received too late, but item is still received");
    }

    public static List<Item> loadItems() throws Exception {
        List<Item> items = new ArrayList<Item>();

        loadBase(ITEMS_FILE).forEach((object) -> items.add((Item) object));

        return items;
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
