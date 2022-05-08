package kr.ac.koreatech.os.pss.app.component.raw.n.timeline;

import javafx.scene.paint.Color;
import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.core.impl.PerformanceCore;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class NGanttChart extends NTimelineWidget {


    public NGanttChart() {
    }

    public class Wrapper implements NTimelineElement {
        DefaultProcess process;

        public Wrapper(DefaultProcess process) {
            this.process = process;
        }

        @Override
        public Color getColor() {
            return new Color(Math.random(), Math.random(), Math.random(), 1);
        }

        @Override
        public int getStartIndex() {
            return process.getStartTime();
        }

        @Override
        public int getEndIndex() {
            return process.getEndTime();
        }

        @Override
        public String toString() {
            return process.toString();
        }
    }

    public void schedule(ScheduleData scheduleData) {
        init();
        Set<Map.Entry<AbstractCore, List<DefaultProcess>>> entries = scheduleData.getSchedule().entrySet();
        for (Map.Entry<AbstractCore, List<DefaultProcess>> e : entries) {
            AbstractCore core = e.getKey();
            List<DefaultProcess> schedule = e.getValue();
            List<NTimelineElement> nTimelineElements = schedule.stream().map(p->(NTimelineElement) new Wrapper(p)).toList();

            NTimelinePane timelinePane = new NTimelinePane(core.getId() + " " + (core instanceof PerformanceCore ? "(성능)" : "(효율)"), nTimelineElements);
            timelinePane.deleteButtonHalfHighlightColor = timelinePane.highlightColor;
            timelinePane.deleteButtonHighlightColor = timelinePane.outlineColor;
            timelineVBox.getChildren().add(timelinePane);
        }
    }
}
