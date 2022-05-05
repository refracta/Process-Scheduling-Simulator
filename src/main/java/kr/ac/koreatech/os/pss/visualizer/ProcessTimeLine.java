package kr.ac.koreatech.os.pss.visualizer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;

import java.io.IOException;

public class ProcessTimeLine extends Pane {
    protected Pane pane;

    protected double height;

    protected int criteriaEndTime;
    protected double lengthFactor;
    private int actionState;

    private ProcessControls processControls;
    private TimeLineBar timeLineBar;

    private DefaultProcess process;

    public static ProcessTimeLine getProcessTimeLine() throws IOException {
        return ProcessTimeLine.getProcessTimeLine(10);
    }

    public static ProcessTimeLine getProcessTimeLine(int criteriaArrivalTime) throws IOException {
        ProcessTimeLine processTimeLine = new ProcessTimeLine(criteriaArrivalTime);
        return processTimeLine;
    }

    protected ProcessTimeLine() throws IOException {
        this(10);
    }

    protected ProcessTimeLine(int criteriaEndTime) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("processTimeLine.fxml"));
        fxmlLoader.setController(this);
        this.pane = fxmlLoader.load();
        this.criteriaEndTime = criteriaEndTime;
    }

    public void init() {
        height = 30;
        lengthFactor = 800 / criteriaEndTime;
        timeLineBar = new TimeLineBar(0, 1, 800, height, lengthFactor);

        for (int i = 0; i <= criteriaEndTime; i++) { pane.getChildren().add(new Line(i * lengthFactor, 0, i * lengthFactor, height)); }
        pane.getChildren().add(timeLineBar);

        this.process = new DefaultProcess(0, 1);
    }

    private void init(int criteriaEndTime) {
        this.criteriaEndTime = criteriaEndTime;
        init();
    }

    @FXML
    public void onMouseMoved(MouseEvent event) {
        double processedX = event.getX() - timeLineBar.getLayoutX();

        if (timeLineBar.isInLeft(processedX) || timeLineBar.isInRight(processedX) || timeLineBar.isInMiddle(processedX)) {
            if (timeLineBar.isInLeft(processedX) || timeLineBar.isInRight(processedX))
                pane.setCursor(Cursor.H_RESIZE);
            else
                pane.setCursor(Cursor.DEFAULT);
            timeLineBar.makeTransparent();
        } else {
            timeLineBar.makeOpaque();
            pane.setCursor(Cursor.DEFAULT);
        }
    }

    @FXML
    public void onMouseExited(MouseEvent event) {
        timeLineBar.makeOpaque();
        pane.setCursor(Cursor.DEFAULT);
    }

    @FXML
    public void onMouseDragged(MouseEvent event) {
        double processedX = event.getX() - timeLineBar.getLayoutX();

        if (actionState == 0) {
            if (timeLineBar.isInMiddle(processedX))
                actionState = 1;
            else if (timeLineBar.isInLeft(processedX))
                actionState = 2;
            else if (timeLineBar.isInRight(processedX))
                actionState = 3;
        }

        if (actionState == 1) {
            timeLineBar.setLayoutX(event.getX() - timeLineBar.getWidth() / 2);
        } else if (actionState == 2) {
            if (event.getX() < (timeLineBar.getArrivalTime() + timeLineBar.getBurstTime()) * lengthFactor) {
                pane.setCursor(Cursor.H_RESIZE);
                timeLineBar.setLayoutX(event.getX());
                double originalX = timeLineBar.getArrivalTime() * lengthFactor;
                double newLength = timeLineBar.getBurstTime() * lengthFactor + (originalX - event.getX());
                timeLineBar.setWidthAndShow(newLength);
            }
        } else if (actionState == 3) {
            if (event.getX() > timeLineBar.getLayoutX()) {
                pane.setCursor(Cursor.H_RESIZE);
                double originalX = timeLineBar.getArrivalTime() * lengthFactor;
                double newLength = event.getX() - originalX;
                timeLineBar.setWidthAndShow(newLength);
            }
        }
    }

    @FXML
    public void onMouseReleased(MouseEvent event) {
        if (actionState == 1) {
            int index = timeLineBar.getMovedIndex(event.getX() - timeLineBar.getWidth() / 2);
            timeLineBar.setArrivalTime(index);
            timeLineBar.setLayoutX(index * lengthFactor);
        } else if (actionState == 2) {
            int index = timeLineBar.getLeftExpendedIndex(event.getX());
            timeLineBar.setBurstTime(timeLineBar.getBurstTime() + timeLineBar.getArrivalTime() - index);
            timeLineBar.setArrivalTime(index);
            timeLineBar.setLayoutX(index * lengthFactor);
            timeLineBar.update();
        } else if (actionState == 3) {
            int index = timeLineBar.getRightExpendedIndex(event.getX());
            timeLineBar.setBurstTime(index - timeLineBar.getArrivalTime());
            timeLineBar.update();
        }

        pane.setCursor(Cursor.DEFAULT);

        process.setArrivalTime(timeLineBar.getArrivalTime());
        process.setBurstTime(timeLineBar.getBurstTime());

        actionState = 0;

        if (getEndTime() > processControls.getCriteriaEndTime()) {
            processControls.setCriteriaEndTime(getEndTime());
            processControls.updateAllCriteriaEndTime(getEndTime());
            setCriteriaEndTimeAndUpdateTimeLine(getEndTime());
        }
    }

    /**
     * 프로세스가 종료되는 시점을 변경하였을 때 화면에 표시되는 눈금 갱신
     *
     * @param criteriaEndTime
     */
    public void setCriteriaEndTimeAndUpdateTimeLine(int criteriaEndTime) {
        pane.getChildren().remove(0, pane.getChildren().size());
        for (int i = 0; i <= criteriaEndTime; i++) { pane.getChildren().add(new Line(i * lengthFactor, 0, i * lengthFactor, height)); }
        pane.getChildren().add(timeLineBar);
    }

    /**
     * 축적 변경 시 표시되는 TimeLineBar 길이 및 표시 눈금 갱신
     *
     * @param criteriaEndTime
     * @param maxEndTime
     */
    public void setCriteriaEndTimeAndLengthFactorAndUpdate(int criteriaEndTime, double lengthFactor) {
        this.criteriaEndTime = Math.max(this.criteriaEndTime, criteriaEndTime);
        this.lengthFactor = lengthFactor;

        pane.getChildren().remove(0, pane.getChildren().size());
        for (int i = 0; i <= this.criteriaEndTime; i++) { pane.getChildren().add(new Line(i * lengthFactor, 0, i * lengthFactor, height)); }
        pane.getChildren().add(timeLineBar);

        timeLineBar.updateLengthFactor(lengthFactor);
        timeLineBar.setLayoutX(timeLineBar.getArrivalTime() * lengthFactor);
    }

    public DefaultProcess getProcess() {
        process.resetLeftBurstTime();
        return process;
    }

    public Pane getPane() {
        return pane;
    }

    public int getCriteriaArrivalTime() {
        return criteriaEndTime;
    }

    public int getEndTime() {
        return process.getArrivalTime() + process.getBurstTime();
    }

    public TimeLineBar getTimeLineBar() {
        return timeLineBar;
    }

    public void setProcessControls(ProcessControls processControls) {
        this.processControls = processControls;
    }
}
