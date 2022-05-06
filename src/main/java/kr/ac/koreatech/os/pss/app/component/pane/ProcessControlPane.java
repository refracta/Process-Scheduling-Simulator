package kr.ac.koreatech.os.pss.app.component.pane;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
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
    private void addProcess(MouseEvent event) throws IOException {
        ProcessTimelinePane timelinePane = new ProcessTimelinePane(10, 80, 30);
        TimelineBar timelineBar = timelinePane.getTimeLineBar();
        ProcessTimelineContainerPane containerPane = SingleComponent.getInstance(ProcessTimelineContainerPane.class);
        double lengthFactor = containerPane.getLengthFactor();
        // TODO: 변수명 변경, 상수 분리할 것.

        /* WrapperPane 이용해 추가하기.
        containerPane.getProcessIDVBox().getChildren().add();
        containerPane.getProcessDelVBox().getChildren().add();
         */

        // 임시 Rectangle 객체 삽입.
        containerPane.getProcessIDVBox().getChildren().add(new Rectangle(100, 31));
        containerPane.getProcessDelVBox().getChildren().add(new Rectangle(200, 31));
        containerPane.getProcessVBox().getChildren().add(timelinePane);
        containerPane.getProcessTimeLInes().add(timelinePane);

        // 이벤트 핸들러 매핑.
        timelinePane.setOnMouseMoved(e -> {
            double processedX = e.getX() - timelineBar.getLayoutX();

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

        timelinePane.setOnMouseDragged(e -> {
            double processedX = e.getX() - timelineBar.getLayoutX();

            switch (timeline.getActionState()) {
                case IDLE:
                    if (bar.isInMiddle(processedX, lengthFactor))
                        timeline.setActionState(ProcessTimelinePane.ActionState.MOVE);
                    else if (bar.isInLeft(processedX)) timeline.setActionState(ActionState.EXTEND_LEFT);
                    else if (bar.isInRight(processedX, lengthFactor)) timeline.setActionState(ActionState.EXTEND_RIGHT);
                    break;
                case EXTEND_LEFT:
                    timelinePane.setCursor(Cursor.H_RESIZE);
                    double newWidth = timelineBar.getLayoutX() + timelineBar.getWidth() - Math.max(0, e.getX());
                    timelineBar.setLayoutX(Math.max(0, e.getX()));
                    timelineBar.setWidth(newWidth);
                    break;
                case EXTEND_RIGHT:
                    timelinePane.setCursor(Cursor.H_RESIZE);
                    newWidth = e.getX() - timelineBar.getLayoutX();
                    timelineBar.setWidth(newWidth);
                    break;
                case MOVE:
                    timelineBar.setLayoutX(Math.max(0, e.getX() - timelineBar.getWidth() / 2));
                    break;
            }
        });

        timeline.setOnMouseReleased(e -> {
            switch (timeline.getActionState()) {
                case EXTEND_LEFT:
                    int index = timelineBar.getLeftExpendedIndex(e.getX(), lengthFactor);
                    timelineBar.update(index, timelineBar.getEndTime() - index, lengthFactor);
                    timelineBar.setLayoutX(timelineBar.getArrivalTime() * lengthFactor);
                    break;
                case EXTEND_RIGHT:
                    index = timelineBar.getRightExpendedIndex(e.getX(), lengthFactor);
                    timelineBar.update(timelineBar.getArrivalTime(), index - timelineBar.getArrivalTime(), lengthFactor);
                    break;
                case MOVE:
                    index = timelineBar.getMovedIndex(e.getX() - timelineBar.getWidth() / 2, lengthFactor);
                    timelineBar.update(index, timelineBar.getBurstTime(), lengthFactor);
                    timelineBar.setLayoutX(timelineBar.getArrivalTime() * lengthFactor);
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
