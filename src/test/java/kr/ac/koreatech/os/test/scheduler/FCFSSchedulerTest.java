package kr.ac.koreatech.os.test.scheduler;

import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.core.impl.EfficiencyCore;
import kr.ac.koreatech.os.pss.core.impl.PerformanceCore;
import kr.ac.koreatech.os.pss.process.AbstractProcess;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;
import kr.ac.koreatech.os.pss.scheduler.impl.FCFSScheduler;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

/**
 * FCFS 스케쥴러 테스트 클래스
 *
 * @author refracta
 */
public class FCFSSchedulerTest {
    @Test
    public void startFCFSSchedule() {
        FCFSScheduler fcfsScheduler = new FCFSScheduler();
        AbstractCore[] cores = {new EfficiencyCore(), new PerformanceCore(), new PerformanceCore()};
        DefaultProcess[] processes = {
                new DefaultProcess(0, 1, 4),
                new DefaultProcess(1, 2, 4),
                new DefaultProcess(2, 3, 7),
                new DefaultProcess(3, 3, 7),
                new DefaultProcess(4, 3, 7),
                new DefaultProcess(5, 3, 7),
        };
        ScheduleData scheduleData = fcfsScheduler.schedule(cores, processes);
        double totalPowerUsage = 0;
        for (int i = 0; i < cores.length; i++) {
            AbstractCore currentCore = cores[i];
            List<DefaultProcess> coreSchedule = scheduleData.getSchedule().get(currentCore);
            double powerUsage = scheduleData.getPowerUsage(currentCore);
            totalPowerUsage += powerUsage;
            System.out.println("Core[" + i + ":" + currentCore.getClass().getSimpleName() + "]" + ": " + "[" + coreSchedule.stream().map(AbstractProcess::getName).collect(Collectors.joining(", ")) + "]: " + powerUsage + "W");
        }
        System.out.println("Total power usage: " + totalPowerUsage + "W");
        System.out.println("Result processes:");
        System.out.println(scheduleData.getResultProcesses().stream().map(p -> "\t" + p.toString()).collect(Collectors.joining("\n")));
        System.out.println("Average response time: " + scheduleData.getAverageResponseTime());
    }
}
