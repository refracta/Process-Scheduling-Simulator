package kr.ac.koreatech.os.pss.app.component.raw.n.timeline;

import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;

public class NTimelineBarPane extends Pane {
    public enum ActionState {
        IDLE, RESIZE_LEFT, RESIZE_RIGHT, MOVE
    }

    protected static final int OUTLINE_CURSOR_SELECTION_RANGE = 5;
    protected final Color outlineColor;
    protected Rectangle innerRectangle;
    protected Line leftOutline;
    protected Line rightOutline;

    protected double fullWidth;
    protected double minWidth = 10;

    protected Optional<BiConsumer<Double, Double>> scaleUpdateListener = Optional.empty();

    private Line getVerticalOutline() {
        Line line = new Line(0, 0, 0, innerRectangle.getHeight() - 2);
        line.setStrokeWidth(1);
        line.setTranslateY(1);
        line.setStroke(outlineColor);
        return line;
    }

    public double getBoxLeft() {
        return innerRectangle.getX();
    }

    public double getBoxRight() {
        return innerRectangle.getX() + innerRectangle.getWidth();
    }

    public boolean isValidWidth(double width) {
        return 0 < width && width <= fullWidth;
    }

    public void setBoxLeft(double left) {
        left = left < 0 ? 0 : left;
        double width = innerRectangle.getWidth() + getBoxLeft() - left;
        if (isValidWidth(width) && width >= minWidth) {
            innerRectangle.setWidth(width);
            innerRectangle.setX(left);
            fitOutline();
            scaleUpdateListener.ifPresent(l -> l.accept(start(), range()));
        }
    }

    public void move(double delta) {
        double moveX;
        if (innerRectangle.getX() + delta < 0) {
            moveX = 0;
        } else if (innerRectangle.getX() + innerRectangle.getWidth() + delta > fullWidth) {
            moveX = fullWidth - innerRectangle.getWidth();
        } else {
            moveX = innerRectangle.getX() + delta;
        }
        innerRectangle.setX(moveX);
        fitOutline();
        scaleUpdateListener.ifPresent(l -> l.accept(start(), range()));

    }

    public void setBoxRight(double right) {
        right = right > fullWidth ? fullWidth : right;
        double width = innerRectangle.getWidth() + right - getBoxRight();
        if (isValidWidth(width) && width >= minWidth) {
            innerRectangle.setWidth(width);
            fitOutline();
            scaleUpdateListener.ifPresent(l -> l.accept(start(), range()));
        }
    }

    public NTimelineBarPane(Rectangle innerRectangle, Color outlineColor) {
        this.innerRectangle = innerRectangle;
        this.fullWidth = innerRectangle.getWidth();
        this.outlineColor = outlineColor;


        leftOutline = getVerticalOutline();
        leftOutline.setTranslateX(1);
        rightOutline = getVerticalOutline();
        rightOutline.setTranslateX(-1);

        fitOutline();
        getChildren().add(innerRectangle);
        getChildren().add(leftOutline);
        getChildren().add(rightOutline);

        AtomicReference<ActionState> state = new AtomicReference<>(ActionState.IDLE);
        addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
            double boxLeft = getBoxLeft();
            double boxRight = getBoxRight();
            if (boxLeft - OUTLINE_CURSOR_SELECTION_RANGE <= event.getX() && event.getX() <= boxLeft + OUTLINE_CURSOR_SELECTION_RANGE) {
                state.set(ActionState.RESIZE_LEFT);
                setCursor(Cursor.H_RESIZE);
            } else if (boxRight - OUTLINE_CURSOR_SELECTION_RANGE <= event.getX() && event.getX() <= boxRight + OUTLINE_CURSOR_SELECTION_RANGE) {
                state.set(ActionState.RESIZE_RIGHT);
                setCursor(Cursor.H_RESIZE);
            } else {
                state.set(ActionState.MOVE);
                setCursor(Cursor.MOVE);
            }
        });
        addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            setCursor(Cursor.DEFAULT);
        });
        AtomicReference<Double> lastPressedX = new AtomicReference<>(0.0);

        addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            lastPressedX.set(event.getX());
        });

        addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            double x = event.getX();
            if (state.get() == ActionState.RESIZE_LEFT) {
                setBoxLeft(x);
                setCursor(Cursor.H_RESIZE);
            } else if (state.get() == ActionState.RESIZE_RIGHT) {
                setBoxRight(x);
                setCursor(Cursor.H_RESIZE);
            } else if (state.get() == ActionState.MOVE) {
                double delta = x - lastPressedX.get();
                move(delta);
                lastPressedX.set(x);
                setCursor(Cursor.MOVE);
            }
        });
    }

    public void fitOutline() {
        leftOutline.setLayoutX(innerRectangle.getX());
        leftOutline.setLayoutY(innerRectangle.getY());
        rightOutline.setLayoutX(innerRectangle.getX() + innerRectangle.getWidth());
        rightOutline.setLayoutY(innerRectangle.getY());
    }

    public double start() {
        return innerRectangle.getX() / fullWidth;
    }

    public double range() {
        return innerRectangle.getWidth() / fullWidth;
    }

    public BiConsumer<Double, Double> getScaleUpdateListener() {
        return scaleUpdateListener.get();
    }

    public void setScaleUpdateListener(BiConsumer<Double, Double> scaleUpdateListener) {
        this.scaleUpdateListener = Optional.of(scaleUpdateListener);
    }
}
