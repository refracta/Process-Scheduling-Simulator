package kr.ac.koreatech.os.pss.entrypoint;

import javafx.application.Application;
import kr.ac.koreatech.os.pss.app.PSSApplication;

import java.util.logging.Logger;

/**
 * 애플리케이션 엔트리 포인트
 *
 * @author refracta
 * @author unta1337
 */
public class Starter {
    /**
     * Start 클래스를 위한 기본 로거.
     */
    private final static Logger LOGGER = Logger.getGlobal();

    /**
     * Starter.java 메인 메소드
     *
     * @param args String[] args
     */
    public static void main(String[] args) {
        Logger.getGlobal().info("Hello");
        Application.launch(PSSApplication.class, args);
        Logger.getGlobal().info("Hello2");
    }
}
