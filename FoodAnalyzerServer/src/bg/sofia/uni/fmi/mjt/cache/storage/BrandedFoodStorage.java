package bg.sofia.uni.fmi.mjt.cache.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import bg.sofia.uni.fmi.mjt.api.objects.BrandedFood;
import bg.sofia.uni.fmi.mjt.commands.exceptions.InternalServerProblemException;

public class BrandedFoodStorage {
	private static final String NEW_LINE = System.lineSeparator();
	private static final Gson GSON = new Gson();

	private File brandedFoodStorage;

	public BrandedFoodStorage(String brandedFoodStorageName) throws InternalServerProblemException {
		brandedFoodStorage = new File(brandedFoodStorageName);
		try {
			brandedFoodStorage.createNewFile();
		} catch (IOException e) {
			throw new InternalServerProblemException("Could not create branded food storage file", e);
		}
	}

	public Map<String, BrandedFood> loadBrandedFoodData() throws InternalServerProblemException {
		Map<String, BrandedFood> brandedFoods = new HashMap<>();

		try (var reader = new BufferedReader(new FileReader(brandedFoodStorage))) {
			String line = reader.readLine();

			while (line != null) {
				BrandedFood brandedFood = GSON.fromJson(line, BrandedFood.class);
				brandedFoods.put(brandedFood.getGtinUpc(), brandedFood);
				line = reader.readLine();
			}
		} catch (IOException e) {
			throw new InternalServerProblemException("Could not load branded food data", e);
		}

		return brandedFoods;
	}

	public void saveBrandedFoodData(List<BrandedFood> brandedFoods) throws InternalServerProblemException {
		try (FileWriter writer = new FileWriter(brandedFoodStorage, true)) {
			for (BrandedFood brandedFood : brandedFoods) {
				writer.write(GSON.toJson(brandedFood));
				writer.write(NEW_LINE);
			}

			writer.flush();
		} catch (IOException e) {
			throw new InternalServerProblemException("Could not save branded food data", e);
		}
	}

}
