package kr.ac.koreatech.os.pss.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import kr.ac.koreatech.os.pss.app.component.pane.VisualizerPane;
import kr.ac.koreatech.os.pss.app.loader.FontLoader;
import kr.ac.koreatech.os.pss.app.loader.utils.FXMLUtils;

/**
 * 스케쥴러 비주얼라이저 클래스
 *
 * @author unta1337
 * @author refracta
 */
public class PSSApplication extends Application {

    /**
     * 비주얼라이저 Java-FX 엔트리 포인트
     *
     * @param primaryStage Stage
     * @throws Exception Application start's Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FontLoader.load();

        primaryStage.setScene(new Scene(FXMLUtils.create(VisualizerPane.class)));

        primaryStage.setTitle("프로세스 스케줄링 시뮬레이터 | 10조");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));

        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
