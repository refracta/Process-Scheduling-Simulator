package kr.ac.koreatech.os.test.scheduler;

import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.core.impl.EfficiencyCore;
import kr.ac.koreatech.os.pss.core.impl.PerformanceCore;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;
import kr.ac.koreatech.os.pss.scheduler.AbstractScheduler;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SchedulerTimeoutTest {
    @Test
    public void timeoutTest() {
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
    }
}
