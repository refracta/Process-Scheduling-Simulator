package kr.ac.koreatech.os.pss.visualizer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

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
        Font.loadFont(SchedulingVisualizer.class.getResourceAsStream("font/NanumSquare_acB.ttf"), 10.0);
        Font.loadFont(SchedulingVisualizer.class.getResourceAsStream("font/NanumSquare_acEB.ttf"), 10.0);
        Font.loadFont(SchedulingVisualizer.class.getResourceAsStream("font/NanumSquareR_acL.ttf"), 10.0);
        Font.loadFont(SchedulingVisualizer.class.getResourceAsStream("font/NanumSquareR_acR.ttf"), 10.0);
        Font.loadFont(SchedulingVisualizer.class.getResourceAsStream("font/NanumSquareB.ttf"), 10.0);
        Font.loadFont(SchedulingVisualizer.class.getResourceAsStream("font/NanumSquareEB.ttf"), 10.0);
        Font.loadFont(SchedulingVisualizer.class.getResourceAsStream("font/NanumSquareL.ttf"), 10.0);
        Font.loadFont(SchedulingVisualizer.class.getResourceAsStream("font/NanumSquareR.ttf"), 10.0);
    }
}