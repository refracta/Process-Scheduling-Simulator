package kr.ac.koreatech.os.pss.app.component.raw.timeline.impl;

import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import kr.ac.koreatech.os.pss.app.component.pane.ProcessTimelineContainerPane;
import kr.ac.koreatech.os.pss.app.component.raw.timeline.ScaleHandler;
import kr.ac.koreatech.os.pss.app.component.structure.SingleComponent;
import kr.ac.koreatech.os.pss.app.component.utils.TooltipUtils;
import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GanttChartTimeLinePane extends Pane implements ScaleHandler {
    private List<TimelineBar> partedScheduleTimeLineBar;

    private ScheduleData scheduleData;
    private AbstractCore core;
    private List<DefaultProcess> processes;

    public GanttChartTimeLinePane(ScheduleData scheduleData, AbstractCore core) {
        this(10, 103, 30, scheduleData, core);
    }

    public GanttChartTimeLinePane(int maxEndTime, double lengthFactor, double height, ScheduleData scheduleData, AbstractCore core) {
        this.partedScheduleTimeLineBar = new ArrayList<>();
        this.scheduleData = scheduleData;
        this.core = core;
        this.processes = scheduleData.getCoreSchedule(core);

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

            Tooltip timelineBarTooltip = TooltipUtils.getDefaultTooltip(ganttCharTimeLineBar);

            int timelineBarIndex = partedScheduleTimeLineBar.size() - 1;

            ganttCharTimeLineBar.setOnMouseEntered(event -> timelineBarTooltip.setText(
                    "프로세스 이름: " + process.getName() + "\n" +
                    "남은 Burst Time: " + process.getLeftBurstTime() + "\n" +
                    "대기 시간: " + process.getWaitingTime() + "\n" +
                    "응답 시간: " + String.format("%.1f", process.getResponseRatio())
            ));

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
