package kr.ac.koreatech.os.pss.app.component.pane;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import kr.ac.koreatech.os.pss.app.component.raw.n.timeline.NTimelineWidget;
import kr.ac.koreatech.os.pss.app.component.structure.SingleComponent;
import kr.ac.koreatech.os.pss.app.component.utils.RandomUtils;

import java.net.URL;
import java.util.ResourceBundle;

public class ProcessControlPane extends SingleComponent {

    @FXML
    private JFXButton delAllButton;

    @FXML
    public Pane testContainerPane;

    private ProcessTimelineContainerPane containerPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        containerPane = SingleComponent.getInstance(ProcessTimelineContainerPane.class);
    }

    @FXML
    private void addProcess(MouseEvent event) {
        ProcessControlPane instance = SingleComponent.getInstance(ProcessControlPane.class);

        NTimelineWidget nTimelineWidget = new NTimelineWidget();
        nTimelineWidget.bindToTargetPaneHeight(instance.testContainerPane);
        instance.testContainerPane.getChildren().add(nTimelineWidget);
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
