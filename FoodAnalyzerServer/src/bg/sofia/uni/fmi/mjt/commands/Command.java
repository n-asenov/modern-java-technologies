package bg.sofia.uni.fmi.mjt.commands;

import java.util.List;

import bg.sofia.uni.fmi.mjt.api.InvalidFoodIdException;
import bg.sofia.uni.fmi.mjt.api.NoMatchException;
import bg.sofia.uni.fmi.mjt.commands.exceptions.InternalServerProblemException;
import bg.sofia.uni.fmi.mjt.commands.exceptions.InvalidNumberOfArgumentsException;

public interface Command {

    String execute(List<String> arguments) throws InvalidNumberOfArgumentsException,
            InvalidFoodIdException, InternalServerProblemException, NoMatchException;

}
