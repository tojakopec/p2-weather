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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.Enums.*;
import utils.Settings;

import java.util.Properties;

/**
 * A pop-up window (Stage) for editing application settings.
 * This window is a "modal," meaning it blocks interaction with the main
 * application until it is closed.
 * It reads the current settings from a Settings object and saves them back on request.
 * Also saves the settings to the settings file so they persist if we close/reopen the app.
 */
public class SettingsWindow extends Stage {
    private Settings settings;
    private ComboBox<TemperatureUnit> tempBox;
    private ComboBox<WindSpeedUnit> windBox;
    private ComboBox<PrecipitationUnit> precipBox;
    private Spinner<Integer> daysSpinner;

    public SettingsWindow(Settings settings, Stage owner) {
        this.settings = settings;
        Properties props = settings.getProperties();

        // Makes this window a blocking pop-up (modal)
        initModality(Modality.APPLICATION_MODAL);
        initOwner(owner);
        setTitle("Settings");
        setResizable(false);

        // Using grid to neatly organize the items
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        // Temperature settings labels
        Label tempLabel = new Label("Temperature Units:");
        tempLabel.getStyleClass().add("settings-label");
        grid.add(tempLabel, 0, 0);
        tempBox = new ComboBox<>(FXCollections.observableArrayList(TemperatureUnit.values()));
        tempBox.setValue(TemperatureUnit.valueOf(props.getProperty("temperature_unit")));
        grid.add(tempBox, 1, 0);

        // Wind Speed
        Label windLabel = new Label("Wind Speed Units:");
        windLabel.getStyleClass().add("settings-label");
        grid.add(windLabel, 0, 1);
        windBox = new ComboBox<>(FXCollections.observableArrayList(WindSpeedUnit.values()));
        windBox.setValue(WindSpeedUnit.valueOf(props.getProperty("wind_speed_unit")));
        grid.add(windBox, 1, 1);

        // Precipitation
        Label precipLabel = new Label("Precipitation Units:");
        precipLabel.getStyleClass().add("settings-label");
        grid.add(precipLabel, 0, 2);
        precipBox = new ComboBox<>(FXCollections.observableArrayList(PrecipitationUnit.values()));
        precipBox.setValue(PrecipitationUnit.valueOf(props.getProperty("precipitation_unit")));
        grid.add(precipBox, 1, 2);

        // Forecast Days
        Label daysLabel = new Label("Forecast Days:");
        daysLabel.getStyleClass().add("settings-label");
        grid.add(daysLabel, 0, 3);
        daysSpinner = new Spinner<>(1, 16, Integer.parseInt(props.getProperty("forecast_days")));
        grid.add(daysSpinner, 1, 3);

        // Save Button
        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> saveAndClose());

        // Cancel button]
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> close());

        HBox buttonBox = new HBox(10, cancelButton, saveButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(20, grid, buttonBox);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10));
        layout.getStyleClass().add("settings-window");

        Scene scene = new Scene(layout);

        // Apply the same stylesheet as the main app.
        scene.getStylesheets().add("stylesheets/style.css");
        setScene(scene);
    }

    // Called when the Save button is clicked. Reads the values from the controls and updates the settings object,
    // which then saves it to the .properties file
    private void saveAndClose() {
        settings.updateSettings("temperature_unit", tempBox.getValue().toString());
        settings.updateSettings("wind_speed_unit", windBox.getValue().toString());
        settings.updateSettings("precipitation_unit", precipBox.getValue().toString());
        settings.updateSettings("forecast_days", daysSpinner.getValue().toString());

        // The settings are saved to file by updateSettings() -> saveSettings()
        close();
    }
}
