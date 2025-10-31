package gui.components;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import models.Forecast;
import models.Location;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents the main weather display part.
 * This VBox organizes the current summary, the hourly/daily toggles,
 * and all child views (Hourly, Daily, Details) for a selected location.
 * It uses JavaFX properties and bindings to update reactively.
 */
public class ForecastView extends VBox {
    // The core "input" properties. MainApp sets these, and all internal bindings react.
    private final ObjectProperty<Location> selectedLocation = new SimpleObjectProperty<>();
    private final ObjectProperty<Forecast> forecast = new SimpleObjectProperty<>();

    // Parts for the current summary box at the top of the forecast view
    private final Label locationNameLabel = new Label();
    private final Label locationTimeLabel = new Label();
    private final Label currentTemperatureLabel = new Label();
    private final Label dailyHighLabel = new Label();
    private final Label dailyLowLabel = new Label();
    private final Label windSpeedLabel = new Label();

    // Helpers for the clock. Clock is location-specific.
    private Timeline clockTimeline;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");

    private HourlyForecastView hourlyView;
    private DailyForecastView dailyView;
    private WeatherDetailsView detailsView;


    public ForecastView() {
        super(5);
        this.getStyleClass().add("forecast-view");

        locationNameLabel.getStyleClass().add("label-location-name");
        locationTimeLabel.getStyleClass().add("label-location-time");

        // Listen for changes to the forecast object to start/stop the local clock.
        forecast.addListener((obs, oldF, newF) -> {
            if (newF != null && newF.getTimezone() != null) {
                startLocationClock(newF.getTimezone());
            } else {
                stopLocationClock();
            }
        });

        // All labels are bound to properties. They will update automatically
        // when the selectedLocation or forecast properties change,
        // without needing manual setters.
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

        Label windIcon = new Label("\uf021");
        windIcon.getStyleClass().add("wind-icon");
        // Manual pixel nudge to visually align the icon font with the Arial text font.
        windIcon.setTranslateY(-2);

        HBox windBox = new HBox(5, windIcon, windSpeedLabel);
        windBox.setAlignment(Pos.CENTER);

        HBox dailyHighAndLowBox = new HBox(5, dailyHighLabel, dailyLowLabel);
        dailyHighAndLowBox.getStyleClass().add("daily-highlow-box");
        dailyHighAndLowBox.setAlignment(Pos.CENTER);


        VBox currentSummaryBox = new VBox(5, locationNameLabel, locationTimeLabel, currentTemperatureLabel, dailyHighAndLowBox, windBox);
        currentSummaryBox.setAlignment(Pos.CENTER);

        // Toggler to alternate between hourly and daily view
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

        // Prevents the user from de-selecting a toggle (which would hide both views).
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

        // Bind the visibility of the child views to the toggle button's selected state.
        hourlyView.visibleProperty().bind(hourlyToggle.selectedProperty());
        hourlyView.managedProperty().bind(hourlyToggle.selectedProperty());

        dailyView.visibleProperty().bind(dailyToggle.selectedProperty());
        dailyView.managedProperty().bind(dailyToggle.selectedProperty());

        // The "details" box that lives at the bottom
        detailsView = new WeatherDetailsView();
        detailsView.forecastProperty().bind(this.forecast);

        // Add all major sections to this VBox
        this.getChildren().addAll(currentSummaryBox, toggleBox, hourlyView, dailyView, detailsView);
    }


    // Exposes the selectedLocation property so MainApp can bind to it.

    public ObjectProperty<Location> selectedLocationProperty() {
        return selectedLocation;
    }

    // Exposes the forecast property so children can bind to it.
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

    // Starts a new Timeline-based clock for the specific timezone.
    // Updates locationTimeLabel every second
    private void startLocationClock(String timezoneId) {
        stopLocationClock(); // Stop any existing clock

        try {
            ZoneId zoneId = ZoneId.of(timezoneId);

            // Create a Timeline that runs every second
            clockTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                ZonedDateTime now = ZonedDateTime.now(zoneId);
                locationTimeLabel.setText(now.format(timeFormatter));
            }));

            clockTimeline.setCycleCount(Timeline.INDEFINITE);
            clockTimeline.play();
        } catch (Exception e) {
            System.err.println("Could not parse timezone: " + timezoneId);
            locationTimeLabel.setText("Invalid Timezone");
        }
    }

    // Stops and clears the clock timeline
    private void stopLocationClock() {
        if (clockTimeline != null) {
            clockTimeline.stop();
        }
        locationTimeLabel.setText("");
    }

}
