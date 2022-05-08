package kr.ac.koreatech.os.pss.scheduler.impl;

import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author YOUKK
 * @author refracta
 */
public class RR2QScheduler extends RRScheduler {
    private static final String PROCESS_RUN_QUEUE = "processRunQueue";
    private final int runQueueLimit;

    public RR2QScheduler(int timeQuantum, int runQueueLimit) {
        super(timeQuantum);
        this.runQueueLimit = runQueueLimit;
    }

    @Override
    protected void init(List<AbstractCore> cores, List<DefaultProcess> processes, ScheduleData scheduleData) {
        super.init(cores, processes, scheduleData);
        scheduleData.put(PROCESS_RUN_QUEUE, new LinkedList<>());
    }

    @Override
    public void schedule(int time, List<AbstractCore> cores, List<DefaultProcess> processes, ScheduleData scheduleData) {
        LinkedList<DefaultProcess> processRunQueue = (LinkedList<DefaultProcess>) scheduleData.get(PROCESS_RUN_QUEUE);
        List<DefaultProcess> filteredRunQueue = processRunQueue.stream().filter(p -> !p.isFinished()).toList();
        processRunQueue.clear();
        processRunQueue.addAll(filteredRunQueue);

        if (processRunQueue.size() < runQueueLimit) {
            LinkedList<DefaultProcess> filteredList = new LinkedList<>(processes.stream().filter(p -> !p.isFinished() && p.getArrivalTime() <= time && !p.isIncludedIn(processRunQueue)).sorted(Comparator.comparingInt(DefaultProcess::getArrivalTime)).toList());
            while (!filteredList.isEmpty() && processRunQueue.size() < runQueueLimit) {
                processRunQueue.add(filteredList.pollFirst());
            }
        }
        super.schedule(time, cores, processRunQueue, scheduleData);
        for (int i = 0; i < processRunQueue.size(); i++) {
            processRunQueue.set(i, processRunQueue.get(i).findById(processes).get());
            // AbstractSchedule logic에서 업데이트 한 것을 processRunQueue에 적용
        }
    }
}
