package bg.sofia.uni.fmi.mjt.weather.dto;

public class WeatherForecast {
    private double latitude;
    private double longitude;
    private String timezone;
    private DataPoint currently;
    private DataBlock hourly;
    private DataBlock daily;
    
    public WeatherForecast(double latitude, double longitude, String timezone) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timezone = timezone;
    }

    public double getLatitude() {
        return latitude;
    }
    
    public double getLongitude() {
        return longitude;
    }
    
    public String getTimezone() {
        return timezone;
    }
    
    public DataPoint getCurrently() {
        return currently;
    }

    public DataBlock getHourly() {
        return hourly;
    }

    public DataBlock getDaily() {
        return daily;
    }
    
    @Override
    public String toString() {
        return "Latitude: " + latitude + System.lineSeparator() 
            + "Longitude: " + longitude + System.lineSeparator() 
            + "Timezone: " + timezone + System.lineSeparator() 
            + "Currently: " + currently.toString() + System.lineSeparator()
            + "Hourly: " + hourly.toString() + System.lineSeparator() 
            + "Daily: " + daily.toString();
    }
}
