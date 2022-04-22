package kr.ac.koreatech.os.test.scheduler;

import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.core.impl.EfficiencyCore;
import kr.ac.koreatech.os.pss.core.impl.PerformanceCore;
import kr.ac.koreatech.os.pss.process.AbstractProcess;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;
import kr.ac.koreatech.os.pss.scheduler.impl.FCFSScheduler;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * FCFS 스케쥴러 테스트 클래스
 *
 * @author refracta
 */
public class FCFSSchedulerTest {
    @Test
    public void test1() {
        FCFSScheduler fcfsScheduler = new FCFSScheduler();
        AbstractCore[] cores = {new EfficiencyCore(), new PerformanceCore(), new PerformanceCore()};
        DefaultProcess[] processes = {
                new DefaultProcess(1, 4),
                new DefaultProcess(2, 4),
                new DefaultProcess(3, 7),
                new DefaultProcess(3, 7),
                new DefaultProcess(3, 7),
                new DefaultProcess(3, 7),
        };
        ScheduleData scheduleData = fcfsScheduler.schedule(cores, processes);
        for (int i = 0; i < cores.length; i++) {
            AbstractCore currentCore = cores[i];
            List<DefaultProcess> coreSchedule = scheduleData.getSchedule().get(currentCore);
            double powerUsage = scheduleData.getPowerUsage(currentCore);
            System.out.println("Core[" + i + ":" + currentCore.getClass().getSimpleName() + "]" + ": " + "[" + coreSchedule.stream().map(AbstractProcess::getName).collect(Collectors.joining(", ")) + "]: " + powerUsage + "W");
        }
        System.out.println("Total power usage: " + scheduleData.getTotalPowerUsage() + "W");
        System.out.println("Result processes:");
        System.out.println(scheduleData.getResultProcesses().stream().map(p -> "\t" + p.toString()).collect(Collectors.joining("\n")));
        System.out.println("Average response time: " + scheduleData.getAverageResponseTime());
    }

    @Test
    public void test2() {
        FCFSScheduler fcfsScheduler = new FCFSScheduler();
        Random random = new Random();
        List<AbstractCore> cores = random.ints(10, 0, 2).mapToObj(i -> i == 0 ? new EfficiencyCore() : new PerformanceCore()).collect(toList());
        List<DefaultProcess> processes = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            processes.add(new DefaultProcess(random.nextInt(100), random.nextInt(1, 100)));
        }
        ScheduleData scheduleData = fcfsScheduler.schedule(cores, processes);
        for (int i = 0; i < cores.size(); i++) {
            AbstractCore currentCore = cores.get(i);
            List<DefaultProcess> coreSchedule = scheduleData.getSchedule().get(currentCore);
            double powerUsage = scheduleData.getPowerUsage(currentCore);
            System.out.println("Core[" + i + ":" + currentCore.getClass().getSimpleName() + "]" + ": " + "[" + coreSchedule.stream().map(AbstractProcess::getName).collect(Collectors.joining(", ")) + "]: " + powerUsage + "W");
        }
        System.out.println("Total power usage: " + scheduleData.getTotalPowerUsage() + "W");
        System.out.println("Result processes:");
        System.out.println(scheduleData.getResultProcesses().stream().map(p -> "\t" + p.toString()).collect(Collectors.joining("\n")));
        System.out.println("Average response time: " + scheduleData.getAverageResponseTime());
    }
}
