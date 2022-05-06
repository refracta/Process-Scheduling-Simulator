package kr.ac.koreatech.os.pss.app.component.pane;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import kr.ac.koreatech.os.pss.app.component.structure.SingleComponent;
import kr.ac.koreatech.os.pss.app.legacy.timeline.LAbstractTimeLine;
import kr.ac.koreatech.os.pss.app.legacy.timeline.impl.LProcessTimeLine;
import kr.ac.koreatech.os.pss.app.loader.annotation.CreatableController;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@CreatableController
public class ProcessTimelineContainerPane extends SingleComponent {
    @FXML
    private VBox processIDVBox;
    @FXML
    private VBox processVBox;
    @FXML
    private VBox processDelVBox;

    @FXML
    private ScrollPane processIDScrollPane;
    @FXML
    private ScrollPane processScrollPane;
    @FXML
    private ScrollPane processDelScrollPane;

    private double width;
    private double height;

    private List<ProcessTimelinePane> processTimeLines;
    private int criteriaEndTime;
    private int maxEndTime;
    private double lengthFactor;

    public ProcessTimelineContainerPane() {
    }

    public ProcessTimelineContainerPane(int criteriaEndTime, double width, double height) throws IOException {
        ProcessTimelineContainerPane processTimelineContainerPane = new ProcessTimelineContainerPane(criteriaEndTime, 800, 20);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        this.width = 800;
        this.height = 30;

        this.processTimeLines = new ArrayList<>();
        this.criteriaEndTime = 10;
        this.lengthFactor = this.width / this.criteriaEndTime;

        // 스크롤 관련 이벤트 핸들러 시작.
        processIDScrollPane.vvalueProperty().addListener((observable, oldValue, newValue) -> {
            processScrollPane.vvalueProperty().setValue(newValue.doubleValue());
            processDelScrollPane.vvalueProperty().setValue(newValue.doubleValue());
        });

        processScrollPane.vvalueProperty().addListener((observable, oldValue, newValue) -> {
            processIDScrollPane.vvalueProperty().setValue(newValue.doubleValue());
            processDelScrollPane.vvalueProperty().setValue(newValue.doubleValue());
        });

        processDelScrollPane.vvalueProperty().addListener((observable, oldValue, newValue) -> {
            processIDScrollPane.vvalueProperty().setValue(newValue.doubleValue());
            processScrollPane.vvalueProperty().setValue(newValue.doubleValue());
        });
        // 스크롤 관련 이벤트 핸들러 끝.
    }

    @FXML
    public void delProcess(MouseEvent event) throws IOException {
        JFXButton target = (JFXButton) event.getSource();
        int index = ((VBox) target.getParent().getParent()).getChildren().indexOf(target.getParent());

        processIDVBox.getChildren().remove(index);
        processVBox.getChildren().remove(index);
        processDelVBox.getChildren().remove(index);
        processTimeLines.remove(index);

        updateAllScales();
    }

    public List<DefaultProcess> getprocess() {
        List<DefaultProcess> processList = new ArrayList<DefaultProcess>();
        for (ProcessTimelinePane p : processTimeLines) {
            processList.add(((ProcessTimelinePane)p).getProcess());
        }
        return processList;
    }

    public void updateAllScales() {
        maxEndTime = processTimeLines.stream().mapToInt(p -> ((ProcessTimelinePane) p).getProcess().getEndTime()).max().getAsInt();
        processTimeLines.forEach(p -> p.updateScale(Math.max(criteriaEndTime, maxEndTime), lengthFactor));
    }

    public void setLengthFactor(double lengthFactor) {
        this.lengthFactor = lengthFactor;
    }

    public void setCriteriaEndTime(int criteriaEndTime) {
        this.criteriaEndTime = criteriaEndTime;
        this.lengthFactor = width / criteriaEndTime;
    }

    public double getLengthFactor() {
        return lengthFactor;
    }

    public int getMaxEndTime() {
        return maxEndTime;
    }

    public int getCriteriaEndTime() {
        return criteriaEndTime;
    }

    public void delAllprocess() {
        processIDVBox.getChildren().clear();
        processVBox.getChildren().clear();
        processDelVBox.getChildren().clear();
        processTimeLines.clear();

        updateAllScales();
    }

    public VBox getProcessIDVBox() {
        return processIDVBox;
    }

    public VBox getProcessVBox() {
        return processVBox;
    }

    public VBox getProcessDelVBox() {
        return processDelVBox;
    }

    public List<ProcessTimelinePane> getProcessTimeLInes() {
        return processTimeLines;
    }
}