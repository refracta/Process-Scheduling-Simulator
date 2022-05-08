package kr.ac.koreatech.os.test.scheduler;

import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.core.impl.EfficiencyCore;
import kr.ac.koreatech.os.pss.core.impl.PerformanceCore;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;
import kr.ac.koreatech.os.pss.scheduler.impl.FCFSScheduler;
import kr.ac.koreatech.os.pss.scheduler.utils.PrintUtils;
import kr.ac.koreatech.os.test.utils.TestScheduleUtils;
import org.junit.jupiter.api.Test;

/**
 * FCFS 스케줄러 테스트 클래스
 *
 * @author refracta
 */
public class FCFSSchedulerTest {
    @Test
    public void test1() {
        TestScheduleUtils.runWithDefaultTestSet(new FCFSScheduler());
    }

    @Test
    public void test2() {
        TestScheduleUtils.runWithEasyLevelRandomTestSet(new FCFSScheduler(), System.currentTimeMillis());
    }

    @Test
    public void test3() {
        TestScheduleUtils.runWithHardLevelRandomTestSet(new FCFSScheduler(), System.currentTimeMillis());
    }

    @Test
    public void test4() {
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
        PrintUtils.printScheduleData(scheduleData);
    }
}
