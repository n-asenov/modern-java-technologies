package bg.sofia.uni.fmi.mjt.commands.exceptions;

public class InvalidNumberOfArgumentsException extends Exception {
    private static final long serialVersionUID = -4688894906381988283L;

    public InvalidNumberOfArgumentsException(String message) {
        super(message);
    }
}
