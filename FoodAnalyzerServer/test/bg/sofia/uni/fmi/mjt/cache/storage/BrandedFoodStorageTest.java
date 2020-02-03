package bg.sofia.uni.fmi.mjt.cache.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

import org.junit.Test;

import bg.sofia.uni.fmi.mjt.api.objects.BrandedFood;

public class BrandedFoodStorageTest {

    @Test
    public void testLoadBrandedFoodDataFromEmptyInput() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream emptyInput = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(emptyInput);
        ByteArrayInputStream input = new ByteArrayInputStream(emptyInput.toByteArray());

        BrandedFoodStorage brandedFoodStorage = new BrandedFoodStorage(input, output);

        Map<String, BrandedFood> brandedFoods = brandedFoodStorage.loadBrandedFoodData();

        assertEquals(0, brandedFoods.size());
    }

    @Test
    public void testLoadBrandedFoodDataFromInputWithOneObject()
            throws IOException, ClassNotFoundException {
        ByteArrayOutputStream testInput = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(testInput);
        BrandedFood expectedBrandedFood = new BrandedFood(1, "test", "test", "test");
        output.writeObject(expectedBrandedFood);
        ByteArrayInputStream input = new ByteArrayInputStream(testInput.toByteArray());

        BrandedFoodStorage brandedFoodStorage = new BrandedFoodStorage(input, output);

        Map<String, BrandedFood> brandedFoods = brandedFoodStorage.loadBrandedFoodData();

        assertEquals(1, brandedFoods.size());
        assertTrue(brandedFoods.containsKey(expectedBrandedFood.getGtinUpc()));

        BrandedFood actualBrandedFood = brandedFoods.get(expectedBrandedFood.getGtinUpc());

        assertEquals(expectedBrandedFood.getFdcId(), actualBrandedFood.getFdcId());
        assertEquals(expectedBrandedFood.getDescription(), actualBrandedFood.getDescription());
        assertEquals(expectedBrandedFood.getDataType(), actualBrandedFood.getDataType());
    }

}
