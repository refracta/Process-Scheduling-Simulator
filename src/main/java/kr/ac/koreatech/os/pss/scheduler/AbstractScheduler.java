package kr.ac.koreatech.os.pss.scheduler;

import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;
import kr.ac.koreatech.os.pss.process.impl.EmptyProcess;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;
import kr.ac.koreatech.os.pss.scheduler.exception.ScheduleTimeoutException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 추상 스케줄러 클래스
 * 이 추상 스케줄러 클래스는 상속하여 특정 스케줄러 클래스를 구현하는데 사용된다.
 *
 * @author refracta
 */
public abstract class AbstractScheduler {
    /**
     * 최대 스케줄 시간
     */
    private int maxTime = 65536 - 1;

    /**
     * 추상 스케줄러 클래스의 생성자
     *
     * @param maxTime 최대 스케줄 시간
     */
    public AbstractScheduler(int maxTime) {
        this.maxTime = maxTime;
    }

    /**
     * 추상 스케줄러 클래스의 생성자
     */
    public AbstractScheduler() {
    }

    public final int getMaxTime() {
        return maxTime;
    }

    public final void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }

    /**
     * 스케줄러 클래스를 구현할 때 직접 재정의하여 구현해야하는 함수
     * 이 함수에서는 매 시간(time)마다 scheduleData.getSchedule() (Map&lt;AbstractCore, List&lt;DefaultProcess&gt;&gt;)의 value (List&lt;DefaultProcess)의 index(=time)에 프로세스를 채워 넣어야 한다. 채워 넣지 않은 경우 schedule 함수가 종료 된 후 모든 코어의 index(=time)에 EmptyProcess가 추가된다.
     * 매 시간(time)마다 processes 매개변수는 이전 시간에 호출된 processes 매개변수를 복사(clone)한 값이 호출된다.
     * scheduleData.getSchedule()의 index(=time)에 추가된 프로세스가 있는 경우 다음 시간 (time+1)에 호출되는 processes의 해당 프로세스는 코어의 성능 수치 만큼 남은 실행 시간이 감소한다.
     * 모든 프로세스의 isFinished가 참을 반환하면 이 함수 호출이 중지된다. (남은 실행 시간 == 0)
     * <p>
     * 스케줄 작업이 끝난 후(이 함수 호출이 중지된 후) scheduleData.getSchedule()에서 빈 스케줄 공간은 모두 EmptyProcess로 후처리되어 채워진다.
     * 예를 들어 [[P, P], [P, P, P, P]]로 스케줄 작업이 끝나면 [[P, P, EP, EP], [P, P, P, P]]로 후처리가 이루어진다.
     *
     * @param time         스케줄링 시간
     * @param cores        스케줄링을 요청한 코어 리스트
     * @param processes    스케줄링을 요청한 프로세스 리스트
     * @param scheduleData 스케줄링 결과를 저장하는 데이터 객체
     */
    protected abstract void schedule(int time, List<AbstractCore> cores, List<DefaultProcess> processes, ScheduleData scheduleData);

    /**
     * 스케줄러의 스케줄이 시작되기 전에 호출되는 함수, 재정의하여 전처리 작업 또는 스케줄링간 필요한 멤버 변수를 초기화하는 용도로 사용 할 수 있다.
     *
     * @param cores        스케줄링을 요청한 코어 리스트
     * @param processes    스케줄링을 요청한 프로세스 리스트
     * @param scheduleData 스케줄링 결과를 저장하는 데이터 객체
     */
    protected void init(List<AbstractCore> cores, List<DefaultProcess> processes, ScheduleData scheduleData) {
    }

    /**
     * 스케줄러의 스케줄이 끝난 후 호출되는 함수, 재정의하여 후처리 작업을 하는 용도로 사용할 수 있다.
     *
     * @param cores           스케줄링을 요청한 코어 리스트
     * @param resultProcesses 결과 프로세스 리스트
     * @param scheduleData    스케줄링 결과를 저장하는 데이터 객체
     */
    protected void end(List<AbstractCore> cores, List<DefaultProcess> resultProcesses, ScheduleData scheduleData) {
    }

    /**
     * 실제 사용자가 스케줄 요청을 하기위해 호출하는 외부에서 접근 가능한 스케줄 함수
     *
     * @param cores     스케줄링을 요청한 코어 리스트
     * @param processes 스케줄링 결과를 저장하는 데이터 객체
     * @return 스케줄링 결과를 저장하는 데이터 객체
     */
    public final ScheduleData schedule(List<AbstractCore> cores, List<DefaultProcess> processes) {
        ScheduleData scheduleData = new ScheduleData();
        List<AbstractCore> copyCores = List.copyOf(cores).stream().map(AbstractCore::clone).toList();
        List<DefaultProcess> copyProcesses = List.copyOf(processes).stream().map(DefaultProcess::clone).toList();

        Map<AbstractCore, List<DefaultProcess>> schedule = scheduleData.getSchedule();
        copyCores.forEach(c -> schedule.put(c, new ArrayList<>()));

        init(copyCores, copyProcesses, scheduleData);
        for (int time = 0; !copyProcesses.stream().allMatch(DefaultProcess::isFinished); time++) {
            if (maxTime <= time) {
                throw new ScheduleTimeoutException(maxTime, copyCores, copyProcesses, scheduleData);
            }
            schedule(time, copyCores, copyProcesses, scheduleData);

            Set<Map.Entry<AbstractCore, List<DefaultProcess>>> entries = schedule.entrySet();
            for (Map.Entry<AbstractCore, List<DefaultProcess>> e : entries) {
                AbstractCore core = e.getKey();
                List<DefaultProcess> tProcesses = e.getValue();
                for (int t = time; t < tProcesses.size(); t++) {
                    DefaultProcess sp = tProcesses.get(t);
                    DefaultProcess cp = sp.findById(copyProcesses).get();

                    if (cp.getStartTime() == DefaultProcess.NOT_INITIALIZED) {
                        cp.setStartTime(t);
                    }
                    if (sp.getStartTime() == DefaultProcess.NOT_INITIALIZED) {
                        sp.setStartTime(cp.getStartTime());
                    }
                    if (sp.getWaitingTime() == DefaultProcess.NOT_INITIALIZED) {
                        sp.setTurnaroundTime(t + 1 - sp.getArrivalTime());
                        sp.setWaitingTime(sp.getTurnaroundTime() - scheduleData.getRealBurstTime(sp, t));
                    }

                    if (sp.getLeftBurstTime() - core.getPerformance() <= 0) {
                        sp.setTurnaroundTime(t + 1 - sp.getArrivalTime());
                        sp.setWaitingTime(sp.getTurnaroundTime() - scheduleData.getRealBurstTime(sp, t));

                        cp.setLeftBurstTime(0);
                        cp.setTurnaroundTime(sp.getTurnaroundTime());
                        cp.setWaitingTime(sp.getWaitingTime());
                    } else if (!cp.isFinished() && t == time) {
                        cp.setLeftBurstTime(cp.getLeftBurstTime() - core.getPerformance());
                    }
                }
            }
            copyProcesses = List.copyOf(copyProcesses).parallelStream().map(DefaultProcess::clone).collect(Collectors.toList());
            for (AbstractCore core : copyCores) {
                List<DefaultProcess> coreSchedule = scheduleData.getSchedule().get(core);
                if (coreSchedule.size() <= time) {
                    coreSchedule.add(EmptyProcess.getInstance());
                }
            }
        }
        Collection<List<DefaultProcess>> values = schedule.values();
        int max = values.parallelStream().mapToInt(List::size).max().getAsInt();
        for (List<DefaultProcess> s : values) {
            s.addAll(Collections.nCopies(max - s.size(), EmptyProcess.getInstance()));
        }
        end(copyCores, copyProcesses, scheduleData);
        scheduleData.getResultProcesses().addAll(copyProcesses);
        return scheduleData;
    }

    /**
     * 실제 사용자가 스케줄 요청을 하기위해 호출하는 외부에서 접근 가능한 스케줄 함수
     * ScheduleData schedule(List&lt;AbstractCore&gt; cores, List&lt;DefaultProcess&gt; processes)과 동치이다.
     *
     * @param cores     스케줄링을 요청한 코어 리스트
     * @param processes 스케줄링 결과를 저장하는 데이터 객체
     * @return 스케줄링 결과를 저장하는 데이터 객체
     */
    public final ScheduleData schedule(AbstractCore[] cores, DefaultProcess[] processes) {
        return schedule(Arrays.asList(cores), Arrays.asList(processes));
    }
}
