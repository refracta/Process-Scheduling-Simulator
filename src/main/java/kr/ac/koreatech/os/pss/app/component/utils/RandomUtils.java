package kr.ac.koreatech.os.pss.app.component.utils;

import java.util.Random;

/**
 * 랜덤 프로세스 생성을 위한 랜덤 유틸리티 클래스
 *
 * @author refracta
 * @author unta1337
 */
public class RandomUtils {
    /**
     * 난수 생성을 위한 스태틱 객체
     */
    public static Random random = new Random();

    public static void setRandomWithCurrentTime() {
        RandomUtils.random.setSeed(System.currentTimeMillis());
    }
}
