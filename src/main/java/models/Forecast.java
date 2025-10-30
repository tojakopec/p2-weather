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


    @JsonProperty("current_weather")
    private CurrentWeather current;

    @JsonProperty("current_weather_units")
    private CurrentWeatherUnits currentWeatherUnits;

    private static class CurrentWeatherUnits {
        private String time;
        private String interval;

        @JsonProperty("temperature")
        private String temperature;
        @JsonProperty("windspeed")
        private String windSpeedUnit;

        @JsonProperty("winddirection")
        private String windDirectionUnit;

        @JsonProperty("is_day")
        private String isDay;
        @JsonProperty("weathercode")
        private String weatherCode;
        
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CurrentWeather {

        private String time;
        private int interval;

        @JsonProperty("temperature")
        private double temperature;

        @JsonProperty("is_day")
        private int isDay; // 1 for day, 0 for night

        @JsonProperty("weathercode")
        private int weatherCode;

        @JsonProperty("windspeed")
        private double windSpeed;

        @JsonProperty("winddirection")
        private int windDirection;


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

        public List<Double> getWindSpeeds(){
            return this.windSpeed10m;
        }
        public List<Double> getUvIndices(){
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

    public CurrentWeather getCurrent() {
        return current;
    }

    public double getCurrentTemperature() {
        return current.temperature;
    }

    public String getFormattedTemperature() {
      return String.format("%.0f%s", getCurrentTemperature(), getCurrentTemperatureUnit());
    }
    public void setCurrent(CurrentWeather current) {
        this.current = current;
    }

    public String getCurrentTemperatureUnit() {
        return this.currentWeatherUnits.temperature;
    }

    public String getFormattedDailyHigh(){
        return String.format("%.0f%s", daily.temperature2mMax.getFirst(), getCurrentTemperatureUnit());
    }

    public String getFormattedDailyLow(){
        return String.format("%.0f%s", daily.temperature2mMin.getFirst(), getCurrentTemperatureUnit());
    }


    public String getCurrentWindSpeedUnit() {
        return this.currentWeatherUnits.windSpeedUnit;
    }

    public String getFormattedWindSpeed(){
        return String.format("%.1f%s", current.windSpeed, getCurrentWindSpeedUnit());
    }

    public void setWindSpeedUnit(String windSpeedUnit) {
        this.currentWeatherUnits.windSpeedUnit = windSpeedUnit;
    }


    public int getCurrentWeatherCode() {
        return this.current.weatherCode;
    }
    public int getHourlyWeatherCode(){
        return this.hourly.weatherCode.getFirst();
    }
    public void setWeatherCode(int weatherCode) {
        this.weatherCode = weatherCode;
    }
    public int getIsDay() {
        return isDay;
    }
    public void setIsDay(int isDay) {
        this.isDay = isDay;
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