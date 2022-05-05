package kr.ac.koreatech.os.pss.visualizer.legacy;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

class LTimeLineBar extends Pane {
    private int arrivalTime;
    private int burstTime;

    private double lengthFactor;

    private Rectangle bar;

    private Color barColor;

    public LTimeLineBar(int arrivalTime, int burstTime, double windowWidth, double windowHeight, double lengthFactor) {
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.lengthFactor = lengthFactor;

        this.barColor = Color.color(Math.random(), Math.random(), Math.random());

        this.bar = new Rectangle(0, 0, burstTime * lengthFactor, windowHeight);
        this.bar.setFill(Color.color(this.barColor.getRed(), this.barColor.getGreen(), this.barColor.getBlue(), 1));

        getChildren().add(this.bar);
    }

    public void makeTransparent() {
        bar.setFill(Color.color(barColor.darker().getRed(), barColor.darker().getGreen(), barColor.darker().getBlue(), 0.8));
    }

    public void makeOpaque() {
        bar.setFill(Color.color(barColor.getRed(), barColor.getGreen(), barColor.getBlue(), 1));
    }

    public boolean isInLeft(double x) {
        double criteria = 0;
        return criteria - 10 <= x && x <= criteria + 10;
    }

    public boolean isInMiddle(double x) {
        double leftCriteria = 10;
        double rightCriteria = burstTime * lengthFactor - 10;
        return leftCriteria <= x && x <= rightCriteria;
    }

    public boolean isInRight(double x) {
        double criteria = burstTime * lengthFactor;
        return criteria - 10 <= x && x <= criteria + 10;
    }

    public int getMovedIndex(double x) {
        if (x < 0) return 0;

        int cellIndex = 1;
        while (x > cellIndex * lengthFactor) cellIndex++;
        if (x < cellIndex * lengthFactor - lengthFactor / 2) cellIndex--;
        return cellIndex;
    }

    public int getLeftExpendedIndex(double x) {
        if (x < 0) return 0;
        if (x > (arrivalTime + burstTime) * lengthFactor) return arrivalTime + burstTime - 1;

        int cellIndex = 1;
        while (x > cellIndex * lengthFactor) cellIndex++;
        return cellIndex - 1;
    }

    public int getRightExpendedIndex(double x) {
        if (x < (arrivalTime + 1) * lengthFactor) return arrivalTime + 1;

        int cellIndex = 1;
        while (x > cellIndex * lengthFactor) cellIndex++;
        return cellIndex;
    }

    public void setWidthAndShow(double width) {
        bar.setWidth(width);
    }

    public void updateLengthFactor(double lengthFactor) {
        this.lengthFactor = lengthFactor;
        update();
    }

    public void update() {
        bar.setWidth(burstTime * lengthFactor);
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }
}

