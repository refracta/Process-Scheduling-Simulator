package kr.ac.koreatech.os.pss.app.component.raw.n.timeline;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public interface NTimelineElement {
    Color getColor();

    int getStartIndex();

    int getEndIndex();

    default int size() {
        return getEndIndex() - getStartIndex();
    }

    default void onHoverEvent(Pane pane) {
    }
}
