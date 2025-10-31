package utils;

import java.io.*;
import java.util.Properties;

/**
 * Manages the application's user settings.
 * This class uses the Java Properties API to store key-value pairs
 * in a simple .properties text file, allowing settings to persist
 * between app launches.
 */
public class Settings {
    private static final String SETTINGS_FILE = "settings.properties";
    private Properties properties = new Properties();

    /**
     * Constructs the Settings object and immediately loads
     * any existing settings from the file.
     */
    public Settings() {
        loadSettings();
    }

    /**
     * Tries to load the .properties file. If the file doesn't exist
     * (e.g., first launch), it calls createDefaultSettings().
     */
    public void loadSettings() {
        try (InputStream input = new FileInputStream(SETTINGS_FILE)){
            properties.load(input);
            System.out.println("Configuration loaded from file: " + SETTINGS_FILE + ".");
        } catch (IOException e) {
            System.out.println("Configuration file not found. Creating default settings.");
            createDefaultSettings();
        }
    }

    /**
     * Creates a new settings.properties file with the default
     * application values.
     */
    private void createDefaultSettings() {
        try (OutputStream output = new FileOutputStream(SETTINGS_FILE)) {


            properties.setProperty("temperature_unit", "celsius"); //fahrenheit
            properties.setProperty("wind_speed_unit", "kmh");//ms mph kn
            properties.setProperty("precipitation_unit", "mm"); // inches
            properties.setProperty("forecast_days", "7"); //up to 16
            properties.setProperty("forecast_interval", "hourly");
            properties.store(output, null);
            System.out.println("Configuration saved.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Properties getProperties() {
        return properties;
    }

    /**
     * Updates a single setting in memory and immediately saves the
     * entire properties object back to the file.
     */
    public void updateSettings(String key, String value) {
        properties.setProperty(key, value);
        saveSettings();
    }

    /**
     * Writes the current in-memory 'properties' object to the
     * settings.properties file, overwriting its previous contents.
     */
    public void saveSettings() {
        try (OutputStream output = new FileOutputStream(SETTINGS_FILE)) {
            properties.store(output, null);
            System.out.println("Configuration saved.");
        } catch (IOException e) {
            System.out.println("Uh oh! Something went wrong saving the configuration");
            e.printStackTrace();
        }
    }
}
