package bg.sofia.uni.fmi.mjt.commands;

import java.io.IOException;
import java.util.List;

import bg.sofia.uni.fmi.mjt.api.FoodDataAPIClient;
import bg.sofia.uni.fmi.mjt.api.InvalidFoodIdException;
import bg.sofia.uni.fmi.mjt.api.NoMatchException;
import bg.sofia.uni.fmi.mjt.api.objects.Food;
import bg.sofia.uni.fmi.mjt.cache.ServerCache;
import bg.sofia.uni.fmi.mjt.commands.exceptions.InternalServerProblemException;
import bg.sofia.uni.fmi.mjt.commands.exceptions.InvalidNumberOfArgumentsException;

public class GetFoodByName implements Command {
    private ServerCache serverCache;
    private FoodDataAPIClient apiClient;

    public GetFoodByName(ServerCache serverCache, FoodDataAPIClient apiClient) {
        this.serverCache = serverCache;
        this.apiClient = apiClient;
    }

    @Override
    public String execute(List<String> arguments) throws InvalidNumberOfArgumentsException,
            InvalidFoodIdException, InternalServerProblemException, NoMatchException {
        validateArguments(arguments);

        if (serverCache.containsFood(arguments)) {
            return getResult(serverCache.getFood(arguments));
        }

        List<Food> foods = makeRequest(arguments);

        saveFoodsInServerCache(foods);

        return getResult(foods);
    }

    private void validateArguments(List<String> arguments)
            throws InvalidNumberOfArgumentsException {
        final int incorrectNumberOfArguments = 0;
        String message = "The command should accept >= 1 arguments";

        if (arguments.size() == incorrectNumberOfArguments) {
            throw new InvalidNumberOfArgumentsException(message);
        }
    }

    private String getResult(List<Food> foods) {
        StringBuilder result = new StringBuilder();

        for (int index = 0; index < foods.size(); index++) {
            result.append(foods.get(index).toString());
            
            if (index != foods.size() - 1) {
                result.append(System.lineSeparator());
            }
        }

        return result.toString();
    }

    private String getFoodName(List<String> arguments) {
        StringBuilder foodName = new StringBuilder();
        String space = "%20";

        for (int index = 0; index < arguments.size(); index++) {
            foodName.append(arguments.get(index));

            if (index != arguments.size() - 1) {
                foodName.append(space);
            }
        }

        return foodName.toString();
    }

    private List<Food> makeRequest(List<String> arguments)
            throws InternalServerProblemException, NoMatchException {
        try {
            return apiClient.searchFood(getFoodName(arguments));
        } catch (IOException | InterruptedException e) {
            String message = "Internal server problem occured";

            throw new InternalServerProblemException(message, e);
        }
    }

    private void saveFoodsInServerCache(List<Food> foods) throws InternalServerProblemException {
        String branded = "Branded";

        try {
            for (Food food : foods) {
                serverCache.saveFood(food);

                if (food.getDataType() == branded) {
                    serverCache.saveBrandedFood(apiClient.getBrandedFood(food.getFdcId()));
                }
            }
        } catch (IOException | InterruptedException | InvalidFoodIdException e) {
            String message = "Internal server problem occured";

            throw new InternalServerProblemException(message, e);
        }
    }

}
