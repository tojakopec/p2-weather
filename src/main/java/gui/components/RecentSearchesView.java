package gui.components;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.VBox;
import models.Location;
import models.SearchEntry;
import utils.RecentSearches;

import java.time.format.DateTimeFormatter;

public class RecentSearchesView extends VBox {

    private RecentSearches recentSearches;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yy h:mm a");

    private final ObjectProperty<Location> reSearchLocation = new SimpleObjectProperty<>();

    public RecentSearchesView(RecentSearches recentSearches) {
        this.recentSearches = recentSearches;
        getStyleClass().add("drawer-menu");
        setPadding(new Insets(80, 10, 10, 10));
        setSpacing(5);

        setAlignment(Pos.TOP_LEFT);
        setPrefWidth(200);
        setMaxWidth(250);

        populateView();
    }

    public void populateView() {
        getChildren().clear();

        Label title = new Label("Recent Searches");
        title.getStyleClass().add("forecast-title-label");
        getChildren().add(title);

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

            itemBox.setOnMouseClicked(event -> {
                reSearchLocation.set(entry.getLocation());
            });

            getChildren().add(itemBox);

        }
    }

    public ObjectProperty<Location> reSearchLocationProperty() {
        return reSearchLocation;
    }
}
