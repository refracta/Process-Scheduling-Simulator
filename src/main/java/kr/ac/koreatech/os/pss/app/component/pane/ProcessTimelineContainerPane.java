package kr.ac.koreatech.os.pss.app.component.pane;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import kr.ac.koreatech.os.pss.app.component.raw.timeline.impl.UnionTimeline;
import kr.ac.koreatech.os.pss.app.component.structure.SingleComponent;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ProcessTimelineContainerPane extends SingleComponent {
    @FXML
    private VBox processNameVBox;
    @FXML
    private VBox processTimelineVBox;
    @FXML
    private VBox processDeleteVBox;

    @FXML
    private ScrollPane processIDScrollPane;
    @FXML
    private ScrollPane processScrollPane;
    @FXML
    private ScrollPane processDelScrollPane;

    private double width;
    private double height;

    private List<UnionTimeline> processTimeLines;

    private int criteriaEndTime;
    private int maxEndTime;
    private double lengthFactor;


    public ProcessTimelineContainerPane() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        this.width = 850;
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

    public void addTimeline() {
        UnionTimeline unionTimeline = UnionTimeline.create(this, lengthFactor);
        processNameVBox.getChildren().add(unionTimeline.getWrappedIdText());
        processDeleteVBox.getChildren().add(unionTimeline.getWrappedDeleteButton());
        processTimelineVBox.getChildren().add(unionTimeline.getTimeline());
        processTimeLines.add(unionTimeline);
    }

    public void deleteTimeline(UnionTimeline unionTimeline) {
        int index = processTimeLines.indexOf(unionTimeline);
        processNameVBox.getChildren().remove(index);
        processTimelineVBox.getChildren().remove(index);
        processDeleteVBox.getChildren().remove(index);
        processTimeLines.remove(index);
        updateAllScales();
    }

    public void deleteAllTimeline() {
        processNameVBox.getChildren().clear();
        processTimelineVBox.getChildren().clear();
        processDeleteVBox.getChildren().clear();
        processTimeLines.clear();
        updateAllScales();
    }

    public List<DefaultProcess> getProcessList() {
        List<DefaultProcess> processList = new ArrayList<DefaultProcess>();
        for (UnionTimeline t : processTimeLines) {
            processList.add(t.getTimeline().getProcess());
        }
        return processList;
    }


    public void updateAllScales() {
        if (processTimeLines.isEmpty()) return;
        maxEndTime = getProcessList().stream().mapToInt(DefaultProcess::getEndTime).max().getAsInt();
        processTimeLines.forEach(t -> t.getTimeline().updateScale(Math.max(criteriaEndTime, maxEndTime), lengthFactor));
    }

    public void setCriteriaEndTime(int criteriaEndTime) {
        this.criteriaEndTime = criteriaEndTime;
        this.lengthFactor = width / criteriaEndTime;
    }

    public void setLengthFactor(double lengthFactor) {
        this.lengthFactor = lengthFactor;
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

    public VBox getProcessNameVBox() {
        return processNameVBox;
    }

    public VBox getProcessTimelineVBox() {
        return processTimelineVBox;
    }

    public VBox getProcessDeleteVBox() {
        return processDeleteVBox;
    }

    public List<UnionTimeline> getProcessTimelines() {
        return processTimeLines;
    }
}
