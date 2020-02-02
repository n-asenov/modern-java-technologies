package bg.sofia.uni.fmi.mjt.commands;

import java.util.List;

import bg.sofia.uni.fmi.mjt.commands.exceptions.InvalidNumberOfArgumentsException;

public interface Command {
    
    String execute(List<String> arguments) throws InvalidNumberOfArgumentsException;
    
}
