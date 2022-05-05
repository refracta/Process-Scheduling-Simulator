package kr.ac.koreatech.os.pss.app.component.pane;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import kr.ac.koreatech.os.pss.app.component.structure.SingleComponent;
import kr.ac.koreatech.os.pss.app.legacy.LProcessControls;
import kr.ac.koreatech.os.pss.app.legacy.LProcessTimeLine;
import kr.ac.koreatech.os.pss.app.legacy.LProcessTimeLineIndex;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ProcessControlPane extends SingleComponent {
    private GridPane pane;

    @FXML
    private ScrollPane timeLineScrollPane;
    @FXML
    private ScrollPane test;

    @FXML
    private VBox processesVBox;
    @FXML
    private ScrollPane processesScrollPane;
    @FXML
    private VBox processesIDVBox;
    @FXML
    private ScrollPane processesIDScrollPane;
    @FXML
    private VBox processesDelVBox;
    @FXML
    private ScrollPane processesDelScrollPane;

    @FXML
    private JFXSlider criteriaEndTimeSlider;

    private List<LProcessTimeLine> processTimeLines;
    private int criteriaEndTime;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
/*//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("processControls.fxml"));
//        fxmlLoader.setController(this);
        this.processTimeLines = new ArrayList<LProcessTimeLine>();
        this.criteriaEndTime = 10;

//        processesIDVBox.getChildren().add(FXMLLoader.load(getClass().getResource("processIDColumn.fxml")));
//        LProcessTimeLineIndex temp = LProcessTimeLineIndex.getProcessTimeLineIndex();
//        temp.init();
//        processesVBox.getChildren().add(temp.getPane());
//        processesDelVBox.getChildren().add(FXMLLoader.load(getClass().getResource("processIDColumn.fxml")));

        // 스크롤 관련 이벤트 핸들러 시작.
        processesDelScrollPane.vvalueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                processesIDScrollPane.vvalueProperty().setValue(newValue.doubleValue());
                processesScrollPane.vvalueProperty().setValue(newValue.doubleValue());
            }
        });

        processesIDScrollPane.vvalueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                processesDelScrollPane.vvalueProperty().setValue(newValue.doubleValue());
                processesScrollPane.vvalueProperty().setValue(newValue.doubleValue());
            }
        });

        processesScrollPane.vvalueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                processesIDScrollPane.vvalueProperty().setValue(newValue.doubleValue());
                processesDelScrollPane.vvalueProperty().setValue(newValue.doubleValue());
            }
        });
        // 스크롤 관련 이벤트 핸들러 끝.

        // 축적 슬라이더 조작 이벤트 핸들러.
        criteriaEndTimeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                int maxEndTime = processTimeLines.stream().mapToInt(processTimeLine -> processTimeLine.getEndTime()).max().getAsInt();
                criteriaEndTime = Math.max(newValue.intValue(), maxEndTime);
                processTimeLines.forEach(processTimeLine -> processTimeLine.setCriteriaEndTimeAndLengthFactorAndUpdate(criteriaEndTime, 800 / criteriaEndTime));
            }
        });*/
    }

    @FXML
    private void addProcess(MouseEvent event) throws IOException {
        int currentCriteriaEndTime = criteriaEndTimeSlider.valueProperty().getValue().intValue();
        LProcessTimeLine processTimeLine = LProcessTimeLine.getProcessTimeLine(criteriaEndTime > currentCriteriaEndTime ? criteriaEndTime : currentCriteriaEndTime);
        processTimeLine.init();

        GridPane processIDPane = FXMLLoader.load(getClass().getResource("processID.fxml"));
        ((Text) (processIDPane.getChildren().get(0))).setText(processTimeLine.getProcess().getName());

//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("processDelButton.fxml"));
//        fxmlLoader.setController(this);
//        Pane processDelButton = fxmlLoader.load();
//        processesDelVBox.setAlignment(Pos.CENTER);
//
//        processesVBox.getChildren().add(processTimeLine.getPane());
//        processesIDVBox.getChildren().add(processIDPane);
//        processesDelVBox.getChildren().add(processDelButton);

//        processTimeLine.setProcessControls(this);
//        processTimeLines.add(processTimeLine);
    }

    @FXML
    private void delProcess(MouseEvent event) throws IOException {
        JFXButton target = (JFXButton) event.getSource();
        int index = ((VBox) target.getParent().getParent()).getChildren().indexOf(target.getParent());

        processesDelVBox.getChildren().remove(index);
        processesIDVBox.getChildren().remove(index);

        processesVBox.getChildren().remove(index);
        processTimeLines.remove(index - 1);
    }

    @FXML
    private void delAllProcesses(MouseEvent event) {
        while (processesVBox.getChildren().size() > 1) {
            processesVBox.getChildren().remove(1);
            processesIDVBox.getChildren().remove(1);
            processesDelVBox.getChildren().remove(1);
            processTimeLines.remove(0);
        }
    }

    public void initProcesses() {
        if (processTimeLines == null) return;
        processTimeLines.stream().forEach(process -> process.init());
    }

    public List<DefaultProcess> getProcesses() {
        List<DefaultProcess> processesList = new ArrayList<DefaultProcess>();
        processTimeLines.stream().forEach(p -> processesList.add(p.getProcess()));
        return processesList;
    }

    public List<LProcessTimeLine> getProcessTimeLines() {
        return processTimeLines;
    }

    public GridPane getPane() {
        return pane;
    }

    public void setCriteriaEndTime(int criteriaEndTime) {
        this.criteriaEndTime = criteriaEndTime;
    }

    public int getCriteriaEndTime() {
        return criteriaEndTime;
    }

    public void updateAllCriteriaEndTime(int criteriaEndTime) {
        processTimeLines.forEach(p -> p.setCriteriaEndTimeAndUpdateTimeLine(criteriaEndTime));
    }
}
