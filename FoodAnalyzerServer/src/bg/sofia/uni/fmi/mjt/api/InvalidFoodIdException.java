package bg.sofia.uni.fmi.mjt.api;

public class InvalidFoodIdException extends Exception {
    private static final long serialVersionUID = -1080272853462970043L;
    
    public InvalidFoodIdException(String message) {
        super(message);
    }
}
