package kr.ac.koreatech.os.pss.core.impl;

import kr.ac.koreatech.os.pss.core.AbstractCore;

/**
 * 성능 코어 클래스
 *
 * @author refracta
 */
public class PerformanceCore extends AbstractCore {
    /**
     * 성능 코어 클래스의 생성자
     */
    public PerformanceCore() {
        super(2, 3, 0.1);
    }

    /**
     * 성능 코어 클래스의 생성자
     *
     * @param id 코어 식별자
     */
    public PerformanceCore(int id) {
        super(id, 2, 3, 0.1);
    }

    @Override
    public String toString() {
        return "EfficiencyCore{" +
                "performance=" + getPerformance() +
                ", runningPower=" + getRunningPower() +
                ", standbyPower=" + getStandbyPower() +
                '}';
    }

    @Override
    public PerformanceCore clone() {
        return (PerformanceCore) super.clone();
    }
}
