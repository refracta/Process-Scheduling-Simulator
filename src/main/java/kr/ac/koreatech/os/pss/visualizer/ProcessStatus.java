package kr.ac.koreatech.os.pss.visualizer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
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
public class ProcessStatus extends GridPane {
    /**
     * 컨트롤러 객체가 담당하고 있는 GridPane 객체
     */
    private GridPane pane;

    /**
     * 성능 코어의 개수를 출력하는 FXML 요소
     */
    @FXML
    Text numPCores;
    /**
     * 효율 코어의 개수를 출력하는 FXML 요소
     */
    @FXML
    Text numECores;

    /**
     * Process Status에서 출력할 사항을 설정하여 컨트롤러로 반환
     *
     * @param cores
     * @param scheduleData
     * @return
     * @throws IOException
     */
    public static ProcessStatus getProcessStatus(List<AbstractCore> cores, ScheduleData scheduleData) throws IOException {
        ProcessStatus controller = new ProcessStatus();
        controller.init(cores, scheduleData);
        return controller;
    }

    /**
     * Process Status에서 출력할 사항을 설정하여 컨트롤러로 반환
     * ProcessStatus getProcessStatus(List&lt;AbstractCore&gt; cores, ScheduleData scheduleData)과 동치이다.
     *
     * @param cores
     * @param scheduleData
     * @return
     * @throws IOException
     */
    public static ProcessStatus getProcessStatus(AbstractCore[] cores, ScheduleData scheduleData) throws IOException {
        return getProcessStatus(Arrays.asList(cores), scheduleData);
    }

    /**
     * ProcessStatus의 생성자
     * 별도의 정적 메소드를 통해 객체를 생성하므로 private
     *
     * @throws IOException
     */
    private ProcessStatus() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/processStatus.fxml"));
        fxmlLoader.setController(this);
        this.pane = fxmlLoader.load();
    }

    /**
     * ProcessStatus 생성 후 기본 정보를 설정하기 위한 메소드
     *
     * @param cores
     * @param scheduleData
     */
    public void init(List<AbstractCore> cores, ScheduleData scheduleData) {
        numPCores.setText("Num P Cores: " + Integer.toString(scheduleData.getNumPCores()));
        numECores.setText("Num E Cores: " + Integer.toString(scheduleData.getNumECores()));
    }

    public GridPane getPane() {
        return pane;
    }
}