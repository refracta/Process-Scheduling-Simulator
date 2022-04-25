package kr.ac.koreatech.os.pss.visualizer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.core.impl.EfficiencyCore;
import kr.ac.koreatech.os.pss.core.impl.PerformanceCore;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;
import kr.ac.koreatech.os.pss.scheduler.impl.FCFSScheduler;

/**
 * 스케쥴러 비주얼라이저 클래스
 *
 * @author unta1337
 * @author refracta
 */
public class SchedulingVisualizer extends Application {

    /**
     * 비주얼라이저 Java-FX 엔트리 포인트
     *
     * @param primaryStage Stage
     * @throws Exception Application start's Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        preLoad();

        FCFSScheduler fcfsScheduler = new FCFSScheduler();
        AbstractCore[] cores = {new EfficiencyCore(), new PerformanceCore(), new PerformanceCore()};
        DefaultProcess[] processes = {
                new DefaultProcess(0, 1, 4),
                new DefaultProcess(1, 2, 4),
                new DefaultProcess(2, 3, 7),
                new DefaultProcess(3, 3, 7),
                new DefaultProcess(4, 3, 7),
                new DefaultProcess(5, 3, 7),
        };
        ScheduleData scheduleData = fcfsScheduler.schedule(cores, processes);

        ProcessStatus processStatus = ProcessStatus.getProcessStatus(cores, scheduleData);

        Scene scene = new Scene(processStatus.getPane());
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * 외부 폰트 목록
     */
    private static final String[] FONTS = {"NanumSquareB.ttf", "NanumSquareEB.ttf", "NanumSquareL.ttf", "NanumSquareR.ttf", "NanumSquare_acB.ttf", "NanumSquare_acEB.ttf", "NanumSquare_acL.ttf", "NanumSquare_acR.ttf"};

    /**
     * 폰트 및 이미지 등 리소스를 불러옴
     */
    public void preLoad() {
        for (String font : FONTS) {
            Font.loadFont(SchedulingVisualizer.class.getResourceAsStream("font" + "/" + font), -1);
        }
    }
}
