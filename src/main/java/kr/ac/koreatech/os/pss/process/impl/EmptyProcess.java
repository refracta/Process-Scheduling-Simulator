package kr.ac.koreatech.os.pss.process.impl;

/**
 * 빈 프로세스 클래스
 * 코어를 대기시킬 때 이 프로세스를 대기열에 추가하여 사용한다.
 *
 * @author refracta
 */
public class EmptyProcess extends DefaultProcess {
    /**
     * 빈 프로세스 클래스의 싱글톤(Singleton) 인스턴스
     */
    private static EmptyProcess instance;

    /**
     * 빈 프로세스 클래스의 생성자
     */
    private EmptyProcess() {
        super(-1, "EP", -1, -1);
    }

    /**
     * 빈 프로세스 클래스 단일 객체를 반환한다.
     *
     * @return 빈 프로세스 단일 객체
     */
    public static synchronized EmptyProcess getInstance() {
        if (instance == null) {
            instance = new EmptyProcess();
        }
        return instance;
    }
}
