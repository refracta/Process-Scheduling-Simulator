package kr.ac.koreatech.os.pss.scheduler;

import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * 우선 순위에 따른 간단한 스케줄링을 하는 스케줄러
 *
 * @author refracta
 */
public abstract class PriorityScheduler extends AbstractScheduler {
    private final Comparator<DefaultProcess> comparator;

    public PriorityScheduler(Comparator<DefaultProcess> comparator) {
        this.comparator = comparator;
    }

    @Override
    protected void schedule(int time, List<AbstractCore> cores, List<DefaultProcess> processes, ScheduleData scheduleData) {
        PriorityQueue<DefaultProcess> processQueue = new PriorityQueue<>(comparator);
        processQueue.addAll(processes.stream().filter(p -> !p.isFinished() && p.getArrivalTime() <= time).toList());
        for (AbstractCore core : cores) {
            if (processQueue.isEmpty()) {
                break;
            }
            List<DefaultProcess> coreSchedule = scheduleData.getCoreSchedule(core);
            if (!coreSchedule.isEmpty()) {
                if (coreSchedule.size() < time + 1) {
                    // DefaultProcess previous = coreSchedule.get(time - 1);
                    DefaultProcess targetProcess = processQueue.poll();
                    coreSchedule.addAll(targetProcess.getContiguousProcesses(core.getPerformance()));
                } // else -> already exist schedule
            } else {
                DefaultProcess targetProcess = processQueue.poll();
                coreSchedule.addAll(targetProcess.getContiguousProcesses(core.getPerformance()));
            }
        }
    }
}
