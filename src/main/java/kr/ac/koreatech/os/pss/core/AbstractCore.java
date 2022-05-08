package kr.ac.koreatech.os.pss.core;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 추상 코어 클래스
 *
 * @author refracta
 */
public class AbstractCore implements Cloneable, Comparable<AbstractCore> {
    /**
     * 코어의 정수 식별자(id)를 중복없이 자동 생성하기 위한 카운터
     */
    private static final AtomicInteger idCount = new AtomicInteger(0);

    /**
     * 코어를 구별하기 위한 정수 식별자
     */
    private int id;
    /**
     * 성능 수치
     * 이 값이 1일 때 단위 시간(1초)당 실행 시간(Burst time) 감소는 1이 된다.
     */
    private int performance;
    /**
     * 실행 전력
     * 코어가 프로세스를 실행할 때의 전기 소모량이다.
     */
    private double runningPower;
    /**
     * 대기 전력
     * 코어가 프로세스를 실행하지 않을 때(EmptyProcess를 실행할 때)의 전기 소모량이다.
     */
    private double standbyPower;

    /**
     * 추상 코어 클래스의 생성자
     *
     * @param performance  성능 수치
     * @param runningPower 실행 전력
     * @param standbyPower 대기 전력
     */
    public AbstractCore(int performance, double runningPower, double standbyPower) {
        init(performance, runningPower, standbyPower);
    }

    /**
     * 추상 코어 클래스의 생성자
     *
     * @param id           식별자
     * @param performance  성능 수치
     * @param runningPower 실행 전력
     * @param standbyPower 대기 전력
     */
    public AbstractCore(int id, int performance, double runningPower, double standbyPower) {
        setId(id);
        init(performance, runningPower, standbyPower);
    }

    /**
     * 클래스의 멤버 변수를 초기화한다.
     *
     * @param performance  성능 수치
     * @param runningPower 실행 전력
     * @param standbyPower 대기 전력
     */
    private void init(int performance, double runningPower, double standbyPower) {
        this.id = idCount.incrementAndGet();
        this.performance = performance;
        this.runningPower = runningPower;
        this.standbyPower = standbyPower;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id > idCount.intValue()) {
            idCount.set(id);
        }
        this.id = id;
    }

    public int getPerformance() {
        return performance;
    }

    public void setPerformance(int performance) {
        this.performance = performance;
    }

    public double getRunningPower() {
        return runningPower;
    }

    public void setRunningPower(double runningPower) {
        this.runningPower = runningPower;
    }

    public double getStandbyPower() {
        return standbyPower;
    }

    public void setStandbyPower(double standbyPower) {
        this.standbyPower = standbyPower;
    }

    public static void resetCoreIdCount() {
        AbstractCore.idCount.set(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractCore that = (AbstractCore) o;
        return id == that.id && performance == that.performance && Double.compare(that.runningPower, runningPower) == 0 && Double.compare(that.standbyPower, standbyPower) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, performance, runningPower, standbyPower);
    }

    @Override
    public String toString() {
        return "AbstractCore{" +
                "performance=" + performance +
                ", runningPower=" + runningPower +
                ", standbyPower=" + standbyPower +
                '}';
    }

    @Override
    public AbstractCore clone() {
        AbstractCore clone = null;
        try {
            clone = (AbstractCore) super.clone();
            clone.id = id;
            clone.performance = performance;
            clone.runningPower = runningPower;
            clone.standbyPower = standbyPower;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }

    @Override
    public int compareTo(AbstractCore o) {
        return Integer.compare(id, o.getId());
    }
}
