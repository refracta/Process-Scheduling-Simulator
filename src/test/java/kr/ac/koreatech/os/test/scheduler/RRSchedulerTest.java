package kr.ac.koreatech.os.test.scheduler;

import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.core.impl.EfficiencyCore;
import kr.ac.koreatech.os.pss.core.impl.PerformanceCore;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;
import kr.ac.koreatech.os.pss.scheduler.impl.RRScheduler;
import kr.ac.koreatech.os.pss.scheduler.utils.PrintUtils;
import kr.ac.koreatech.os.test.utils.TestScheduleUtils;
import org.junit.jupiter.api.Test;

/**
 * Round Robin 스케줄러 테스트 클래스
 *
 * @author refracta
 */
public class RRSchedulerTest {
    @Test
    public void test1WithTQ2() {
        TestScheduleUtils.runWithDefaultTestSet(new RRScheduler(2));
    }

    @Test
    public void test1WithTQ3() {
        TestScheduleUtils.runWithDefaultTestSet(new RRScheduler(3));
    }

    @Test
    public void test1WithTQ4() {
        TestScheduleUtils.runWithDefaultTestSet(new RRScheduler(4));
    }

    @Test
    public void test2() {
        TestScheduleUtils.runWithEasyLevelRandomTestSet(new RRScheduler(2), System.currentTimeMillis());
    }

    @Test
    public void test3() {
        TestScheduleUtils.runWithHardLevelRandomTestSet(new RRScheduler(2), System.currentTimeMillis());
    }


    @Test
    public void test4() {
        ScheduleData schedule = new RRScheduler(2).schedule(new AbstractCore[]{new PerformanceCore(), new PerformanceCore(), new EfficiencyCore(), new EfficiencyCore()}, new DefaultProcess[]{new DefaultProcess(1, 8), new DefaultProcess(0, 11)});
        PrintUtils.printScheduleData(schedule);
    }
}
