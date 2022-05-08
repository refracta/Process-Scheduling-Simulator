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

    public NTimelineWidget(int endpoint, int numScaleInterval) {
        this();
        this.endpoint = endpoint;
        this.numScaleInterval = numScaleInterval;
    }
    public NTimelineWidget() {
        widthProperty().addListener((observable, oldValue, newValue) -> resized(newValue, getHeight()));
        heightProperty().addListener((observable, oldValue, newValue) -> resized(getWidth(), newValue));
    }

    public void resized(Number width, Number height) {
        init();
    }

    protected NTimelineScalePlatePane scalePlatePane;
    protected VBox timelineVBox;
    protected ScrollPane timelineScrollPane;
    protected NTimelineScalerPane scalerPane;

    public Color getVerticalIndicatorColor() {
        return verticalIndicatorColor;
    }

    public void setVerticalIndicatorColor(Color verticalIndicatorColor) {
        this.verticalIndicatorColor = verticalIndicatorColor;
    }

    public NTimelineScalePlatePane getScalePlatePane() {
        return scalePlatePane;
    }

    public void setScalePlatePane(NTimelineScalePlatePane scalePlatePane) {
        this.scalePlatePane = scalePlatePane;
    }

    public VBox getTimelineVBox() {
        return timelineVBox;
    }

    public void setTimelineVBox(VBox timelineVBox) {
        this.timelineVBox = timelineVBox;
    }

    public ScrollPane getTimelineScrollPane() {
        return timelineScrollPane;
    }

    public void setTimelineScrollPane(ScrollPane timelineScrollPane) {
        this.timelineScrollPane = timelineScrollPane;
    }

    public NTimelineScalerPane getScalerPane() {
        return scalerPane;
    }

    public void setScalerPane(NTimelineScalerPane scalerPane) {
        this.scalerPane = scalerPane;
    }

    private int endpoint = 240;
    private int numScaleInterval = 12;




    public void init() {
        getChildren().clear();

        scalePlatePane = new NTimelineScalePlatePane();
        setTop(scalePlatePane);

        scalerPane = new NTimelineScalerPane(endpoint, numScaleInterval);
        scalerPane.setScalePlatePane(scalePlatePane);
        setBottom(scalerPane);

        timelineVBox = new VBox();
        timelineScrollPane = new ScrollPane(timelineVBox);
        timelineScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        timelineScrollPane.setFitToWidth(true);
        timelineScrollPane.getStyleClass().add("edge-to-edge");
        setCenter(timelineScrollPane);

        scalePlatePane.setUpdateListener((start, range, endpoint1, samplingInterval, unitStep, timelineData) -> {
            timelineVBox.getChildren().stream().forEach(e->{
                NTimelinePane e1 = (NTimelinePane) e;
                e1.update(start,range,endpoint1,samplingInterval,unitStep,timelineData);
            });
        });

//        initVerticalIndicator();
    }

    private void initVerticalIndicator() {
        Line verticalIndicator = new Line();
        verticalIndicator.setStroke(verticalIndicatorColor);

        EventHandler<? super MouseEvent> showVerticalIndicator = (EventHandler<MouseEvent>) event -> {
            verticalIndicator.setStartX(0);
            verticalIndicator.setStartY(0);
            verticalIndicator.setEndX(0);
            verticalIndicator.setEndY(getHeight() - scalerPane.getHeight());

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
