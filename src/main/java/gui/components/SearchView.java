package gui.components;

import api.Geocoder;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.Location;

import java.util.List;

public class SearchView extends VBox {
    private SearchBar searchBar;
    private Geocoder geocoder;
    private VBox resultsList;
    private int highlightedIndex = -1;

    private ObjectProperty<Location> selectedLocation = new SimpleObjectProperty<>();


    public SearchView() {
        super(5);
        this.searchBar = new SearchBar();
        this.resultsList = new VBox(5);
        this.geocoder = new Geocoder();

        configureResultsList();

        this.getChildren().addAll(searchBar, resultsList);

        this.searchBar.setOnSearch(event -> {
            String query = searchBar.getSearchText();
            if (query != null && !query.trim().isEmpty()) {
                handleSearch(query);
            }
        });

        // Enables cycling through the search results using arrow keys
        // Pressing ENTER initiates forecast search
        this.searchBar.getTextField().setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case DOWN -> { moveSelection(1); event.consume();}
                case UP -> { moveSelection(-1); event.consume(); }
                case ENTER -> {
                    if (highlightedIndex >= 0) {
                        confirmSelection();
                        event.consume();
                    }
                }
            }
        });

        this.searchBar.getTextField().textProperty().addListener((obs, oldText, newText) -> {
            // Reset the highlighted result whenever the user types again
            highlightedIndex = -1;
        });


        // Close the search results box if the user clicks outside it
        this.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                    if(resultsList.isVisible()
                        && !resultsList.localToScene(resultsList.getBoundsInLocal()).contains(event.getSceneX(), event.getSceneY())
                        && !searchBar.localToScene(searchBar.getBoundsInLocal()).contains(event.getSceneX(), event.getSceneY())){
                        resultsList.setVisible(false);
                        resultsList.setManaged(false);
                    }
                });
            }
        });

        // Close the search results box if the user presses the Escape key
        this.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                resultsList.setVisible(false);
                resultsList.setManaged(false);
            }
        });


    }

    // Hide the search results box until a search has been complete
    private void configureResultsList() {
        resultsList.getStyleClass().add("search-results-list");
        resultsList.setVisible(false);
        resultsList.setManaged(false);
    }

    private void handleSearch(String query){
        System.out.println("Searching for: " + query);

        // Query the Geocoding API and retrieve location objects (which contain coordinates we use to query the Weather API)
        List<Location> locations = geocoder.searchLocationsByName(query);

        // Clear any residual search results from other searches
        resultsList.getChildren().clear();



        if(locations != null && !locations.isEmpty()){
            System.out.println("Found " + locations.size() + " locations matching the query.");



            // If locations were found, populate the result box with a label per location found
            for (Location location : locations) {
                HBox labelBox = new HBox(5);
                labelBox.getStyleClass().add("search-result-label-box");
                labelBox.setUserData(location);
                labelBox.setOnMouseEntered(e -> highlightResult(resultsList.getChildren().indexOf(labelBox)));

                // Label contains the name of location, followed by administrative municipality (e.g., a state, county or similar)
                // followed by the location's country
                Label label = new Label(location.getName()
                        + (location.getAdmin1() != null ? ", " + location.getAdmin1() : "")
                        + " (" + location.getCountry() + ")");

                // Listen for mouse clicks on the search result (to start the weather request for that location)
                label.setOnMouseClicked(event -> handleSelection(location));
                label.getStyleClass().add("search-result-label");
                labelBox.getChildren().add(label);
                resultsList.getChildren().add(labelBox);
            }

        } else {
            HBox labelBox = new HBox(5);
            labelBox.getStyleClass().add("search-result-label-box");
            resultsList.setVisible(false);
            resultsList.setManaged(false);
            Label label = new Label("No results found for query: " + query);
            label.getStyleClass().add("search-result-label");
            labelBox.getChildren().add(label);
            System.out.println("No results found for query: " + query);
            resultsList.getChildren().add(labelBox);
        }
        // Make the result box visible once it is populated
        resultsList.setVisible(true);
        resultsList.setManaged(true);

        highlightedIndex = -1;
        searchBar.getTextField().requestFocus();
    }

    private void handleSelection(Location selected) {
        if (selected != null) {
            selectedLocation.set(selected);
            resultsList.setVisible(false);  // Hide the list
            resultsList.setManaged(false);
        }
    }

    private void highlightResult(int index){
        if (index < 0 || index >= resultsList.getChildren().size()) return;

        // remove highlight from all
        for (int i = 0; i < resultsList.getChildren().size(); i++) {
            resultsList.getChildren().get(i).getStyleClass().remove("highlighted-result");
        }

        // add highlight to selected
        resultsList.getChildren().get(index).getStyleClass().add("highlighted-result");
        highlightedIndex = index;

    }

    private void moveSelection(int direction){
        int count = resultsList.getChildren().size();
        if (count == 0) return;

        int newIndex = highlightedIndex + direction;
        if (newIndex < 0) newIndex = 0;
        if (newIndex >= count) newIndex = count - 1;

        highlightResult(newIndex);
    }

    private void confirmSelection(){
        if (highlightedIndex >= 0 && highlightedIndex < resultsList.getChildren().size()) {
            HBox labelBox = (HBox) resultsList.getChildren().get(highlightedIndex);
            Location selected = (Location) labelBox.getUserData();
            handleSelection(selected);
        }
    }

    public ObjectProperty<Location> selectedLocationProperty() {
        return selectedLocation;
    }

    public Location getSelectedLocation() {
        return selectedLocation.get();
    }



}
