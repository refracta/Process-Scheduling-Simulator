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
        // System.out.println(scheduleData.get(CHECK_TIMES));
    }


    @Override
    protected void schedule(int time, List<AbstractCore> cores, List<DefaultProcess> processes, ScheduleData scheduleData) {
        LinkedList<Integer> checkTimes = (LinkedList<Integer>) scheduleData.get(CHECK_TIMES);

        PriorityQueue<DefaultProcess> processQueue = new PriorityQueue<>(Comparator.comparingInt(DefaultProcess::getLeftBurstTime));
        processQueue.addAll(processes.stream().filter(p -> !p.isFinished() && p.getArrivalTime() <= time).toList());

        if (!checkTimes.isEmpty()) {
            Integer target = checkTimes.get(0);
            if (target != time) {
                return;
            } else {
                checkTimes.poll();
            }
            /*
                System.out.println("=========================================================");
                System.out.println("Time: " + time);
                System.out.println("checkTimes: " + checkTimes);
                System.out.println("processQueue: " + processQueue.stream().map(p -> p.getName()).toList());
                System.out.print("Before: ");
                PrintUtils.printScheduleData(scheduleData, true);
            */

            for (AbstractCore core : cores) {
                if (processQueue.isEmpty()) {
                    break;
                }
                List<DefaultProcess> coreSchedule = scheduleData.getCoreSchedule(core);

                DefaultProcess targetProcess = null;
                if (!coreSchedule.isEmpty()) {
                    if (coreSchedule.size() < time + 1) {
                        // DefaultProcess previous = coreSchedule.get(time - 1);
                        targetProcess = processQueue.poll();
                    } // else -> already exist schedule
                } else {
                    targetProcess = processQueue.poll();
                }

                if (targetProcess != null) {
                    int needBurstTime = (int) Math.ceil(targetProcess.getLeftBurstTime() / (double) core.getPerformance());
                    Integer nextTarget = target + needBurstTime;

                    if (checkTimes.size() < 1) {
                        checkTimes.add(0, nextTarget);
                    } else if (needBurstTime > checkTimes.get(0) - target) {
                        nextTarget = checkTimes.get(0);
                    }
                    /*
                        System.out.println();
                        System.out.println("needBurstTime: " + needBurstTime);
                        System.out.println("checkTimes.get(0): " + checkTimes.get(0));
                        System.out.println("target: " + target);
                        System.out.println("nextTarget: " + nextTarget);
                    */
                    int scheduleTime = nextTarget - target;
                    coreSchedule.addAll(targetProcess.getContiguousProcesses(scheduleTime * core.getPerformance(), core.getPerformance()));
                    /*
                        System.out.println("scheduleTime: " + scheduleTime);
                        System.out.print("After: ");
                        PrintUtils.printScheduleData(scheduleData, true);
                    */
                }
            }
        }
    }
}
