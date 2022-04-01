package kr.ac.koreatech.os.pss.entrypoint;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.logging.Logger;

/**
 * @author refracta
 */
public class Starter extends Application {
    /**
     * Default logger for Starter class
     */
    private final static Logger LOGGER = Logger.getGlobal();

    /**
     * Starter.java main method
     *
     * @param args String[] args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starter.java start method
     *
     * @param primaryStage Stage
     * @throws Exception Application start's Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("안녕!");
        Button btn = new Button();
        btn.setText("Print '안녕'");
        btn.setOnAction(event -> LOGGER.info("Hello World!"));
        StackPane root = new StackPane();
        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 300, 300));
        primaryStage.show();
    }
}
