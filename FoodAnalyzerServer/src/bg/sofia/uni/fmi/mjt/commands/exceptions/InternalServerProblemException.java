package bg.sofia.uni.fmi.mjt.commands.exceptions;

public class InternalServerProblemException extends Exception {
    private static final long serialVersionUID = -7443042438571947621L;
       
    public InternalServerProblemException(String message) {
        super(message);
    }
    
    public InternalServerProblemException(String message, Throwable cause) {
        super(message, cause);
    }
}
