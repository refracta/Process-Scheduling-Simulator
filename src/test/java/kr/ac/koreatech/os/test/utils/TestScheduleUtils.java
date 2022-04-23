package kr.ac.koreatech.os.test.utils;

import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;
import kr.ac.koreatech.os.pss.scheduler.AbstractScheduler;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;
import kr.ac.koreatech.os.pss.scheduler.utils.PrintUtils;

import java.util.List;

/**
 * 스케줄 테스트를 위한 유틸리티 클래스
 *
 * @author refracta
 */
public class TestScheduleUtils {
    private static void setRandomSeed(long seed) {
        System.out.println("RandomSeed: " + seed);
        TestSetUtils.setSeed(seed);
    }

    /**
     * 교안과 동일한 데이터 셋으로 테스트를 시행한다.
     *
     * @param scheduler 테스트할 스케줄러
     */
    public static void runWithDefaultTestSet(AbstractScheduler scheduler) {
        ScheduleData scheduleData = scheduler.schedule(TestSetUtils.getDefaultTestCores(), TestSetUtils.getDefaultTestProcesses());
        PrintUtils.printScheduleData(scheduleData);
    }

    /**
     * 쉬운 난이도로 랜덤 생성된 데이터 셋으로 테스트를 시행한다.
     *
     * @param scheduler 테스트할 스케줄러
     */
    public static void runWithEasyLevelRandomTestSet(AbstractScheduler scheduler, long randomSeed) {
        setRandomSeed(randomSeed);
        List<AbstractCore> cores = TestSetUtils.getRandomTestCores(3);
        List<DefaultProcess> processes = TestSetUtils.getRandomTestProcesses(5, 6, 15);
        ScheduleData scheduleData = scheduler.schedule(cores, processes);
        PrintUtils.printScheduleData(scheduleData);
    }

    /**
     * 어려운 난이도로 랜덤 생성된 데이터 셋으로 테스트를 시행한다.
     *
     * @param scheduler 테스트할 스케줄러
     */
    public static void runWithHardLevelRandomTestSet(AbstractScheduler scheduler, long randomSeed) {
        setRandomSeed(randomSeed);
        List<AbstractCore> cores = TestSetUtils.getRandomTestCores(10);
        List<DefaultProcess> processes = TestSetUtils.getRandomTestProcesses(500, 100, 100);
        ScheduleData scheduleData = scheduler.schedule(cores, processes);
        PrintUtils.printScheduleData(scheduleData);
    }
}
