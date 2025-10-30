package gui.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.control.ScrollPane;

import java.awt.*;
import java.util.List;


public abstract class ForecastListView<T> extends ScrollPane {
    protected final HBox container = new HBox(20);
    public ForecastListView(List<T> forecasts) {
        setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        setVbarPolicy(ScrollBarPolicy.NEVER);
        setFitToHeight(true);

        container.setPadding(new Insets(10));
        container.setAlignment(Pos.CENTER_LEFT);

        for (T f : forecasts) {
            container.getChildren().add(createForecastCard(f));
        }

        setContent(container);
    }

    protected abstract Node createForecastCard(T forecast);
}
