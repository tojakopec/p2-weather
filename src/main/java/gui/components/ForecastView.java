package gui.components;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.Forecast;
import models.Location;

public class ForecastView extends VBox {
    private final ObjectProperty<Location> selectedLocation = new SimpleObjectProperty<>();
    private final ObjectProperty<Forecast> forecast = new SimpleObjectProperty<>();
    private final Label locationNameLabel = new Label();
    private final Label currentTemperatureLabel = new Label();
    private final Label dailyHighLabel = new Label();
    private final Label dailyLowLabel = new Label();
    private final Label windSpeedLabel = new Label();
    private HourlyForecastView hourlyView;
    private DailyForecastView dailyView;


    public ForecastView() {
        super(5);
        this.getStyleClass().add("forecast-view");

        locationNameLabel.getStyleClass().add("label-location-name");
        // Bind the label to automatically change and react to the latest user search
        locationNameLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            Location location = selectedLocation.get();
            return location == null ? "Search for a location to see the weather forecast." : (
                    location.getName() +", " + location.getAdmin1());
        }, selectedLocation));

        currentTemperatureLabel.getStyleClass().add("label-current-temperature");
        currentTemperatureLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            Forecast f = forecast.get();
            return f == null ? "" : "Current: " + f.getFormattedTemperature();
        }, forecast));

        dailyHighLabel.getStyleClass().add("label-daily-high");
        dailyHighLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            Forecast f = forecast.get();
            return f == null ? "" : "High: " + f.getFormattedDailyHigh();
        }, forecast));

        dailyLowLabel.getStyleClass().add("label-daily-low");
        dailyLowLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            Forecast f = forecast.get();
            return f == null ? "" : "Low: " + f.getFormattedDailyLow();
        }, forecast));

        windSpeedLabel.getStyleClass().add("label-wind-speed");
        windSpeedLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            Forecast f = forecast.get();
            return f == null ? "" : f.getFormattedWindSpeed();
        }, forecast));

        HBox dailyHighAndLowBox = new HBox(5, dailyHighLabel, dailyLowLabel);
        dailyHighAndLowBox.getStyleClass().add("daily-highlow-box");

        VBox currentSummaryBox = new VBox(5, locationNameLabel, currentTemperatureLabel, dailyHighAndLowBox);

        ToggleButton hourlyToggle = new ToggleButton("Hourly");
        hourlyToggle.setSelected(true);
        hourlyToggle.getStyleClass().add("view-toggle-button");
        hourlyToggle.setId("hourly-toggle-button");

        ToggleButton dailyToggle = new ToggleButton("Daily");
        dailyToggle.getStyleClass().add("view-toggle-button");
        dailyToggle.setId("daily-toggle-button");

        ToggleGroup viewToggleGroup = new ToggleGroup();
        hourlyToggle.setToggleGroup(viewToggleGroup);
        dailyToggle.setToggleGroup(viewToggleGroup);

        viewToggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
                    if (newToggle == null) {
                        // If the new selection is null (user clicked selected toggle),
                        // re-select the old toggle.
                        viewToggleGroup.selectToggle(oldToggle);
                    }
        });

        HBox toggleBox = new HBox(hourlyToggle, dailyToggle);
        toggleBox.getStyleClass().add("view-toggle-box");
        toggleBox.setAlignment(Pos.CENTER);

        hourlyView = new HourlyForecastView();
        hourlyView.forecastProperty().bind(forecast);

        dailyView = new DailyForecastView();
        dailyView.forecastProperty().bind(forecast);

        hourlyView.visibleProperty().bind(hourlyToggle.selectedProperty());
        hourlyView.managedProperty().bind(hourlyToggle.selectedProperty());

        dailyView.visibleProperty().bind(dailyToggle.selectedProperty());
        dailyView.managedProperty().bind(dailyToggle.selectedProperty());


        this.getChildren().addAll(currentSummaryBox, toggleBox, hourlyView, dailyView);
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
