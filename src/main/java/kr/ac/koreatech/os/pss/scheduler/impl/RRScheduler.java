package kr.ac.koreatech.os.pss.scheduler.impl;

import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;
import kr.ac.koreatech.os.pss.scheduler.AbstractScheduler;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Round Robin 스케줄러 클래스
 *
 * @author refracta
 */
public class RRScheduler extends AbstractScheduler {
    private static final String PROCESS_QUEUE = "processQueue";
    private final int timeQuantum;

    public RRScheduler(int timeQuantum) {
        this.timeQuantum = timeQuantum;
    }

    @Override
    protected void init(List<AbstractCore> cores, List<DefaultProcess> processes, ScheduleData scheduleData) {
        scheduleData.put(PROCESS_QUEUE, new LinkedList<>());
    }

    @Override
    protected void schedule(int time, List<AbstractCore> cores, List<DefaultProcess> processes, ScheduleData scheduleData) {
        List<DefaultProcess> previousList = scheduleData.getProcesses(time - 1);
        List<DefaultProcess> currentList = scheduleData.getProcesses(time);

        LinkedList<DefaultProcess> processQueue = (LinkedList<DefaultProcess>) scheduleData.get(PROCESS_QUEUE);
        List<DefaultProcess> filteredProcesses = processes.stream().filter(p -> !p.isFinished() &&
                p.getArrivalTime() <= time &&
                !p.isIncludedIn(processQueue)).toList();

        List<DefaultProcess> arrivalSortedProcesses = filteredProcesses.stream()
                .filter(p -> !p.isIncludedIn(previousList)).sorted(Comparator.comparingInt(DefaultProcess::getArrivalTime)).toList();

        processQueue.addAll(arrivalSortedProcesses);

        processQueue.addAll(filteredProcesses.stream()
                .filter(p -> p.isIncludedIn(previousList) && !p.isIncludedIn(currentList)).toList());

        for (AbstractCore core : cores) {
            if (processQueue.isEmpty()) {
                break;
            }
            List<DefaultProcess> coreSchedule = scheduleData.getCoreSchedule(core);
            if (!coreSchedule.isEmpty()) {
                if (coreSchedule.size() < time + 1) {
                    DefaultProcess targetProcess = processQueue.poll();
                    coreSchedule.addAll(targetProcess.getContiguousProcesses(timeQuantum * core.getPerformance(), core.getPerformance()));
                } // else -> already exist schedule
            } else {
                DefaultProcess targetProcess = processQueue.poll();
                coreSchedule.addAll(targetProcess.getContiguousProcesses(timeQuantum * core.getPerformance(), core.getPerformance()));
            }
        }
    }
}
