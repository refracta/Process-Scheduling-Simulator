package kr.ac.koreatech.os.pss.app.component.raw.n.timeline;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import kr.ac.koreatech.os.pss.app.component.utils.TextUtils;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Optional;
import java.util.function.Function;

public class NTimelineScalerPane extends NTimelineComponent {
    protected double endpoint;
    protected int numScaleInterval;
    protected int HORIZONTAL_MARGIN = 8;
    protected int VERTICAL_MARGIN = 7;
    protected int MAX_HEIGHT = 43;
    protected int TOP_MARGIN = 7;
    protected int BOTTOM_MARGIN = 10;
    protected int SEARCH_ICON_SIZE = 17;
    protected int SEARCH_ICON_HORIZONTAL_MARGIN = 3;
    protected int SEARCH_ICON_VERTICAL_MARGIN = 1;
    protected int SEARCH_ICON_BOX_SIZE = 24;
    protected int SCALER_SCALE_HEIGHT = SEARCH_ICON_BOX_SIZE / 6;

    protected Color highlightColor = Color.rgb(25, 99, 146);
    protected Color backgroundColor = Color.rgb(230, 230, 230);
    protected Color searchIconColor = Color.WHITE;
    protected Color searchIconBoxColor = highlightColor;
    protected Color scalerBoxColor = Color.rgb(255, 173, 68);
    protected Color scalerBoxOutlineColor = Color.rgb(178, 150, 72);
    protected Color scalerBackgroundBoxColor = Color.WHITE;
    protected Color scalerLineColor = Color.BLACK;

    protected Pane searchButtonPane;
    protected NTimelineBarPane scalerBoxPane;
    protected Shape scalerBackgroundBox;
    protected Shape scalerLine;

    protected Optional<NTimelineChangeListener> changeListener = Optional.empty();

    public NTimelineScalerPane(int endpoint, int numScaleInterval) {
        this.endpoint = endpoint;
        this.numScaleInterval = numScaleInterval;
        setMinHeight(MAX_HEIGHT);
    }

    Function<Double, String> scalerLabelMapper = aDouble -> aDouble.intValue() + "T";

    public Pane getSearchButtonPane() {
        Pane pane = new Pane();
        Rectangle rectangle = new Rectangle(SEARCH_ICON_BOX_SIZE, SEARCH_ICON_BOX_SIZE);
        rectangle.setFill(searchIconBoxColor);

        FontIcon searchIcon = new FontIcon(FontAwesomeSolid.SEARCH);
        searchIcon.setIconSize(SEARCH_ICON_SIZE);
        searchIcon.setX(SEARCH_ICON_HORIZONTAL_MARGIN);
        searchIcon.setY(searchIcon.getBoundsInLocal().getHeight() + SEARCH_ICON_VERTICAL_MARGIN);
        searchIcon.setFill(searchIconColor);

        pane.getChildren().add(rectangle);
        pane.getChildren().add(searchIcon);

        pane.setLayoutX(HORIZONTAL_MARGIN);
        pane.setLayoutY(VERTICAL_MARGIN);
        return pane;
    }


    public NTimelineBarPane getScalerBoxPane() {
        Rectangle boxRectangle = new Rectangle(getScalerWidth(), SEARCH_ICON_BOX_SIZE);
        boxRectangle.setFill(scalerBoxColor);
        NTimelineBarPane box = new NTimelineBarPane(boxRectangle, scalerBoxOutlineColor);
        box.setBarUpdateListener((start, range) -> changeListener.ifPresent(c -> c.change(start, range, endpoint)));
        box.setLayoutX(SCALER_START_X);
        box.setLayoutY(VERTICAL_MARGIN);
        return box;
    }

    public Shape getScalerBackgroundBox() {
        Rectangle box = new Rectangle(SCALER_START_X, VERTICAL_MARGIN, getScalerWidth(), SEARCH_ICON_BOX_SIZE);
        box.setFill(scalerBackgroundBoxColor);
        return box;
    }

    public Shape getScalerLine() {
        Line line = new Line(SCALER_START_X + 1, VERTICAL_MARGIN + SEARCH_ICON_BOX_SIZE + 1, getWidth() - HORIZONTAL_MARGIN - 1, VERTICAL_MARGIN + SEARCH_ICON_BOX_SIZE + 1);
        line.setStrokeWidth(2);
        line.setStroke(scalerLineColor);
        return line;
    }

    protected int SCALER_START_X = HORIZONTAL_MARGIN + SEARCH_ICON_BOX_SIZE;

    protected double getScalerWidth() {
        return getWidth() - HORIZONTAL_MARGIN * 2 - SEARCH_ICON_BOX_SIZE;
    }

    @Override
    public void init() {
        setBackground(new Background(new BackgroundFill(backgroundColor, new CornerRadii(0), Insets.EMPTY)));

        getChildren().clear();
        getChildren().add(scalerBackgroundBox = getScalerBackgroundBox());
        getChildren().add(scalerBoxPane = getScalerBoxPane());
        System.out.println("inited");


        getChildren().add(scalerLine = getScalerLine());
        getChildren().add(searchButtonPane = getSearchButtonPane());
        // 1200 / 12 = 100

        double unitStep = endpoint / (numScaleInterval * 2);
        double scalerWidth = getScalerWidth();
        for (int i = 1; i < numScaleInterval * 2; i++) {
            double position = unitStep * i;
            double renderPosition = +SCALER_START_X + (position / endpoint) * scalerWidth;

            if (i % 2 == 1) {
                Line line = new Line(renderPosition, VERTICAL_MARGIN + SEARCH_ICON_BOX_SIZE / 2 - (SCALER_SCALE_HEIGHT / 2), renderPosition, VERTICAL_MARGIN + SEARCH_ICON_BOX_SIZE / 2 + (SCALER_SCALE_HEIGHT / 2));
                line.setPickOnBounds(false);
                getChildren().add(line);
            } else {
                Text defaultText = TextUtils.getDefaultText(scalerLabelMapper.apply(position), 14);
                defaultText.setPickOnBounds(false);
                getChildren().add(defaultText);
                defaultText.applyCss();

                defaultText.setX(renderPosition - defaultText.getBoundsInLocal().getWidth() / 2);
                defaultText.setY(VERTICAL_MARGIN + defaultText.getBoundsInLocal().getHeight() + 2);
            }
        }
        if(lastStart != null && lastRange != null){
            setScale(lastStart, lastRange);
        }
    }

    public void removeEventHandlerAtSearchButtonPane(EventType<? super Event> eventType, EventHandler<? super Event> eventFilter) {
        searchButtonPane.removeEventHandler(eventType, eventFilter);
    }

    public void addEventHandlerAtSearchButtonPane(EventType<? super Event> eventType, EventHandler<? super Event> eventFilter) {
        searchButtonPane.addEventHandler(eventType, eventFilter);
    }

    public double getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(double endpoint) {
        this.endpoint = endpoint;
    }

    public void setScalePlatePane(NTimelineScalePlatePane scalePlatePane) {
        this.changeListener = Optional.of((start, range, endpoint) -> scalePlatePane.drawScale(start, range, endpoint));
        scalePlatePane.drawScale(0, 1, endpoint);
    }
    Double lastStart;
    Double lastRange;
    public void setScale(double start, double range) {
        this.lastStart = start;
        this.lastRange = range;
        double width = getScalerWidth();
        start = start * width;
        double end = start + width * range;
        if(scalerBoxPane != null){
            scalerBoxPane.setBoxLeft(start * width);
            scalerBoxPane.setBoxRight(end);
        }
    }
}
