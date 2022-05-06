package kr.ac.koreatech.os.test.scheduler;

import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;
import kr.ac.koreatech.os.pss.scheduler.impl.RR2QScheduler;
import kr.ac.koreatech.os.pss.scheduler.utils.PrintUtils;
import kr.ac.koreatech.os.test.utils.TestScheduleUtils;
import kr.ac.koreatech.os.test.utils.TestSetUtils;
import org.junit.jupiter.api.Test;

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
    public void test2WithTQ2AndRQ2() {
        List<AbstractCore> cores = TestSetUtils.getRandomTestCores(2);
        List<DefaultProcess> processes = TestSetUtils.getRandomTestProcesses(15, 6, 15);
        ScheduleData scheduleData = new RR2QScheduler(2, 2).schedule(cores, processes);
        PrintUtils.printScheduleData(scheduleData);
    }
}


