package kr.ac.koreatech.os.pss.app.legacy;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.text.Text;
import javafx.scene.layout.GridPane;
import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 출력 창에서 Processors Status GridPane을 담당하는 컨트롤러 클래스
 *
 * @author unta1337
 */
public class LProcessorsStatus extends GridPane {
    /**
     * 컨트롤러 객체가 담당하고 있는 GridPane 객체
     */
    private GridPane pane;

    /**
     * 성능 코어의 개수를 출력하는 FXML 요소
     */
    @FXML
    Text numPerformanceCores;
    /**
     * 효율 코어의 개수를 출력하는 FXML 요소
     */
    @FXML
    Text numEfficiencyCores;
    /**
     * 성능 코어가 소비한 전력 총합
     */
    @FXML
    Text performanceCorePowerUsage;
    /**
     * 효율 코어가 소비한 전력 총합
     */
    @FXML
    Text efficiencyCorePowerUsage;
    /**
     * 처리한 프로세스 개수
     */
    @FXML
    Text numProcesses;
    /**
     * 프로세스 처리 전체 수행 시간
     */
    @FXML
    Text totalElapsedUnitTime;
    /**
     * 프로세스 평균 반환 시간
     */
    @FXML
    Text averageResponseUnitTime;

    /**
     * Processors Status에서 출력할 사항을 설정하여 컨트롤러로 반환
     *
     * @return
     * @throws IOException
     */
    public static LProcessorsStatus getProcessorsStatus() throws IOException {
        return new LProcessorsStatus();
    }

    /**
     * Processors Status에서 출력할 사항을 설정하여 컨트롤러로 반환
     *
     * @param cores
     * @param scheduleData
     * @return
     * @throws IOException
     */
    public static LProcessorsStatus getProcessorsStatus(List<AbstractCore> cores, ScheduleData scheduleData) throws IOException {
        LProcessorsStatus controller = new LProcessorsStatus();
        controller.init(cores, scheduleData);
        return controller;
    }

    /**
     * Processors Status에서 출력할 사항을 설정하여 컨트롤러로 반환
     * ProcessorsStatus getProcessorsStatus(List&lt;AbstractCore&gt; cores, ScheduleData scheduleData)과 동치이다.
     *
     * @param cores
     * @param scheduleData
     * @return
     * @throws IOException
     */
    public static LProcessorsStatus getProcessorsStatus(AbstractCore[] cores, ScheduleData scheduleData) throws IOException {
        return getProcessorsStatus(Arrays.asList(cores), scheduleData);
    }

    /**
     * ProcessorsStatus의 생성자
     * 별도의 정적 메소드를 통해 객체를 생성하므로 private
     *
     * @throws IOException
     */
    private LProcessorsStatus() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("processorsStatus.fxml"));
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

        numProcesses.setText(Integer.toString(scheduleData.getNumProcesses()));

        performanceCorePowerUsage.setText(String.format("%.1f", scheduleData.getPerformaceCorePowerUsage()));
        efficiencyCorePowerUsage.setText(String.format("%.1f", scheduleData.getEfficiencyCorePowerUsage()));

        totalElapsedUnitTime.setText(Integer.toString(scheduleData.getTotalElapsedTime()));
        averageResponseUnitTime.setText(String.format("%.1f", scheduleData.getAverageResponseTime()));
    }

    /**
     * 새로운 스케줄링 작업이 들어올 때 스케줄링 실행 후 내용 반영
     *
     * @param cores
     * @param scheduleData
     */
    public void update(List<AbstractCore> cores, ScheduleData scheduleData) {
        init(cores, scheduleData);
    }

    public GridPane getPane() {
        return pane;
    }
}
