package kr.ac.koreatech.os.pss.entrypoint;

import kr.ac.koreatech.os.pss.visualizer.SchedulingVisualizer;
import kr.ac.koreatech.os.pss.visualizer.SchedulingVisualizerController;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.logging.Logger;

/**
 * 애플리케이션 엔트리 포인트
 *
 * @author refracta
 * @author unta1337
 */
public class Starter extends SchedulingVisualizer {
    /**
     * Start 클래스를 위한 기본 로거.
     */
    private final static Logger LOGGER = Logger.getGlobal();

    /**
     * Starter.java 메인 메소드
     *
     * @param args String[] args
     */
    public static void main(String[] args) throws Exception { launch(args); }
}
