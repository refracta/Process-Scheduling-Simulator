package kr.ac.koreatech.os.pss.visualizer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CoreInfoController extends GridPane implements Initializable {
    private GridPane pane;

    @FXML
    public Text averageResponseTime;
    @FXML
    public Text powerUsage;

    public static CoreInfoController getCoreInfoController(ScheduleData scheduleData) throws IOException {
        CoreInfoController controller = new CoreInfoController();
        controller.init(scheduleData);
        return controller;
    }

    private CoreInfoController() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/coreInfo.fxml"));
        fxmlLoader.setController(this);
        this.pane = fxmlLoader.load();
    }

    private void init(ScheduleData scheduleData) {
        averageResponseTime.setText(Double.toString(scheduleData.getAverageResponseTime()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public GridPane getPane() {
        return pane;
    }
}