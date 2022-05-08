package kr.ac.koreatech.os.test.scheduler;

import kr.ac.koreatech.os.pss.scheduler.impl.HRRNScheduler;
import kr.ac.koreatech.os.test.utils.TestScheduleUtils;
import org.junit.jupiter.api.Test;

/**
 * FCFS 스케줄러 테스트 클래스
 *
 * @author refracta
 */
public class HRRNSchedulerTest {
    @Test
    public void test1() {
        TestScheduleUtils.runWithDefaultTestSet(new HRRNScheduler());
    }

    @Test
    public void test2() {
        TestScheduleUtils.runWithEasyLevelRandomTestSet(new HRRNScheduler(), System.currentTimeMillis());
    }

    @Test
    public void test3() {
        TestScheduleUtils.runWithHardLevelRandomTestSet(new HRRNScheduler(), System.currentTimeMillis());
    }

}
