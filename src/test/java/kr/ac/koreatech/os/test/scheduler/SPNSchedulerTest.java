package kr.ac.koreatech.os.test.scheduler;

import kr.ac.koreatech.os.pss.scheduler.impl.SPNScheduler;
import kr.ac.koreatech.os.test.utils.TestScheduleUtils;
import org.junit.jupiter.api.Test;

/**
 * SPN 스케줄러 테스트 클래스
 *
 * @author refracta
 */
public class SPNSchedulerTest {
    @Test
    public void test1() {
        TestScheduleUtils.runWithDefaultTestSet(new SPNScheduler());
    }

    @Test
    public void test2() {
        TestScheduleUtils.runWithEasyLevelRandomTestSet(new SPNScheduler(), System.currentTimeMillis());
    }

    @Test
    public void test3() {
        TestScheduleUtils.runWithHardLevelRandomTestSet(new SPNScheduler(), System.currentTimeMillis());
    }
}
