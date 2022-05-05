package kr.ac.koreatech.os.pss.visualizer;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;

public class ProcessTimeLineIndex extends ProcessTimeLine {
    public static ProcessTimeLineIndex getProcessTimeLineIndex() throws IOException {
        ProcessTimeLineIndex processTimeLineIndex = new ProcessTimeLineIndex();
        return processTimeLineIndex;
    }

    private ProcessTimeLineIndex() throws IOException {
        super();
    }

    @Override
    public void init() {
        height = 30;
        lengthFactor = 800 / criteriaEndTime;
        for (int i = 0; i < criteriaEndTime; i++) {
            Pane index;
            try {
                index = FXMLLoader.load(getClass().getResource("processID.fxml"));
                ((Text) index.getChildren().get(0)).setText(Integer.toString(i));
                index.setLayoutX(lengthFactor * i);
                index.setMinWidth(lengthFactor);
                index.setPrefWidth(lengthFactor);
                index.setMaxWidth(lengthFactor);
                pane.getChildren().add(index);
            } catch (Exception e) { }
        }
    }
}
