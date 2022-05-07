package kr.ac.koreatech.os.pss.app.component.raw.timeline.impl;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import kr.ac.koreatech.os.pss.app.component.raw.timeline.ScaleHandler;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;

import java.util.concurrent.atomic.AtomicInteger;

public class ProcessTimelinePane extends Pane implements ScaleHandler {
    /**
     * getProcess 실행 시 프로세스의 정수 식별자(id)를 중복없이 자동 생성하기 위한 카운터
     */
    private static final AtomicInteger idCount = new AtomicInteger(0);

    private int id;

    private final TimelineBar timeLineBar;
    private ActionState actionState;


    public ProcessTimelinePane() {
        this(10, 90, 30);
    }

    public ProcessTimelinePane(int maxEndTime, double lengthFactor, double height) {
        this.id = idCount.incrementAndGet();

        this.actionState = ActionState.IDLE;

        this.timeLineBar = new TimelineBar(0, 1, lengthFactor, height);

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

    public static void resetIdCount() {
        idCount.set(0);
    }

    public DefaultProcess getProcess() {
        return new DefaultProcess(id, timeLineBar.getArrivalTime(), timeLineBar.getBurstTime());
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

    public int getProcessId() {
        return id;
    }

    public enum ActionState {
        IDLE, EXTEND_LEFT, EXTEND_RIGHT, MOVE
    }
}
