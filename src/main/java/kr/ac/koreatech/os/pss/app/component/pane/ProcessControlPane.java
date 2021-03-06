package kr.ac.koreatech.os.pss.app.component.pane;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import kr.ac.koreatech.os.pss.app.component.structure.SingleComponent;
import kr.ac.koreatech.os.pss.app.component.utils.RandomUtils;

import java.net.URL;
import java.util.ResourceBundle;

public class ProcessControlPane extends SingleComponent {
    @FXML
    private JFXSlider criteriaEndTimeSlider;

    @FXML
    private JFXButton delAllButton;

    @FXML
    private GridPane processTimelineContainerPane;

    private ProcessTimelineContainerPane containerPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        containerPane = SingleComponent.getInstance(ProcessTimelineContainerPane.class);

        // 축적 슬라이더 이벤트 핸들러.
        criteriaEndTimeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            containerPane.setCriteriaEndTime(newValue.intValue());
            containerPane.updateAllScales();
        });
    }

    @FXML
    private void addProcess(MouseEvent event) {
        containerPane.addTimeline();
    }

    @FXML
    private void addRandomProcess(MouseEvent event) {
        int arrivalTime = RandomUtils.random.nextInt(containerPane.getCriteriaEndTime());
        int burstTime = RandomUtils.random.nextInt(containerPane.getCriteriaEndTime() - arrivalTime) + 1;

        containerPane.addTimeline(arrivalTime, burstTime);
    }

    @FXML
    private void deleteProcess(MouseEvent event) {
        containerPane.deleteAllTimeline();
    }
}
