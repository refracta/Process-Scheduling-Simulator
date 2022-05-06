package kr.ac.koreatech.os.pss.app.legacy;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * 스케쥴러 비주얼라이저 클래스
 *
 * @author unta1337
 * @author refracta
 */
public class LSchedulingVisualizer extends Application {

    /**
     * 외부 폰트 목록
     */
    private static final String[] FONTS = {"NanumSquareB.ttf", "NanumSquareEB.ttf", "NanumSquareL.ttf", "NanumSquareR.ttf", "NanumSquare_acB.ttf", "NanumSquare_acEB.ttf", "NanumSquare_acL.ttf", "NanumSquare_acR.ttf"};

    /**
     * 비주얼라이저 Java-FX 엔트리 포인트
     *
     * @param primaryStage Stage
     * @throws Exception Application start's Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        preLoad();

        LSchedulerControls schedulerControls = LSchedulerControls.getSchedulerControls();

        Scene scene = new Scene(schedulerControls.getRoot());
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * 폰트 및 이미지 등 리소스를 불러옴
     */
    public void preLoad() {
        for (String font : FONTS) {
            Font.loadFont(LSchedulingVisualizer.class.getResourceAsStream("font" + "/" + font), -1);
        }
    }
}
