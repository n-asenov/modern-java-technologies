package bg.sofia.uni.fmi.mjt.api.objects;

public class BrandedFood extends Food {
    private String gtinUpc;
    
    public BrandedFood(long fdcId, String description, String dataType, String gtinUpc) {
        super(fdcId, description, dataType);
        this.gtinUpc = gtinUpc;
    }

    public String getGtinUpc() {
        return gtinUpc;
    }
}
