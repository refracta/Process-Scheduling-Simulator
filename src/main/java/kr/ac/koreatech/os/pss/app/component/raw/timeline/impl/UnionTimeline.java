package kr.ac.koreatech.os.pss.app.component.raw.timeline.impl;

import com.jfoenix.controls.JFXButton;
import javafx.scene.Cursor;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import kr.ac.koreatech.os.pss.app.component.pane.ProcessDeleteButton;
import kr.ac.koreatech.os.pss.app.component.pane.ProcessTimelineContainerPane;
import kr.ac.koreatech.os.pss.app.component.utils.GridPaneUtils;
import kr.ac.koreatech.os.pss.app.component.utils.TextUtils;
import kr.ac.koreatech.os.pss.app.loader.utils.FXMLUtils;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;

import java.util.Objects;

public class UnionTimeline {
    private final ProcessTimelineContainerPane container;
    private Text idText;
    private ProcessTimelinePane timeline;
    private JFXButton deleteButton;

    public UnionTimeline(ProcessTimelineContainerPane container, Text idText, ProcessTimelinePane timeline, JFXButton deleteButton) {
        this.container = container;
        this.idText = idText;
        this.timeline = timeline;
        this.deleteButton = deleteButton;
    }

    public static UnionTimeline create(ProcessTimelineContainerPane container) {
        ProcessTimelinePane timeline = new ProcessTimelinePane();
        timeline.updateScale(Math.max(container.getCriteriaEndTime(), container.getMaxEndTime()), container.getLengthFactor());
        TimelineBar bar = timeline.getTimelineBar();

        timeline.setOnMouseMoved(e -> {
            double processedX = e.getX() - bar.getLayoutX();

            boolean leftCondition = bar.isInLeft(processedX);
            boolean rightCondition = bar.isInRight(processedX, container.getLengthFactor());
            boolean middleCondition = bar.isInMiddle(processedX, container.getLengthFactor());

            if (leftCondition || rightCondition || middleCondition) {
                if (leftCondition || rightCondition) timeline.setCursor(Cursor.H_RESIZE);
                else timeline.setCursor(Cursor.DEFAULT);
                bar.makeTransparent();
            } else {
                bar.makeOpaque();
                bar.setCursor(Cursor.DEFAULT);
            }
        });

        timeline.setOnMouseExited(e -> {
            bar.makeOpaque();
            timeline.setCursor(Cursor.DEFAULT);
        });

        timeline.setOnMouseDragged(e -> {
            double processedX = e.getX() - bar.getLayoutX();

            switch (timeline.getActionState()) {
                case IDLE:
                    if (bar.isInMiddle(processedX, container.getLengthFactor()))
                        timeline.setActionState(ProcessTimelinePane.ActionState.MOVE);
                    else if (bar.isInLeft(processedX))
                        timeline.setActionState(ProcessTimelinePane.ActionState.EXTEND_LEFT);
                    else if (bar.isInRight(processedX, container.getLengthFactor()))
                        timeline.setActionState(ProcessTimelinePane.ActionState.EXTEND_RIGHT);
                    break;
                case EXTEND_LEFT:
                    timeline.setCursor(Cursor.H_RESIZE);
                    double newWidth = bar.getLayoutX() + bar.getWidth() - Math.max(0, e.getX());
                    bar.setLayoutX(Math.max(0, e.getX()));
                    bar.setWidth(newWidth);
                    break;
                case EXTEND_RIGHT:
                    timeline.setCursor(Cursor.H_RESIZE);
                    newWidth = e.getX() - bar.getLayoutX();
                    bar.setWidth(newWidth);
                    break;
                case MOVE:
                    bar.setLayoutX(Math.max(0, e.getX() - bar.getWidth() / 2));
                    break;
            }
        });

        timeline.setOnMouseReleased(e -> {
            switch (timeline.getActionState()) {
                case EXTEND_LEFT:
                    int index = bar.getLeftExpendedIndex(e.getX(), container.getLengthFactor());
                    bar.update(index, bar.getEndTime() - index, container.getLengthFactor());
                    bar.setLayoutX(bar.getArrivalTime() * container.getLengthFactor());
                    break;
                case EXTEND_RIGHT:
                    index = bar.getRightExpendedIndex(e.getX(), container.getLengthFactor());
                    bar.update(bar.getArrivalTime(), index - bar.getArrivalTime(), container.getLengthFactor());
                    break;
                case MOVE:
                    index = bar.getMovedIndex(e.getX() - bar.getWidth() / 2, container.getLengthFactor());
                    bar.update(index, bar.getBurstTime(), container.getLengthFactor());
                    bar.setLayoutX(bar.getArrivalTime() * container.getLengthFactor());
                    break;
            }

            DefaultProcess process = timeline.getProcess();
            process.setArrivalTime(bar.getArrivalTime());
            process.setBurstTime(bar.getBurstTime());
            process.setLeftBurstTime(bar.getBurstTime());

            timeline.setCursor(Cursor.DEFAULT);
            timeline.setActionState(ProcessTimelinePane.ActionState.IDLE);

            container.updateAllScales();
        });
        JFXButton deleteButton = (JFXButton) FXMLUtils.create(ProcessDeleteButton.class);
        UnionTimeline unionTimeline = new UnionTimeline(container, TextUtils.getDefaultText(timeline.getProcess().getName(), 20), timeline, deleteButton);
        deleteButton.setOnMouseClicked(e -> {
            container.deleteTimeline(unionTimeline);
        });
        return unionTimeline;
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

    public ProcessTimelinePane getTimeline() {
        return timeline;
    }

    public void setTimeline(ProcessTimelinePane timeline) {
        this.timeline = timeline;
    }

    public JFXButton getDeleteButton() {
        return deleteButton;
    }

    public void setDeleteButton(JFXButton deleteButton) {
        this.deleteButton = deleteButton;
    }

    public GridPane getWrappedDeleteButton() {
        GridPane wrap = GridPaneUtils.wrap(deleteButton);
        wrap.setMinWidth(114);
        return wrap;
    }

    @Override
    public String toString() {
        return "UnionTimeline{" + "container=" + container + ", idText=" + idText + ", timeline=" + timeline + ", deleteButton=" + deleteButton + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnionTimeline that = (UnionTimeline) o;
        return Objects.equals(container, that.container) && Objects.equals(idText, that.idText) && Objects.equals(timeline, that.timeline) && Objects.equals(deleteButton, that.deleteButton);
    }

    @Override
    public int hashCode() {
        return Objects.hash(container, idText, timeline, deleteButton);
    }
}
