package bg.sofia.uni.fmi.mjt.shopping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bg.sofia.uni.fmi.mjt.shopping.item.Item;

public class MapShoppingCart implements ShoppingCart {
    private Map<Item, Integer> cart;
    
    public MapShoppingCart() {
        cart = new HashMap<>();
    }

    @Override
    public void addItem(Item item) {
        if (item != null) {
            Integer occurrences = cart.get(item);
            if (occurrences == null) {
                cart.put(item, 1);
            } else {
                cart.put(item, occurrences + 1);
            }
        }
    }
    
    @Override
    public void removeItem(Item item) throws ItemNotFoundException {
        if (!cart.containsKey(item)) {
            throw new ItemNotFoundException();
        }
        int occurrences = cart.get(item);
        if (occurrences == 1) {
            cart.remove(item);
        } else {
            cart.put(item, occurrences - 1);
        }
    }
    
    @Override
    public Collection<Item> getUniqueItems() {
        return cart.keySet();
    }

    @Override
    public Collection<Item> getSortedItems() {
        List<Item> items = new ArrayList<>(cart.keySet());
        Collections.sort(items, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return Integer.compare(cart.get(o2), cart.get(o1));
            }
        });
        return items;
    }

    @Override
    public double getTotal() {
        double total = 0.0;
        for (Item item : cart.keySet()) {
            total += (item.getPrice() * cart.get(item));
        }
        return total;
    }
}
