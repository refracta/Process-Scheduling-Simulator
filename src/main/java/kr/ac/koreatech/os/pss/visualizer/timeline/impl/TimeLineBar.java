package kr.ac.koreatech.os.pss.visualizer.timeline.impl;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TimeLineBar extends Rectangle {
    private int arrivalTime;
    private int burstTime;

    private Color opaqueColor;
    private Color transparentColor;

    public TimeLineBar(int arrivalTime, int burstTime, double width, double height) {
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;

        this.opaqueColor = Color.color(Math.random(), Math.random(), Math.random());
        this.transparentColor = Color.color(this.opaqueColor.darker().getRed(), this.opaqueColor.darker().getGreen(), this.opaqueColor.getBlue(), 0.8);

        setWidth(width);
        setHeight(height);
        setFill(this.opaqueColor);
    }

    public void makeOpaque() {
        setFill(this.opaqueColor);
    }

    public void makeTransparent() {
        setFill(this.transparentColor);
    }

    public boolean isInLeft(double x) {
        double criteria = 0;
        return criteria - 10 <= x && x <= criteria + 10;
    }

    public boolean isInRight(double x, double lengthFactor) {
        double criteria = burstTime * lengthFactor;
        return criteria - 10 <= x && x <= criteria + 10;
    }

    public boolean isInMiddle(double x, double lengthFactor) {
        double leftCriteria = 10;
        double rightCriteria = burstTime * lengthFactor - 10;
        return leftCriteria <= x && x <= rightCriteria;
    }

    public int getLeftExpendedIndex(double x, double lengthFactor) {
        if (x < 0) return 0;
        if (x > (arrivalTime + burstTime) * lengthFactor) return arrivalTime + burstTime - 1;

        int cellIndex = (int) (x / lengthFactor);
        return cellIndex;
    }

    public int getRightExpendedIndex(double x, double lengthFactor) {
        if (x < (arrivalTime + 1) * lengthFactor) return arrivalTime + 1;

        int cellIndex = (int) (x / lengthFactor) + 1;
        return cellIndex;
    }

    public int getMovedIndex(double x, double lengthFactor) {
        if (x < 0) return 0;

        int cellIndex = (int) Math.round(x / lengthFactor);
        return cellIndex;
    }

    public void update(int arrivalTime, int burstTime, double lengthFactor) {
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        setWidth(this.burstTime * lengthFactor);
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public int getEndTime() {
        return arrivalTime + burstTime;
    }
}