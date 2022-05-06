package kr.ac.koreatech.os.pss.app.component.pane;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import kr.ac.koreatech.os.pss.app.component.raw.timeline.impl.ProcessTimelinePane;
import kr.ac.koreatech.os.pss.app.component.raw.timeline.impl.TimelineBar;
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

import static kr.ac.koreatech.os.pss.app.component.raw.timeline.impl.ProcessTimelinePane.ActionState;

@CreatableController
public class ProcessTimelineContainerPane extends SingleComponent {
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

    private double width;
    private double height;

    private List<LAbstractTimeLine> processTimeLines;
    private int criteriaEndTime;
    private int maxEndTime;
    private double lengthFactor;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        this.width = 800;
        this.height = 30;

        this.processTimeLines = new ArrayList<>();
        this.criteriaEndTime = 10;
        this.lengthFactor = this.width / this.criteriaEndTime;

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
}


    public ProcessTimelineContainerPane(int criteriaEndTime, double width, double height) throws IOException {
        ProcessTimelineContainerPane processTimelineContainerPane = new ProcessTimelineContainerPane(criteriaEndTime, 800, 20);
    }

    @FXML
    public void delProcess(MouseEvent event) throws IOException {
        JFXButton target = (JFXButton) event.getSource();
        int index = ((VBox) target.getParent().getParent()).getChildren().indexOf(target.getParent());

        processesIDVBox.getChildren().remove(index);
        processesVBox.getChildren().remove(index);
        processesDelVBox.getChildren().remove(index);
        processTimeLines.remove(index);

        updateAllScales();
    }

    public List<DefaultProcess> getProcesses() {
        List<DefaultProcess> processesList = new ArrayList<DefaultProcess>();
        for (LAbstractTimeLine p : processTimeLines) {
            processesList.add(((LProcessTimeLine)p).getProcess());
        }
        return processesList;
    }

    public void updateAllScales() {
        maxEndTime = processTimeLines.stream().mapToInt(p -> ((LProcessTimeLine) p).getProcess().getEndTime()).max().getAsInt();
        processTimeLines.forEach(p -> p.updateScale(Math.max(criteriaEndTime, maxEndTime)));
    }

    public double getLengthFactor() {
        return lengthFactor;
    }

    public void delAllProcesses() {
        processesIDVBox.getChildren().clear();
        processesVBox.getChildren().clear();
        processesDelVBox.getChildren().clear();
        processTimeLines.clear();

        updateAllScales();
    }
}
