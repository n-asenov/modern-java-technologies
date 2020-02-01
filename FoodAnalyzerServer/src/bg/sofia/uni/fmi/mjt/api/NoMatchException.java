package bg.sofia.uni.fmi.mjt.api;

public class NoMatchException extends Exception {
    private static final long serialVersionUID = -5904261751033604354L;

    public NoMatchException(String message) {
        super(message);
    }
}
