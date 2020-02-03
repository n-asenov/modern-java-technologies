package bg.sofia.uni.fmi.mjt.commands;

import java.io.IOException;
import java.util.List;

import bg.sofia.uni.fmi.mjt.api.FoodDataApiClient;
import bg.sofia.uni.fmi.mjt.api.InvalidFoodIdException;
import bg.sofia.uni.fmi.mjt.api.objects.FoodDetails;
import bg.sofia.uni.fmi.mjt.cache.ServerCache;
import bg.sofia.uni.fmi.mjt.commands.exceptions.InternalServerProblemException;
import bg.sofia.uni.fmi.mjt.commands.exceptions.InvalidNumberOfArgumentsException;

public class GetFoodReportById implements Command {
    private ServerCache serverCache;
    private FoodDataApiClient apiClient;

    public GetFoodReportById(ServerCache serverCache, FoodDataApiClient apiClient) {
        this.serverCache = serverCache;
        this.apiClient = apiClient;
    }

    @Override
    public String execute(List<String> arguments) throws InvalidNumberOfArgumentsException,
            InvalidFoodIdException, InternalServerProblemException {
        validateArguments(arguments);

        final int index = 0;
        final long foodId = getFoodId(arguments.get(index));

        if (serverCache.containsFoodDetails(foodId)) {
            return serverCache.getFoodDetails(foodId).toString();
        }

        FoodDetails foodDetails = makeApiRequest(foodId);

        saveFoodDetailsInServerCache(foodDetails);

        return foodDetails.toString();
    }

    private void validateArguments(List<String> arguments)
            throws InvalidNumberOfArgumentsException {
        final int correctNumberOfArguments = 1;
        String message = "The command accepts only 1 argument";

        if (arguments.size() != correctNumberOfArguments) {
            throw new InvalidNumberOfArgumentsException(message);
        }
    }

    private long getFoodId(String argument) throws InvalidFoodIdException {
        try {
            return Long.parseLong(argument);
        } catch (NumberFormatException e) {
            String message = "Food ID must be a number.";

            throw new InvalidFoodIdException(message, e);
        }
    }

    private FoodDetails makeApiRequest(long foodId)
            throws InvalidFoodIdException, InternalServerProblemException {
        try {
            return apiClient.getFoodDetails(foodId);
        } catch (IOException | InterruptedException e) {
            String message = "Internal server problem occured";

            throw new InternalServerProblemException(message, e);
        }
    }

    private void saveFoodDetailsInServerCache(FoodDetails foodDetails)
            throws InternalServerProblemException {
        try {
            serverCache.saveFoodDetails(foodDetails);
        } catch (IOException e) {
            String message = "Internal server problem occured";

            throw new InternalServerProblemException(message, e);
        }
    }

}
