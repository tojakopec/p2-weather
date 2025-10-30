package gui.components;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.Enums.*;
import utils.Settings;

import java.util.Properties;

public class SettingsWindow extends Stage {
    private Settings settings;
    private ComboBox<TemperatureUnit> tempBox;
    private ComboBox<WindSpeedUnit> windBox;
    private ComboBox<PrecipitationUnit> precipBox;
    private Spinner<Integer> daysSpinner;

    public SettingsWindow(Settings settings, Stage owner) {
        this.settings = settings;
        Properties props = settings.getProperties();

        initModality(Modality.APPLICATION_MODAL);
        initOwner(owner);
        setTitle("Settings");
        setResizable(false);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        // Temperature
        grid.add(new Label("Temperature Unit:"), 0, 0);
        tempBox = new ComboBox<>(FXCollections.observableArrayList(TemperatureUnit.values()));
        tempBox.setValue(TemperatureUnit.valueOf(props.getProperty("temperature_unit")));
        grid.add(tempBox, 1, 0);

        // Wind Speed
        grid.add(new Label("Wind Speed Unit:"), 0, 1);
        windBox = new ComboBox<>(FXCollections.observableArrayList(WindSpeedUnit.values()));
        windBox.setValue(WindSpeedUnit.valueOf(props.getProperty("wind_speed_unit")));
        grid.add(windBox, 1, 1);

        // Precipitation
        grid.add(new Label("Precipitation Unit:"), 0, 2);
        precipBox = new ComboBox<>(FXCollections.observableArrayList(PrecipitationUnit.values()));
        precipBox.setValue(PrecipitationUnit.valueOf(props.getProperty("precipitation_unit")));
        grid.add(precipBox, 1, 2);

        // Forecast Days
        grid.add(new Label("Forecast Days:"), 0, 3);
        daysSpinner = new Spinner<>(1, 16, Integer.parseInt(props.getProperty("forecast_days")));
        grid.add(daysSpinner, 1, 3);

        // Save Button
        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> saveAndClose());

        VBox layout = new VBox(20, grid, saveButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        scene.getStylesheets().add("stylesheets/style.css");
        setScene(scene);


    }

    private void saveAndClose() {
        // Update the settings object
        settings.updateSettings("temperature_unit", tempBox.getValue().toString());
        settings.updateSettings("wind_speed_unit", windBox.getValue().toString());
        settings.updateSettings("precipitation_unit", precipBox.getValue().toString());
        settings.updateSettings("forecast_days", daysSpinner.getValue().toString());

        // The settings are saved to file by updateSettings() -> saveSettings()
        close();
    }
}
