package kr.ac.koreatech.os.pss.app.component.pane;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import kr.ac.koreatech.os.pss.app.component.raw.timeline.impl.ProcessTimelinePane;
import kr.ac.koreatech.os.pss.app.component.raw.timeline.impl.TimelineBar;
import kr.ac.koreatech.os.pss.app.component.structure.SingleComponent;
import kr.ac.koreatech.os.pss.app.loader.annotation.CreatableController;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static kr.ac.koreatech.os.pss.app.component.raw.timeline.impl.ProcessTimelinePane.ActionState;


@CreatableController
public class ProcessControlPane extends SingleComponent {
    @FXML
    private JFXSlider criteriaEndTimeSlider;

    @FXML
    private JFXButton delAllButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        // 축적 슬라이더 이벤트 핸들러.
        criteriaEndTimeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            // update Scale하는 거 함수로 빼서 설정할 것.
//            processTimeLines.forEach(p -> p.updateScale(Math.max(criteriaEndTime, maxEndTime)));
        });
    }

    @FXML
    private void addProcess(MouseEvent event) throws IOException {
//        int newCriteriaEndTime = criteriaEndTime > currentCriteriaEndTime ? criteriaEndTime : currentCriteriaEndTime;
////        LProcessTimeLine processTimeLine = new LProcessTimeLine(newCriteriaEndTime, width, height, this);
//
//        GridPane processIDPane = FXMLLoader.load(getClass().getResource("processID.fxml"));
////        ((Text) (processIDPane.getChildren().get(0))).setText(processTimeLine.getProcess().getName());
//
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("processDelButton.fxml"));
//        fxmlLoader.setController(this);
//        Pane processDelButton = fxmlLoader.load();
//        processesDelVBox.setAlignment(Pos.CENTER);
//
//        processesIDVBox.getChildren().add(processIDPane);
////        processesVBox.getChildren().add(processTimeLine);
//        processesDelVBox.getChildren().add(processDelButton);
////        processTimeLines.add(processTimeLine);

        ProcessTimelinePane timelinePane = new ProcessTimelinePane(10, 80, 30);
        TimelineBar timelineBar = timelinePane.getTimeLineBar();
        ProcessTimelineContainerPane containerPane = SingleComponent.getInstance(ProcessTimelineContainerPane.class);
        double lengthFactor = containerPane.getLengthFactor();
        // TODO: 하향식으로 변환하기.

        timelinePane.setOnMouseMoved(e -> {
            double processedX = e.getX() - timelineBar.getLayoutX();

            boolean leftCondition = timelineBar.isInLeft(processedX);
            boolean rightCondition = timelineBar.isInRight(processedX, lengthFactor);
            boolean middleCondition = timelineBar.isInMiddle(processedX, lengthFactor);

            if (leftCondition || rightCondition || middleCondition) {
                if (leftCondition || rightCondition)
                    timelinePane.setCursor(Cursor.H_RESIZE);
                else
                    timelinePane.setCursor(Cursor.DEFAULT);
                timelineBar.makeTransparent();
            } else {
                timelineBar.makeOpaque();
                timelineBar.setCursor(Cursor.DEFAULT);
            }
        });

        timelinePane.setOnMouseExited(e -> {
            timelineBar.makeOpaque();
            timelinePane.setCursor(Cursor.DEFAULT);
        });

        timelinePane.setOnMouseDragged(e -> {
            double processedX = event.getX() - timelineBar.getLayoutX();

            switch (timelinePane.getActionState()) {
                case IDLE:
                    if (timelineBar.isInMiddle(processedX, lengthFactor))
                        timelinePane.setActionState(ProcessTimelinePane.ActionState.MOVE);
                    else if (timelineBar.isInLeft(processedX))
                        timelinePane.setActionState(ActionState.EXTEND_LEFT);
                    else if (timelineBar.isInRight(processedX, lengthFactor))
                        timelinePane.setActionState(ActionState.EXTEND_RIGHT);
                    break;
                case EXTEND_LEFT:
                    timelinePane.setCursor(Cursor.H_RESIZE);
                    double newWidth = timelineBar.getLayoutX() + timelineBar.getWidth() - Math.max(0, event.getX());
                    timelineBar.setLayoutX(Math.max(0, event.getX()));
                    timelineBar.setWidth(newWidth);
                    break;
                case EXTEND_RIGHT:
                    timelinePane.setCursor(Cursor.H_RESIZE);
                    newWidth = event.getX() - timelineBar.getLayoutX();
                    timelineBar.setWidth(newWidth);
                    break;
                case MOVE:
                    timelineBar.setLayoutX(Math.max(0, event.getX() - timelineBar.getWidth() / 2));
                    break;
            }
        });

        timelinePane.setOnMouseReleased(e -> {
            switch (timelinePane.getActionState()) {
                case EXTEND_LEFT:
                    int index = timelineBar.getLeftExpendedIndex(event.getX(), lengthFactor);
                    timelineBar.update(index, timelineBar.getEndTime() - index, lengthFactor);
                    timelineBar.setLayoutX(timelineBar.getArrivalTime() * lengthFactor);
                    break;
                case EXTEND_RIGHT:
                    index = timelineBar.getRightExpendedIndex(event.getX(), lengthFactor);
                    timelineBar.update(timelineBar.getArrivalTime(), index - timelineBar.getArrivalTime(), lengthFactor);
                    break;
                case MOVE:
                    index = timelineBar.getMovedIndex(event.getX() - timelineBar.getWidth() / 2, lengthFactor);
                    timelineBar.update(index, timelineBar.getBurstTime(), lengthFactor);
                    timelineBar.setLayoutX(timelineBar.getArrivalTime() * lengthFactor);
                    break;
            }

            DefaultProcess process = timelinePane.getProcess();
            process.setArrivalTime(timelineBar.getArrivalTime());
            process.setBurstTime(timelineBar.getBurstTime());

            timelinePane.setCursor(Cursor.DEFAULT);
            timelinePane.setActionState(ActionState.IDLE);

            containerPane.updateAllScales();
        });

        delAllButton.setOnMouseClicked(e -> {
            containerPane.delAllProcesses();
        });
    }
}
