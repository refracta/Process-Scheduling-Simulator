package kr.ac.koreatech.os.test.scheduler;

import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.core.impl.EfficiencyCore;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;
import kr.ac.koreatech.os.pss.scheduler.impl.GMRLScheduler;
import kr.ac.koreatech.os.pss.scheduler.impl.RR2QScheduler;
import kr.ac.koreatech.os.pss.scheduler.utils.PrintUtils;
import kr.ac.koreatech.os.test.utils.TestScheduleUtils;
import kr.ac.koreatech.os.test.utils.TestSetUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
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

        List<AbstractCore> cores = Arrays.asList(new AbstractCore[]{new EfficiencyCore()});
        List<DefaultProcess> processes = new ArrayList<>();
        processes.add(new DefaultProcess(0, 3));
        processes.add(new DefaultProcess(1, 7));
        processes.add(new DefaultProcess(3, 2));
        processes.add(new DefaultProcess(5, 5));
        processes.add(new DefaultProcess(6, 3));
        /**
         DefaultProcess{id=1, name=P1, finished=true, startTime=0, arrivalTime=0, burstTime=3, leftBurstTime=0, waitingTime=2, turnaroundTime=5, responseRatio=1.6666666666666667, normalizedTurnaroundTime=1.6666666666666667}
         DefaultProcess{id=2, name=P2, finished=true, startTime=2, arrivalTime=1, burstTime=7, leftBurstTime=0, waitingTime=12, turnaroundTime=19, responseRatio=2.7142857142857144, normalizedTurnaroundTime=2.7142857142857144}
         DefaultProcess{id=3, name=P3, finished=true, startTime=9, arrivalTime=3, burstTime=2, leftBurstTime=0, waitingTime=6, turnaroundTime=8, responseRatio=4.0, normalizedTurnaroundTime=4.0}
         DefaultProcess{id=4, name=P4, finished=true, startTime=7, arrivalTime=5, burstTime=5, leftBurstTime=0, waitingTime=9, turnaroundTime=14, responseRatio=2.8, normalizedTurnaroundTime=2.8}
         DefaultProcess{id=5, name=P5, finished=true, startTime=11, arrivalTime=6, burstTime=3, leftBurstTime=0, waitingTime=5, turnaroundTime=8, responseRatio=2.6666666666666665, normalizedTurnaroundTime=2.6666666666666665}

         */
        ScheduleData scheduleData = new GMRLScheduler(2, 3).schedule(cores, processes);
        PrintUtils.printScheduleData(scheduleData);
    }
}
