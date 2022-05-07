package kr.ac.koreatech.os.pss.app.component.raw.timeline.impl;

import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import kr.ac.koreatech.os.pss.app.component.pane.GanttChartContainerPane;
import kr.ac.koreatech.os.pss.app.component.utils.GridPaneUtils;
import kr.ac.koreatech.os.pss.app.component.utils.TextUtils;
import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;

import java.util.List;

public class UnionGanttChartTimeline {
    private final GanttChartContainerPane container;
    private Text idText;
    private GanttChartTimeLinePane ganttChartTimeLine;

    public UnionGanttChartTimeline(GanttChartContainerPane container, Text idText, GanttChartTimeLinePane ganttChartTimeLinePane) {
        this.container = container;
        this.idText = idText;
        this.ganttChartTimeLine = ganttChartTimeLinePane;
    }

    public static UnionGanttChartTimeline create(GanttChartContainerPane container, AbstractCore core, List<DefaultProcess> processes) {
        GanttChartTimeLinePane ganttChartTimeLine = new GanttChartTimeLinePane(core, processes);
        ganttChartTimeLine.updateScale(container.getGreatEndTime(), container.getLengthFactor());

        return new UnionGanttChartTimeline(container, TextUtils.getDefaultText(Integer.toString(core.getId()), 20), ganttChartTimeLine);
    }

    public Text getIdText() {
        return idText;
    }

    public void setIdText(Text idText) {
        this.idText = idText;
    }

    public GridPane getWrappedIdText() {
        GridPane wrap = GridPaneUtils.wrap(idText);
        wrap.setPrefWidth(100);
        wrap.setPrefHeight(30);
        return wrap;
    }

    public GanttChartTimeLinePane getGanttChartTimeLine() {
        return ganttChartTimeLine;
    }

    public void setGanttChartTimeLine(GanttChartTimeLinePane ganttChartTimeLine) {
        this.ganttChartTimeLine = ganttChartTimeLine;
    }

    public int getEndTime() {
        return ganttChartTimeLine.getEndTime();
    }

//    @Override
//    public String toString() {
//        return "UnionTimeline{" + "container=" + container + ", idText=" + idText + ", timeline=" + ganttChartTimeLine + ", deleteButton=" + deleteButton + '}';
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        UnionGanttChartTimeline that = (UnionGanttChartTimeline) o;
//        return Objects.equals(container, that.container) && Objects.equals(idText, that.idText) && Objects.equals(ganttChartTimeLine, that.ganttChartTimeLine) && Objects.equals(deleteButton, that.deleteButton);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(container, idText, ganttChartTimeLine);
//    }
}
