package kr.ac.koreatech.os.pss.app.legacy.timeline.impl;

import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import kr.ac.koreatech.os.pss.app.legacy.LProcessControls;
import kr.ac.koreatech.os.pss.app.legacy.timeline.LAbstractTimeLine;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;

import java.io.IOException;

public class LProcessTimeLine extends LAbstractTimeLine {
    private final DefaultProcess process;
    private final LTimeLineBar timeLineBar;

    public LProcessTimeLine(int criteriaEndTime, double width, double height, LProcessControls processControls) throws IOException {
        super(width, height, processControls);

        this.setOnMouseMoved(this::onMouseMoved);
        this.setOnMouseExited(this::onMouseExited);
        this.setOnMouseDragged(this::onMouseDragged);
        this.setOnMouseReleased(this::onMouseReleased);

        this.process = new DefaultProcess(0, 1);
        this.timeLineBar = new LTimeLineBar(process.getArrivalTime(), process.getBurstTime(), width / criteriaEndTime, height);

        updateScale(criteriaEndTime);
    }

    @Override
    public void updateScale(int criteriaEndTime) {
        try {
            getChildren().remove(0, getChildren().size());
        } catch (Exception exception) {
        }
        for (int i = 0; i <= criteriaEndTime; i++) {
            this.getChildren().add(new Line(i * processControls.getLengthFactor(), 0, i * processControls.getLengthFactor(), 30));
        }
        this.getChildren().add(this.timeLineBar);

        timeLineBar.update(timeLineBar.getArrivalTime(), timeLineBar.getBurstTime(), processControls.getLengthFactor());
        timeLineBar.setLayoutX(timeLineBar.getArrivalTime() * processControls.getLengthFactor());
    }

    public void onMouseMoved(MouseEvent event) {
        double processedX = event.getX() - timeLineBar.getLayoutX();

        boolean leftCondition = timeLineBar.isInLeft(processedX);
        boolean rightCondition = timeLineBar.isInRight(processedX, processControls.getLengthFactor());
        boolean middleCondition = timeLineBar.isInMiddle(processedX, processControls.getLengthFactor());

        if (leftCondition || rightCondition || middleCondition) {
            if (leftCondition || rightCondition)
                setCursor(Cursor.H_RESIZE);
            else
                setCursor(Cursor.DEFAULT);
            timeLineBar.makeTransparent();
        } else {
            timeLineBar.makeOpaque();
            setCursor(Cursor.DEFAULT);
        }
    }

    public void onMouseExited(MouseEvent event) {
        timeLineBar.makeOpaque();
        setCursor(Cursor.DEFAULT);
    }

    public void onMouseDragged(MouseEvent event) {
        double processedX = event.getX() - timeLineBar.getLayoutX();

        if (actionState == ActionState.IDLE) {
            if (timeLineBar.isInMiddle(processedX, processControls.getLengthFactor()))
                actionState = ActionState.MOVE;
            else if (timeLineBar.isInLeft(processedX))
                actionState = ActionState.EXTEND_LEFT;
            else if (timeLineBar.isInRight(processedX, processControls.getLengthFactor()))
                actionState = ActionState.EXTEND_RIGHT;
        }

        switch (actionState) {
            case EXTEND_LEFT:
                setCursor(Cursor.H_RESIZE);
                double newWidth = timeLineBar.getLayoutX() + timeLineBar.getWidth() - Math.max(0, event.getX());
                timeLineBar.setLayoutX(Math.max(0, event.getX()));
                timeLineBar.setWidth(newWidth);
                break;
            case EXTEND_RIGHT:
                setCursor(Cursor.H_RESIZE);
                newWidth = event.getX() - timeLineBar.getLayoutX();
                timeLineBar.setWidth(newWidth);
                break;
            case MOVE:
                timeLineBar.setLayoutX(Math.max(0, event.getX() - timeLineBar.getWidth() / 2));
                break;
        }
    }

    public void onMouseReleased(MouseEvent event) {
        switch (actionState) {
            case EXTEND_LEFT:
                int index = timeLineBar.getLeftExpendedIndex(event.getX(), processControls.getLengthFactor());
                timeLineBar.update(index, timeLineBar.getEndTime() - index, processControls.getLengthFactor());
                timeLineBar.setLayoutX(timeLineBar.getArrivalTime() * processControls.getLengthFactor());
                break;
            case EXTEND_RIGHT:
                index = timeLineBar.getRightExpendedIndex(event.getX(), processControls.getLengthFactor());
                timeLineBar.update(timeLineBar.getArrivalTime(), index - timeLineBar.getArrivalTime(), processControls.getLengthFactor());
                break;
            case MOVE:
                index = timeLineBar.getMovedIndex(event.getX() - timeLineBar.getWidth() / 2, processControls.getLengthFactor());
                timeLineBar.update(index, timeLineBar.getBurstTime(), processControls.getLengthFactor());
                timeLineBar.setLayoutX(timeLineBar.getArrivalTime() * processControls.getLengthFactor());
                break;
        }

        process.setArrivalTime(timeLineBar.getArrivalTime());
        process.setBurstTime(timeLineBar.getBurstTime());

        setCursor(Cursor.DEFAULT);
        actionState = ActionState.IDLE;

        processControls.updateAllScales();
    }

    public DefaultProcess getProcess() {
        return process;
    }
}
