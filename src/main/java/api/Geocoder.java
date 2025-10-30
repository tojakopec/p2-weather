package api;/*
    This class will help us find the right "target" for the weather forecast search.
    A lot of places with the same name exist, so we have to select the right one.
    Uses the open-meteo.com Geocoding API to return a list of locations that match the query.

    This is also comes in handy as the returned locations contain the latitude and longitude,
    which are necessary to query the weather API for a weather forecast.
 */
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import models.GeocodingResponse;
import models.Location;

/*
    The Jackson library helps with handling (serializing/deserializing) JSON files,
    and both open-meteo APIs we will use return JSON as their response.
 */
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.json.JsonMapper;


import static java.lang.System.out;

public class Geocoder {
    private HttpClient client;
    private HttpRequest request;
    private String numberOfResults = "5";
    private final ObjectMapper mapper;
    private String baseUrl = "https://geocoding-api.open-meteo.com/v1/search?name=";

    public Geocoder() {
        this.client = HttpClient.newHttpClient();
        // keep deserializing even if the fields in JSON and our Location class don't fully match
        this.mapper = JsonMapper.builder()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .build();
    }

    public List<Location> searchLocationsByName(String query){

        try{
            // encode the query to ensure proper charset is used
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);

            // the query is the only mandatory parameter, the api documentation mentions several optional parameters
            // like count, format, language, open-meteo apikey (not needed for non-commercial use) etc.
            String url = baseUrl + encodedQuery + "&count=" + numberOfResults;

            // build the Http request with the GET method (means we're trying to collect a resource)
            request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            // then query the endpoint and handle the response as a String
           HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

           // response code of "200 OK" indicates that everything went well with the http request & response
           if (response.statusCode() == 200) {

               // put the results into a GeocodingResponse helper class
               GeocodingResponse geocodingResponse = mapper.readValue(response.body(), GeocodingResponse.class);

               if (geocodingResponse != null && geocodingResponse.getResults() != null){
                   return geocodingResponse.getResults();
               } else {
                   out.println("No results found for query: " + query);
                   return Collections.emptyList();
               }
           } else {
               out.println("API request failed with status code: " + response.statusCode());
               out.println("Response Body: " + response.body());
               return Collections.emptyList();
           }

        } catch (IOException | InterruptedException e){
            out.println("Something went wrong while searching for locations: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        } catch (tools.jackson.core.exc.JacksonIOException e){
            out.println("Something went wrong while parsing the response body.");
            return Collections.emptyList();
        }

    }

}
