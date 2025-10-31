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
    private int weatherCode;
    private int isDay;


//    @JsonProperty("current_weather")
//    private CurrentWeather current;
//
//    @JsonProperty("current_weather_units")
//    private CurrentWeatherUnits currentWeatherUnits;

    @JsonProperty("current")
    private CurrentDetails currentDetails;

    @JsonProperty("current_units")
    private CurrentUnits currentUnits;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CurrentUnits {
        @JsonProperty("temperature_2m")
        private String temperature;

        @JsonProperty("windspeed_10m")
        private String windSpeedUnit;

        @JsonProperty("rain")
        private String rain;
        @JsonProperty("showers")
        private String showers;

        public String getTemperature() { return temperature; }
        public String getWindSpeedUnit() { return windSpeedUnit; }
        public String getRain() { return rain; }
        public String getShowers() { return showers; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CurrentDetails {
        private String time;

        @JsonProperty("temperature_2m")
        private double temperature;

        @JsonProperty("relative_humidity_2m")
        private int relativeHumidity2m;

        @JsonProperty("apparent_temperature")
        private double apparentTemperature;

        @JsonProperty("rain")
        private double rain;
        @JsonProperty("showers")
        private double showers;

        @JsonProperty("is_day")
        private int isDay;

        @JsonProperty("weathercode")
        private int weatherCode;

        @JsonProperty("windspeed_10m")
        private double windSpeed;

        // Getters
        public double getTemperature() {
            return temperature;
        }
        public int getRelativeHumidity2m() {
            return relativeHumidity2m;
        }
        public double getApparentTemperature() {
            return apparentTemperature;
        }
        public int getIsDay() {
            return isDay;
        }
        public int getWeatherCode() {
            return weatherCode;
        }
        public double getWindSpeed() {
            return windSpeed;
        }

        public double getRain() { return rain; }
        public double getShowers() { return showers; }
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Hourly {

        @JsonProperty("time")
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

        @JsonProperty("is_day")
        private List<Integer> isDay;

        public List<Integer> getWeatherCodes(){
            return this.weatherCode;
        }

        public List<Integer> getPrecipitationProbability(){
            return precipitationProbability;
        }

        public List<Double> getWindSpeeds(){
            return this.windSpeed10m;
        }
        public List<Double> getUvIndex(){
            return this.uvIndex;
        }
        public List<Double> getTemperatures(){
            return this.temperature2m;
        }

        public List<String> getTimes(){
            return this.time;
        }

        public List<Integer> getIsDay(){
            return this.isDay;
        }

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

        @JsonProperty("time")
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

        public List<String> getTime(){
            return time;
        }

        public List<String> getSunrise(){
            return sunrise;
        }

        public List<String> getSunset(){
            return sunset;
        }

        public List<Integer> getWeatherCode(){
            return weatherCode;
        }

        public List<Double> getTemperature2mMax(){
            return temperature2mMax;
        }

        public List<Double> getTemperature2mMin(){
            return temperature2mMin;
        }

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

    public CurrentDetails getCurrentDetails(){
        return currentDetails;
    }

    public void setCurrentDetails(CurrentDetails currentDetails){
        this.currentDetails = currentDetails;
    }

    // Formatted getters

    public double getCurrentTemperature() {
        if (currentDetails == null) return 0.0;
        return currentDetails.getTemperature();
    }

    public String getCurrentTemperatureUnit() {
        if (currentUnits == null) return "°";
        return currentUnits.getTemperature();
    }

    public String getCurrentWindSpeedUnit() {
        if (currentUnits == null) return "";
        return currentUnits.getWindSpeedUnit();
    }

    public String getFormattedTemperature() {
        if (currentDetails == null) return "-";
        return String.format("%.0f%s", getCurrentTemperature(), getCurrentTemperatureUnit());
    }

    public String getFormattedWindSpeed(){
        if (currentDetails == null) return "-";
        return String.format("%.1f%s", currentDetails.getWindSpeed(), getCurrentWindSpeedUnit());
    }

    public String getCurrentRainUnit() {
        if (currentUnits == null) return "mm"; // Default
        return currentUnits.getRain();
    }

    public String getCurrentShowersUnit() {
        if (currentUnits == null) return "mm"; // Default
        return currentUnits.getShowers();
    }

    public int getCurrentWeatherCode() {
        if (currentDetails == null) return 0; // Default to "clear"
        return currentDetails.getWeatherCode();
    }

    public int getIsDay() {
        if (currentDetails == null) return 1; // Default to "day"
        return currentDetails.getIsDay();
    }

    public String getFormattedDailyHigh(){
        if (daily == null || daily.getTemperature2mMax() == null || daily.getTemperature2mMax().isEmpty()) return "-";
        return String.format("%.0f%s", daily.getTemperature2mMax().getFirst(), getCurrentTemperatureUnit());
    }

    public String getFormattedDailyLow(){
        if (daily == null || daily.getTemperature2mMin() == null || daily.getTemperature2mMin().isEmpty()) return "-";
        return String.format("%.0f%s", daily.getTemperature2mMin().getFirst(), getCurrentTemperatureUnit());
    }


    @Override
    public String toString(){
        return "ForecastResponse{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", timezone='" + timezone + '\'' +
                ", hourly=" + hourly +
                ", daily=" + daily +
                ", current=" + currentDetails +
                '}';
    }
}