package kr.ac.koreatech.os.pss.app.component.pane;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import kr.ac.koreatech.os.pss.app.component.structure.SingleComponent;
import kr.ac.koreatech.os.pss.app.loader.annotation.CreatableController;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;

@CreatableController
public class ProcessorStatusPane extends SingleComponent {
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
     * ProcessorsStatus 생성 후 기본 정보를 설정하기 위한 메소드
     *
     * @param scheduleData
     */
    public void setInformation(ScheduleData scheduleData) {
        numPerformanceCores.setText(String.valueOf(scheduleData.getNumPerformanceCores()));
        numEfficiencyCores.setText(String.valueOf(scheduleData.getNumEfficiencyCores()));

        numProcesses.setText(String.valueOf(scheduleData.getNumProcesses()));

        performanceCorePowerUsage.setText(String.format("%.1f", scheduleData.getPerformaceCorePowerUsage()));
        efficiencyCorePowerUsage.setText(String.format("%.1f", scheduleData.getEfficiencyCorePowerUsage()));

        totalElapsedUnitTime.setText(Integer.toString(scheduleData.getTotalElapsedTime()));
        averageResponseUnitTime.setText(String.format("%.1f", scheduleData.getAverageResponseTime()));
    }
}
