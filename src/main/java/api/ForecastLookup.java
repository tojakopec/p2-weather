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

/**
 * Handles all API communication with the Open-Meteo weather service.
 * This class is responsible for building the API request URL based on
 * user settings and parsing the JSON response into the Forecast model.
 */
public class ForecastLookup {
    // The HttpClient for making all web requests.
    private HttpClient client;
    private HttpRequest request;

    // The Jackson ObjectMapper is used to convert JSON strings into Java objects.
    private final ObjectMapper mapper;

    // Base URL for the Open-Meteo API. We add parameters on top of that.
    private String baseUrl = "https://api.open-meteo.com/v1/forecast?latitude=";
    private String longitudeUrl = "&longitude=";
    private final Settings settings;

    /**
     * Constructs the ForecastLookup service.
     * @param settings The shared Settings object, used to get user preferences (e.g., units).
     */
    public ForecastLookup(Settings settings) {
        this.client = HttpClient.newHttpClient();
        this.settings = settings;
        // Configure the mapper to ignore new fields from the API,
        // which prevents the app from crashing if the API is updated.
        // Also, we are not interested in all the fields from the API, so if it returns something we don't evaluate
        // Just ignore it
        this.mapper = JsonMapper.builder()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .build();
    }

    /**
     * Fetches the weather forecast for a specific location.
     * @param location The Location object containing the latitude and longitude.
     * @return A Forecast object populated with all data, or null if the request fails.
     */
    public Forecast getForecast(Location location) {
        try{
            Properties props = settings.getProperties();

            // Dynamic URL Building
            // Assemble the full API URL by combining the base URL, location coordinates,
            // and all the user's saved preferences for units, days, etc.
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

            // Build the HTTP GET request.
            request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            // Send the request and get the response as a string.
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // If the request was successful (HTTP 200),
                // use the Jackson mapper to parse the JSON response body
                // into our Forecast.java object structure.
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
