package kr.ac.koreatech.os.pss.scheduler.impl;

import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;
import kr.ac.koreatech.os.pss.scheduler.AbstractScheduler;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;


public class MyScheduler_yk extends AbstractScheduler {
    private static final String PROCESS_RUNQUEUE = "processRunQueue";
    private static final String PROCESS_READYQUEUE = "processReadyQueue";
    private final int timeQuantum;
    private final int runQueueNum;

    public MyScheduler_yk(int timeQuantum, int runQueueNum) {
        this.timeQuantum = timeQuantum;
        this.runQueueNum = runQueueNum;
    }

    @Override
    protected void init(List<AbstractCore> cores, List<DefaultProcess> processes, ScheduleData scheduleData) {
        //super.init(cores, processes, scheduleData);
        scheduleData.put(PROCESS_RUNQUEUE, new LinkedList<>());
        scheduleData.put(PROCESS_READYQUEUE, new LinkedList<>());
    }

    @Override
    public void schedule(int time, List<AbstractCore> cores, List<DefaultProcess> processes, ScheduleData scheduleData) {
        List<DefaultProcess> previousList = scheduleData.getProcesses(time - 1);
        List<DefaultProcess> currentList = scheduleData.getProcesses(time);

        //LinkedList<DefaultProcess> processRunQueue = (LinkedList<DefaultProcess>) scheduleData.get(PROCESS_RUNQUEUE);
        LinkedList<DefaultProcess> processRunQueue = new LinkedList<>();
        LinkedList<DefaultProcess> processReadyQueue = (LinkedList<DefaultProcess>) scheduleData.get(PROCESS_READYQUEUE);


        List<DefaultProcess> filteredProcesses = processes.stream().filter(p -> !p.isFinished() &&
                p.getArrivalTime() <= time &&
                !p.isIncludedIn(processReadyQueue)).toList();

        List<DefaultProcess> arrivalSortedProcesses = filteredProcesses.stream()
                .filter(p -> !p.isIncludedIn(previousList)).sorted(Comparator.comparingInt(DefaultProcess::getArrivalTime)).toList();

        processReadyQueue.addAll(arrivalSortedProcesses);

        // RunQueue를 채워하는 경우 : RunQueue가 비어있고 readyQueue는 안 비어져 있는 경우
        // 만약 RunQueue가 비어있음 && readyQueue도 비어져있음 => break
        while (processRunQueue.size() <= runQueueNum) {
            if (processReadyQueue.isEmpty()) {
                break;
            }
            processRunQueue.add(processReadyQueue.poll());
        }

        processRunQueue.addAll(filteredProcesses.stream()
                .filter(p -> p.isIncludedIn(previousList) && !p.isIncludedIn(currentList)).toList());



        for (AbstractCore core : cores) {
            if (processRunQueue.isEmpty()) {
                break;
            }
            List<DefaultProcess> coreSchedule = scheduleData.getCoreSchedule(core);
            if (!coreSchedule.isEmpty()) {
                if (coreSchedule.size() < time + 1) {
                    DefaultProcess targetProcess = processRunQueue.poll();
                    coreSchedule.addAll(targetProcess.getContiguousProcesses(timeQuantum * core.getPerformance(), core.getPerformance()));
                }
            } else {
                DefaultProcess targetProcess = processRunQueue.poll();
                coreSchedule.addAll(targetProcess.getContiguousProcesses(timeQuantum * core.getPerformance(), core.getPerformance()));
            }
        }
    }
}
