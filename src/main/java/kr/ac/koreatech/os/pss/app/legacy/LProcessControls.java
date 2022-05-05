package kr.ac.koreatech.os.pss.app.legacy;

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
import kr.ac.koreatech.os.pss.app.legacy.timeline.LAbstractTimeLine;
import kr.ac.koreatech.os.pss.app.legacy.timeline.impl.LProcessTimeLine;
import kr.ac.koreatech.os.pss.app.legacy.timeline.impl.LProcessTimeLineIndex;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LProcessControls extends GridPane {
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

    private double width;
    private double height;

    private List<LAbstractTimeLine> processTimeLines;
    private int criteriaEndTime;
    private int maxEndTime;
    private double lengthFactor;

    public static LProcessControls getProcessControls() throws IOException {
        return new LProcessControls();
    }

    private LProcessControls() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("processControls.fxml"));
        fxmlLoader.setController(this);
        this.pane = fxmlLoader.load();

        this.width = 800;
        this.height = 30;

        this.processTimeLines = new ArrayList<>();
        this.criteriaEndTime = 10;
        this.lengthFactor = this.width / this.criteriaEndTime;

        processesIDVBox.getChildren().add(FXMLLoader.load(getClass().getResource("processIDColumn.fxml")));
        processesVBox.getChildren().add(new LProcessTimeLineIndex(criteriaEndTime, this.width, this.height, this));
        processesDelVBox.getChildren().add(FXMLLoader.load(getClass().getResource("processIDColumn.fxml")));
        processTimeLines.add((LAbstractTimeLine) processesVBox.getChildren().get(0));

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
            lengthFactor = this.width / criteriaEndTime;

            processTimeLines.forEach(p -> p.updateScale(Math.max(criteriaEndTime, maxEndTime)));
        });
    }

    @FXML
    private void addProcess(MouseEvent event) throws IOException {
        int currentCriteriaEndTime = criteriaEndTimeSlider.valueProperty().getValue().intValue();
        int newCriteriaEndTime = criteriaEndTime > currentCriteriaEndTime ? criteriaEndTime : currentCriteriaEndTime;
        LProcessTimeLine processTimeLine = new LProcessTimeLine(newCriteriaEndTime, width, height, this);

        GridPane processIDPane = FXMLLoader.load(getClass().getResource("processID.fxml"));
        ((Text) (processIDPane.getChildren().get(0))).setText(processTimeLine.getProcess().getName());

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("processDelButton.fxml"));
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
            LProcessTimeLine p = (LProcessTimeLine) processTimeLines.get(i);
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
        int tempMaxEndTime = 0;
        for (int i = 1; i < processTimeLines.size(); i++) {
            LProcessTimeLine ptl = (LProcessTimeLine) processTimeLines.get(i);
            if (ptl.getProcess().getEndTime() > tempMaxEndTime)
                tempMaxEndTime = ptl.getProcess().getEndTime();
        }
        maxEndTime = tempMaxEndTime;
        processTimeLines.forEach(p -> p.updateScale(Math.max(criteriaEndTime, maxEndTime)));
    }
}
