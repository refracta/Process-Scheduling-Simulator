package kr.ac.koreatech.os.test.utils;

import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.core.impl.EfficiencyCore;
import kr.ac.koreatech.os.pss.core.impl.PerformanceCore;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.toList;

/**
 * 테스트 세트 생성을 위한 유틸리티 클래스
 *
 * @author refracta
 */
public class TestSetUtils {
    /**
     * 테스트 세트 생성에 사용되는 랜덤 객체
     */
    private static Random random = new Random();

    /**
     * 랜덤 객체를 설정한다
     *
     * @param random 랜덤 객체
     */
    public static void setRandom(Random random) {
        TestSetUtils.random = random;
    }

    /**
     * 랜덤 객체의 시드를 설정한다.
     *
     * @param seed 의사 난수 시드 값
     */
    public static void setSeed(long seed) {
        random.setSeed(seed);
    }

    /**
     * 모든 종류의 코어를 동일한 비율로 랜덤으로 뽑아서 코어의 개수만큼의 코어 리스트를 만들어 반환한다.
     *
     * @param numberOfCore 코어의 개수
     * @return 랜덤 생성된 코어 리스트
     */
    public static List<AbstractCore> getRandomTestCores(int numberOfCore) {
        return random.ints(numberOfCore, 0, 2).mapToObj(i -> i == 0 ? new EfficiencyCore() : new PerformanceCore()).collect(toList());
    }

    /**
     * 주어진 프로세스 개수, 도착 시간 상한, 실행 시간 상한에 맞게 프로세스 리스트를 랜덤 생성하여 반환한다.
     *
     * @param numberOfProcess  프로세스 개수
     * @param arrivalTimeBound 도착 시간 상한
     * @param burstTimeBound   실행 시간 상한
     * @return 랜덤 생성된 프로세스 리스트
     */
    public static List<DefaultProcess> getRandomTestProcesses(int numberOfProcess, int arrivalTimeBound, int burstTimeBound) {
        List<DefaultProcess> processes = new ArrayList<>();
        for (int i = 0; i < numberOfProcess; i++) {
            processes.add(new DefaultProcess(random.nextInt(arrivalTimeBound), random.nextInt(1, burstTimeBound)));
        }
        return processes;
    }

    /**
     * 교안과 동일한 코어 세트를 반환한다.
     *
     * @return 코어 리스트 (교안과 동일)
     */
    public static List<AbstractCore> getDefaultTestCores() {
        List<AbstractCore> cores = new ArrayList<>();
        cores.add(new EfficiencyCore());
        return cores;
    }

    /**
     * 교안과 동일한 프로세스 세트를 반환한다.
     *
     * @return 프로세스 리스트 (교안과 동일)
     */
    public static List<DefaultProcess> getDefaultTestProcesses() {
        List<DefaultProcess> processes = new ArrayList<>();
        processes.add(new DefaultProcess(0, 3));
        processes.add(new DefaultProcess(1, 7));
        processes.add(new DefaultProcess(3, 2));
        processes.add(new DefaultProcess(5, 5));
        processes.add(new DefaultProcess(6, 3));
        return processes;
    }
}
