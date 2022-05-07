package kr.ac.koreatech.os.pss.scheduler.impl;

import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;
import kr.ac.koreatech.os.pss.scheduler.AbstractScheduler;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class GMRLScheduler extends AbstractScheduler {

    private static final String PROCESS_QUEUE = "processQueue";
    private static final String CURRENT_FLAG_COUNT = "processCount";
    private int timeQuantum;
    private int flagCount;


    // 플래그 카운터

    public GMRLScheduler(int timeQuantum, int flagCount) {
        this.timeQuantum = timeQuantum;
        this.flagCount = flagCount;
    }

    protected void init(List<AbstractCore> cores, List<DefaultProcess> processes, ScheduleData scheduleData) {
        scheduleData.put(PROCESS_QUEUE, new LinkedList<DefaultProcess>());
        scheduleData.put(CURRENT_FLAG_COUNT, 0);
    }


    @Override
    protected void schedule(int time, List<AbstractCore> cores, List<DefaultProcess> processes, ScheduleData scheduleData) {
        Integer currentFlagCount = (Integer) scheduleData.get(CURRENT_FLAG_COUNT);
        List<DefaultProcess> previousList = scheduleData.getProcesses(time - 1);
        // 모든 코어의 스케줄 리스트에서 직전(t-1) 시간 색인에 적재된 모든 프로세스를 가져옴
        List<DefaultProcess> currentList = scheduleData.getProcesses(time);
        // 모든 코어의 스케줄 리스트에서 현재 시간 색인에 적재된 모든 프로세스를 가져옴

        LinkedList<DefaultProcess> processQueue = (LinkedList<DefaultProcess>) scheduleData.get(PROCESS_QUEUE);
        // init에서 초기화했던 프로세스 큐 가져오기
        List<DefaultProcess> filteredProcesses = processes.stream().filter(p -> !p.isFinished() &&
                p.getArrivalTime() <= time &&
                !p.isIncludedIn(processQueue)).toList();
        // 끝나지 않았고, 도착 시간보다 현재 시간이 같거나 지났고, 프로세스 큐에 없는 조건으로 프로세스를 필터링

        List<DefaultProcess> arrivalSortedProcesses = filteredProcesses.stream()
                .filter(p -> !p.isIncludedIn(previousList)).sorted(Comparator.comparingInt(DefaultProcess::getArrivalTime)).toList();
        // [끝나지 않았고, 도착 시간보다 현재 시간이 같거나 지났고, 프로세스 큐에 없고, 직전 시간에 스케줄되지 않은] 프로세스들을 도착 시간 순으로 오름차순 정렬한 프로세스들

        processQueue.addAll(arrivalSortedProcesses);
        // 프로세스 큐에 arrivalSortedProcesses 프로세스들을 먼저 넣기

        processQueue.addAll(filteredProcesses.stream()
                .filter(p -> p.isIncludedIn(previousList) && !p.isIncludedIn(currentList)).toList());
        // 프로세스 큐에 [끝나지 않았고, 도착 시간보다 현재 시간이 같거나 지났고, 프로세스 큐에 없고, 직전 시간에 스케줄되고, 현재 시간에 스케줄되지 않은] 프로세스들을 넣기

        // 현재 프로세스 큐 상태
        // [[끝나지 않았고, 도착 시간보다 현재 시간이 같거나 지났고, 프로세스 큐에 없고, 직전 시간에 스케줄되지 않은] 프로세스들을 도착 시간 순으로 오름차순 정렬한 프로세스들] +
        // [끝나지 않았고, 도착 시간보다 현재 시간이 같거나 지났고, 프로세스 큐에 없고, 직전 시간에 스케줄되고, 현재 시간에 스케줄되지 않은 프로세스들]
        // = [(직전 시간에 스케줄 안된 프로세스들을 도착 시간 순으로 오름차순 정렬), (직전 시간에 스케줄되고, 현재 시간에 스케줄되지 않은 프로세스들)]

        // PriorityScheduler와 동일
        for (AbstractCore core : cores) {
            if (processQueue.isEmpty()) {
                break;
            }
            List<DefaultProcess> coreSchedule = scheduleData.getCoreSchedule(core);
            boolean isValidScheduleTime = false;
            if (!coreSchedule.isEmpty()) {
                if (coreSchedule.size() < time + 1) {
                    isValidScheduleTime = true;
                } // else -> already exist schedule
            } else {
                isValidScheduleTime = true;
            }

            if (isValidScheduleTime) {
                DefaultProcess targetProcess = processQueue.poll();
                if (currentFlagCount >= flagCount && !processQueue.isEmpty()) {
                    LinkedList<DefaultProcess> copyProcessQueue = new LinkedList<>(processQueue);
                    copyProcessQueue.add(targetProcess);
                    copyProcessQueue.sort(Comparator.comparingInt(DefaultProcess::getLeftBurstTime));
                    targetProcess = copyProcessQueue.pollLast();
                    processQueue.remove(targetProcess);
                    currentFlagCount = 0;
                } else {
                    currentFlagCount++;
                }
                coreSchedule.addAll(targetProcess.getContiguousProcesses(timeQuantum * core.getPerformance(), core.getPerformance()));
            }
            scheduleData.put(CURRENT_FLAG_COUNT, currentFlagCount);
        }
    }

}
