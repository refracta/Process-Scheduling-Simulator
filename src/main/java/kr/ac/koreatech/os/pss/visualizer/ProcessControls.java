package kr.ac.koreatech.os.pss.visualizer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;
import kr.ac.koreatech.os.pss.visualizer.timeline.AbstractTimeLine;
import kr.ac.koreatech.os.pss.visualizer.timeline.impl.ProcessTimeLine;
import kr.ac.koreatech.os.pss.visualizer.timeline.impl.ProcessTimeLineIndex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProcessControls extends GridPane {
    private GridPane pane;

    @FXML
    private VBox processesIDVBox;
    @FXML
    private VBox processesVBox;
    @FXML
    private VBox processesDelVBox;

    @FXML
    private ScrollPane processesIDScrollPane;
    @FXML
    private ScrollPane processesScrollPane;
    @FXML
    private ScrollPane processesDelScrollPane;

    @FXML
    private JFXSlider criteriaEndTimeSlider;

    private List<AbstractTimeLine> processTimeLines;
    private int criteriaEndTime;
    private int maxEndTime;
    private double lengthFactor;

    public static ProcessControls getProcessControls() throws IOException {
        return new ProcessControls();
    }

    private ProcessControls() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/processControls.fxml"));
        fxmlLoader.setController(this);
        this.pane = fxmlLoader.load();
        this.processTimeLines = new ArrayList<>();
        this.criteriaEndTime = 10;
        this.lengthFactor = 800 / this.criteriaEndTime;

        processesIDVBox.getChildren().add(FXMLLoader.load(getClass().getResource("fxml/processIDColumn.fxml")));
        processesVBox.getChildren().add(new ProcessTimeLineIndex(criteriaEndTime, 800, 30, this));
        processesDelVBox.getChildren().add(FXMLLoader.load(getClass().getResource("fxml/processIDColumn.fxml")));
        processTimeLines.add((AbstractTimeLine) processesVBox.getChildren().get(0));

        // 스크롤 관련 이벤트 핸들러 시작.
        processesIDScrollPane.vvalueProperty().addListener((observable, oldValue, newValue) -> {
            processesScrollPane.vvalueProperty().setValue(newValue.doubleValue());
            processesDelScrollPane.vvalueProperty().setValue(newValue.doubleValue());
        });

        processesScrollPane.vvalueProperty().addListener((observable, oldValue, newValue) -> {
            processesIDScrollPane.vvalueProperty().setValue(newValue.doubleValue());
            processesDelScrollPane.vvalueProperty().setValue(newValue.doubleValue());
        });

        processesDelScrollPane.vvalueProperty().addListener((observable, oldValue, newValue) -> {
            processesIDScrollPane.vvalueProperty().setValue(newValue.doubleValue());
            processesScrollPane.vvalueProperty().setValue(newValue.doubleValue());
        });
        // 스크롤 관련 이벤트 핸들러 끝.

        // 축적 슬라이더 조작 이벤트 핸들러.
        criteriaEndTimeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            criteriaEndTime = newValue.intValue();
            lengthFactor = 800 / criteriaEndTime;

            processTimeLines.forEach(p -> p.updateScale(Math.max(criteriaEndTime, maxEndTime)));
        });
    }

    @FXML
    private void addProcess(MouseEvent event) throws IOException {
        int currentCriteriaEndTime = criteriaEndTimeSlider.valueProperty().getValue().intValue();
        int newCriteriaEndTime = criteriaEndTime > currentCriteriaEndTime ? criteriaEndTime : currentCriteriaEndTime;
        ProcessTimeLine processTimeLine = new ProcessTimeLine(newCriteriaEndTime, 800, 30, this);

        GridPane processIDPane = FXMLLoader.load(getClass().getResource("fxml/processID.fxml"));
        ((Text) (processIDPane.getChildren().get(0))).setText(processTimeLine.getProcess().getName());

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/processDelButton.fxml"));
        fxmlLoader.setController(this);
        Pane processDelButton = fxmlLoader.load();
        processesDelVBox.setAlignment(Pos.CENTER);

        processesIDVBox.getChildren().add(processIDPane);
        processesVBox.getChildren().add(processTimeLine);
        processesDelVBox.getChildren().add(processDelButton);
        processTimeLines.add(processTimeLine);
    }

    @FXML
    private void delProcess(MouseEvent event) throws IOException {
        JFXButton target = (JFXButton) event.getSource();
        int index = ((VBox)target.getParent().getParent()).getChildren().indexOf(target.getParent());

        processesIDVBox.getChildren().remove(index);
        processesVBox.getChildren().remove(index);
        processesDelVBox.getChildren().remove(index);
        processTimeLines.remove(index);

        updateAllScales();
    }

    @FXML
    private void delAllProcesses(MouseEvent event) {
        while (processesVBox.getChildren().size() > 1) {
            processesIDVBox.getChildren().remove(1);
            processesVBox.getChildren().remove(1);
            processesDelVBox.getChildren().remove(1);
            processTimeLines.remove(1);
        }

        updateAllScales();
    }

    public List<DefaultProcess> getProcesses() {
        List<DefaultProcess> processesList = new ArrayList<DefaultProcess>();
        for (int i = 1; i < processTimeLines.size(); i++) {
            ProcessTimeLine p = (ProcessTimeLine) processTimeLines.get(i);
            processesList.add(p.getProcess());
        }
        return processesList;
    }

    public GridPane getPane() {
        return pane;
    }

    public double getLengthFactor() {
        return lengthFactor;
    }

    public void updateAllScales() {
        int temp = 0;
        for (int i = 1; i < processTimeLines.size(); i++) {
            ProcessTimeLine ptl = (ProcessTimeLine) processTimeLines.get(i);
            if (ptl.getProcess().getEndTime() > temp)
                temp = ptl.getProcess().getEndTime();
        }
        maxEndTime = temp;
        processTimeLines.forEach(p -> p.updateScale(Math.max(criteriaEndTime, maxEndTime)));
    }
}