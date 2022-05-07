package kr.ac.koreatech.os.test.scheduler;

import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;
import kr.ac.koreatech.os.pss.scheduler.impl.GMRLScheduler;
import kr.ac.koreatech.os.pss.scheduler.impl.RR2QScheduler;
import kr.ac.koreatech.os.pss.scheduler.utils.PrintUtils;
import kr.ac.koreatech.os.test.utils.TestScheduleUtils;
import kr.ac.koreatech.os.test.utils.TestSetUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Round Robin 스케줄러 테스트 클래스
 *
 * @author refracta
 */
public class GMRLSchedulerTest {
    @Test
    public void test1WithTQ2() {
        TestScheduleUtils.runWithDefaultTestSet(new GMRLScheduler(2, 3));
    }

    @Test
    public void test1WithTQ3() {
        TestScheduleUtils.runWithDefaultTestSet(new GMRLScheduler(3, 3));
    }

    @Test
    public void test1WithTQ4() {
        TestScheduleUtils.runWithDefaultTestSet(new GMRLScheduler(4, 3));
    }

    @Test
    public void test2() {
        TestScheduleUtils.runWithEasyLevelRandomTestSet(new GMRLScheduler(2, 3), System.currentTimeMillis());
    }

    @Test
    public void test3() {
        TestScheduleUtils.runWithHardLevelRandomTestSet(new GMRLScheduler(2, 3), System.currentTimeMillis());
    }

    @Test
    public void test4WithTQ2AndRQ2() {
        List<AbstractCore> cores = TestSetUtils.getRandomTestCores(2);
        List<DefaultProcess> processes = TestSetUtils.getRandomTestProcesses(9, 6, 15);
        ScheduleData scheduleData = new GMRLScheduler(2, 2).schedule(cores, processes);
        PrintUtils.printScheduleData(scheduleData);
    }
}
