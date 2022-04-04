package kr.ac.koreatech.os.pss.visualizer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

import static kr.ac.koreatech.os.pss.utility.ResourceUtils.getPackagePath;
import static kr.ac.koreatech.os.pss.utility.ResourceUtils.getResourceFiles;


public class SchedulingVisualizer extends Application {
    /**
     * Starter.java start method
     *
     * @param primaryStage Stage
     * @throws Exception Application start's Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        preLoad();

        Pane root = FXMLLoader.load(SchedulingVisualizer.class.getResource("SchedulingVisualizerMain.fxml"));
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public void preLoad() {
        try {
            String path = getPackagePath(SchedulingVisualizer.class);
            getResourceFiles(path + "font").stream().forEach(font -> {
                Font.loadFont(getClass().getResourceAsStream(path + font), 0);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
