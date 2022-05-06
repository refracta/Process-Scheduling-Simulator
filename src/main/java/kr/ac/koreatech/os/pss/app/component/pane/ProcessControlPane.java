package kr.ac.koreatech.os.pss.app.component.pane;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
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

    @FXML
    private GridPane processTimelineContainerPane;

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

        ProcessTimelinePane timeline = new ProcessTimelinePane(10, 80, 30);
        // TODO: 상수 분리할 것
        TimelineBar bar = timeline.getTimelineBar();
        ProcessTimelineContainerPane container = SingleComponent.getInstance(ProcessTimelineContainerPane.class);
        double lengthFactor = container.getLengthFactor();

        timeline.setOnMouseMoved(e -> {
            double processedX = e.getX() - bar.getLayoutX();

            boolean leftCondition = bar.isInLeft(processedX);
            boolean rightCondition = bar.isInRight(processedX, lengthFactor);
            boolean middleCondition = bar.isInMiddle(processedX, lengthFactor);

            if (leftCondition || rightCondition || middleCondition) {
                if (leftCondition || rightCondition) timeline.setCursor(Cursor.H_RESIZE);
                else timeline.setCursor(Cursor.DEFAULT);
                bar.makeTransparent();
            } else {
                bar.makeOpaque();
                bar.setCursor(Cursor.DEFAULT);
            }
        });

        timeline.setOnMouseExited(e -> {
            bar.makeOpaque();
            timeline.setCursor(Cursor.DEFAULT);
        });

        timeline.setOnMouseDragged(e -> {
            double processedX = event.getX() - bar.getLayoutX();

            switch (timeline.getActionState()) {
                case IDLE:
                    if (bar.isInMiddle(processedX, lengthFactor))
                        timeline.setActionState(ProcessTimelinePane.ActionState.MOVE);
                    else if (bar.isInLeft(processedX)) timeline.setActionState(ActionState.EXTEND_LEFT);
                    else if (bar.isInRight(processedX, lengthFactor)) timeline.setActionState(ActionState.EXTEND_RIGHT);
                    break;
                case EXTEND_LEFT:
                    timeline.setCursor(Cursor.H_RESIZE);
                    double newWidth = bar.getLayoutX() + bar.getWidth() - Math.max(0, event.getX());
                    bar.setLayoutX(Math.max(0, event.getX()));
                    bar.setWidth(newWidth);
                    break;
                case EXTEND_RIGHT:
                    timeline.setCursor(Cursor.H_RESIZE);
                    newWidth = event.getX() - bar.getLayoutX();
                    bar.setWidth(newWidth);
                    break;
                case MOVE:
                    bar.setLayoutX(Math.max(0, event.getX() - bar.getWidth() / 2));
                    break;
            }
        });

        timeline.setOnMouseReleased(e -> {
            switch (timeline.getActionState()) {
                case EXTEND_LEFT:
                    int index = bar.getLeftExpendedIndex(event.getX(), lengthFactor);
                    bar.update(index, bar.getEndTime() - index, lengthFactor);
                    bar.setLayoutX(bar.getArrivalTime() * lengthFactor);
                    break;
                case EXTEND_RIGHT:
                    index = bar.getRightExpendedIndex(event.getX(), lengthFactor);
                    bar.update(bar.getArrivalTime(), index - bar.getArrivalTime(), lengthFactor);
                    break;
                case MOVE:
                    index = bar.getMovedIndex(event.getX() - bar.getWidth() / 2, lengthFactor);
                    bar.update(index, bar.getBurstTime(), lengthFactor);
                    bar.setLayoutX(bar.getArrivalTime() * lengthFactor);
                    break;
            }

            DefaultProcess process = timeline.getProcess();
            process.setArrivalTime(bar.getArrivalTime());
            process.setBurstTime(bar.getBurstTime());

            timeline.setCursor(Cursor.DEFAULT);
            timeline.setActionState(ActionState.IDLE);

            container.updateAllScales();
        });

        delAllButton.setOnMouseClicked(e -> {
            container.delAllprocess();
        });
    }
}
