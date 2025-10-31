package gui.components;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.Forecast;
import utils.WeatherIconManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

/**
 * A component that displays the daily forecast (e.g., 7-day) in a
 * horizontal list. It is a child component of ForecastView and reacts
 * to changes in the main forecast property.
 * Changing the settings in the app will change the amount of days displayed here
 */
public class DailyForecastView extends VBox {

    // The main forecast data, bound from the parent ForecastView.
    private final ObjectProperty<Forecast> forecast = new SimpleObjectProperty<>();

    // The HBox that holds all the individual day VBoxes.
    private final HBox contentBox = new HBox(15);
    private final Label titleLabel = new Label("7-Day Forecast");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("M/d");

    public DailyForecastView() {
        super(10);
        this.getStyleClass().add("daily-forecast-view"); // New CSS class
        titleLabel.getStyleClass().add("forecast-title-label");
        // Center the title and the scroll pane horizontally within this VBox.
        this.setAlignment(Pos.CENTER);

        ScrollPane scrollPane = new ScrollPane(contentBox);
        // We only want horizontal scrolling if needed, never vertical (for this weather app scenario, at least).
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.getStyleClass().add("forecast-scroll-pane");
        // This forces the contentBox to fill the full width of the scroll pane.
        scrollPane.setFitToWidth(true);

        contentBox.getStyleClass().add("forecast-content-box");
        contentBox.setAlignment(Pos.CENTER);

        // Listen for changes to the forecast property and rebuild the view.
        forecast.addListener((obs, oldF, newF) -> populateView(newF));

        this.getChildren().addAll(titleLabel, scrollPane);
    }

    // Clears and rebuilds the daily forecast items based on new forecast data.
    private void populateView(Forecast f) {
        contentBox.getChildren().clear();
        if (f == null || f.getDaily() == null || f.getDaily().getTime().isEmpty()) {
            titleLabel.setText("No Forecast Available");
            return;
        }

        Forecast.Daily dailyData = f.getDaily();

        // Get all the lists of data from the model.
        List<String> times = dailyData.getTime();
        List<Double> highs = dailyData.getTemperature2mMax();
        List<Double> lows = dailyData.getTemperature2mMin();
        List<Integer> codes = dailyData.getWeatherCode();
        String tempUnit = f.getCurrentTemperatureUnit();

        // Dynamically update the title based on how many days we received/set in the settings.
        titleLabel.setText(times.size() + "-Day Forecast");

        int numDays = times.size();
        for (int i = 0; i < numDays; i++) {
            // VBox for each day
            VBox itemBox = new VBox(5);
            itemBox.getStyleClass().add("forecast-item-box");
            itemBox.setMinHeight(VBox.USE_PREF_SIZE);
            itemBox.setAlignment(Pos.CENTER);

            LocalDate date = LocalDate.parse(times.get(i), DateTimeFormatter.ISO_LOCAL_DATE);

            // Format the data for display
            String day = formatDay(date);
            String dateString = date.format(dateFormatter);
            String tempHigh = String.format("%.0f%s", highs.get(i), tempUnit);
            String tempLow = String.format("%.0f%s", lows.get(i), tempUnit);
            String iconChar = WeatherIconManager.getIconCharacter(codes.get(i));

            // Create Labels
            Label dayLabel = new Label(day);
            dayLabel.getStyleClass().add("forecast-item-day");

            Label dateLabel = new Label(dateString);
            dateLabel.getStyleClass().add("forecast-item-date");

            Label iconLabel = new Label(iconChar);
            iconLabel.getStyleClass().add("forecast-item-icon-large");

            Label highLabel = new Label(tempHigh);
            highLabel.getStyleClass().add("forecast-item-high");

            Label lowLabel = new Label(tempLow);
            lowLabel.getStyleClass().add("forecast-item-low"); // New CSS class

            // Add them all
            itemBox.getChildren().addAll(dayLabel, dateLabel, iconLabel, highLabel, lowLabel);
            contentBox.getChildren().add(itemBox);
        }
    }

    // Helper method to format the date.
    // Converts a LocalDate into "Today" or the short day name (e.g., "Fri").
    private String formatDay(LocalDate date) {
        if (date.equals(LocalDate.now())) {
            return "Today";
        }
        return date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault());
    }

    public ObjectProperty<Forecast> forecastProperty() {
        return forecast;
    }
}