package gui;

import api.ForecastLookup;
import api.Geocoder;
import gui.components.SearchBar;
import gui.components.SearchView;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;
import models.Location;

import java.util.List;

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


        VBox root = new VBox(10 );
        root.setAlignment(Pos.TOP_CENTER);

        root.getChildren().add(searchView);


        searchView.maxWidthProperty().bind(root.widthProperty().multiply(0.5));

        searchView.selectedLocationProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                System.out.println("Selected location: " + newValue);
                // trigger the weather forecast view here.
                ForecastLookup locationForecast = new ForecastLookup();
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
