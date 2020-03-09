package bg.sofia.uni.fmi.mjt.cache.storage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import bg.sofia.uni.fmi.mjt.api.objects.BrandedFood;

public class BrandedFoodStorage extends Storage {

	public BrandedFoodStorage(String brandedFoodStorageName) throws IOException {
		super(brandedFoodStorageName);
	}

	public Map<String, BrandedFood> loadBrandedFoodData() throws IOException {
		Map<String, BrandedFood> brandedFoods = new HashMap<>();

		try (var reader = new BufferedReader(new FileReader(getStorage()))) {
			String line = reader.readLine();

			while (line != null) {
				BrandedFood brandedFood = GSON.fromJson(line, BrandedFood.class);
				brandedFoods.put(brandedFood.getGtinUpc(), brandedFood);
				line = reader.readLine();
			}
		}

		return brandedFoods;
	}

}
