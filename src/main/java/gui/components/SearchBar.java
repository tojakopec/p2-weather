package gui.components;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Priority;

public class SearchBar extends HBox {
    public TextField searchField = new TextField();
    public Button searchButton = new Button("Search");

    // Constructor creates a search field to enter the location to be searched and a button to press to initiate search
    public SearchBar() {
        super(5);
        // TODO: Switch styling to CSS file
        searchField.setPromptText("Enter a city name");
        searchField.getStyleClass().add("search-field");
        searchButton.setDefaultButton(true);
        searchButton.getStyleClass().add("search-button");

        HBox.setHgrow(searchField, Priority.ALWAYS);
        this.getChildren().addAll(searchField, searchButton);
    }

    // GETTER METHODS

    // Returns the text inside the TextField
    public String getSearchText() {
        return searchField.getText();
    }

    // Returns the TextField itself
    public TextField getTextField() {
        return searchField;
    }

    public void setOnSearch(EventHandler<ActionEvent> eventHandler) {
        searchButton.setOnAction(eventHandler);
    }

}
