package kr.ac.koreatech.os.test.scheduler;

import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.core.impl.EfficiencyCore;
import kr.ac.koreatech.os.pss.core.impl.PerformanceCore;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;
import kr.ac.koreatech.os.pss.scheduler.AbstractScheduler;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;
import kr.ac.koreatech.os.pss.scheduler.exception.ScheduleTimeoutException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 스케줄 시간 초과 오류를 테스트하는 클래스
 *
 * @author refracta
 */
public class SchedulerTimeoutTest {

    @Test
    public void timeoutTest() {
        assertThrows(ScheduleTimeoutException.class, () -> {
            AbstractScheduler testScheduler = new AbstractScheduler() {
                @Override
                protected void schedule(int time, List<AbstractCore> cores, List<DefaultProcess> processes, ScheduleData scheduleData) {
                }
            };
            AbstractCore[] cores = {new EfficiencyCore(), new PerformanceCore()};
            DefaultProcess[] processes = {
                    new DefaultProcess(0, 4),
                    new DefaultProcess(1, 3),
                    new DefaultProcess(3, 3),
            };
            testScheduler.schedule(cores, processes);
        });
    }
}
