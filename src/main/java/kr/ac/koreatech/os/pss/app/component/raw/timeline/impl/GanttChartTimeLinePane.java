package kr.ac.koreatech.os.pss.app.component.raw.timeline.impl;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import kr.ac.koreatech.os.pss.app.component.raw.timeline.ScaleHandler;
import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;

import java.util.ArrayList;
import java.util.List;

public class GanttChartTimeLinePane extends Pane implements ScaleHandler {
    private List<TimelineBar> partedScheduleTimeLineBar;

    public GanttChartTimeLinePane(AbstractCore core, List<DefaultProcess> processes) {
        this(10, 103, 30, core, processes);
    }

    public GanttChartTimeLinePane(int maxEndTime, double lengthFactor, double height, AbstractCore core, List<DefaultProcess> processes) {
        this.partedScheduleTimeLineBar = new ArrayList<TimelineBar>();

        updateGanttChart(core, processes, lengthFactor, height);
        updateScale(maxEndTime, lengthFactor);
    }

    public void updateGanttChart(AbstractCore core, List<DefaultProcess> processes, double lengthFactor, double height) {
        getChildren().clear();
        partedScheduleTimeLineBar.clear();

        for (DefaultProcess process : processes) {
            if (process.getId() == -1) continue;
            TimelineBar ganttCharTimeLineBar = new TimelineBar(process.getArrivalTime(), 1, lengthFactor, height);
            partedScheduleTimeLineBar.add(ganttCharTimeLineBar);
            getChildren().add(ganttCharTimeLineBar);
        }
    }

    @Override
    public void updateScale(int maxEndTime, double lengthFactor) {
        getChildren().clear();
        for (int i = 0; i < maxEndTime; i++)
            this.getChildren().add(new Line(i * lengthFactor, 0, i * lengthFactor, 30 - 1));
    }
}
