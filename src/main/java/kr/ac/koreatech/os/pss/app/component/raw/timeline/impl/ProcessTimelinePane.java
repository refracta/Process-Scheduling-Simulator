package kr.ac.koreatech.os.pss.app.component.raw.timeline.impl;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import kr.ac.koreatech.os.pss.app.component.raw.timeline.ScaleHandler;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;

public class ProcessTimelinePane extends Pane implements ScaleHandler {
    private ActionState actionState;
    private final DefaultProcess process;
    private final TimelineBar timeLineBar;


    public ProcessTimelinePane() {
        this(10, 90, 30);
    }

    public ProcessTimelinePane(int maxEndTime, double lengthFactor, double height) {
        this.actionState = ActionState.IDLE;

        this.process = new DefaultProcess(0, 1);
        this.timeLineBar = new TimelineBar(process.getArrivalTime(), process.getBurstTime(), lengthFactor, height);

        updateScale(maxEndTime, lengthFactor);
    }

    @Override
    public void updateScale(int maxEndTime, double lengthFactor) {
        getChildren().clear();
        for (int i = 0; i < maxEndTime; i++)
            this.getChildren().add(new Line(i * lengthFactor, 0, i * lengthFactor, 30));

        this.getChildren().add(this.timeLineBar);

        timeLineBar.update(timeLineBar.getArrivalTime(), timeLineBar.getBurstTime(), lengthFactor);
        timeLineBar.setLayoutX(timeLineBar.getArrivalTime() * lengthFactor);
    }

    public DefaultProcess getProcess() {
        return process;
    }

    public TimelineBar getTimelineBar() {
        return timeLineBar;
    }

    public ActionState getActionState() {
        return actionState;
    }

    public void setActionState(ActionState actionState) {
        this.actionState = actionState;
    }

    public enum ActionState {
        IDLE, EXTEND_LEFT, EXTEND_RIGHT, MOVE
    }
}
