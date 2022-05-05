package kr.ac.koreatech.os.pss.visualizer.timeline;

import javafx.scene.layout.Pane;
import kr.ac.koreatech.os.pss.visualizer.ProcessControls;

import java.io.IOException;

public abstract class AbstractTimeLine extends Pane {
    protected enum ActionState {
        IDLE, EXTEND_LEFT, EXTEND_RIGHT, MOVE
    };
    protected ActionState actionState;

    protected ProcessControls processControls;

    protected AbstractTimeLine(double width, double height, ProcessControls processControls) throws IOException {
        super();

        this.actionState = ActionState.IDLE;
        this.processControls = processControls;
    }

    public abstract void updateScale(int criteriaEndTime);
}