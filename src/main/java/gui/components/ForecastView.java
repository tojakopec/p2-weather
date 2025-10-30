package gui.components;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import models.Forecast;
import models.Location;
import utils.RecentSearches;

public class ForecastView extends VBox {
    public Location selectedLocation;
    public Forecast forecast;

    public ForecastView(Location location) {
        super(5);
        this.selectedLocation = location;
    }

    public ForecastView() {
        super(5);
        this.getChildren().addAll(LocationName());

    }


    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }

    private Label LocationName(){
        Label locationName = new Label("No Location Selected");
        locationName.getStyleClass().add("label-location-name");
        return locationName;
    }

    public Forecast getForecast() {
        return forecast;
    }

    public Location getSelectedLocation() {
        return selectedLocation;
    }
}
