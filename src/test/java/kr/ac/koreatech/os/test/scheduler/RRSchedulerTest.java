package kr.ac.koreatech.os.test.scheduler;

import kr.ac.koreatech.os.pss.scheduler.impl.RRScheduler;
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
}
