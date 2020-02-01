package bg.sofia.uni.fmi.mjt.api.objects;

public class Food {
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
