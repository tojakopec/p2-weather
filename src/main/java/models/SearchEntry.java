package models;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public class SearchEntry implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Location location;
    private LocalDateTime timestamp;

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
