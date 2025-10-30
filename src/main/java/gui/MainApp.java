package gui;

import api.ForecastLookup;
import gui.components.ForecastView;
import gui.components.SearchView;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;
import models.Forecast;
import utils.RecentSearches;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Screen screen = Screen.getPrimary();

        Rectangle2D bounds = screen.getVisualBounds();
        double sceneWidth = bounds.getWidth() * 0.5;
        double sceneHeight = bounds.getHeight() * 0.5;

        SearchView searchView = new SearchView();
        RecentSearches recentSearches = new RecentSearches();
        ForecastView forecastView = new ForecastView();

        // Makes sure the selected location and forecast matches the one selected in the search results, reactively
        forecastView.selectedLocationProperty().bind(searchView.selectedLocationProperty());

        StackPane stack = new StackPane();
        stack.getChildren().addAll(searchView, forecastView);
        VBox root = new VBox(10 );
        root.setAlignment(Pos.TOP_CENTER);

        root.getChildren().add( stack);


        searchView.maxWidthProperty().bind(root.widthProperty().multiply(0.5));

        searchView.selectedLocationProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                System.out.println("Selected location: " + newValue);
                // Add this location to the recent searches list
                recentSearches.addLocation(newValue);
                // Look up the forecast for the location
                ForecastLookup locationForecast = new ForecastLookup();
                Forecast forecast = locationForecast.getForecast(newValue);

                forecastView.setForecast(forecast);

                System.out.println("Forecast: " + locationForecast.getForecast(newValue));
                // render forecast
            }
        });


        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        scene.getStylesheets().clear();
        scene.getStylesheets().add("style.css");
        primaryStage.setTitle("Weather Forecast");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
