package kr.ac.koreatech.os.pss.scheduler.impl;

import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;
import kr.ac.koreatech.os.pss.scheduler.AbstractScheduler;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;

import java.util.List;

/**
 * Round Robin 스케쥴러 클래스 (미구현됨)
 *
 * @author refracta
 */
public class RRScheduler extends AbstractScheduler {
    private final int timeQuantum;

    public RRScheduler(int timeQuantum) {
        this.timeQuantum = timeQuantum;
    }


    @Override
    protected void schedule(int time, List<AbstractCore> cores, List<DefaultProcess> processes, ScheduleData scheduleData) {

    }
}
