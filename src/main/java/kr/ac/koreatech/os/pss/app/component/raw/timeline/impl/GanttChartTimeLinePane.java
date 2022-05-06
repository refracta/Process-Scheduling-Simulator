package kr.ac.koreatech.os.pss.app.component.raw.timeline.impl;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import kr.ac.koreatech.os.pss.app.component.pane.ProcessTimelineContainerPane;
import kr.ac.koreatech.os.pss.app.component.raw.timeline.ScaleHandler;
import kr.ac.koreatech.os.pss.app.component.structure.SingleComponent;
import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.core.impl.PerformanceCore;
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
        this.partedScheduleTimeLineBar = new ArrayList<TimelineBar>();
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
            getChildren().add(new Line(i * lengthFactor, 0, i * lengthFactor, 30 - 1));

        for (DefaultProcess process : processes) {
            if (process.getId() == -1) continue;
            int currentTime = process.getArrivalTime() + process.getWaitingTime() + (process.getBurstTime() - process.getLeftBurstTime()) / (core instanceof PerformanceCore ? 2 : 1);
            System.out.println(currentTime);
            System.out.println(process.getStartTime());
            TimelineBar ganttCharTimeLineBar = new TimelineBar(process.getArrivalTime(), 1, lengthFactor, 30 - 1);
            ganttCharTimeLineBar.setLayoutX(currentTime * lengthFactor);
            ganttCharTimeLineBar.setColor(processColor.get(process.getName()));
            partedScheduleTimeLineBar.add(ganttCharTimeLineBar);
            getChildren().add(ganttCharTimeLineBar);
        }
    }

    public int getEndTime() {
        if (processes.isEmpty()) return 0;
        return processes.stream().mapToInt(p -> p.getArrivalTime() + p.getWaitingTime() + p.getBurstTime() - p.getLeftBurstTime() + 1).max().getAsInt();
    }
}
