package bg.sofia.uni.fmi.mjt.shopping;

public class ItemNotFoundException extends Exception {
    private static final long serialVersionUID = -5133516179074956716L;
    
    public ItemNotFoundException() {
        super("Item does not exist");
    }
}
