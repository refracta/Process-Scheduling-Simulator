package kr.ac.koreatech.os.pss.app.component.raw.timeline.impl;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import kr.ac.koreatech.os.pss.app.component.raw.timeline.ScaleHandler;
import kr.ac.koreatech.os.pss.app.component.utils.GridPaneUtils;
import kr.ac.koreatech.os.pss.app.component.utils.TextUtils;

public class ProcessTimelineIndexPane extends Pane implements ScaleHandler {

    public ProcessTimelineIndexPane(int maxEndTime, double lengthFactor, double width, double height) {
        updateScale(maxEndTime, lengthFactor);
    }

    @Override
    public void updateScale(int maxEndTime, double lengthFactor) {
        getChildren().clear();
        for (int i = 0; i < maxEndTime; i++) {
            Text index = TextUtils.getDefaultText(Integer.toString(i), 20);
            GridPane indexGridPane = GridPaneUtils.wrap(index);
            indexGridPane.setAlignment(Pos.CENTER_LEFT);
            indexGridPane.setPrefWidth(lengthFactor);
            indexGridPane.setPrefHeight(30);
            indexGridPane.setLayoutX(i * lengthFactor);
            getChildren().add(indexGridPane);
        }
    }
}
