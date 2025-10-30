package gui.components;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import models.Forecast;
import models.Location;

public class ForecastView extends VBox {
    private final ObjectProperty<Location> selectedLocation = new SimpleObjectProperty<>();
    private final ObjectProperty<Forecast> forecast = new SimpleObjectProperty<>();
    private final Label locationNameLabel = new Label();

    public ForecastView() {
        super(5);
        this.getStyleClass().add("forecast-view");

        locationNameLabel.getStyleClass().add("label-location-name");
        // Bind the label to automatically change and react to the latest user search
        locationNameLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            Location location = selectedLocation.get();
            return location == null ? "Search for a location to see the weather forecast." : location.getName();
        }, selectedLocation));

        this.getChildren().add(locationNameLabel);
    }

    public ObjectProperty<Location> selectedLocationProperty() {
        return selectedLocation;
    }

    public ObjectProperty<Forecast> forecastProperty() {
        return forecast;
    }

    public void setForecast(Forecast forecast) {
        this.forecast.set(forecast);
    }

    public Forecast getForecast() {
        return forecast.get();
    }

    public Location getSelectedLocation() {
        return selectedLocation.get();
    }

}
