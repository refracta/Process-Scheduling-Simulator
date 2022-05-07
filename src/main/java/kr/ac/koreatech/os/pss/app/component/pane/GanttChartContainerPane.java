package kr.ac.koreatech.os.pss.app.component.pane;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import kr.ac.koreatech.os.pss.app.component.raw.n.timeline.NGanttChart;
import kr.ac.koreatech.os.pss.app.component.structure.SingleComponent;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;

import java.net.URL;
import java.util.ResourceBundle;

public class GanttChartContainerPane extends SingleComponent {
    @FXML
    StackPane pane;

    private NGanttChart nGanttChart;

    public NGanttChart getnGanttChart() {
        return nGanttChart;
    }

    public void setnGanttChart(NGanttChart nGanttChart) {
        this.nGanttChart = nGanttChart;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        pane.getChildren().add(this.nGanttChart = new NGanttChart());
    }
}
