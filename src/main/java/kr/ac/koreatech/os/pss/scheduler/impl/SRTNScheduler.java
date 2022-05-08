package kr.ac.koreatech.os.pss.scheduler.impl;

import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;
import kr.ac.koreatech.os.pss.scheduler.AbstractScheduler;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;

import java.util.*;

/**
 * Shortest Remaining Time Next 스케줄러 클래스
 *
 * @author refracta
 */
public class SRTNScheduler extends AbstractScheduler {
    private static final String PROCESS_QUEUE = "processQueue";
    private static final String CHECK_TIMES = "checkTimes";


    @Override
    protected void init(List<AbstractCore> cores, List<DefaultProcess> processes, ScheduleData scheduleData) {
        scheduleData.put(PROCESS_QUEUE, new LinkedList<>());
        scheduleData.put(CHECK_TIMES, new LinkedList<>(new HashSet<>(processes.stream().map(DefaultProcess::getArrivalTime).sorted().toList())));
        // 스케줄을 하는 시간들의 색인 설정, 최초에는 각 프로세스의 도착 시간들이 이 시간들이 됨 (HashSet으로 중복 제거)
        // System.out.println(scheduleData.get(CHECK_TIMES));
    }
    // schedule 함수 호출을 Thread-Safe하게 만들기 위해서 스케줄링에 사용되는 변수를 멤버 변수로 사용하지 않고, 각 스케줄 함수마다 독립적으로 생성되는 ScheduleData 내부에 저장함

    @Override
    protected void schedule(int time, List<AbstractCore> cores, List<DefaultProcess> processes, ScheduleData scheduleData) {
        LinkedList<Integer> checkTimes = (LinkedList<Integer>) scheduleData.get(CHECK_TIMES);
        // 스케줄을 하는 시간들의 색인

        PriorityQueue<DefaultProcess> processQueue = new PriorityQueue<>(Comparator.comparingInt(DefaultProcess::getLeftBurstTime));
        // 남은 실행 시간 순으로 정렬하는 우선순위 큐 정의 (오름차순)
        processQueue.addAll(processes.stream().filter(p -> !p.isFinished() && p.getArrivalTime() <= time).toList());
        // 끝나지 않았고, 끝나지 않았고, 도착 시간이 현재 시간보다 지난 프로세스들을 우선순위 큐에 넣기

        // 스케줄이 결정되는 시간이 비지 않았으면
        if (!checkTimes.isEmpty()) {
            Integer currentScheduleTime = checkTimes.get(0);

            // 현재 시간과 스케줄이 결정되는 시간이 동일할 때만 아래 로직을 진행
            if (currentScheduleTime != time) {
                return;
            } else {
                checkTimes.poll();
                // 진행하는 경우이므로, checkTimes에서 시간을 하나 뺌
            }
            /*
                System.out.println("=========================================================");
                System.out.println("Time: " + time);
                System.out.println("checkTimes: " + checkTimes);
                System.out.println("processQueue: " + processQueue.stream().map(p -> p.getName()).toList());
                System.out.print("Before: ");
                PrintUtils.printScheduleData(scheduleData, true);
            */

            // PriorityScheduler와 유사한 흐름
            for (AbstractCore core : cores) {
                if (processQueue.isEmpty()) {
                    break;
                }
                List<DefaultProcess> coreSchedule = scheduleData.getCoreSchedule(core);

                // 스케줄이 이루어져야 하는 타이밍인지 계산
                boolean isValidScheduleTiming = false;
                if (!coreSchedule.isEmpty()) {
                    if (coreSchedule.size() < time + 1) {
                        // DefaultProcess previous = coreSchedule.get(time - 1);
                        isValidScheduleTiming = true;
                    } // else -> already exist schedule
                } else {

                    isValidScheduleTiming = true;
                }

                // 스케줄이 필요한 타이밍이면
                if (isValidScheduleTiming) {

                    DefaultProcess targetProcess = processQueue.poll();
                    // 가장 남은 실행 시간이 적은 순으로 큐에서 빠져나옴

                    int needBurstTime = (int) Math.ceil(targetProcess.getLeftBurstTime() / (double) core.getPerformance());
                    // 프로세스를 끝내는데 필요한 실행 시간
                    Integer nextScheduleTime = currentScheduleTime + needBurstTime;
                    // 다음 실행 시간 계산 (needBurstTime 기준 선 계산, 현재 시간(currentScheduleTime) + needBurstTime)

                    if (checkTimes.isEmpty()) {
                        checkTimes.add(0, nextScheduleTime);
                        // checkTimes이 빈 경우 0번 자리에 nextScheduleTime을 넣기
                    } else if (needBurstTime > checkTimes.get(0) - currentScheduleTime) {
                        // checkTimes에 남은 [스케줄 하는 시간 색인]이 있고, 이 색인(바로 다음 스케줄 하는 시간 색인)과 현재 시간의 차이보다 프로세스를 끝내는데 필요한 실행 시간이 큰 경우
                        nextScheduleTime = checkTimes.get(0);
                        // nextScheduleTime을 checkTimes에 있는 것으로 설정 (프로세스를 끝낼 시간보다 더 짧은 시간으로 스케줄링)
                    }
                    /*
                        System.out.println();
                        System.out.println("needBurstTime: " + needBurstTime);
                        System.out.println("checkTimes.get(0): " + checkTimes.get(0));
                        System.out.println("currentScheduleTime: " + currentScheduleTime);
                        System.out.println("nextScheduleTime: " + nextScheduleTime);
                    */
                    int runTime = nextScheduleTime - currentScheduleTime;
                    //
                    coreSchedule.addAll(targetProcess.getContiguousProcesses(runTime * core.getPerformance(), core.getPerformance()));
                    /*
                        System.out.println("runTime: " + runTime);
                        System.out.print("After: ");
                        PrintUtils.printScheduleData(scheduleData, true);
                    */
                }


            }
        }
    }
}
