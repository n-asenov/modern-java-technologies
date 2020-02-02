package bg.sofia.uni.fmi.mjt.commands;

import java.util.List;

import bg.sofia.uni.fmi.mjt.cache.ServerCache;
import bg.sofia.uni.fmi.mjt.commands.exceptions.InvalidNumberOfArgumentsException;

public class GetFoodByBarcode implements Command {
    private static String INVALID_BARCODE = "There is no food with such barcode.";
    
    private ServerCache serverCache;
    
    public GetFoodByBarcode(ServerCache serverCache) {
        this.serverCache = serverCache;
    }
    
    @Override
    public String execute(List<String> arguments) throws InvalidNumberOfArgumentsException {
        validateArguments(arguments);
       
        final int index = 0;
        String barcode = arguments.get(index);
        
        if (serverCache.containsBrandedFood(barcode)) {
            return serverCache.getBrandedFood(barcode).toString();
        }

        return INVALID_BARCODE;
    }
    
    private void validateArguments(List<String> arguments) throws InvalidNumberOfArgumentsException {
        final int correctNumberOfArguments = 1;
        String message = "The command accepts only 1 argument";
        
        if (arguments.size() != correctNumberOfArguments) {
            throw new InvalidNumberOfArgumentsException(message);
        }
    }
    
}
