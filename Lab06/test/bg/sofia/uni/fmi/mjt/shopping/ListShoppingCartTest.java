package bg.sofia.uni.fmi.mjt.shopping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import bg.sofia.uni.fmi.mjt.shopping.item.Apple;
import bg.sofia.uni.fmi.mjt.shopping.item.Chocolate;
import bg.sofia.uni.fmi.mjt.shopping.item.Item;

public class ListShoppingCartTest {
    private static final double DELTA = 0.000001;

    private ShoppingCart shoppingCart;
    private Item apple;
    private Item chocolate;

    @Before
    public void initialise() {
        final double applePrice = 1.20;
        final double chocolatePrice = 2.50;
        shoppingCart = new ListShoppingCart();
        apple = new Apple("apple", "red", applePrice);
        chocolate = new Chocolate("Milka", "Black", chocolatePrice);
    }

    @Test
    public void testGetTotalWithEmptyShoppingCart() {
        assertEquals(0, shoppingCart.getTotal(), DELTA);
    }

    @Test
    public void testGetTotalWithShopingCartWithOneItem() {
        shoppingCart.addItem(chocolate);
        assertEquals(chocolate.getPrice(), shoppingCart.getTotal(), DELTA);
    }

    @Test
    public void testGetTotalWithShoppingCartWithDifferentItems() {
        shoppingCart.addItem(apple);
        shoppingCart.addItem(chocolate);
        double expectedTotal = apple.getPrice() + chocolate.getPrice();
        assertEquals(expectedTotal, shoppingCart.getTotal(), DELTA);
    }

    @Test
    public void testGetTotalWithShoppingCardWithSameItemAddedSeveralTimes() {
        shoppingCart.addItem(apple);
        shoppingCart.addItem(apple);
        double expectedTotal = apple.getPrice() * 2;
        assertEquals(expectedTotal, shoppingCart.getTotal(), DELTA);
    }

    @Test
    public void testAddItemWithNullItem() {
        shoppingCart.addItem(null);
        assertTrue(shoppingCart.getUniqueItems().isEmpty());
        assertTrue(shoppingCart.getSortedItems().isEmpty());
        assertEquals(0, shoppingCart.getTotal(), DELTA);
    }

    @Test
    public void testAddItemWithNewItem() {
        shoppingCart.addItem(apple);
        Collection<Item> uniqueItems = shoppingCart.getUniqueItems();
        Collection<Item> sortedItems = shoppingCart.getSortedItems();
        assertEquals(1, uniqueItems.size());
        assertTrue(uniqueItems.contains(apple));
        assertEquals(1, sortedItems.size());
        assertTrue(sortedItems.contains(apple));
        assertEquals(apple.getPrice(), shoppingCart.getTotal(), DELTA);
    }

    @Test
    public void testAddItemWithAlreadyAddedItem() {
        shoppingCart.addItem(apple);
        shoppingCart.addItem(apple);
        Collection<Item> uniqueItems = shoppingCart.getUniqueItems();
        Collection<Item> sortedItems = shoppingCart.getSortedItems();
        assertEquals(1, uniqueItems.size());
        assertTrue(uniqueItems.contains(apple));
        assertEquals(1, sortedItems.size());
        assertTrue(sortedItems.contains(apple));
        assertEquals(apple.getPrice() * 2, shoppingCart.getTotal(), DELTA);
    }

    @Test(expected = ItemNotFoundException.class)
    public void testRemoveItemWhenItemIsNotInTheShoppingCart() throws ItemNotFoundException {
        shoppingCart.removeItem(apple);
    }

    @Test
    public void testRemoveItemWhenItemIsInTheShoppingCart() throws ItemNotFoundException {
        shoppingCart.addItem(apple);
        shoppingCart.removeItem(apple);
        assertTrue(shoppingCart.getUniqueItems().isEmpty());
        assertTrue(shoppingCart.getSortedItems().isEmpty());
        assertEquals(0, shoppingCart.getTotal(), DELTA);
    }

    @Test
    public void testRemoveItemWhenItemIsSeveralTimesInShoppingCart() throws ItemNotFoundException {
        shoppingCart.addItem(apple);
        shoppingCart.addItem(apple);
        shoppingCart.removeItem(apple);
        assertTrue(shoppingCart.getUniqueItems().contains(apple));
        assertTrue(shoppingCart.getSortedItems().contains(apple));
        assertEquals(apple.getPrice(), shoppingCart.getTotal(), DELTA);
    }

    @Test
    public void testGetUniqueItemsWhenShoppingCartIsEmpty() {
        assertTrue(shoppingCart.getUniqueItems().isEmpty());
    }

    @Test
    public void testGetUniqueItemsWhenShoppingCartDoesNotHaveDuplicates() {
        shoppingCart.addItem(apple);
        shoppingCart.addItem(chocolate);
        Collection<Item> uniqueItems = shoppingCart.getUniqueItems();
        assertEquals(2, uniqueItems.size());
        assertTrue(uniqueItems.contains(apple));
        assertTrue(uniqueItems.contains(chocolate));
    }

    @Test
    public void testGetUniqueItemsWhenShoppingCartHasDuplicateItems() {
        shoppingCart.addItem(apple);
        shoppingCart.addItem(apple);
        Collection<Item> uniqueItems = shoppingCart.getUniqueItems();
        assertEquals(1, uniqueItems.size());
        assertTrue(uniqueItems.contains(apple));
    }

    @Test
    public void testGetSortedItemsWhenShoppingCartIsEmpty() {
        assertTrue(shoppingCart.getSortedItems().isEmpty());
    }

    @Test
    public void testGetSortedItemsWhenShoppingCartHasItemsWithDifferentCount() {
        shoppingCart.addItem(apple);
        shoppingCart.addItem(chocolate);
        shoppingCart.addItem(chocolate);
        Collection<Item> sortedItems = shoppingCart.getSortedItems();
        assertEquals(2, sortedItems.size());
        Iterator<Item> iterator = sortedItems.iterator();
        assertEquals(chocolate, iterator.next());
        assertEquals(apple, iterator.next());
    }

    @Test
    public void testGetSortedWhenShoppingCartHasItemsWithSameCount() {
        shoppingCart.addItem(apple);
        shoppingCart.addItem(chocolate);
        Collection<Item> sortedItems = shoppingCart.getSortedItems();
        assertEquals(2, sortedItems.size());
        Iterator<Item> iterator = sortedItems.iterator();
        assertEquals(chocolate, iterator.next());
        assertEquals(apple, iterator.next());
    }

}
