package kr.ac.koreatech.os.pss.app.legacy.timeline;

import javafx.scene.layout.Pane;
import kr.ac.koreatech.os.pss.app.legacy.LProcessControls;

import java.io.IOException;

public abstract class LAbstractTimeLine extends Pane {
    protected ActionState actionState;

    protected LProcessControls processControls;

    protected LAbstractTimeLine(double width, double height, LProcessControls processControls) {
        super();

        this.actionState = ActionState.IDLE;
        this.processControls = processControls;
    }

    public abstract void updateScale(int criteriaEndTime);

    protected enum ActionState {
        IDLE, EXTEND_LEFT, EXTEND_RIGHT, MOVE
    }
}
