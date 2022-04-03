package kr.ac.koreatech.os.pss.scheduler.impl;

import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;
import kr.ac.koreatech.os.pss.scheduler.AbstractScheduler;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

/**
 * First Come First Service 스케쥴러 클래스
 *
 * @author refracta
 */
public class FCFSScheduler extends AbstractScheduler {

    @Override
    protected void schedule(int time, List<AbstractCore> cores, List<DefaultProcess> processes, ScheduleData scheduleData) {
        Map<AbstractCore, List<DefaultProcess>> schedule = scheduleData.getSchedule();
        PriorityQueue<DefaultProcess> processQueue = new PriorityQueue<>(Comparator.comparingInt(DefaultProcess::getArrivalTime));
        processQueue.addAll(processes.stream().filter(p -> !p.isFinished() && p.getArrivalTime() <= time).toList());
        for (AbstractCore core : cores) {
            List<DefaultProcess> coreSchedule = schedule.get(core);
            if (processQueue.isEmpty()) {
                break;
            }
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
