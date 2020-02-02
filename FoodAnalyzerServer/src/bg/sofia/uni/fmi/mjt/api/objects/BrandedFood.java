package bg.sofia.uni.fmi.mjt.api.objects;

import java.io.Serializable;

public class BrandedFood extends Food implements Serializable {
    private static final long serialVersionUID = 2274288108138127580L;
    
    private String gtinUpc;
    
    public BrandedFood(long fdcId, String description, String dataType, String gtinUpc) {
        super(fdcId, description, dataType);
        this.gtinUpc = gtinUpc;
    }

    public String getGtinUpc() {
        return gtinUpc;
    }
}
