## Technical Stack

- Java (requires Java 24)
- JavaFX - UI framework
- Maven - Build and dependency management
- Jackson - For handling JSON files (`tools.jackson.databind`)
- Weather Icons - for weather icons (https://erikflowers.github.io/weather-icons/)
### Project Structure
A brief overview of the package layout:
- `/api`: Contains ForecastLookup.java and Geocoder.java for all communication with external APIs.
- `/gui`: Contains the main MainApp.java entry point and all JavaFX components (.java files) that make up the UI.
- `/models`: Contains all data models like Forecast.java and Location.java, used by Jackson to parse API responses.
- `/utils`: Contains helper classes for persistence (FileHandler.java, Settings.java), data (Enums.java), and managing recent searches.
- `/resources`: Contains font and stylesheet
- `/font`: Contains the weathericons-regular-webfont.ttf font.
- `/stylesheets`: Contains the style.css file.

## Getting Started

### Prerequisites
The app is meant to be run from an IDE. I used IntelliJ IDEA.
You need the following to run it:
- Java Development Kit (JDK) 24 or later
- Maven 3.x 
  - `brew install maven` in terminal via homebrew)
  - or if you don't have homebrew:`/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
- An internet connection




### Configuration

This application is zero-config. On first launch, it will automatically create:

`settings.properties`: Stores your saved user preferences (units, etc.).
`recent_searches.ser`: Stores your recent search history.

These files will appear in the root directory of the project.

### Building the Project

1. Clone the repository:
   `git@github.com:tojakopec/p2-weather.git`
2. Navigate to the project root directory (`/w8-weather-2`)
3. Build with Maven:
   - terminal: `mvn clean install`
   - terminal `mvn javafx:run`
4. Use the app

### Using the app after launching
1. Search for a location in the search bar at the top center
2. Select a location from the list of results
   - You can use either mouse or keyboard arrows
3. After selecting, the app will query the API and pull forecast info
4. View your Forecast info
5. Alternate between daily and hourly views
6. Click on the cog wheel (top left) to change preferred units
7. Click on the hamburger icon (three lines, top left) to see your recently searched for locations




