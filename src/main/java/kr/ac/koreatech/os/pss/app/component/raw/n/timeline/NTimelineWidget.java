package kr.ac.koreatech.os.pss.app.component.raw.n.timeline;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class NTimelineWidget extends BorderPane {
    public NTimelineWidget() {
        init();
    }

    public void init() {
        getChildren().clear();

        NTimelinePane pane1 = new NTimelinePane();
        setTop(pane1);

        NTimelineScalerPane pane2 = new NTimelineScalerPane(240, 12);
        setBottom(pane2);

        VBox pane = new VBox();
        pane.getChildren().add(new NTimelinePane());
        pane.getChildren().add(new NTimelinePane());
        pane.getChildren().add(new NTimelinePane());
        pane.getChildren().add(new NTimelinePane());
        pane.getChildren().add(new NTimelinePane());
        ScrollPane scrollPane = new ScrollPane(pane);

        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setFitToWidth(true);

        scrollPane.getStyleClass().add("edge-to-edge");
        setCenter(scrollPane);
    }

    public void bindToTargetPaneHeight(Pane target) {
        minHeightProperty().bind(target.heightProperty());
    }
}
