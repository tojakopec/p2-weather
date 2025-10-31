package api;

import models.Forecast;
import models.Location;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import utils.Settings;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

import static java.lang.System.out;

public class ForecastLookup {
    private HttpClient client;
    private HttpRequest request;
    private final ObjectMapper mapper;
    private String baseUrl = "https://api.open-meteo.com/v1/forecast?latitude=";
    private String longitudeUrl = "&longitude=";
    private final Settings settings;


    public ForecastLookup(Settings settings) {
        this.client = HttpClient.newHttpClient();
        this.settings = settings;
        this.mapper = JsonMapper.builder()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .build();
    }

    public Forecast getForecast(Location location) {
        try{
            Properties props = settings.getProperties();
            String url = baseUrl + location.getLatitude() + longitudeUrl + location.getLongitude()
                    + "&current=temperature_2m,relative_humidity_2m,apparent_temperature,is_day,weathercode,windspeed_10m,rain,showers"
                    + "&current_units=temperature_2m,windspeed_10m,rain,showers"
                    + "&hourly=temperature_2m,relative_humidity_2m,is_day,weather_code,wind_speed_10m,wind_direction_10m,precipitation_probability,uv_index"
                    + "&daily=weather_code,temperature_2m_max,temperature_2m_min,sunrise,sunset"
                    // API settings
                    + "&timezone=auto"
                    + "&temperature_unit=" + props.getProperty("temperature_unit")
                    + "&wind_speed_unit=" + props.getProperty("wind_speed_unit")
                    + "&precipitation_unit=" + props.getProperty("precipitation_unit")
                    + "&forecast_days=" + props.getProperty("forecast_days");
            request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return mapper.readValue(response.body(), Forecast.class);
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
