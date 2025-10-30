import java.util.List;

import api.ForecastLookup;
import api.Geocoder;
import models.Location;
import static java.lang.System.*;

public class Main {
    public static void main(String[] args) {
        Geocoder geocoder = new Geocoder();
        List<Location> locations = geocoder.searchLocationsByName("berlin");

        if (!locations.isEmpty()) {
            out.println("Found " + locations.size() + " locations matching the query.");
            for (Location location : locations) {
                out.println(location);
            }
        } else{
            out.println("No locations found matching the query.");
        }


    }

}
