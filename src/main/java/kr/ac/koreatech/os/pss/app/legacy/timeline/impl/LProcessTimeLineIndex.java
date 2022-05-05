package kr.ac.koreatech.os.pss.app.legacy.timeline.impl;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import kr.ac.koreatech.os.pss.app.legacy.LProcessControls;
import kr.ac.koreatech.os.pss.app.legacy.timeline.LAbstractTimeLine;

import java.io.IOException;

public class LProcessTimeLineIndex extends LAbstractTimeLine {

    public LProcessTimeLineIndex(int criteriaEndTime, double width, double height, LProcessControls processControls) throws IOException {
        super(width, height, processControls);

        updateScale(criteriaEndTime);
    }

    @Override
    public void updateScale(int criteriaEndTime) {
        try {
            getChildren().remove(0, getChildren().size());
        } catch (Exception exception) { }

        for (int i = 0; i < criteriaEndTime; i++) {
            Pane indexPane = null;
            try {
                indexPane = FXMLLoader.load(LProcessControls.class.getResource("processID.fxml"));
                indexPane.setLayoutX(i * processControls.getLengthFactor());
                ((Text) indexPane.getChildren().get(0)).setText(Integer.toString(i));
            } catch (IOException exception) { }
            getChildren().add(indexPane);
        }
    }
}
