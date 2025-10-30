package api;

import models.ForecastResponse;
import models.Location;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.lang.System.out;

public class ForecastLookup {
    private HttpClient client;
    private HttpRequest request;
    private final ObjectMapper mapper;
    private String baseUrl = "https://api.open-meteo.com/v1/forecast?latitude=";
    private String longitudeUrl = "&longitude=";


    public ForecastLookup() {
        this.client = HttpClient.newHttpClient();
        this.mapper = JsonMapper.builder()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .build();
    }

    public ForecastResponse getForecast(Location location) {
        try{
            String url = baseUrl + location.getLatitude() + longitudeUrl + location.getLongitude()
                    + "&hourly=temperature_2m,relative_humidity_2m,is_day,weather_code,wind_speed_10m,wind_direction_10m";
            request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return mapper.readValue(response.body(), ForecastResponse.class);
            } else {
                out.println("API request failed with status code: " + response.statusCode());
                out.println("Response Body: " + response.body());
                return null;
            }
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
