package bg.sofia.uni.fmi.mjt.shopping.item;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class AppleTest {
    private Apple apple;

    @Before
    public void initialise() {
        final double applePrice = 1.20;
        apple = new Apple("apple", "red", applePrice);
    }

    @Test
    public void testEqualsWithEqualObjects() {
        Apple sameApple = new Apple(apple.getName(), apple.getDescription(), apple.getPrice());
        assertTrue(apple.equals(sameApple));
    }

    @Test
    public void testEqualsWithObjectsWithDifferentName() {
        Apple differentApple = new Apple(apple.getName() + "1", apple.getDescription(),
                apple.getPrice());
        assertFalse(apple.equals(differentApple));
    }

    @Test
    public void testEqualsWithObjectsWithDifferentDescription() {
        Apple differentApple = new Apple(apple.getName(), apple.getDescription() + ".",
                apple.getPrice());
        assertFalse(apple.equals(differentApple));
    }

    @Test
    public void testEqualsWithObjectsWithDifferentPrice() {
        Apple differentApple = new Apple(apple.getName(), apple.getDescription(),
                apple.getPrice() + 1);
        assertFalse(apple.equals(differentApple));
    }
}
