package kr.ac.koreatech.os.pss.app.component.pane;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import kr.ac.koreatech.os.pss.app.component.structure.SingleComponent;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;

import java.net.URL;
import java.util.ResourceBundle;

public class ScheduleResultPane extends SingleComponent {
    @FXML
    private TableView<ScheduleResultModel> resultTable;

    @FXML
    private TableColumn<ScheduleResultModel, String> processNameColumn;
    @FXML
    private TableColumn<ScheduleResultModel, Integer> arrivalTimeColumn;
    @FXML
    private TableColumn<ScheduleResultModel, Integer> burstTimeColumn;
    @FXML
    private TableColumn<ScheduleResultModel, Integer> waitingTimeColumn;
    @FXML
    private TableColumn<ScheduleResultModel, Integer> turnaroundTimeColumn;
    @FXML
    private TableColumn<ScheduleResultModel, Double> normalizedTurnaroundTimeColumn;

    @FXML
    private Slider criteriaEndTimeSlider;

    private GanttChartContainerPane containerPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        containerPane = SingleComponent.getInstance(GanttChartContainerPane.class);

        criteriaEndTimeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            containerPane.setCriteriaEndTime(newValue.intValue());
            containerPane.updateAllScales();
        });
    }

    public void generateResultTable(ScheduleData scheduleData) {
        ObservableList<ScheduleResultModel> scheduleResultList = FXCollections.observableArrayList();
        for (DefaultProcess p : scheduleData.getResultProcesses())
            scheduleResultList.add(new ScheduleResultModel(p.getName(), p.getArrivalTime(), p.getBurstTime(), p.getWaitingTime(), p.getTurnaroundTime(), p.getNormalizedTurnaroundTime()));

        processNameColumn.setCellValueFactory(cellData -> cellData.getValue().getProcessName());
        arrivalTimeColumn.setCellValueFactory(cellData -> cellData.getValue().getArrivalTime());
        burstTimeColumn.setCellValueFactory(cellData -> cellData.getValue().getBurstTime());
        waitingTimeColumn.setCellValueFactory(cellData -> cellData.getValue().getWaitingTime());
        turnaroundTimeColumn.setCellValueFactory(cellData -> cellData.getValue().getTurnaroundTime());
        normalizedTurnaroundTimeColumn.setCellValueFactory(cellData -> cellData.getValue().getNormalizedTurnaroundTime());

        resultTable.setItems(scheduleResultList);
    }

    public record ScheduleResultModel(String processName, int arrivalTime, int burstTime, int waitingTime,
                                      int turnaroundTime, double normalizedTurnaroundTime) {
        public ScheduleResultModel(String processName, int arrivalTime, int burstTime, int waitingTime, int turnaroundTime, double normalizedTurnaroundTime) {
            this.processName = processName;
            this.arrivalTime = arrivalTime;
            this.burstTime = burstTime;
            this.waitingTime = waitingTime;
            this.turnaroundTime = turnaroundTime;
            this.normalizedTurnaroundTime = turnaroundTime;
        }

        public ObservableValue<String> getProcessName() {
            return new ReadOnlyObjectWrapper<>(processName);
        }

        public ObservableValue<Integer> getArrivalTime() {
            return new ReadOnlyObjectWrapper<>(arrivalTime);
        }

        public ObservableValue<Integer> getBurstTime() {
            return new ReadOnlyObjectWrapper<>(burstTime);
        }

        public ObservableValue<Integer> getWaitingTime() {
            return new ReadOnlyObjectWrapper<>(waitingTime);
        }

        public ObservableValue<Integer> getTurnaroundTime() {
            return new ReadOnlyObjectWrapper<>(turnaroundTime);
        }

        public ObservableValue<Double> getNormalizedTurnaroundTime() {
            return new ReadOnlyObjectWrapper<>(normalizedTurnaroundTime);
        }
    }
}
