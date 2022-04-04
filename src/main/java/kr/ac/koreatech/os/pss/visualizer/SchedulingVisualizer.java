package kr.ac.koreatech.os.pss.visualizer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

import static kr.ac.koreatech.os.pss.utility.ResourceUtils.getPackagePath;
import static kr.ac.koreatech.os.pss.utility.ResourceUtils.getResourceFiles;

/**
 * 스케쥴러 비주얼라이저 클래스
 *
 * @author refracta
 * @author unta1337
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

        FlowPane root = FXMLLoader.load(SchedulingVisualizer.class.getResource("SchedulingVisualizerMain.fxml"));
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * 폰트 및 이미지 등 리소스를 불러옴
     */
    public void preLoad() {
        try {
            String path = getPackagePath(SchedulingVisualizer.class) + "font";
            getResourceFiles(path).stream().forEach(font -> {
                Font.loadFont(getClass().getResourceAsStream("/" + path + "/" + font), 0);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
