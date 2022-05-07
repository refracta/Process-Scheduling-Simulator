package kr.ac.koreatech.os.pss.app.component.raw.n.timeline;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.concurrent.atomic.AtomicReference;

public class NTimelineWidget extends BorderPane {
    protected Color verticalIndicatorColor = Color.GRAY;

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

        initVerticalIndicator();
    }

    private void initVerticalIndicator() {
        AtomicReference<Line> verticalIndicator = new AtomicReference<>();

        setOnMouseEntered(event -> {
            Line line = new Line(0, 0, 0, getHeight() - scalerPane.getHeight());
            line.setStroke(verticalIndicatorColor);
            getChildren().add(line);
            verticalIndicator.set(line);
        });
        setOnMouseExited(event -> getChildren().remove(verticalIndicator.get()));
        setOnMouseMoved(event -> {
            if (event.getY() > getHeight() - scalerPane.getHeight()) {
                getChildren().remove(verticalIndicator.get());
                return;
            }

            if (!getChildren().contains(verticalIndicator.get())) {
                getChildren().add(verticalIndicator.get());
            }

            verticalIndicator.get().setLayoutX(event.getX());
        });
    }

    public void addTimeline() {
        timelineVBox.getChildren().add(new NTimelinePane());
    }

    public void bindToTargetPaneHeight(Pane target) {
        minHeightProperty().bind(target.heightProperty());
    }
}
