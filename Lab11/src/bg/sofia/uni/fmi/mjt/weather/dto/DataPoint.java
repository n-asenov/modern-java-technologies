package bg.sofia.uni.fmi.mjt.weather.dto;

public class DataPoint {
    private long time;
    private String summary; 
    private double precipProbability;
    private double temperature;
    
    public DataPoint(long time, String summary, double precipProbability, double temperature) {
        this.time = time;
        this.summary = summary;
        this.precipProbability = precipProbability;
        this.temperature = temperature;
    }

    public long getTime() {
        return time;
    }

    public String getSummary() {
        return summary;
    }

    public double getPrecipProbability() {
        return precipProbability;
    }

    public double getTemperature() {
        return temperature;
    }
    
    @Override
    public String toString() {
        return "Time: " + time + System.lineSeparator()
            + "Summary: " + summary + System.lineSeparator()
            + "Precipitation probability: " + precipProbability + System.lineSeparator()
            + "Temperature: " + temperature;
    }
}
