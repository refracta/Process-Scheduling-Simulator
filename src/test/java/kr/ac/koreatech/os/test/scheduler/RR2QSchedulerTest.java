package kr.ac.koreatech.os.test.scheduler;

import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.core.impl.EfficiencyCore;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;
import kr.ac.koreatech.os.pss.scheduler.impl.RR2QScheduler;
import kr.ac.koreatech.os.pss.scheduler.utils.PrintUtils;
import kr.ac.koreatech.os.test.utils.TestScheduleUtils;
import kr.ac.koreatech.os.test.utils.TestSetUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author YOUKK
 * @author refracta
 */
public class RR2QSchedulerTest {

    @Test
    public void test1WithTQ2AndRQN3() {
        TestScheduleUtils.runWithDefaultTestSet(new RR2QScheduler(2, 3));
    }

    @Test
    public void test2WithTQ2AndRQN3() {
        TestScheduleUtils.runWithEasyLevelRandomTestSet(new RR2QScheduler(2, 3), System.currentTimeMillis());
    }

    @Test
    public void test3WithTQ2AndRQN3() {
        TestScheduleUtils.runWithHardLevelRandomTestSet(new RR2QScheduler(2, 3), System.currentTimeMillis());
    }


    @Test
    public void test4WithTQ2AndRQ2() {
        List<AbstractCore> cores = TestSetUtils.getRandomTestCores(2);
        List<DefaultProcess> processes = TestSetUtils.getRandomTestProcesses(15, 6, 15);
        ScheduleData scheduleData = new RR2QScheduler(2, 2).schedule(cores, processes);
        PrintUtils.printScheduleData(scheduleData);
    }

    @Test
    public void test5WithTQ2AndRQ2() {
        List<AbstractCore> cores = Arrays.asList(new AbstractCore[]{new EfficiencyCore(), new EfficiencyCore()});
        List<DefaultProcess> processes = Arrays.asList(new DefaultProcess[]
                {
                        new DefaultProcess(1, 5, 13),
                        new DefaultProcess(2, 1, 6),
                        new DefaultProcess(3, 5, 9),
                        new DefaultProcess(4, 0, 4),
                        new DefaultProcess(5, 4, 12),
                        new DefaultProcess(6, 1, 1),
                        new DefaultProcess(7, 5, 8),
                        new DefaultProcess(8, 5, 8),
                        new DefaultProcess(9, 3, 11),
                        new DefaultProcess(10, 1, 13),
                        new DefaultProcess(11, 5, 6),
                        new DefaultProcess(12, 0, 6),
                        new DefaultProcess(13, 0, 13),
                        new DefaultProcess(14, 0, 6),
                        new DefaultProcess(15, 5, 8)
                }
        );
        ScheduleData scheduleData = new RR2QScheduler(2, 2).schedule(cores, processes);
        PrintUtils.printScheduleData(scheduleData);
    }
}


