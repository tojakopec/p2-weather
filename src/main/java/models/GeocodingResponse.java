package models;

import java.util.List;

/**
 * A wrapper class that matches the JSON structure of the Geocoding API response.
 * The API returns an object that contains a "results" array. This class
 * is used by Jackson to parse that top-level JSON object.
 */
public class GeocodingResponse {


    private List<Location> results;

    public GeocodingResponse() {}


     // Gets the list of locations found by the Geocoding API.

    public List<Location> getResults() {
        return results;
    }
}
