package bg.sofia.uni.fmi.mjt.weather;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Collection;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import bg.sofia.uni.fmi.mjt.weather.dto.DataPoint;
import bg.sofia.uni.fmi.mjt.weather.dto.Geocode;
import bg.sofia.uni.fmi.mjt.weather.dto.WeatherForecast;

public class WeatherForecastClient {
    private final static int MOST_RELEVANT = 0;

    private static Gson gson = new Gson();

    private HttpClient client;
    private String secretKey;
    private String token;
    
    /**
     * 
     * @param client
     * @param secretKey Secret key for Dark Sky API
     * @param token Token for LocationIQ
     */
    public WeatherForecastClient(HttpClient client, String secretKey, String token) {
        this.client = client;
        this.secretKey = secretKey;
        this.token = token;
    }
    
    /**
     * Fetches the weather forecast for the specified location.
     * 
     * @return the forecast
     * @throws InterruptedException
     * @throws IOException
     */
    public WeatherForecast getForecast(String location) throws IOException, InterruptedException {
        location = parseLocation(location);

        Geocode geocode = getGeocodeOfLocation(location);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        getURIForWeatherForecast(geocode.getLat(), geocode.getLon())))
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        
        return gson.fromJson(response.body(), WeatherForecast.class);
    }

    /**
     * Fetches the current weather for the specified location.
     * 
     * @return the current weather
     * @throws InterruptedException 
     * @throws IOException 
     */
    public DataPoint getCurrent(String location) throws IOException, InterruptedException {
        return getForecast(location).getCurrently();
    }

    /**
     * Fetches the hourly weather forecast
     * 
     * @return the hourly weather forecast
     * @throws InterruptedException 
     * @throws IOException 
     */
    public Collection<DataPoint> getHourlyForecast(String location) throws IOException, InterruptedException {
        return getForecast(location).getHourly().getData();
    }

    /**
     * Fetches the weekly weather forecast
     * 
     * @return the weekly weather forecast
     * @throws InterruptedException 
     * @throws IOException 
     */
    public Collection<DataPoint> getWeeklyForecast(String location) throws IOException, InterruptedException {
        return getForecast(location).getDaily().getData();
    }

    private String parseLocation(String location) {
        return location.strip().replaceAll(" ", "%20");
    }

    private Geocode getGeocodeOfLocation(String location) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(getURIForLocation(location)))
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        Type type = new TypeToken<List<Geocode>>() {
        }.getType();

        List<Geocode> results = gson.fromJson(response.body(), type);
        
        return results.get(MOST_RELEVANT);
    }

    private String getURIForLocation(String location) {
        String scheme = "https://";
        String host = "eu1.locationiq.com";
        String path = "/v1/search.php";
        String query = "?key=" + token + "&q=" + location + "&format=json";

        return scheme + host + path + query;
    }

    private String getURIForWeatherForecast(double latitude, double longitude) {
        String scheme = "https://";
        String host = "api.darksky.net";
        String path = "/forecast/" + secretKey + "/" + latitude + "," + longitude;
        String query = "?units=si&lang=bg";

        return scheme + host + path + query;
    }
}
