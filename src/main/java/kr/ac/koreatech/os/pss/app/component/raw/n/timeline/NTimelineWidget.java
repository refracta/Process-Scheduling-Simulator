package kr.ac.koreatech.os.pss.app.component.raw.n.timeline;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class NTimelineWidget extends BorderPane {
    public NTimelineWidget() {
        init();
    }

    private NTimelineScalePlatePane scalePlatePane;
    private VBox timelineVBox;
    private ScrollPane timelineScrollPane;
    private NTimelineScalerPane scalerPane;

    public void init() {
        getChildren().clear();

        scalePlatePane = new NTimelineScalePlatePane();
        setTop(scalePlatePane);

        scalerPane = new NTimelineScalerPane(240, 12);
        setBottom(scalerPane);

        timelineVBox = new VBox();

        timelineScrollPane = new ScrollPane(timelineVBox);
        timelineScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        timelineScrollPane.setFitToWidth(true);
        timelineScrollPane.getStyleClass().add("edge-to-edge");
        setCenter(timelineScrollPane);
    }

    public void addTimeline() {
        timelineVBox.getChildren().add(new NTimelinePane());
    }

    public void bindToTargetPaneHeight(Pane target) {
        minHeightProperty().bind(target.heightProperty());
    }
}
