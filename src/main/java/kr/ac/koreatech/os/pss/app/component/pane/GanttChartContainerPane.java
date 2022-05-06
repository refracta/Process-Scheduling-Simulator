package kr.ac.koreatech.os.pss.app.component.pane;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import kr.ac.koreatech.os.pss.app.component.raw.timeline.impl.GanttChartTimeLinePane;
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
    private VBox ganttChartTimeLineVBox;

    private double width;
    private double height;

    private ProcessTimelineIndexPane ganttCharIndex;
    private List<UnionGanttChartTimeline> ganttChartTimelines;

    private int criteriaEndTime;
    private int maxEndTime;
    private double lengthFactor;

    public void generateGanttChart(ScheduleData scheduleData) {
        scheduleData.getSchedule().forEach((k, v) -> {
            UnionGanttChartTimeline unionGanttChartTimeline = UnionGanttChartTimeline.create(this, k, v);
            ganttChartTimelines.add(unionGanttChartTimeline);
            processNameVBox.getChildren().add(unionGanttChartTimeline.getWrappedIdText());
            ganttChartTimeLineVBox.getChildren().add(unionGanttChartTimeline.getGanttChartTimeLine());
        });
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
}
