package kr.ac.koreatech.os.pss.app.component.raw.n.timeline;

import javafx.event.EventHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

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
        scalerPane.setChangeListener((start, range, endpoint) -> {
            scalePlatePane.drawScale(start, range, endpoint);
        });


        initVerticalIndicator();
    }

    private void initVerticalIndicator() {
        Line verticalIndicator = new Line();
        verticalIndicator.setStroke(verticalIndicatorColor);

        EventHandler<? super MouseEvent> showVerticalIndicator = (EventHandler<MouseEvent>) event -> {
            verticalIndicator.setStartX(0);
            verticalIndicator.setStartY(0);
            verticalIndicator.setEndX(0);
            verticalIndicator.setEndY(getHeight() - scalerPane.getHeight());
            System.out.println(getHeight());
            System.out.println(scalerPane.getHeight());

            if (!getChildren().contains(verticalIndicator)) {
                getChildren().add(verticalIndicator);
            }
        };

        EventHandler<? super MouseEvent> hideVerticalIndicator = (EventHandler<MouseEvent>) event -> {
            getChildren().remove(verticalIndicator);
        };

        EventHandler<? super MouseEvent> verticalIndicatorHandler = (EventHandler<MouseEvent>) event -> {
            if (event.getY() > getHeight() - scalerPane.getHeight()) {
                getChildren().remove(verticalIndicator);
                return;
            }

            if (!getChildren().contains(verticalIndicator)) {
                getChildren().add(verticalIndicator);
            }

            verticalIndicator.setLayoutX(event.getX());
        };

        setOnMouseEntered(showVerticalIndicator);
        setOnMouseMoved(verticalIndicatorHandler);
        setOnMouseDragged(verticalIndicatorHandler);
        setOnMouseExited(hideVerticalIndicator);
    }

    public void addTimeline() {
        timelineVBox.getChildren().add(new NTimelinePane());
    }

    public void bindToTargetPaneHeight(Pane target) {
        minHeightProperty().bind(target.heightProperty());
    }
}
