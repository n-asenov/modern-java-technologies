package bg.sofia.uni.fmi.mjt.reader;

public class InvalidBarcodeImageException extends Exception {
    private static final long serialVersionUID = 807157296490909564L;
    
    public InvalidBarcodeImageException(String message) {
        super(message);
    }
    
    public InvalidBarcodeImageException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
