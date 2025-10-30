package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Location implements Serializable {
    private int id;
    private String name;
    private double latitude;
    private double longitude;
    private double elevation;
    private String admin1;
    private String country;
    private String[] postcodes;
    private Forecast forecast;
    private LocalDateTime lastUpdate;

    /*
        Together with JsonIgnoreProperties, this ensures there's no confusion
        Between camelCase used in Java and snake_case used in JSON
     */
    @JsonProperty("country_code")
    private String countryCode;

    @JsonProperty("country_id")
    private int countryId;

    public Location() {}


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAdmin1() { return admin1; }

    public double getElevation() { return elevation; }
    public void setElevation(double elevation) { this.elevation = elevation; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String[] getPostcodes() { return postcodes; }
    public void setPostcodes(String[] postcodes) { this.postcodes = postcodes; }

    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }

    public int getCountryId() { return countryId; }
    public void setCountryId(int countryId) { this.countryId = countryId; }

    // Used for a cleaner and more compact print of an individual result/location
    @Override
    public String toString(){
        return "Location{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", admin1='" + admin1 + '\'' +
                ", country='" + country + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", postcodes=" + Arrays.toString(postcodes) +
                '}';
    }
}