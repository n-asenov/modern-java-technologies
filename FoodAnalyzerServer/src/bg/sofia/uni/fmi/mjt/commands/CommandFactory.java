package bg.sofia.uni.fmi.mjt.commands;

import java.util.HashMap;
import java.util.Map;

import bg.sofia.uni.fmi.mjt.api.FoodDataAPIClient;
import bg.sofia.uni.fmi.mjt.cache.ServerCache;
import bg.sofia.uni.fmi.mjt.commands.exceptions.InvalidCommandException;

public class CommandFactory {
    private Map<String, Command> commands;
    
    public CommandFactory(ServerCache serverCache, FoodDataAPIClient apiClient) {
        commands = new HashMap<>();
        commands.put("get-food", new GetFoodByName(serverCache, apiClient));
        commands.put("get-food-report", new GetFoodReportById(serverCache, apiClient));
        commands.put("get-food-by-barcode", new GetFoodByBarcode(serverCache));
    }
    
    public Command make(String commandName) throws InvalidCommandException {
        Command command = commands.get(commandName);
        
        if (command == null) {
            String message = "The entered command is invalid.";
            
            throw new InvalidCommandException(message);
        }
        
        return command;
    }
}
