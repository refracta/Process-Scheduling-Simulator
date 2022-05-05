package kr.ac.koreatech.os.pss.visualizer.timeline.impl;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import kr.ac.koreatech.os.pss.visualizer.ProcessControls;
import kr.ac.koreatech.os.pss.visualizer.timeline.AbstractTimeLine;

import java.io.IOException;

public class ProcessTimeLineIndex extends AbstractTimeLine {

    public ProcessTimeLineIndex(int criteriaEndTime, double width, double height, ProcessControls processControls) throws IOException {
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
                indexPane = FXMLLoader.load(ProcessControls.class.getResource("fxml/processID.fxml"));
                indexPane.setLayoutX(i * processControls.getLengthFactor());
                ((Text) indexPane.getChildren().get(0)).setText(Integer.toString(i));
            } catch (IOException exception) { }
            getChildren().add(indexPane);
        }
    }
}