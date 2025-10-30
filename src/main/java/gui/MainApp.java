package gui;

import api.ForecastLookup;
import gui.components.ForecastView;
import gui.components.RecentSearchesView;
import gui.components.SearchView;
import gui.components.SettingsWindow;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;
import javafx.util.Duration;
import models.Forecast;
import models.Location;
import utils.Settings;
import utils.RecentSearches;

public class MainApp extends Application {

    private Settings settings;
    private ForecastLookup forecastLookup;
    private RecentSearches recentSearches;

    private TranslateTransition drawerSlide;
    private RecentSearchesView recentSearchesView;

    private boolean isDrawerOpen = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        settings = new Settings();
        recentSearches = new RecentSearches();
        forecastLookup = new ForecastLookup(settings);

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        double sceneWidth = bounds.getWidth() * 0.5;
        double sceneHeight = bounds.getHeight() * 0.5;

        // These are the core components in the layout, the search bar at the top,
        // and the forecast view "box" that will display the weather data
        SearchView searchView = new SearchView();
        searchView.setMaxSize(VBox.USE_PREF_SIZE, VBox.USE_PREF_SIZE);

        ForecastView forecastView = new ForecastView();

        // Load the last searched for location and pull its forecast (if it exists)
        // Also, set the location property in searchView to this value, as our ForecastView location is reacting to it
        Location lastLocation = RecentSearches.getLatestLocation();
        if (lastLocation != null) {
            searchView.selectedLocationProperty().set(lastLocation);
            Forecast forecast = forecastLookup.getForecast(lastLocation);
            forecastView.setForecast(forecast);
        }

        // Makes sure the selected location and forecast matches the one selected in the search results, reactively
        forecastView.selectedLocationProperty().bind(searchView.selectedLocationProperty());

        // Build the layout
        VBox mainContent = new VBox(10);
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.getChildren().addAll(forecastView);

        StackPane.setMargin(mainContent, new Insets(80, 0, 0, 0));

        VBox resultsList = searchView.getResultsList();

        Label hamburgerIcon = new Label("\u2630");
        hamburgerIcon.getStyleClass().add("icon-button");

        Label cogIcon = new Label("\u2699");
        cogIcon.getStyleClass().add("icon-button");

        HBox iconBox = new HBox(10, hamburgerIcon, cogIcon);
        iconBox.setMaxSize(HBox.USE_PREF_SIZE, HBox.USE_PREF_SIZE);

        recentSearchesView = new RecentSearchesView(recentSearches);
        recentSearchesView.setVisible(false);
        recentSearchesView.widthProperty().addListener((observable, oldValue, newValue) -> {
            recentSearchesView.setTranslateX(-newValue.doubleValue());
        });

        StackPane root = new StackPane();
        root.getChildren().addAll(
                mainContent,
                searchView,
                recentSearchesView,
                iconBox,
                resultsList);
        StackPane.setAlignment(mainContent, Pos.TOP_CENTER);
        StackPane.setAlignment(searchView, Pos.TOP_CENTER);
        StackPane.setMargin(searchView, new Insets(10, 0, 0, 0));

        StackPane.setAlignment(resultsList, Pos.TOP_CENTER);
        StackPane.setAlignment(recentSearchesView, Pos.CENTER_LEFT);

        StackPane.setAlignment(iconBox, Pos.TOP_LEFT);
        StackPane.setMargin(iconBox, new Insets(10, 0, 0, 20));

        searchView.maxWidthProperty().bind(root.widthProperty().multiply(0.5));
        resultsList.maxWidthProperty().bind(root.widthProperty().multiply(0.5));

        searchView.heightProperty().addListener((observable, oldHeight, newHeight) -> {
            StackPane.setMargin(resultsList, new Insets(newHeight.doubleValue(), 0, 0, 0));
        });

        searchView.selectedLocationProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                System.out.println("Selected location: " + newValue);

                // Add this location to the recent searches list
                recentSearches.addLocation(newValue);
                // Populate the recent searches drawer
                recentSearchesView.populateView();

                // Look up the forecast for the location
                Forecast forecast = forecastLookup.getForecast(newValue);
                forecastView.setForecast(forecast);
                System.out.println("Forecast: " + forecast);
            }
        });

        recentSearchesView.reSearchLocationProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                searchView.selectedLocationProperty().set(newValue);
                toggleDrawer();
            }
        });

        hamburgerIcon.setOnMouseClicked(event -> {
            toggleDrawer();
        });

        cogIcon.setOnMouseClicked(event -> {
            SettingsWindow settingsWindow = new SettingsWindow(settings, primaryStage);
            settingsWindow.showAndWait();

            if (searchView.getSelectedLocation() != null) {
                Forecast forecast = forecastLookup.getForecast(searchView.getSelectedLocation());
                forecastView.setForecast(forecast);
            }
        });

        setupDrawerAnimation();

        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        scene.getStylesheets().clear();
        scene.getStylesheets().add("stylesheets/style.css");
        primaryStage.setTitle("Weather Forecast");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setupDrawerAnimation(){
        drawerSlide = new TranslateTransition(Duration.millis(300), recentSearchesView);

        drawerSlide.setOnFinished(e -> {
            if(drawerSlide.getToX() == 0) {
                isDrawerOpen = true;
            } else {
                isDrawerOpen = false;
                recentSearchesView.setVisible(false);
            }
        });
    }

    private void toggleDrawer() {
        if (!isDrawerOpen) {
            drawerSlide.setFromX(-recentSearchesView.getWidth());
            drawerSlide.setToX(0);

            recentSearchesView.setVisible(true);
            drawerSlide.play();
        } else {
            drawerSlide.setFromX(0);
            drawerSlide.setToX(-recentSearchesView.getWidth());

            drawerSlide.play();
        }
    }
}
