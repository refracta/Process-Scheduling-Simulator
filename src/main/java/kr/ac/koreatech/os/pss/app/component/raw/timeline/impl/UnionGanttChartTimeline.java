package kr.ac.koreatech.os.pss.app.component.raw.timeline.impl;

import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import kr.ac.koreatech.os.pss.app.component.pane.GanttChartContainerPane;
import kr.ac.koreatech.os.pss.app.component.pane.ProcessTimelineContainerPane;
import kr.ac.koreatech.os.pss.app.component.utils.GridPaneUtils;
import kr.ac.koreatech.os.pss.app.component.utils.TextUtils;
import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.core.impl.PerformanceCore;
import kr.ac.koreatech.os.pss.process.AbstractProcess;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;

import java.nio.file.attribute.AttributeView;
import java.util.List;

public class UnionGanttChartTimeline {
    private final GanttChartContainerPane container;
    private Text idText;
    private GanttChartTimeLinePane ganttChartTimeLine;

    public UnionGanttChartTimeline(GanttChartContainerPane container, Text idText, GanttChartTimeLinePane timeline) {
        this.container = container;
        this.idText = idText;
        this.ganttChartTimeLine = timeline;
    }

    public static UnionGanttChartTimeline create(GanttChartContainerPane container, AbstractCore core, List<DefaultProcess> processes) {
        GanttChartTimeLinePane ganttChartTimeLine = new GanttChartTimeLinePane(core, processes);
        ganttChartTimeLine.updateGanttChart(core, processes, container.getLengthFactor(), 30);

        UnionGanttChartTimeline unionGanttChartTimeline = new UnionGanttChartTimeline(container, TextUtils.getDefaultText(Integer.toString(core.getId()), 20), ganttChartTimeLine);

//        System.out.println(core.getId());
//        processes.stream().forEach(p -> System.out.println(p.getArrivalTime()));

        return unionGanttChartTimeline;

//        ganttChartTimeLine.setOnMouseMoved(e -> {
//            double processedX = e.getX() - bar.getLayoutX();
//
//            boolean leftCondition = bar.isInLeft(processedX);
//            boolean rightCondition = bar.isInRight(processedX, container.getLengthFactor());
//            boolean middleCondition = bar.isInMiddle(processedX, container.getLengthFactor());
//
//            if (leftCondition || rightCondition || middleCondition) {
//                if (leftCondition || rightCondition) ganttChartTimeLine.setCursor(Cursor.H_RESIZE);
//                else ganttChartTimeLine.setCursor(Cursor.DEFAULT);
//                bar.makeTransparent();
//            } else {
//                bar.makeOpaque();
//                bar.setCursor(Cursor.DEFAULT);
//            }
//        });
//
//        ganttChartTimeLine.setOnMouseExited(e -> {
//            bar.makeOpaque();
//            ganttChartTimeLine.setCursor(Cursor.DEFAULT);
//        });
//
//        ganttChartTimeLine.setOnMouseDragged(e -> {
//            double processedX = e.getX() - bar.getLayoutX();
//
//            switch (ganttChartTimeLine.getActionState()) {
//                case IDLE:
//                    if (bar.isInMiddle(processedX, container.getLengthFactor()))
//                        ganttChartTimeLine.setActionState(ProcessTimelinePane.ActionState.MOVE);
//                    else if (bar.isInLeft(processedX))
//                        ganttChartTimeLine.setActionState(ProcessTimelinePane.ActionState.EXTEND_LEFT);
//                    else if (bar.isInRight(processedX, container.getLengthFactor()))
//                        ganttChartTimeLine.setActionState(ProcessTimelinePane.ActionState.EXTEND_RIGHT);
//                    break;
//                case EXTEND_LEFT:
//                    ganttChartTimeLine.setCursor(Cursor.H_RESIZE);
//                    double newWidth = bar.getLayoutX() + bar.getWidth() - Math.max(0, e.getX());
//                    bar.setLayoutX(Math.max(0, e.getX()));
//                    bar.setWidth(newWidth);
//                    break;
//                case EXTEND_RIGHT:
//                    ganttChartTimeLine.setCursor(Cursor.H_RESIZE);
//                    newWidth = e.getX() - bar.getLayoutX();
//                    bar.setWidth(newWidth);
//                    break;
//                case MOVE:
//                    bar.setLayoutX(Math.max(0, e.getX() - bar.getWidth() / 2));
//                    break;
//            }
//        });
//
//        ganttChartTimeLine.setOnMouseReleased(e -> {
//            switch (ganttChartTimeLine.getActionState()) {
//                case EXTEND_LEFT:
//                    int index = bar.getLeftExpendedIndex(e.getX(), container.getLengthFactor());
//                    bar.update(index, bar.getEndTime() - index, container.getLengthFactor());
//                    bar.setLayoutX(bar.getArrivalTime() * container.getLengthFactor());
//                    break;
//                case EXTEND_RIGHT:
//                    index = bar.getRightExpendedIndex(e.getX(), container.getLengthFactor());
//                    bar.update(bar.getArrivalTime(), index - bar.getArrivalTime(), container.getLengthFactor());
//                    break;
//                case MOVE:
//                    index = bar.getMovedIndex(e.getX() - bar.getWidth() / 2, container.getLengthFactor());
//                    bar.update(index, bar.getBurstTime(), container.getLengthFactor());
//                    bar.setLayoutX(bar.getArrivalTime() * container.getLengthFactor());
//                    break;
//            }
//
//            DefaultProcess process = ganttChartTimeLine.getProcess();
//            process.setArrivalTime(bar.getArrivalTime());
//            process.setBurstTime(bar.getBurstTime());
//
//            ganttChartTimeLine.setCursor(Cursor.DEFAULT);
//            ganttChartTimeLine.setActionState(ProcessTimelinePane.ActionState.IDLE);
//
//            container.updateAllScales();
//        });
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
