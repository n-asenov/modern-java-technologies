package bg.sofia.uni.fmi.mjt.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import bg.sofia.uni.fmi.mjt.api.objects.BrandedFood;
import bg.sofia.uni.fmi.mjt.api.objects.Food;
import bg.sofia.uni.fmi.mjt.api.objects.FoodDetails;
import bg.sofia.uni.fmi.mjt.cache.storage.BrandedFoodStorage;
import bg.sofia.uni.fmi.mjt.cache.storage.FoodDetailsStorage;
import bg.sofia.uni.fmi.mjt.cache.storage.FoodStorage;

public class ServerCacheTest {
    private ServerCache serverCache;
    private FoodStorage foodStorage;
    private BrandedFoodStorage brandedFoodStorage;
    private FoodDetailsStorage foodDetailsStorage;

    @Before
    public void initialize() throws ClassNotFoundException, IOException {
        foodStorage = mock(FoodStorage.class);
        brandedFoodStorage = mock(BrandedFoodStorage.class);
        foodDetailsStorage = mock(FoodDetailsStorage.class);
    }

    @Test
    public void testContainsFoodWhenFoodCacheIsEmpty() throws ClassNotFoundException, IOException {
        List<String> foodNameWords = List.of("raffaello");

        when(foodStorage.loadFoodData()).thenReturn(new HashSet<>());

        serverCache = new ServerCache(foodStorage, brandedFoodStorage, foodDetailsStorage);

        boolean result = serverCache.containsFood(foodNameWords);

        assertFalse(result);
    }

    @Test
    public void testContainsFoodWithFoodWhichIsInCache()
            throws ClassNotFoundException, IOException {
        Food food = new Food(1, "raffaello", "");
        List<String> foodNameWords = List.of("raffaello");

        when(foodStorage.loadFoodData()).thenReturn(Set.of(food));

        serverCache = new ServerCache(foodStorage, brandedFoodStorage, foodDetailsStorage);

        boolean result = serverCache.containsFood(foodNameWords);

        assertTrue(result);
    }

    @Test
    public void testContainsFoodWithFoodWithSeveralWordsInName()
            throws ClassNotFoundException, IOException {
        Food food = new Food(1, "RAFFAELLO, ALMOND COCONUT TREAT", "");
        List<String> foodNameWords = List.of("raffaello", "treat");

        when(foodStorage.loadFoodData()).thenReturn(Set.of(food));

        serverCache = new ServerCache(foodStorage, brandedFoodStorage, foodDetailsStorage);

        boolean result = serverCache.containsFood(foodNameWords);

        assertTrue(result);
    }
    
    @Test
    public void testSaveFoodWithFoodObject() throws ClassNotFoundException, IOException {
        Food food = new Food(1, "test", "test");

        when(foodStorage.loadFoodData()).thenReturn(new HashSet<>());

        serverCache = new ServerCache(foodStorage, brandedFoodStorage, foodDetailsStorage);
        serverCache.saveFood(food);
        
        verify(foodStorage).saveObjectData(food);
        
        assertTrue(serverCache.containsFood(List.of(food.getDescription())));
    }

    @Test
    public void testSaveBrandedFoodWithBrandedFoodObject() throws ClassNotFoundException, IOException {
        BrandedFood food = new BrandedFood(1, "", "", "1");
        
        when(brandedFoodStorage.loadBrandedFoodData()).thenReturn(new HashMap<>());
    
        serverCache = new ServerCache(foodStorage, brandedFoodStorage, foodDetailsStorage);
        serverCache.saveBrandedFood(food);
        
        verify(brandedFoodStorage).saveObjectData(food);
        
        assertTrue(serverCache.containsBrandedFood(food.getGtinUpc()));
    }
    
    @Test
    public void testSaveFoodDetailsWithFoodDetailsObject() throws ClassNotFoundException, IOException {
        FoodDetails foodDetails = new FoodDetails("", "", null, 1);
        
        when(foodDetailsStorage.loadFoodDetailsData()).thenReturn(new HashMap<>());
    
        serverCache = new ServerCache(foodStorage, brandedFoodStorage, foodDetailsStorage);
        serverCache.saveFoodDetails(foodDetails);
        
        verify(foodDetailsStorage).saveObjectData(foodDetails);
        
        assertTrue(serverCache.containsFoodDetails(foodDetails.getFdcId()));
    }
    
    @Test
    public void testGetFoodWithSimpleFoodName() throws ClassNotFoundException, IOException {
        Food firstFood = new Food(1, "raffaello", "");
        Food secondFood = new Food(2, "trat", "");
        
        when(foodStorage.loadFoodData()).thenReturn(Set.of(firstFood, secondFood));
        
        serverCache = new ServerCache(foodStorage, brandedFoodStorage, foodDetailsStorage);
        
        List<Food> result = serverCache.getFood(List.of("raffaello"));
        
        assertEquals(1, result.size());
        assertEquals(firstFood.getFdcId(), result.get(0).getFdcId());
    }
    
    
}
