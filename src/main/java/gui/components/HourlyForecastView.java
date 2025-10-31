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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HourlyForecastView extends VBox {

    private final ObjectProperty<Forecast> forecast = new SimpleObjectProperty<>();
    private final HBox contentBox = new HBox(15);
    private final Label titleLabel = new Label("Hourly Forecast");
    private final DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private final DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("ha"); // "3PM"

    public HourlyForecastView() {
        super(10);
        this.getStyleClass().add("hourly-forecast-view");
        titleLabel.getStyleClass().add("forecast-title-label");

        this.setAlignment(Pos.CENTER);

        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.getStyleClass().add("forecast-scroll-pane");

        contentBox.getStyleClass().add("forecast-content-box");

        forecast.addListener((obs, oldF, newF) -> populateView(newF));

        this.getChildren().addAll(titleLabel, scrollPane);
    }

    private void populateView(Forecast f) {
        contentBox.getChildren().clear();
        if (f == null || f.getHourly() == null) {
            return;
        }

        boolean firstHourAdded = false;

        Forecast.Hourly hourlyData = f.getHourly();
        List<String> times = hourlyData.getTimes();
        List<Double> temps = hourlyData.getTemperatures();
        List<Integer> codes = hourlyData.getWeatherCodes();
        String tempUnit = f.getCurrentTemperatureUnit();
        List<Integer> isDay = hourlyData.getIsDay();

        int numHours = times.size();
        for (int i = 0; i < numHours; i++) {
            // Only show from current hour onward (optional)
            LocalDateTime time = LocalDateTime.parse(times.get(i), inputFormatter);
            if (time.isBefore(LocalDateTime.now().minusHours(1))) {
                continue;
            }

            VBox itemBox = new VBox(5);
            itemBox.getStyleClass().add("forecast-item-box");
            itemBox.setMinHeight(VBox.USE_PREF_SIZE);
            itemBox.setAlignment(Pos.CENTER);

            String hour;
            if (!firstHourAdded) {
                hour = "Now";
                firstHourAdded = true;
            } else {
                hour = time.format(outputFormatter).toLowerCase();
            }
            String temp = String.format("%.0f%s", temps.get(i), tempUnit);
            String iconChar = WeatherIconManager.getIconCharacter(codes.get(i)); // Using 'isDay'

            Label hourLabel = new Label(hour);
            hourLabel.getStyleClass().add("forecast-item-day"); // re-use style

            Label iconLabel = new Label(iconChar);
            iconLabel.getStyleClass().add("forecast-item-icon-large");

            Label tempLabel = new Label(temp);
            tempLabel.getStyleClass().add("forecast-item-high"); // re-use style

            itemBox.getChildren().addAll(hourLabel, iconLabel, tempLabel);
            contentBox.getChildren().add(itemBox);

            // Limit to just 24 hours
            if (contentBox.getChildren().size() >= 24) {
                break;
            }
        }
    }

    public ObjectProperty<Forecast> forecastProperty() {
        return forecast;
    }
}