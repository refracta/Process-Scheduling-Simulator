package kr.ac.koreatech.os.pss.app.component.pane;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import kr.ac.koreatech.os.pss.app.component.structure.SingleComponent;
import kr.ac.koreatech.os.pss.app.component.utils.TextFieldUtils;
import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.core.impl.EfficiencyCore;
import kr.ac.koreatech.os.pss.core.impl.PerformanceCore;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;
import kr.ac.koreatech.os.pss.scheduler.AbstractScheduler;
import kr.ac.koreatech.os.pss.scheduler.ScheduleMethod;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;
import kr.ac.koreatech.os.pss.scheduler.impl.*;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.*;

public class SchedulerControlPane extends SingleComponent {

    private static final Color ICON_ENABLE_COLOR = Color.WHITE;
    private static final Color ICON_DISABLE_COLOR = new Color(1, 1, 1, 0.4);
    /**
     * 성능 코어 개수 설정 텍스트 필드
     */
    @FXML
    TextField numPerformanceCoreTextField;
    /**
     * 효율 코어 개수 설정 텍스트 필드
     */
    @FXML
    TextField numEfficiencyCoreTextField;
    /**
     * 스케줄링 메소드를 설정하는 콤보 박스
     */
    @FXML
    JFXComboBox scheduleMethodComboBox;
    /**
     * 타임 퀀텀 아이콘
     */
    @FXML
    FontIcon timeQuantumIcon;
    /**
     * 타임 퀀텀 설정 텍스트 필드
     */
    @FXML
    TextField timeQuantumTextField;
    /**
     * 실행큐 최대 프로세스 개수 아이콘
     */
    @FXML
    FontIcon queueLimitIcon;
    /**
     * 실행큐 최대 프로세스 개수 설정 텍스트 필드
     */
    @FXML
    TextField queueLimitTextField;
    /**
     * 최대 플래그 카운트 개수 아이콘
     */
    @FXML
    FontIcon flagLimitIcon;
    /**
     * 최대 플래그 카운트 텍스트 필드
     */
    @FXML
    TextField flagLimitTextField;
    @FXML
    GridPane processTimelineContainerPane;
    /**
     * 스케줄링 시작 버튼
     */
    @FXML
    JFXButton startButton;

