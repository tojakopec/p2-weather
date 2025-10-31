package models;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A wrapper class that combines a Location with a timestamp.
 * This is the object that is actually saved in the "recent searches" list.
 *
 * Implements Serializable so it can be written to the .ser file by the FileHandler.
 */
public class SearchEntry implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Location location;
    private LocalDateTime timestamp;

    /**
     * Creates a new search entry for a given location that was searched for and selected as result.
     * The timestamp is automatically set to the current time.
     */
    public SearchEntry(Location location) {
        this.location = location;
        this.timestamp = LocalDateTime.now();
    }

    public Location getLocation() {
        return location;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return location.toString() + " (searched at " + timestamp + ")";
    }
}
