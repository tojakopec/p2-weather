package utils;

/**
 * A container class that holds all the enums for the application.
 * This provides a central, static place to define the allowed options
 * for user settings, making the code safer and more readable than raw strings.
 * These are the only units the API will accept, so enum-ing them ensures "type safety" in a way
 */
public class Enums {
    public enum TemperatureUnit {
        celsius,
        fahrenheit
    }
    public enum WindSpeedUnit {
        kmh,
        ms,
        mph,
        kn
    }
    public enum PrecipitationUnit {
        mm,
        inch
    }
    public enum ForecastInterval {
        hourly,
        daily
    }
}
