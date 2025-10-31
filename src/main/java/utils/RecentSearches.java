package utils;

import models.Location;
import models.SearchEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * A utility class that manages the list of recent user searches.
 * It handles loading the list from a file (deserialization) on startup
 * and saving the updated list back to the file (serialization) after each new search.
 */
public class RecentSearches {
    private static final String RECENT_SEARCHES_FILE = "recent_searches.ser";
    private List<SearchEntry> recentEntries;

    /**
     * On instantiation, this class immediately tries to load the existing search list
     * from the .ser file. If no file is found (e.g., first launch),
     * it initializes an empty list.
     */
    public RecentSearches() {
        recentEntries = FileHandler.readFromFile(RECENT_SEARCHES_FILE);
        if (recentEntries == null) {
            recentEntries = new ArrayList<>();
        }
    }

    // Adds a new location to the recent searches list, saves the list and manages its size
    public void addLocation(Location location) {
        // Removes duplicate locations
        recentEntries.removeIf(entry -> entry.getLocation().toString().equals(location.toString()));

        // The newly added location goes to index 0
        recentEntries.addFirst(new SearchEntry(location));

        // Save only up to 10 recent locations
        if (recentEntries.size() > 10) {
            recentEntries = recentEntries.subList(0, 10);
        }

        FileHandler.writeToFile(RECENT_SEARCHES_FILE, recentEntries);
        System.out.println("Recent searches updated: " + recentEntries);
    }

    // Returns the list of recent entries
    public List<SearchEntry> getRecentEntries() {
        return recentEntries;
    }

    // Get the latest viewed location. Used by MainApp to load a location on startup.
    public static Location getLatestLocation(){
        List<SearchEntry> recentEntries = FileHandler.readFromFile(RECENT_SEARCHES_FILE);
        if (recentEntries == null || recentEntries.isEmpty()) return null;
        return recentEntries.getFirst().getLocation();
    }
}
