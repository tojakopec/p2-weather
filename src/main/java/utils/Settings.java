package utils;

import java.io.*;
import java.util.Properties;

public class Settings {
    private static final String SETTINGS_FILE = "settings.properties";
    private Properties properties = new Properties();


    public Settings() {
        loadSettings();
    }

    public void loadSettings() {
        try (InputStream input = new FileInputStream(SETTINGS_FILE)){
            properties.load(input);
            System.out.println("Configuration loaded from file: " + SETTINGS_FILE + ".");
        } catch (IOException e) {
            System.out.println("Configuration file not found. Creating default settings.");
            createDefaultSettings();
        }
    }

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

    public void updateSettings(String key, String value) {
        properties.setProperty(key, value);
        saveSettings();
    }

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
