package kr.ac.koreatech.os.pss.scheduler;

import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
//////
/**
 * 우선 순위에 따른 간단한 스케줄링을 하는 스케줄러
 *
 * @author refracta
 */
public abstract class PriorityScheduler extends AbstractScheduler {
    private final Comparator<DefaultProcess> comparator;
    // 오름차순으로 정렬되는 comparator를 사용하면, 우선순위 큐의 poll 동작에서 가장 작은 요소가 내보내지게 되면 (가장 좌측에 있는 요소)

    public PriorityScheduler(Comparator<DefaultProcess> comparator) {
        this.comparator = comparator;
    }

    @Override
    protected void schedule(int time, List<AbstractCore> cores, List<DefaultProcess> processes, ScheduleData scheduleData) {
        PriorityQueue<DefaultProcess> processQueue = new PriorityQueue<>(comparator);
        processQueue.addAll(processes.stream().filter(p -> !p.isFinished() && p.getArrivalTime() <= time).toList());
        // 끝나지 않았고, 도착 시간이 현재 시간보다 지난 프로세스들을 comparator로 정렬되는 우선순위 큐에 가져오기
        for (AbstractCore core : cores) {
            // 각 코어별로 순회 시작
            if (processQueue.isEmpty()) {
                // 프로세스 큐에 아무것도 없으면 반복문을 종료
                break;
            }
            List<DefaultProcess> coreSchedule = scheduleData.getCoreSchedule(core);
            // 각 코어에 해당하는 스케줄 리스트를 가져오기
            if (!coreSchedule.isEmpty()) {
                // 코어 스케줄 리스트가 비어있지 않으면
                if (coreSchedule.size() < time + 1) {
                    // 현재 시간에 해당하는 코어 스케줄 리스트 색인에 스케줄된 프로세스가 존재하지 않으면 (getContiguousProcesses(...) 등을 이용하여 스케줄 시, 스케줄 리스트의 현재 시간보다 크거나 같은 프로세스가 스케줄 리스트에 미리 스케줄되어 있을 수 있음)

                    // DefaultProcess previous = coreSchedule.get(time - 1);
                    DefaultProcess targetProcess = processQueue.poll();
                    // 우선순위 프로세스 큐에서 가장 우선순위가 높은 프로세스 가져오기
                    coreSchedule.addAll(targetProcess.getContiguousProcesses(core.getPerformance()));
                    // 현재 코어의 성능 수치 만큼 프로세스의 실행 시간이 끝날때 까지 스케줄 추가
                } // else -> already exist schedule
            } else {
                // 코어 스케줄 리스트가 비어있는 경우
                DefaultProcess targetProcess = processQueue.poll();
                // 우선순위 프로세스 큐에서 가장 우선순위가 높은 프로세스 가져오기
                coreSchedule.addAll(targetProcess.getContiguousProcesses(core.getPerformance()));
                // 현재 코어의 성능 수치 만큼 프로세스의 실행 시간이 끝날때 까지 스케줄 추가
            }
        }
    }
}
