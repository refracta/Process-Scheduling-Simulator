package kr.ac.koreatech.os.pss.process.impl;

import kr.ac.koreatech.os.pss.process.AbstractProcess;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 기본 프로세스 클래스
 * 프로세스 스케쥴링에 필요한 요소가 구현된 프로세스 클래스이다.
 *
 * @author refracta
 */
public class DefaultProcess extends AbstractProcess {
    public static final int NOT_STARTED = -1;
    /**
     * 실행 시작 시간
     */
    private int startTime = -1;
    /**
     * 도착 시간
     */
    private int arrivalTime;
    /**
     * 실행 시간
     */
    private int burstTime;
    /**
     * 남은 실행 시간
     * 이 값은 생성자에서 burstTime과 동일한 값으로 초기화되며, 이 값이 0 이하가 되면 boolean isFinished(); 함수는 참(true)을 반환한다.
     */
    private int leftBurstTime;
    /**
     * 반환 시간
     */
    private int turnaroundTime = 0;
    /**
     * 대기 시간
     */
    private int waitingTime = 0;

    /**
     * 기본 프로세스 클래스의 생성자
     *
     * @param id          프로세스 식별자
     * @param name        프로세스 이름
     * @param arrivalTime 도착 시간
     * @param burstTime   실행 시간
     */
    public DefaultProcess(int id, String name, int arrivalTime, int burstTime) {
        super(id, name);
        this.arrivalTime = arrivalTime;
        this.burstTime = this.leftBurstTime = burstTime;
    }

    /**
     * 기본 프로세스 클래스의 생성자
     *
     * @param name        프로세스 이름
     * @param arrivalTime 도착 시간
     * @param burstTime   실행 시간
     */
    public DefaultProcess(String name, int arrivalTime, int burstTime) {
        super(name);
        this.arrivalTime = arrivalTime;
        this.burstTime = this.leftBurstTime = burstTime;
    }

    /**
     * 기본 프로세스 클래스의 생성자
     *
     * @param id          프로세스 식별자
     * @param arrivalTime 도착 시간
     * @param burstTime   실행 시간
     */
    public DefaultProcess(int id, int arrivalTime, int burstTime) {
        this(id, "P" + id, arrivalTime, burstTime);
    }

    /**
     * 기본 프로세스 클래스의 생성자
     *
     * @param arrivalTime 도착 시간
     * @param burstTime   실행 시간
     */
    public DefaultProcess(int arrivalTime, int burstTime) {
        this.setName("P" + getId());
        this.arrivalTime = arrivalTime;
        this.burstTime = this.leftBurstTime = burstTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public int getLeftBurstTime() {
        return leftBurstTime;
    }

    public void setLeftBurstTime(int leftBurstTime) {
        this.leftBurstTime = leftBurstTime < 0 ? 0 : leftBurstTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public double getNormalizedTurnaroundTime() {
        return turnaroundTime / (double) burstTime;
    }

    /**
     * 프로세스가 종료된 프로세스인지의 여부를 반환한다. 남은 실행 시간이 0 이하가 되면 참(true)을 반환한다.
     *
     * @return 프로세스가 종료되었는지 여부 (부울)
     */
    @Override
    public boolean isFinished() {
        return leftBurstTime <= 0;
    }

    /**
     * 이 객체의 남은 실행 시간(leftBurstTime)을 참조하여 주어진 성능 수치와 스케쥴링 시간 동안의 프로세스 리스트를 생성하여 반환한다.
     * 예를 들어
     * leftBurstTime=3, during=3, performance=1일 때 [DefaultProcess{leftBurstTime=3}, DefaultProcess{leftBurstTime=2}, DefaultProcess{leftBurstTime=1}]을 반환하고
     * leftBurstTime=5, during=3, performance=2일 때 [DefaultProcess{leftBurstTime=5}]을 반환한다.
     *
     * @param during      스케쥴링 시간
     * @param performance 성능 수치
     * @return 스케쥴된 프로세스 리스트
     */
    public List<DefaultProcess> getContiguousProcesses(int during, int performance) {
        List<DefaultProcess> processes = new ArrayList<>();
        for (int time = 0; time + performance <= during; time += performance) {
            DefaultProcess clone = this.clone();
            int leftTime = clone.leftBurstTime - time;
            if (leftTime > 0) {
                clone.setLeftBurstTime(leftTime);
                processes.add(clone);
            } else {
                break;
            }
        }
        return processes;
    }

    /**
     * 이 객체의 남은 실행 시간(leftBurstTime)을 참조하여 주어진 성능 수치로 프로세스 리스트를 생성하여 반환한다.
     *
     * @param performance 스케쥴링 시간
     * @return 스케쥴된 프로세스 리스트
     */
    public List<DefaultProcess> getContiguousProcesses(int performance) {
        return getContiguousProcesses(getLeftBurstTime() + performance, performance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DefaultProcess that = (DefaultProcess) o;
        return startTime == that.startTime && arrivalTime == that.arrivalTime && burstTime == that.burstTime && leftBurstTime == that.leftBurstTime && turnaroundTime == that.turnaroundTime && waitingTime == that.waitingTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), startTime, arrivalTime, burstTime, leftBurstTime, turnaroundTime, waitingTime);
    }

    @Override
    public DefaultProcess clone() {
        DefaultProcess clone = (DefaultProcess) super.clone();
        clone.setStartTime(startTime);
        clone.setArrivalTime(arrivalTime);
        clone.setBurstTime(burstTime);
        clone.setLeftBurstTime(leftBurstTime);
        clone.setTurnaroundTime(turnaroundTime);
        clone.setWaitingTime(waitingTime);
        return clone;
    }

    @Override
    public String toString() {
        return "DefaultProcess{" +
                "id=" + getId() +
                ", name=" + getName() +
                ", finished=" + isFinished() +
                ", startTime=" + startTime +
                ", arrivalTime=" + arrivalTime +
                ", burstTime=" + burstTime +
                ", leftBurstTime=" + leftBurstTime +
                ", turnaroundTime=" + turnaroundTime +
                ", waitingTime=" + waitingTime +
                ", normalizedTurnaroundTime=" + getNormalizedTurnaroundTime() +
                '}';
    }
}
