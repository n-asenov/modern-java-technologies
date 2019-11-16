package bg.sofia.uni.fmi.mjt.shopping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import bg.sofia.uni.fmi.mjt.shopping.item.Item;

public class ListShoppingCart implements ShoppingCart {
    private ArrayList<Item> cart;
    
    public ListShoppingCart() {
        cart = new ArrayList<>();
    }
            
    @Override
    public void addItem(Item item) {
        if (item != null) {
            cart.add(item);
        }
    }
    
    @Override
    public void removeItem(Item item) throws ItemNotFoundException {
        if (!cart.remove(item)) {
            throw new ItemNotFoundException();
        }
    }
    
    @Override
    public Collection<Item> getUniqueItems() {
        Set<Item> uniqueItems = new HashSet<>();
        for (Item item : cart) {
            uniqueItems.add(item);
        }
        return uniqueItems;
    }
    
    @Override
    public Collection<Item> getSortedItems() {
        HashMap<Item, Integer> occurances = getItemsOccurances();
        List<Item> uniqueItems = new ArrayList<>(occurances.keySet());
        Collections.sort(uniqueItems, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return Integer.compare(occurances.get(o2), occurances.get(o1));
            }
        });
        return uniqueItems;
    }

    @Override
    public double getTotal() {
        double total = 0.0;
        for (Item item : cart) {
            total += item.getPrice();
        }
        return total;
    }

    private HashMap<Item, Integer> getItemsOccurances() {
        HashMap<Item, Integer> occurances = new HashMap<>();
        for (Item item : cart) {
            if (!occurances.containsKey(item)) {
                occurances.put(item, 1);
            } else {
                occurances.put(item, occurances.get(item) + 1);
            }
        }
        return occurances;
    }
}
