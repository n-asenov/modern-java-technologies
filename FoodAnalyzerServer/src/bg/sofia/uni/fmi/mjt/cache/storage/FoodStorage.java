package bg.sofia.uni.fmi.mjt.cache.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;

import bg.sofia.uni.fmi.mjt.api.objects.Food;
import bg.sofia.uni.fmi.mjt.commands.exceptions.InternalServerProblemException;

public class FoodStorage {
	private static final String NEW_LINE = System.lineSeparator();
	private static final Gson GSON = new Gson();

	private File foodStorage;

	public FoodStorage(String foodStorageName) throws InternalServerProblemException {
		foodStorage = new File(foodStorageName);
		
		try {
			foodStorage.createNewFile();
		} catch (IOException e) {
			throw new InternalServerProblemException("Could not create food storage file", e);
		}
	}

	public Set<Food> loadFoodData() throws InternalServerProblemException {
		Set<Food> foods = new HashSet<>();

		try (var reader = new BufferedReader(new FileReader(foodStorage))) {
			String line = reader.readLine();

			while (line != null) {
				Food food = GSON.fromJson(line, Food.class);
				foods.add(food);
				line = reader.readLine();
			}
		} catch (IOException e) {
			throw new InternalServerProblemException("Could not load food storage", e);
		}

		return foods;
	}
	
	public void saveFoodData(List<Food> foods) throws InternalServerProblemException {
		try (FileWriter writer = new FileWriter(foodStorage, true)) {
			for (Food food : foods) {
				writer.write(GSON.toJson(food));
				writer.write(NEW_LINE);
			}
			
			writer.flush();
		} catch (IOException e) {
			throw new InternalServerProblemException("Could not save food data", e);
		} 
	}
}
