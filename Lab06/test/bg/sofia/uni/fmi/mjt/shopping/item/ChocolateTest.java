package bg.sofia.uni.fmi.mjt.shopping.item;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ChocolateTest {
    private Chocolate chocolate;

    @Before
    public void initialise() {
        final double chocolatePrice = 2.50;
        chocolate = new Chocolate("Milka", "Black", chocolatePrice);
    }

    @Test
    public void testEqualsWithSameObjects() {
        Chocolate sameChocolate = new Chocolate(chocolate.getName(), chocolate.getDescription(),
                chocolate.getPrice());
        assertTrue(chocolate.equals(sameChocolate));
    }
    
    @Test
    public void testEqualsWithObjectsWithDifferentName() {
        Chocolate differentApple = new Chocolate(chocolate.getName() + "1", chocolate.getDescription(),
                chocolate.getPrice());
        assertFalse(chocolate.equals(differentApple));
    }

    @Test
    public void testEqualsWithObjectsWithDifferentDescription() {
        Chocolate differentApple = new Chocolate(chocolate.getName(), chocolate.getDescription() + ".",
                chocolate.getPrice());
        assertFalse(chocolate.equals(differentApple));
    }

    @Test
    public void testEqualsWithObjectsWithDifferentPrice() {
        Chocolate differentApple = new Chocolate(chocolate.getName(), chocolate.getDescription(),
                chocolate.getPrice() + 1);
        assertFalse(chocolate.equals(differentApple));
    }
}
