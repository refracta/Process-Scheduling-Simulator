package kr.ac.koreatech.os.pss.app.component.raw.timeline.impl;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import kr.ac.koreatech.os.pss.app.component.raw.timeline.ScaleHandler;
import kr.ac.koreatech.os.pss.app.legacy.LProcessControls;

import java.io.IOException;

public class ProcessTimelineIndexPane extends Pane implements ScaleHandler {

    public ProcessTimelineIndexPane(int maxEndTime, double lengthFactor, double width, double height) throws IOException {
        updateScale(maxEndTime, lengthFactor);
    }

    @Override
    public void updateScale(int maxEndTime, double lengthFactor) {
        try {
            getChildren().remove(0, getChildren().size());
        } catch (Exception exception) {
        }

        for (int i = 0; i < maxEndTime; i++) {
            Pane indexPane = null;
            try {
                indexPane = FXMLLoader.load(LProcessControls.class.getResource("processID.fxml"));
                indexPane.setLayoutX(i * lengthFactor);
                ((Text) indexPane.getChildren().get(0)).setText(Integer.toString(i));
            } catch (IOException exception) {
            }
            getChildren().add(indexPane);
        }
    }
}
