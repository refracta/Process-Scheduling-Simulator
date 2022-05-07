package kr.ac.koreatech.os.pss.app.component.raw.timeline.impl;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import kr.ac.koreatech.os.pss.app.component.pane.ProcessTimelineContainerPane;
import kr.ac.koreatech.os.pss.app.component.raw.timeline.ScaleHandler;
import kr.ac.koreatech.os.pss.app.component.structure.SingleComponent;
import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GanttChartTimeLinePane extends Pane implements ScaleHandler {
    private List<TimelineBar> partedScheduleTimeLineBar;

    private AbstractCore core;
    private List<DefaultProcess> processes;

    public GanttChartTimeLinePane(AbstractCore core, List<DefaultProcess> processes) {
        this(10, 103, 30, core, processes);
    }

    public GanttChartTimeLinePane(int maxEndTime, double lengthFactor, double height, AbstractCore core, List<DefaultProcess> processes) {
        this.partedScheduleTimeLineBar = new ArrayList<>();
        this.core = core;
        this.processes = processes;

        updateScale(maxEndTime, lengthFactor);
    }

    @Override
    public void updateScale(int maxEndTime, double lengthFactor) {
        getChildren().clear();
        partedScheduleTimeLineBar.clear();

        Map<String, Color> processColor = new HashMap<>();
        for (UnionTimeline p : SingleComponent.getInstance(ProcessTimelineContainerPane.class).getProcessTimelines()) {
            processColor.put(p.getIdText().getText(), p.getTimeline().getTimelineBar().getOpaqueColor());
        }

        for (int i = 0; i < maxEndTime; i++)
            getChildren().add(new Line(i * lengthFactor, 0, i * lengthFactor, 30));

        for (int currentTime = 0; currentTime < processes.size(); currentTime++) {
            DefaultProcess process = processes.get(currentTime);

            if (process.getId() == -1) continue;

            TimelineBar ganttCharTimeLineBar = new TimelineBar(process.getArrivalTime(), 1, lengthFactor, 30);
            ganttCharTimeLineBar.setLayoutX(currentTime * lengthFactor);
            ganttCharTimeLineBar.setColor(processColor.get(process.getName()));
            partedScheduleTimeLineBar.add(ganttCharTimeLineBar);
            getChildren().add(ganttCharTimeLineBar);

            int timelineBarIndex = partedScheduleTimeLineBar.size() - 1;
            ganttCharTimeLineBar.setOnMouseMoved(event -> makeAllRelatedTimelineBarTransparent(timelineBarIndex));
            ganttCharTimeLineBar.setOnMouseExited(event -> makeAllRelatedTimelineBarOpaque(timelineBarIndex));
        }
    }

    public void makeAllRelatedTimelineBarTransparent(int index) {
        TimelineBar targetTimelineBar = partedScheduleTimeLineBar.get(index);

        int i = index;
        while (i >= 0) {
            TimelineBar destTimelineBar = partedScheduleTimeLineBar.get(i);
            if (!destTimelineBar.getOpaqueColor().equals(targetTimelineBar.getOpaqueColor())) break;
            destTimelineBar.makeTransparent();
            i--;
        }

        i = index + 1;
        while (i < partedScheduleTimeLineBar.size()) {
            TimelineBar destTimelineBar = partedScheduleTimeLineBar.get(i);
            if (!destTimelineBar.getOpaqueColor().equals(targetTimelineBar.getOpaqueColor())) break;
            destTimelineBar.makeTransparent();
            i++;
        }
    }

    public void makeAllRelatedTimelineBarOpaque(int index) {
        TimelineBar targetTimelineBar = partedScheduleTimeLineBar.get(index);

        int i = index;
        while (i >= 0) {
            TimelineBar destTimelineBar = partedScheduleTimeLineBar.get(i);
            if (!destTimelineBar.getOpaqueColor().equals(targetTimelineBar.getOpaqueColor())) break;
            destTimelineBar.makeOpaque();
            i--;
        }

        i = index + 1;
        while (i < partedScheduleTimeLineBar.size()) {
            TimelineBar destTimelineBar = partedScheduleTimeLineBar.get(i);
            if (!destTimelineBar.getOpaqueColor().equals(targetTimelineBar.getOpaqueColor())) break;
            destTimelineBar.makeOpaque();
            i++;
        }
    }

    public int getEndTime() {
        return processes.size();
    }
}
