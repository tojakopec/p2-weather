package gui.components;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import models.Location;
import models.SearchEntry;
import utils.RecentSearches;

import java.time.format.DateTimeFormatter;

/**
 * A VBox component that serves as a slide-out "drawer" menu.
 * It displays a list of recently searched locations and allows the user
 * to click on one to re-trigger a search in the MainApp.
 */
public class RecentSearchesView extends VBox {

    private RecentSearches recentSearches;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yy h:mm a");

    // This property is the "output" of this view.
    // When a user clicks a recent item, this property is updated with that item's Location.
    // MainApp listens for changes to this property to trigger a new search.
    private final ObjectProperty<Location> reSearchLocation = new SimpleObjectProperty<>();

    public RecentSearchesView(RecentSearches recentSearches) {
        this.recentSearches = recentSearches;
        getStyleClass().add("drawer-menu");
        setPadding(new Insets(80, 10, 10, 10));
        setSpacing(5);

        setAlignment(Pos.TOP_LEFT);
        setPrefWidth(200);
        setMaxWidth(250);

        // Load the initial list of searches
        populateView();
    }

    /**
     * Clears and rebuilds the list of recent search items.
     * This is called by MainApp (and then the constructor) after a new search is performed to keep the list fresh.
     */
    public void populateView() {
        getChildren().clear();

        Label title = new Label("Recent Searches");
        title.getStyleClass().add("forecast-title-label");
        getChildren().add(title);

        // Loop through the entries from the .ser file
        // The .ser file saves our recent searches
        // So we can use them again even if we close the app
        for (SearchEntry entry : recentSearches.getRecentEntries()){
            VBox itemBox = new VBox(2);
            itemBox.getStyleClass().add("recent-search-item");

            Label locationLabel = new Label(entry.getLocation().getName());
            locationLabel.getStyleClass().add("recent-search-location");

            Label adminLabel = new Label(entry.getLocation().getAdmin1() + ", " + entry.getLocation().getCountry());
            adminLabel.getStyleClass().add("recent-search-admin");

            Label timeLabel = new Label(entry.getTimestamp().format(formatter));
            timeLabel.getStyleClass().add("recent-search-time");

            itemBox.getChildren().addAll(locationLabel, adminLabel, timeLabel);

            // When an item is clicked, update the reSearchLocation property.
            // This fires a listener in MainApp.
            itemBox.setOnMouseClicked(event -> {
                reSearchLocation.set(entry.getLocation());
            });

            getChildren().add(itemBox);

        }
    }

    // Expose the reSearchLocation prop so MainApp can listen for clicks and update the location property
    public ObjectProperty<Location> reSearchLocationProperty() {
        return reSearchLocation;
    }
}
