package kr.ac.koreatech.os.pss.scheduler.impl;

import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;
import kr.ac.koreatech.os.pss.scheduler.PriorityScheduler;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;

import java.util.Comparator;
import java.util.List;

/**
 * High Response Ratio Next 스케줄러 클래스
 *
 * @author refracta
 */
public class HRRNScheduler extends PriorityScheduler {
    public HRRNScheduler() {
        super(Comparator.comparingDouble(DefaultProcess::getResponseRatio).reversed());
    }

    @Override
    protected void schedule(int time, List<AbstractCore> cores, List<DefaultProcess> processes, ScheduleData scheduleData) {
        processes.stream().filter(p -> !p.isFinished() && p.getArrivalTime() <= time).forEach(p -> {
            p.setTurnaroundTime(time - p.getArrivalTime());
            p.setWaitingTime(p.getTurnaroundTime() - scheduleData.getRealBurstTime(p, time));
        });
        super.schedule(time, cores, processes, scheduleData);
    }
}
