package kr.ac.koreatech.os.pss.scheduler.impl;

import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;
import kr.ac.koreatech.os.pss.scheduler.AbstractScheduler;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

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
    // schedule 함수 호출을 Thread-Safe하게 만들기 위해서 스케줄링에 사용되는 변수를 멤버 변수로 사용하지 않고, 각 스케줄 함수마다 독립적으로 생성되는 ScheduleData 내부에 저장함

    @Override
    protected void schedule(int time, List<AbstractCore> cores, List<DefaultProcess> processes, ScheduleData scheduleData) {
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
            if (!coreSchedule.isEmpty()) {
                if (coreSchedule.size() < time + 1) {
                    DefaultProcess targetProcess = processQueue.poll();
                    coreSchedule.addAll(targetProcess.getContiguousProcesses(timeQuantum * core.getPerformance(), core.getPerformance()));
                    // timeQuantum 만큼만 스케줄 추가
                } // else -> already exist schedule
            } else {
                DefaultProcess targetProcess = processQueue.poll();
                coreSchedule.addAll(targetProcess.getContiguousProcesses(timeQuantum * core.getPerformance(), core.getPerformance()));
                // timeQuantum 만큼만 스케줄 추가
            }
        }
    }
}
