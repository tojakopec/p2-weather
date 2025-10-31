package gui.components;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import models.Forecast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WeatherDetailsView extends VBox {
    private final ObjectProperty<Forecast> forecast = new SimpleObjectProperty<>();
    private final GridPane grid = new GridPane();
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");

    private final Label feelsLikeValue = new Label("-");
    private final Label humidityValue = new Label("-");
    private final Label precipValue = new Label("-");
    private final Label rainValue = new Label("-");
    private final Label showersValue = new Label("-");
    private final Label uvIndexValue = new Label("-");
    private final Label sunriseValue = new Label("-");
    private final Label sunsetValue = new Label("-");

    public WeatherDetailsView() {
        super(10);
        this.getStyleClass().add("weather-details-view");

        // Set up a grid that's going to hold all the labels and data
        grid.getStyleClass().add("details-grid");
        grid.setHgap(10);
        grid.setVgap(10);

        // Populate it with labels
        grid.add(new Label("Feels Like"), 0, 0);
        grid.add(new Label("Humidity"), 0, 1);
        grid.add(new Label("Precipitation Chance"), 0, 2);
        grid.add(new Label("Rain"), 0, 3);
        grid.add(new Label("Sunrise"), 2, 0);
        grid.add(new Label("Sunset"), 2, 1);
        grid.add(new Label("UV Index"), 2, 2);
        grid.add(new Label("Showers"), 2, 3);

        grid.getChildren().forEach(node -> node.getStyleClass().add("details-label"));

        // These are dynamic labels that will change values as needed
        feelsLikeValue.getStyleClass().add("details-value");
        humidityValue.getStyleClass().add("details-value");
        precipValue.getStyleClass().add("details-value");
        sunriseValue.getStyleClass().add("details-value");
        sunsetValue.getStyleClass().add("details-value");
        uvIndexValue.getStyleClass().add("details-value");
        rainValue.getStyleClass().add("details-value");
        showersValue.getStyleClass().add("details-value");

        grid.add(feelsLikeValue, 1, 0);
        grid.add(humidityValue, 1, 1);
        grid.add(precipValue, 1, 2);
        grid.add(rainValue, 1, 3);
        grid.add(sunriseValue, 3, 0);
        grid.add(sunsetValue, 3, 1);
        grid.add(uvIndexValue, 3, 2);
        grid.add(showersValue, 3, 3);

        // Listen for changes in the forecast
        forecast.addListener((obs, oldF, newF) -> populateView(newF));

        this.getChildren().add(grid);
    }

    private void populateView(Forecast f) {
        if (f == null) {
            // Reset labels if forecast is null
            feelsLikeValue.setText("-");
            humidityValue.setText("-");
            precipValue.setText("-");
            uvIndexValue.setText("-");
            sunriseValue.setText("-");
            sunsetValue.setText("-");
            rainValue.setText("-");
            showersValue.setText("-");
            return;
        }

        String tempUnit = f.getCurrentTemperatureUnit();

        // Get current details of the location, other than just temperatures
        if (f.getCurrentDetails() != null) {
            feelsLikeValue.setText(String.format("%.0f%s", f.getCurrentDetails().getApparentTemperature(), tempUnit));
            humidityValue.setText(f.getCurrentDetails().getRelativeHumidity2m() + "%");
            rainValue.setText(String.format("%.1f%s", f.getCurrentDetails().getRain(), f.getCurrentRainUnit()));
            showersValue.setText(String.format("%.1f%s", f.getCurrentDetails().getShowers(), f.getCurrentShowersUnit()));
        } else {
            feelsLikeValue.setText("-");
            humidityValue.setText("-");
            rainValue.setText("-");
            showersValue.setText("-");
        }

        // Get details for the current hour
        if (f.getHourly() != null) {
            int currentHour = LocalDateTime.now().getHour();
            if (f.getHourly().getPrecipitationProbability() != null && f.getHourly().getPrecipitationProbability().size() > currentHour) {
                precipValue.setText(f.getHourly().getPrecipitationProbability().get(currentHour) + "%");
            } else precipValue.setText("-");
            if (f.getHourly().getUvIndex() != null && f.getHourly().getUvIndex().size() > currentHour) {
                uvIndexValue.setText(String.format("%.1f", f.getHourly().getUvIndex().get(currentHour)));
            } else uvIndexValue.setText("-");
        }

        // Get details for the day
        if (f.getDaily() != null) {
            // Parse time with timezone, then format
            try {
                LocalDateTime sunrise = LocalDateTime.parse(f.getDaily().getSunrise().getFirst());
                LocalDateTime sunset = LocalDateTime.parse(f.getDaily().getSunset().getFirst());

                sunriseValue.setText(sunrise.format(timeFormatter));
                sunsetValue.setText(sunset.format(timeFormatter));
            } catch (Exception e) {
                System.err.println("Could not parse sunrise/sunset: " + e.getMessage());
                sunriseValue.setText("-");
                sunsetValue.setText("-");
            }
        }
    }

    public ObjectProperty<Forecast> forecastProperty() {
        return forecast;
    }
}
