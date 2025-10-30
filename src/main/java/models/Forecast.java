package models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Main wrapper class for the entire weather forecast API JSON response.
 * We use @JsonIgnoreProperties(ignoreUnknown = true) so that if the API
 * adds new fields, our Java code won't break during JSON parsing.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Forecast {

    private double latitude;
    private double longitude;
    private String timezone;

    private Hourly hourly;
    private Daily daily;
    private Current current;

    @JsonProperty("temperature_unit")
    private String temperatureUnit;

    @JsonProperty("wind_speed_unit")
    private String windSpeedUnit;

    @JsonProperty("precipitation_unit")
    private String precipitationUnit;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Current {

        private String time;
        private int interval;

        @JsonProperty("temperature_2m")
        private double temperature2m;

        @JsonProperty("relative_humidity_2m")
        private int relativeHumidity2m;

        @JsonProperty("is_day")
        private int isDay; // Often 1 for day, 0 for night

        @JsonProperty("weather_code")
        private int weatherCode;

        @JsonProperty("wind_speed_10m")
        private double windSpeed10m;

        @JsonProperty("wind_direction_10m")
        private int windDirection10m;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Hourly {

        private List<String> time;

        @JsonProperty("temperature_2m")
        private List<Double> temperature2m;

        @JsonProperty("relative_humidity_2m")
        private List<Integer> relativeHumidity2m;

        @JsonProperty("precipitation_probability")
        private List<Integer> precipitationProbability;

        @JsonProperty("weather_code")
        private List<Integer> weatherCode;

        @JsonProperty("wind_speed_10m")
        private List<Double> windSpeed10m;

        @JsonProperty("uv_index")
        private List<Double> uvIndex;

        @Override
        public String toString(){
            return "Hourly{" +
                    "time=" + time +
                    ", temperature2m=" + temperature2m +
                    ", relativeHumidity2m=" + relativeHumidity2m +
                    ", precipitationProbability=" + precipitationProbability +
                    ", weatherCode=" + weatherCode +
                    ", windSpeed10m=" + windSpeed10m +
                    ", uvIndex=" + uvIndex +
                    '}';
        }

    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Daily {

        private List<String> time;

        @JsonProperty("weather_code")
        private List<Integer> weatherCode;

        @JsonProperty("temperature_2m_max")
        private List<Double> temperature2mMax;

        @JsonProperty("temperature_2m_min")
        private List<Double> temperature2mMin;

        private List<String> sunrise;
        private List<String> sunset;

        @JsonProperty("precipitation_sum")
        private List<Double> precipitationSum;

        @JsonProperty("precipitation_probability_max")
        private List<Integer> precipitationProbabilityMax;

    }


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Hourly getHourly() {
        return hourly;
    }

    public void setHourly(Hourly hourly) {
        this.hourly = hourly;
    }

    public Daily getDaily() {
        return daily;
    }

    public void setDaily(Daily daily) {
        this.daily = daily;
    }

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    public String getTemperatureUnit() {
        return temperatureUnit;
    }

    public void setTemperatureUnit(String temperatureUnit) {
        this.temperatureUnit = temperatureUnit;
    }

    public String getWindSpeedUnit() {
        return windSpeedUnit;
    }

    public void setWindSpeedUnit(String windSpeedUnit) {
        this.windSpeedUnit = windSpeedUnit;
    }

    public String getPrecipitationUnit() {
        return precipitationUnit;
    }

    public void setPrecipitationUnit(String precipitationUnit) {
        this.precipitationUnit = precipitationUnit;
    }

    @Override
    public String toString(){
        return "ForecastResponse{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", timezone='" + timezone + '\'' +
                ", hourly=" + hourly +
                ", daily=" + daily +
                ", current=" + current +
                '}';
    }
}