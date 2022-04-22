package kr.ac.koreatech.os.test.scheduler;

import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.core.impl.EfficiencyCore;
import kr.ac.koreatech.os.pss.core.impl.PerformanceCore;
import kr.ac.koreatech.os.pss.process.AbstractProcess;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;
import kr.ac.koreatech.os.pss.scheduler.AbstractScheduler;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 스케쥴러 예제 클래스
 *
 * @author refracta
 */
public class SchedulerExampleTest {

    public void printScheduleData(ScheduleData scheduleData, boolean onlyCoreSchedule) {
        List<AbstractCore> cores = new ArrayList<>(scheduleData.getSchedule().keySet());
        cores.sort(Comparator.comparingInt(AbstractCore::getId));
        for (int i = 0; i < cores.size(); i++) {
            AbstractCore currentCore = cores.get(i);
            List<DefaultProcess> coreSchedule = scheduleData.getSchedule().get(currentCore);
            double powerUsage = scheduleData.getPowerUsage(currentCore);
            System.out.println("Core[" + i + ":" + currentCore.getClass().getSimpleName() + "]" + ": " + "[" + coreSchedule.stream().map(AbstractProcess::getName).collect(Collectors.joining(", ")) + "]: " + powerUsage + "W");
        }
        if (!onlyCoreSchedule) {
            System.out.println("Total power usage: " + scheduleData.getTotalPowerUsage() + "W");
            System.out.println("Result processes:");
            System.out.println(scheduleData.getResultProcesses().stream().map(p -> "\t" + p.toString()).collect(Collectors.joining("\n")));
            System.out.println("Average response time: " + scheduleData.getAverageResponseTime());
        }
    }

    @Test
    public void example1() {
        AbstractScheduler testScheduler = new AbstractScheduler() {
            @Override
            protected void schedule(int time, List<AbstractCore> cores, List<DefaultProcess> processes, ScheduleData scheduleData) {
                List<DefaultProcess> core0Schedule = scheduleData.getSchedule().get(cores.get(0));
                // EfficiencyCore
                List<DefaultProcess> core1Schedule = scheduleData.getSchedule().get(cores.get(1));
                // PerformanceCore
                DefaultProcess process0 = processes.get(0);
                // new DefaultProcess(0, 0, 4)
                DefaultProcess process1 = processes.get(1);
                // new DefaultProcess(1, 2, 3)
                DefaultProcess process2 = processes.get(2);
                // new DefaultProcess(2, 3, 3)

                if (time == 0) {
                    core0Schedule.add(process0);
                } else if (time == 1) {
                    core0Schedule.add(process0);
                    core1Schedule.add(process1);
                } else if (time == 2) {
                    core0Schedule.add(process1);
                    core1Schedule.add(process0);
                } else if (time == 4 || time == 5 || time == 6) {
                    core0Schedule.add(process2);
                }

                System.out.println("Time: " + time);
                printScheduleData(scheduleData, true);
            }
        };
        AbstractCore[] cores = {new EfficiencyCore(), new PerformanceCore()};
        DefaultProcess[] processes = {
                new DefaultProcess(0, 0, 4),
                new DefaultProcess(1, 1, 3),
                new DefaultProcess(2, 3, 3),
        };
        ScheduleData scheduleData = testScheduler.schedule(cores, processes);
        System.out.println();
        System.out.println("Result:");
        printScheduleData(scheduleData, false);
    }
}
