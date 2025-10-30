package utils;

import javafx.scene.text.Font;
import java.io.InputStream;

public class WeatherIconManager {

    private static final Font weatherFont;

    // Static block to load the font when the class is first loaded
    static {
        Font tempFont;
        try (InputStream is = WeatherIconManager.class.getResourceAsStream("/font/weathericons-regular-webfont.ttf")) {
            if (is == null) {
                throw new IllegalStateException("Weather icon font not found in resources.");
            }
            // Load the font at a base size
            tempFont = Font.loadFont(is, 14);
        } catch (Exception e) {
            System.err.println("Failed to load weather icon font: " + e.getMessage());
            tempFont = Font.getDefault();
        }
        weatherFont = tempFont;
    }

    public static Font getFont() {
        return weatherFont;
    }

    /**
     * Gets the correct icon character (as a String) for a given WMO code
     * @param wmoCode The WMO weather interpretation cod
     * @return A String containing the single Unicode character for the icon.
     */
    public static String getIconCharacter(int wmoCode) {

        return switch (wmoCode) {
            case 0 -> "\uf00d"; //"\uf02e"; // Clear sky
            case 1 -> "\uf00c"; // "\uf083"; // Mainly clear
            case 2 -> "\uf002"; // "\uf083"; // Partly cloudy (using the same night icon)
            case 3 -> "\uf013";                 // Overcast
            case 45, 48 -> "\uf014";           // Fog
            case 51, 53, 55 -> "\uf017";       // Drizzle
            case 56, 57 -> "\uf0b5";           // Freezing Drizzle
            case 61, 63, 65 -> "\uf019";       // Rain
            case 66, 67 -> "\uf0b5";           // Freezing Rain
            case 71, 73, 75 -> "\uf01b";       // Snow fall
            case 77 -> "\uf01a";               // Snow grains
            case 80, 81, 82 -> "\uf018";       // Rain showers
            case 85, 86 -> "\uf01b";           // Snow showers
            case 95 -> "\uf01e";               // Thunderstorm
            case 96, 99 -> "\uf01d";           // Thunderstorm with hail
            default -> "\uf07b";               // Default (NA)
        };
    }
}