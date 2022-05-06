package kr.ac.koreatech.os.pss.app.component.pane;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import kr.ac.koreatech.os.pss.app.component.raw.timeline.impl.ProcessTimelineIndexPane;
import kr.ac.koreatech.os.pss.app.component.raw.timeline.impl.UnionGanttChartTimeline;
import kr.ac.koreatech.os.pss.app.component.structure.SingleComponent;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GanttChartContainerPane extends SingleComponent {
    @FXML
    private VBox ganttChartIndexVBox;
    @FXML
    private VBox processNameVBox;
    @FXML
    private VBox ganttChartTimelineVBox;

    @FXML
    private ScrollPane ganttChartIndexScrollPane;
    @FXML
    private ScrollPane processNameScrollPane;
    @FXML
    private ScrollPane ganttChartTimelineScrollPane;

    private double width;
    private double height;

    private ProcessTimelineIndexPane ganttCharIndex;
    private List<UnionGanttChartTimeline> ganttChartTimelines;

    private int criteriaEndTime;
    private int maxEndTime;
    private double lengthFactor;

    public void generateGanttChart(ScheduleData scheduleData) {
        ganttChartTimelines.clear();
        processNameVBox.getChildren().clear();
        ganttChartTimelineVBox.getChildren().clear();

        scheduleData.getSchedule().forEach((k, v) -> {
            UnionGanttChartTimeline unionGanttChartTimeline = UnionGanttChartTimeline.create(this, k, v);
            ganttChartTimelines.add(unionGanttChartTimeline);
            processNameVBox.getChildren().add(unionGanttChartTimeline.getWrappedIdText());
            ganttChartTimelineVBox.getChildren().add(unionGanttChartTimeline.getGanttChartTimeLine());
        });

        updateAllScales();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        this.width = 1030;
        this.height = 30;

        this.ganttChartTimelines = new ArrayList<>();
        this.criteriaEndTime = 10;
        this.lengthFactor = this.width / this.criteriaEndTime;

        this.ganttCharIndex = new ProcessTimelineIndexPane(criteriaEndTime, lengthFactor, width, height);

        ganttChartIndexVBox.getChildren().add(ganttCharIndex);

        processNameScrollPane.vvalueProperty().addListener((observable, oldValue, newValue) -> {
            ganttChartTimelineScrollPane.vvalueProperty().setValue(newValue.doubleValue());
        });

        ganttChartTimelineScrollPane.vvalueProperty().addListener((observable, oldValue, newValue) -> {
            processNameScrollPane.vvalueProperty().setValue(newValue.doubleValue());
        });

        ganttChartIndexScrollPane.hvalueProperty().addListener((observable, oldValue, newValue) -> {
            ganttChartTimelineScrollPane.hvalueProperty().setValue(newValue.doubleValue());
        });

        ganttChartTimelineScrollPane.hvalueProperty().addListener(((observable, oldValue, newValue) -> {
            ganttChartIndexScrollPane.hvalueProperty().setValue(newValue.doubleValue());
        }));
    }

    public void updateAllScales() {
        ganttCharIndex.updateScale(Math.max(criteriaEndTime, maxEndTime), lengthFactor);
        if (ganttChartTimelines.isEmpty()) return;
        maxEndTime = ganttChartTimelines.stream().mapToInt(l -> l.getEndTime()).max().getAsInt();
        System.out.println(maxEndTime);
        ganttCharIndex.updateScale(Math.max(criteriaEndTime, maxEndTime), lengthFactor);
        ganttChartTimelines.forEach(t -> t.getGanttChartTimeLine().updateScale(Math.max(criteriaEndTime, maxEndTime), lengthFactor));
    }

    public int getMaxEndTime() {
        return maxEndTime;
    }

    public double getLengthFactor() {
        return lengthFactor;
    }

    public int getCriteriaEndTime() {
        return criteriaEndTime;
    }

    public void setCriteriaEndTime(int criteriaEndTime) {
        this.criteriaEndTime = criteriaEndTime;
        this.lengthFactor = width / criteriaEndTime;
    }
}
