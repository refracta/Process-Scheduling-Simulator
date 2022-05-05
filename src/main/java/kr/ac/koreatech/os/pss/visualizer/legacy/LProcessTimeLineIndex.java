package kr.ac.koreatech.os.pss.visualizer.legacy;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;

public class LProcessTimeLineIndex extends LProcessTimeLine {
    public static LProcessTimeLineIndex getProcessTimeLineIndex() throws IOException {
        LProcessTimeLineIndex processTimeLineIndex = new LProcessTimeLineIndex();
        return processTimeLineIndex;
    }

    private LProcessTimeLineIndex() throws IOException {
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
