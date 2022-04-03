package kr.ac.koreatech.os.pss.core.impl;

import kr.ac.koreatech.os.pss.core.AbstractCore;

/**
 * 효율 코어 클래스
 *
 * @author refracta
 */
public class EfficiencyCore extends AbstractCore {
    /**
     * 효율 코어 클래스의 생성자
     */
    public EfficiencyCore() {
        super(1, 1, 0.1);
    }

    /**
     * 효율 코어 클래스의 생성자
     *
     * @param id 코어 식별자
     */
    public EfficiencyCore(int id) {
        super(id, 1, 1, 0.1);
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
    public EfficiencyCore clone() {
        return (EfficiencyCore) super.clone();
    }
}
