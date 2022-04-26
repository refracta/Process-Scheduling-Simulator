package kr.ac.koreatech.os.pss.visualizer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 출력 창에서 Process Status GridPane을 담당하는 컨트롤러 클래스
 *
 * @author unta1337
 */
public class ProcessorsStatus extends GridPane {
    /**
     * 컨트롤러 객체가 담당하고 있는 GridPane 객체
     */
    private GridPane pane;

    /**
     * 성능 코어의 개수를 출력하는 FXML 요소
     */
    @FXML
    Label numPerformanceCores;
    /**
     * 효율 코어의 개수를 출력하는 FXML 요소
     */
    @FXML
    Label numEfficiencyCores;
    @FXML
    Label performanceCorePowerUsage;
    @FXML
    Label efficiencyCorePowerUsage;
    @FXML
    Label totalElapsedUnitTime;
    @FXML
    Label averageTurnaroundUnitTime;

    /**
     * Process Status에서 출력할 사항을 설정하여 컨트롤러로 반환
     *
     * @param cores
     * @param scheduleData
     * @return
     * @throws IOException
     */
    public static ProcessorsStatus getProcessorsStatus(List<AbstractCore> cores, ScheduleData scheduleData) throws IOException {
        ProcessorsStatus controller = new ProcessorsStatus();
        controller.init(cores, scheduleData);
        return controller;
    }

    /**
     * Process Status에서 출력할 사항을 설정하여 컨트롤러로 반환
     * ProcessorsStatus getProcessorsStatus(List&lt;AbstractCore&gt; cores, ScheduleData scheduleData)과 동치이다.
     *
     * @param cores
     * @param scheduleData
     * @return
     * @throws IOException
     */
    public static ProcessorsStatus getProcessorsStatus(AbstractCore[] cores, ScheduleData scheduleData) throws IOException {
        return getProcessorsStatus(Arrays.asList(cores), scheduleData);
    }

    /**
     * ProcessorsStatus의 생성자
     * 별도의 정적 메소드를 통해 객체를 생성하므로 private
     *
     * @throws IOException
     */
    private ProcessorsStatus() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/processorsStatus.fxml"));
        fxmlLoader.setController(this);
        this.pane = fxmlLoader.load();
    }

    /**
     * ProcessorsStatus 생성 후 기본 정보를 설정하기 위한 메소드
     *
     * @param cores
     * @param scheduleData
     */
    public void init(List<AbstractCore> cores, ScheduleData scheduleData) {
        numPerformanceCores.setText(Integer.toString(scheduleData.getNumPerformanceCores()));
        numEfficiencyCores.setText(Integer.toString(scheduleData.getNumEfficiencyCores()));

        performanceCorePowerUsage.setText("45");
        efficiencyCorePowerUsage.setText("15");

        totalElapsedUnitTime.setText("10");
        averageTurnaroundUnitTime.setText("5.5");
    }

    public GridPane getPane() {
        return pane;
    }
}