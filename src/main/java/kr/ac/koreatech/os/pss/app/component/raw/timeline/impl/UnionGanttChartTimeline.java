package kr.ac.koreatech.os.pss.app.component.raw.timeline.impl;

import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import kr.ac.koreatech.os.pss.app.component.pane.GanttChartContainerPane;
import kr.ac.koreatech.os.pss.app.component.utils.GridPaneUtils;
import kr.ac.koreatech.os.pss.app.component.utils.TextUtils;
import kr.ac.koreatech.os.pss.app.component.utils.TooltipUtils;
import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.core.impl.PerformanceCore;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;

public class UnionGanttChartTimeline {
    private final GanttChartContainerPane container;
    private Text idText;
    private GanttChartTimeLinePane ganttChartTimeLine;

    public UnionGanttChartTimeline(GanttChartContainerPane container, Text idText, GanttChartTimeLinePane ganttChartTimeLinePane) {
        this.container = container;
        this.idText = idText;
        this.ganttChartTimeLine = ganttChartTimeLinePane;
    }

    public static UnionGanttChartTimeline create(GanttChartContainerPane container, ScheduleData scheduleData, AbstractCore core) {
        GanttChartTimeLinePane ganttChartTimeLine = new GanttChartTimeLinePane(scheduleData, core);
        ganttChartTimeLine.updateScale(container.getGreatEndTime(), container.getLengthFactor());

        String coreType = core instanceof PerformanceCore ? " (성능)" : " (효율)";
        Text coreName = TextUtils.getDefaultText(core.getId() + coreType, 20);

        Tooltip tooltip = TooltipUtils.getDefaultTooltip(coreName);

        coreName.setOnMouseEntered(event -> tooltip.setText(
                "코어 아이디: " + core.getId() + "\n" +
                "코어 타입: " + coreType + "\n" +
                "사용 전력: " + String.format("%.1f", scheduleData.getPowerUsage(core))
        ));

        return new UnionGanttChartTimeline(container, coreName, ganttChartTimeLine);
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
