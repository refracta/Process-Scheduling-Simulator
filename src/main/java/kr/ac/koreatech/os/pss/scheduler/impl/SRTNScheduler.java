package kr.ac.koreatech.os.pss.scheduler.impl;

import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;
import kr.ac.koreatech.os.pss.scheduler.AbstractScheduler;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;

import java.util.List;

/**
 * Shortest Remaining Time Next 스케쥴러 클래스 (미구현됨)
 *
 * @author refracta
 */
public class SRTNScheduler extends AbstractScheduler {
    @Override
    protected void schedule(int time, List<AbstractCore> cores, List<DefaultProcess> processes, ScheduleData scheduleData) {

    }
}
