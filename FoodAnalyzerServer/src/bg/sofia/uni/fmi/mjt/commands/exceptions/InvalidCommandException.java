package bg.sofia.uni.fmi.mjt.commands.exceptions;

public class InvalidCommandException extends Exception {
    private static final long serialVersionUID = -6863436597234069373L;
    
    public InvalidCommandException(String message) {
        super(message);
    }
}
