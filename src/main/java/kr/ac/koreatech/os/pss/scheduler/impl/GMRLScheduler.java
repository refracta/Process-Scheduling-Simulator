package kr.ac.koreatech.os.pss.scheduler.impl;

import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;
import kr.ac.koreatech.os.pss.scheduler.PriorityScheduler;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class GMRLScheduler extends RRScheduler {

    private static final String PROCESS_QUEUE = "processQueue";
    private int Flag_count = 3;
    // 플래그 카운터

    public GMRLScheduler(int timeQuantum) {
        super(timeQuantum);
        this.Flag_count = Flag_count;
    }

    @Override
    protected void init(List<AbstractCore> cores, List<DefaultProcess> processes, ScheduleData scheduleData) {
        super.init(cores, processes, scheduleData);
        scheduleData.put(PROCESS_QUEUE, new LinkedList<>());
    }

    @Override
    protected void schedule(int time, List<AbstractCore> cores, List<DefaultProcess> processes, ScheduleData scheduleData) {

        List<DefaultProcess> previousList = scheduleData.getProcesses(time - 1);
        List<DefaultProcess> currentList = scheduleData.getProcesses(time);

        LinkedList<DefaultProcess> processQueue = (LinkedList<DefaultProcess>) scheduleData.get(PROCESS_QUEUE);
        List<DefaultProcess> filteredProcesses = processes.stream().filter(p -> !p.isFinished() && p.getArrivalTime() <= time && !p.isIncludedIn(processQueue)).toList();

        List<DefaultProcess> arrivalSortedProcesses = filteredProcesses.stream()
                .filter(p -> !p.isIncludedIn(previousList)).sorted(Comparator.comparingInt(DefaultProcess::getArrivalTime)).toList();
        // [끝나지 않았고, 도착 시간보다 현재 시간이 같거나 지났고, 프로세스 큐에 없고, 직전 시간에 스케줄되지 않은] 프로세스들을 도착 시간 순으로 오름차순 정렬한 프로세스들

        List<DefaultProcess> LargerBurstTimeSortedProcesses = filteredProcesses.stream()
                        .filter(p-> !p.isIncludedIn(previousList)).sorted(Comparator.comparingInt(DefaultProcess::getBurstTime).reversed()).toList();
        // [끝나지 않았고, 도착 시간보다 현재 시간이 같거나 지났고, 프로세스 큐에 없고, 직전 시간에 스케줄되지 않은] 프로세스들을 실행 시간 순으로 내림차순 정렬한 프로세스들
        
        
        processQueue.addAll(arrivalSortedProcesses);
        // 프로세스 큐에 arrivalSortedProcesses 프로세스들을 먼저 넣기

        processQueue.addAll(filteredProcesses.stream()
                .filter(p -> p.isIncludedIn(previousList) && !p.isIncludedIn(currentList)).toList());
        // 프로세스 큐에 [끝나지 않았고, 도착 시간보다 현재 시간이 같거나 지났고, 프로세스 큐에 없고, 직전 시간에 스케줄되고, 현재 시간에 스케줄되지 않은] 프로세스들을 넣기




        // 플래그에 따라


        // ??



        // PriorityScheduler와 동일
        for (AbstractCore core : cores) {
            if (processQueue.isEmpty()) {
                break;
            }
            List<DefaultProcess> coreSchedule = scheduleData.getCoreSchedule(core);
            if (!coreSchedule.isEmpty()) {
                if (Flag_count <= 3) {
                    if (coreSchedule.size() < time + 1) {
                        DefaultProcess targetProcess = processQueue.poll();
                        coreSchedule.addAll(targetProcess.getContiguousProcesses(timeQuantum * core.getPerformance(), core.getPerformance()));
                        // timeQuantum 만큼만 스케줄 추가
                        Flag_count += 1;
                    } // else -> already exist schedule
                } else if (Flag_count == 3) {
                    DefaultProcess targetProcess = processQueue.poll();
                    Flag_count = 0;
                }
            } else {
                DefaultProcess targetProcess = processQueue.poll();
                coreSchedule.addAll(targetProcess.getContiguousProcesses(timeQuantum * core.getPerformance(), core.getPerformance()));
                // timeQuantum 만큼만 스케줄 추가
            }
        }

    }
    }


