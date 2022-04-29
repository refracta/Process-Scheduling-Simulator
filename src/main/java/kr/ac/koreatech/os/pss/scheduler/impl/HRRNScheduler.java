package kr.ac.koreatech.os.pss.scheduler.impl;

import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;
import kr.ac.koreatech.os.pss.scheduler.PriorityScheduler;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;

import java.util.Comparator;
import java.util.List;

/**
 * High Response Ratio Next 스케줄러 클래스
 *
 * @author refracta
 */
public class HRRNScheduler extends PriorityScheduler {
    public HRRNScheduler() {
        super(Comparator.comparingDouble(DefaultProcess::getResponseRatio).reversed());
        // 응답 시간을 기준으로 우선순위 스케줄러의 comparator 설정
    }

    @Override
    protected void schedule(int time, List<AbstractCore> cores, List<DefaultProcess> processes, ScheduleData scheduleData) {
        processes.stream().filter(p -> !p.isFinished() && p.getArrivalTime() <= time).forEach(p -> {
            p.setTurnaroundTime(time - p.getArrivalTime());
            p.setWaitingTime(p.getTurnaroundTime() - scheduleData.getRealBurstTime(p, time));
        });
        // responseRatio를 계산하기 위해서는 turnaroundTime, waitingTime이 계산되어야 함
        // turnaroundTime, waitingTime등은 스케줄 리스트에 적재가 완료되거나, 해당 프로세스가 끝난 다음에 설정되지만, 당장 스케줄링을 위해서 현재 프로세스의 ResponseRatio 계산이 필요하므로 이를 먼저 계산함
        super.schedule(time, cores, processes, scheduleData);
        // PriorityScheduler에 이후 스케줄을 맡김
    }
}
