package kr.ac.koreatech.os.test.scheduler;

import kr.ac.koreatech.os.pss.scheduler.impl.SRTNScheduler;
import kr.ac.koreatech.os.test.utils.TestScheduleUtils;
import org.junit.jupiter.api.Test;

/**
 * SPTN 스케줄러 테스트 클래스
 *
 * @author refracta
 */
public class SRTNSchedulerTest {
    @Test
    public void test1() {
        TestScheduleUtils.runWithDefaultTestSet(new SRTNScheduler());
    }

    @Test
    public void test2() {
        TestScheduleUtils.runWithEasyLevelRandomTestSet(new SRTNScheduler(), System.currentTimeMillis());
    }

    @Test
    public void test3() {
        TestScheduleUtils.runWithHardLevelRandomTestSet(new SRTNScheduler(), System.currentTimeMillis());
    }
}
