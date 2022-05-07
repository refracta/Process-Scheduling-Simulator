package kr.ac.koreatech.os.pss.app.component.raw.n.timeline;

import javafx.animation.FillTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.util.Duration;
import kr.ac.koreatech.os.pss.app.component.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class NTimelinePane extends NTimelineComponent {
    protected final List<NTimelineElement> elements;
    protected final String name;


    protected Text label;
    protected Shape outline;
    protected Shape deleteButton;
    protected Shape labelOverlay;
    protected FillTransition outlineTransition;
    protected FillTransition deleteButtonTransition;
    protected final Color highlightColor = Color.rgb(25, 99, 146);
    protected final Color outlineColor = Color.rgb(230, 230, 230);

    protected final Color deleteButtonHighlightColor = Color.rgb(246, 21, 0);
    protected final Color deleteButtonHalfHighlightColor = Color.rgb(86, 140, 173);
    protected final Color labelOverlayColor = Color.AQUA;
    // new Color(1, 1, 1, 0.5);


    protected final int HOVER_OVERLAY_VERTICAL_LINE_WIDTH = 8;
    protected final int HOVER_OVERLAY_HORIZONTAL_LINE_HEIGHT = 3;
    protected final int DELETE_BUTTON_WIDTH = 24;
    protected final int MAX_HEIGHT = 39;
    protected final int LABEL_HORIZONTAL_MARGIN = 6;
    protected final int LABEL_FONT_SIZE = 18;
    protected final int LABEL_TOP_MARGIN = 24;

    protected final int TIMELINE_START = HOVER_OVERLAY_VERTICAL_LINE_WIDTH + DELETE_BUTTON_WIDTH;

    public NTimelinePane(String name, List<NTimelineElement> elements) {
        this.name = name;
        this.elements = elements;
        setMinHeight(MAX_HEIGHT);
    }

    public NTimelinePane() {
        this("P" + new Random().nextInt(100), new ArrayList<>());
    }

    protected Shape getOutline() {
        return Shape.subtract(new Rectangle(getWidth(), getHeight()), new Rectangle(HOVER_OVERLAY_VERTICAL_LINE_WIDTH, HOVER_OVERLAY_HORIZONTAL_LINE_HEIGHT, getWidth() - HOVER_OVERLAY_VERTICAL_LINE_WIDTH * 2, getHeight() - HOVER_OVERLAY_HORIZONTAL_LINE_HEIGHT * 2));
    }

    protected Shape getDeleteButton() {
        return new Rectangle(HOVER_OVERLAY_VERTICAL_LINE_WIDTH, HOVER_OVERLAY_HORIZONTAL_LINE_HEIGHT, DELETE_BUTTON_WIDTH, getHeight() - HOVER_OVERLAY_HORIZONTAL_LINE_HEIGHT * 2);
    }

    protected Shape getLabelOverlay() {
        return new Rectangle(TIMELINE_START, HOVER_OVERLAY_HORIZONTAL_LINE_HEIGHT, LABEL_HORIZONTAL_MARGIN * 2 + label.getBoundsInLocal().getWidth(), getHeight() - HOVER_OVERLAY_HORIZONTAL_LINE_HEIGHT * 2);
    }

    @Override
    public void init() {
        getChildren().clear();

        label = TextUtils.getDefaultText(name, LABEL_FONT_SIZE);
        label.setX(TIMELINE_START + LABEL_HORIZONTAL_MARGIN);
        label.setY(LABEL_TOP_MARGIN);

        outline = getOutline();
        outline.setFill(outlineColor);

        AtomicBoolean isMouseHover = new AtomicBoolean(false);
        deleteButton = getDeleteButton();
        deleteButton.setFill(outlineColor);
        deleteButton.setOnMouseEntered(event -> {
            deleteButtonTransition.stop();
            deleteButton.setFill(deleteButtonHighlightColor);
        });

        deleteButton.setOnMouseExited(event -> {
            if (isMouseHover.get()) {
                deleteButton.setFill(deleteButtonHalfHighlightColor);
            } else {
                deleteButton.setFill(outlineColor);
            }
        });

        getChildren().add(outline);
        getChildren().add(deleteButton);
        getChildren().add(label);

        label.applyCss();
        labelOverlay = getLabelOverlay();
        labelOverlay.setFill(labelOverlayColor);
        getChildren().add(1, labelOverlay);

        setOnMouseEntered(event -> {
            isMouseHover.set(true);
            label.setText("");
            labelOverlay.setFill(Color.TRANSPARENT);
            outlineTransition = new FillTransition(Duration.millis(150), outline, outlineColor, highlightColor);
            outlineTransition.play();
            deleteButtonTransition = new FillTransition(Duration.millis(150), deleteButton, outlineColor, deleteButtonHalfHighlightColor);
            deleteButtonTransition.play();
        });

        setOnMouseExited(event -> {
            isMouseHover.set(false);
            label.setText(name);
            labelOverlay.setFill(labelOverlayColor);
            outlineTransition.stop();
            deleteButtonTransition.stop();
            deleteButton.setFill(outlineColor);
            outline.setFill(outlineColor);
        });
    }
}
