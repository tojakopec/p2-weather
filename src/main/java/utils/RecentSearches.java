package utils;

import models.Location;
import models.SearchEntry;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RecentSearches {
    private static final String RECENT_SEARCHES_FILE = "recent_searches.ser";
    private List<SearchEntry> recentEntries;
    
    public RecentSearches() {
        recentEntries = FileHandler.readFromFile(RECENT_SEARCHES_FILE);
        if (recentEntries == null) {
            recentEntries = new ArrayList<>();
        }
    }

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

    public List<SearchEntry> getRecentEntries() {
        return recentEntries;
    }
}
