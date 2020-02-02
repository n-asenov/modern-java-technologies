package bg.sofia.uni.fmi.mjt.api.objects;

import java.io.Serializable;

public class Food implements Serializable {
    private static final long serialVersionUID = 478889938666476851L;

    private long fdcId;
    private String description;
    private String dataType;

    public Food(long fdcId, String description, String dataType) {
        this.fdcId = fdcId;
        this.description = description;
        this.dataType = dataType;
    }

    public long getFdcId() {
        return fdcId;
    }

    public String getDescription() {
        return description;
    }

    public String getDataType() {
        return dataType;
    }
}
