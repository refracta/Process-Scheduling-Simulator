package kr.ac.koreatech.os.pss.scheduler.impl;

import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;
import kr.ac.koreatech.os.pss.scheduler.PriorityScheduler;

import java.util.Comparator;

/**
 * Shortest Process Next 스케줄러 클래스
 *
 * @author refracta
 */
public class SPNScheduler extends PriorityScheduler {
    public SPNScheduler() {
        super(Comparator.comparingInt(DefaultProcess::getBurstTime));
        // 실행 시간을 기준으로 우선순위 스케줄러의 comparator 설정
    }
}