    public Optional<ScheduleMethod> getCurrentScheduleMethod() {
        try {
            return Optional.of(ScheduleMethod.getEnum(scheduleMethodComboBox.getValue().toString()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * schedulerMethodComboBox에서 메소드 변경 시 필드 활성 및 비활성화.
     */
    private void applyScheduleMethod(ScheduleMethod method) {
        setTimeQuantumDisable(true);
        setQueueLimitDisable(true);
        setFlagLimitDisable(true);

        switch (method) {
            case RR:
                setTimeQuantumDisable(false);
                break;
            case RR2Q:
                setTimeQuantumDisable(false);
                setQueueLimitDisable(false);
                break;
            case GMRL:
                setTimeQuantumDisable(false);
                setFlagLimitDisable(false);
                break;
        }
    }

    /**
     * 스케줄링 시작 이벤트 처리기
     * 지정된 스케줄링의 파라미터 조건이 만족되었는지 확인 후 스케줄링 수행
     *
     * @param event
     */
    @FXML
    private void startSchedule(MouseEvent event) {
        if (!isSchedulerReady()) {
            return;
        }

        List<AbstractCore> cores = new ArrayList<AbstractCore>();
        int numPerformanceCore = TextFieldUtils.getNumericValue(numPerformanceCoreTextField, 0);
        for (int i = 0; i < numPerformanceCore; i++) cores.add(new PerformanceCore());

        int numEfficiencyCore = TextFieldUtils.getNumericValue(numEfficiencyCoreTextField, 0);
        for (int i = 0; i < numEfficiencyCore; i++) cores.add(new EfficiencyCore());

//        DefaultProcess[] processes = {
//                new DefaultProcess(1, 0, 2),
//                new DefaultProcess(2, 0, 3),
//                new DefaultProcess(3, 0, 7),
//                new DefaultProcess(4, 0, 7),
//                new DefaultProcess(5, 0, 6),
//                new DefaultProcess(6, 0, 5),
//        };

        List<DefaultProcess> processes = SingleComponent.getInstance(ProcessTimelineContainerPane.class).getProcessList();

        AbstractScheduler scheduler = getConfiguredScheduler();
//        ScheduleData scheduleData = scheduler.schedule(cores, Arrays.asList(processes));
        ScheduleData scheduleData = scheduler.schedule(cores, processes);

        SingleComponent.getInstance(ProcessorStatusPane.class).setInformation(scheduleData);
        SingleComponent.getInstance(ScheduleResultPane.class).printResultConsole(scheduleData);

        // 테스트용
        SingleComponent.getInstance(GanttChartContainerPane.class).generateGanttChart(scheduleData);
    }

    /**
     * 사용자가 타임 퀀텀 변수를 입력할 수 있도록 입력을 활성화
     */
    private void setTimeQuantumDisable(boolean value) {
        timeQuantumIcon.setFill(value ? ICON_DISABLE_COLOR : ICON_ENABLE_COLOR);
        timeQuantumTextField.setDisable(value);
    }

    /**
     * 사용자가 실행큐 프로세스 최대 개수 변수를 입력할 수 없도록 입력을 비활성화
     */
    private void setQueueLimitDisable(boolean value) {
        queueLimitIcon.setFill(value ? ICON_DISABLE_COLOR : ICON_ENABLE_COLOR);
        queueLimitTextField.setDisable(value);
    }


    /**
     * 사용자가 최대 플래그 카운트 변수를 입력할 수 없도록 입력을 비활성화
     */
    private void setFlagLimitDisable(boolean value) {
        flagLimitIcon.setFill(value ? ICON_DISABLE_COLOR : ICON_ENABLE_COLOR);
        flagLimitTextField.setDisable(value);
    }

    /**
     * 현재 선택된 스케줄링이 가능한 상태인지 검사하여 반환
     *
     * @return 스케줄링 수행 가능 여부
     */
    private boolean isSchedulerReady() {
        Optional<ScheduleMethod> scheduleMethod = getCurrentScheduleMethod();
        int numPerformanceCore = TextFieldUtils.getNumericValue(numPerformanceCoreTextField, 0);
        int numEfficiencyCore = TextFieldUtils.getNumericValue(numEfficiencyCoreTextField, 0);
        if (scheduleMethod.isPresent()) {
            if (!(numPerformanceCore > 0 || numEfficiencyCore > 0)) return false;

            switch (scheduleMethod.get()) {
                case RR:
                    return isRRReady();
                case RR2Q:
                    return isCustom1Ready();
                case GMRL:
                    return isCustom2Ready();
                default:
                    return true;
            }
        } else {
            return false;
        }
    }

    /**
     * RR 스케줄링이 가능한 상태인지 검사하여 반환
     *
     * @return RR 스케줄링 수행 가능 여부
     */
    private boolean isRRReady() {
        return TextFieldUtils.getNumericValue(timeQuantumTextField, 0) > 0;
    }

    /**
     * Custom 1 스케줄링이 가능한 상태인지 검사하여 반환
     *
     * @return Custom 1 스케줄링 수행 가능 여부
     */
    private boolean isCustom1Ready() {
        return isRRReady() && TextFieldUtils.getNumericValue(queueLimitTextField, 0) > 0;
    }

    /**
     * Custom 2 스케줄링이 가능한 상태인지 검사하여 반환
     *
     * @return Custom 2 스케줄링 수행 가능 여부
     */
    private boolean isCustom2Ready() {
        return isRRReady() && TextFieldUtils.getNumericValue(flagLimitTextField, 0) > 0;
    }

    /**
     * 현재 스케줄링 설정에 맞는 스케줄러 반환
     *
     * @return 현재 스케줄링 설정에 맞는 스케줄러
     */
    public AbstractScheduler getConfiguredScheduler() {
        Optional<ScheduleMethod> method = getCurrentScheduleMethod();
        if (method.isPresent()) {
            switch (method.get()) {
                case FCFS:
                    return new FCFSScheduler();
                case RR:
                    return new RRScheduler(TextFieldUtils.getNumericValue(timeQuantumTextField, 1));
                case SPN:
                    return new SRTNScheduler();
                case SRTN:
                    return new SPNScheduler();
                case HRRN:
                    return new HRRNScheduler();
                case RR2Q:
                case GMRL:
            }
        }
        return new FCFSScheduler();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        for (ScheduleMethod method : ScheduleMethod.values()) {
            scheduleMethodComboBox.getItems().add(method.getValue());
        }

        scheduleMethodComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            applyScheduleMethod(ScheduleMethod.getEnum(newValue.toString()));
        });

        setTimeQuantumDisable(true);
        setQueueLimitDisable(true);
        setFlagLimitDisable(true);
    }
}
