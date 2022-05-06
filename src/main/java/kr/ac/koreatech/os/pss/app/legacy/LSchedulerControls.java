package kr.ac.koreatech.os.pss.app.legacy;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import kr.ac.koreatech.os.pss.core.AbstractCore;
import kr.ac.koreatech.os.pss.core.impl.EfficiencyCore;
import kr.ac.koreatech.os.pss.core.impl.PerformanceCore;
import kr.ac.koreatech.os.pss.process.impl.DefaultProcess;
import kr.ac.koreatech.os.pss.scheduler.AbstractScheduler;
import kr.ac.koreatech.os.pss.scheduler.ScheduleMethod;
import kr.ac.koreatech.os.pss.scheduler.data.ScheduleData;
import kr.ac.koreatech.os.pss.scheduler.impl.*;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 프로그램의 Controller 역할을 담당하는 컨트롤러 클래스
 * 출력 창에서 Scheduler Controls GridPane을 담당하는 컨트롤러 클래스
 *
 * @author unta1337
 */
public class LSchedulerControls extends GridPane {
    /**
     * 스케줄러 시각화의 모든 요소를 담고 있는 루트 Pane
     */
    private GridPane root;
    /**
     * 스케줄러 설정의 요소를 담고 있는 Pane
     */
    private FlowPane leftMenu;
    /**
     * 프로세스 설정과 간트 차트 요소를 담고 있는 Pane
     */
    private FlowPane rightMenu;

    /**
     * 컨트롤러 객체가 담당하고 있는 GridPane 객체
     */
    private GridPane pane;

    /**
     * 프로세서 상태 Pane
     */
    private LProcessorsStatus processorsStatus;
    /**
     * 프로세스 설정 Pane
     */
    private LProcessControls processControls;

    /**
     * 성능 코어 개수
     */
    private int numPerformanceCore;
    /**
     * 효율 코어 개수
     */
    private int numEfficiencyCore;

    /**
     * 현재 선택된 스케줄링 기법
     */
    private ScheduleMethod currentScheduleMethod;
    /**
     * RR에서의 타임 퀀텀
     */
    private int currentTimeQuantum;
    /**
     * Custom 1에서 실행큐에 넣을 수 있는 프로세스의 최대 개수
     */
    private int currentQueueLimit;
    /**
     * Custom 2에서 사용할 최대 플래그 카운트
     */
    private int currentFlagLimit;

    /**
     * 성능 코어 개수 설정 텍스트 필드
     */
    @FXML
    TextField numPerformanceCoreTextField;
    /**
     * 성능 코어 개수 설정 버튼
     */
    @FXML
    JFXButton numPerformanceCoreButton;

    /**
     * 효율 코어 개수 설정 텍스트 필드
     */
    @FXML
    TextField numEfficiencyCoreTextField;
    /**
     * 효율 코어 개수 설정 버튼
     */
    @FXML
    JFXButton numEfficiencyCoreButton;

    /**
     * 스케줄링 메소드를 설정하는 버튼
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
     * 타임 퀀텀 설정 버튼
     */
    @FXML
    JFXButton timeQuantumButton;

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
     * 실행큐 최대 프로세스 개수 설정 버튼
     */
    @FXML
    JFXButton queueLimitButton;

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
    /**
     * 최대 플래그 카운트 설정 버튼
     */
    @FXML
    JFXButton flagLimitButton;

    /**
     * 스케줄링 시작 버튼
     */
    @FXML
    JFXButton startButton;

    /**
     * Scheduler Controls에 출력할 사항을 설정하여 컨트롤러로 반환
     *
     * @return SchedulerControls의 컨트롤러
     * @throws IOException
     */
    public static LSchedulerControls getSchedulerControls() throws IOException {
        LSchedulerControls controller = new LSchedulerControls();
        controller.init();
        return controller;
    }

    /**
     * ProcessorsStatus의 생성자
     * 별도의 정적 메소드를 통해 객체를 생성하므로 private
     *
     * @throws IOException
     */
    private LSchedulerControls() throws IOException {
        leftMenu = new FlowPane();
        rightMenu = new FlowPane();

        root = FXMLLoader.load(getClass().getResource("mainFrame.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("schedulerControls.fxml"));
        fxmlLoader.setController(this);
        this.pane = fxmlLoader.load();
    }

    /**
     * ProcessorsStatus 생성 후 기본 정보를 설정하기 위한 메소드
     */
    public void init() throws IOException {
        processorsStatus = LProcessorsStatus.getProcessorsStatus();
        processControls = LProcessControls.getProcessControls();

        leftMenu.getChildren().add(pane);
        leftMenu.getChildren().add(processorsStatus.getPane());

        rightMenu.getChildren().add(processControls.getPane());

        root.add(leftMenu, 0, 0);
        root.add(rightMenu, 1, 0);

        Arrays.stream(ScheduleMethod.values()).forEach((schedulerMethod) -> {
            scheduleMethodComboBox.getItems().add(schedulerMethod.getValue());
        });

        setTimeQuantumDisable();
        setQueueLimitDisable();
        setFlagLimitDisable();
    }

    /**
     * 스케줄러 종류 선택을 적용하는 이벤트 처리기
     *
     * @param event
     */
    @FXML
    private void applyScheduleMethod(MouseEvent event) {
        try {
            currentScheduleMethod = ScheduleMethod.getEnum(scheduleMethodComboBox.getValue().toString());
        } catch (Exception exception) {
        }

        if (currentScheduleMethod == null) return;

        setTimeQuantumDisable();
        setQueueLimitDisable();
        setFlagLimitDisable();

        switch (currentScheduleMethod) {
            case RR:
                setTimeQuantumEnable();
                break;
            case Custom1:
                setTimeQuantumEnable();
                setQueueLimitEnable();
                break;
            case Custom2:
                setTimeQuantumEnable();
                setFlagLimitEnable();
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
    private void startSchedule(MouseEvent event) throws IOException {
        if (!isSchedulerReady()) {
            return;
        }

        // # 임시 설정
        // processes: 추후 프로세스 설정 클래스에서 받아올 예정.
        List<AbstractCore> cores = new ArrayList<AbstractCore>();
        for (int i = 0; i < numPerformanceCore; i++) cores.add(new PerformanceCore());
        for (int i = 0; i < numEfficiencyCore; i++) cores.add(new EfficiencyCore());

//        DefaultProcess[] processes = {
//                new DefaultProcess(1, 0, 2),
//                new DefaultProcess(2, 0, 3),
//                new DefaultProcess(3, 0, 7),
//                new DefaultProcess(4, 0, 7),
//                new DefaultProcess(5, 0, 6),
//                new DefaultProcess(6, 0, 5),
//        };

        List<DefaultProcess> processes = processControls.getProcesses();

        AbstractScheduler scheduler = getScheduler();
//        ScheduleData scheduleData = scheduler.schedule(cores, Arrays.asList(processes));
        ScheduleData scheduleData = scheduler.schedule(cores, processes);

        updateProcessorsStatus(cores, scheduleData);
    }

    /**
     * 성능 코어 개수 갱신
     *
     * @param event
     */
    @FXML
    private void updateNumPerformanceCore(MouseEvent event) {
        try {
            numPerformanceCore = Integer.parseInt(numPerformanceCoreTextField.getText());
        } catch (NumberFormatException exception) {
            numPerformanceCore = 0;
        } catch (Exception exception) {
        }
    }

    /**
     * 효율 코어 개수 갱신
     *
     * @param event
     */
    @FXML
    private void updateNumEfficiencyCore(MouseEvent event) {
        try {
            numEfficiencyCore = Integer.parseInt(numEfficiencyCoreTextField.getText());
        } catch (NumberFormatException exception) {
            numEfficiencyCore = 0;
        } catch (Exception exception) {
        }
    }

    /**
     * 타임 퀀텀 변수 갱신
     *
     * @param event
     */
    @FXML
    private void updateTimeQuantum(MouseEvent event) {
        try {
            currentTimeQuantum = Integer.parseInt(timeQuantumTextField.getText());
        } catch (NumberFormatException exception) {
            currentTimeQuantum = 0;
        } catch (Exception exception) {
        }
    }

    /**
     * 실행큐 최대 프로세스 개수 변수 갱신
     *
     * @param event
     */
    @FXML
    private void updateQueueLimit(MouseEvent event) {
        try {
            currentQueueLimit = Integer.parseInt(queueLimitTextField.getText());
        } catch (NumberFormatException exception) {
            currentQueueLimit = 0;
        } catch (Exception exception) {
        }
    }

    /**
     * 최대 플래그 카운트 변수 갱신
     *
     * @param event
     */
    @FXML
    private void updateFlagLimit(MouseEvent event) {
        try {
            currentFlagLimit = Integer.parseInt(flagLimitTextField.getText());
        } catch (NumberFormatException exception) {
            currentFlagLimit = 0;
        } catch (Exception exception) {
        }
    }

    /**
     * 입력된 코어와 스케줄 데이터를 통해 프로세서 상태 갱신 및 적용
     *
     * @param cores
     * @param scheduleData
     * @throws IOException
     */
    public void updateProcessorsStatus(List<AbstractCore> cores, ScheduleData scheduleData) throws IOException {
        processorsStatus.update(cores, scheduleData);
    }


    /**
     * 입력된 코어와 스케줄 데이터를 통해 프로세서 상태 갱신 및 적용
     * void updateProcessorsStatus(List&lt;AbstractCore&gt; cores, ScheduleData scheduleData)과 동치이다.
     *
     * @param cores
     * @param scheduleData
     * @throws IOException
     */
    public void updateProcessorsStatus(AbstractCore[] cores, ScheduleData scheduleData) throws IOException {
        updateProcessorsStatus(Arrays.asList(cores), scheduleData);
    }

    /**
     * 사용자가 타임 퀀텀 변수를 입력할 수 있도록 입력을 활성화
     */
    private void setTimeQuantumEnable() {
        timeQuantumIcon.setFill(new Color(1, 1, 1, 1));
        timeQuantumIcon.setFill(Color.WHITE);
        timeQuantumTextField.setDisable(false);
        timeQuantumButton.setDisable(false);
    }

    /**
     * 사용자가 타임 퀀텀 변수를 입력할 수 없도록 입력을 비활성화
     */
    private void setTimeQuantumDisable() {
        timeQuantumIcon.setFill(new Color(1, 1, 1, 0.4));
        timeQuantumTextField.setDisable(true);
        timeQuantumButton.setDisable(true);
    }

    /**
     * 사용자가 실행큐 프로세스 최대 개수 변수를 입력할 수 있도록 입력을 활성화
     */
    private void setQueueLimitEnable() {
        queueLimitIcon.setFill(new Color(1, 1, 1, 1));
        queueLimitTextField.setDisable(false);
        queueLimitButton.setDisable(false);
    }

    /**
     * 사용자가 실행큐 프로세스 최대 개수 변수를 입력할 수 없도록 입력을 비활성화
     */
    private void setQueueLimitDisable() {
        queueLimitIcon.setFill(new Color(1, 1, 1, 0.4));
        queueLimitTextField.setDisable(true);
        queueLimitButton.setDisable(true);
    }

    /**
     * 사용자가 최대 플래그 카운트 변수를 입력할 수 있도록 입력을 활성화
     */
    private void setFlagLimitEnable() {
        flagLimitIcon.setFill(new Color(1, 1, 1, 1));
        flagLimitTextField.setDisable(false);
        flagLimitButton.setDisable(false);
    }

    /**
     * 사용자가 최대 플래그 카운트 변수를 입력할 수 없도록 입력을 비활성화
     */
    private void setFlagLimitDisable() {
        flagLimitIcon.setFill(new Color(1, 1, 1, 0.4));
        flagLimitTextField.setDisable(true);
        flagLimitButton.setDisable(true);
    }

    /**
     * 현재 선택된 스케줄링이 가능한 상태인지 검사하여 반환
     *
     * @return 스케줄링 수행 가능 여부
     */
    private boolean isSchedulerReady() {
        if (currentScheduleMethod == null) return false;
        if (!(numPerformanceCore > 0 || numEfficiencyCore > 0))
            return false;

        switch (currentScheduleMethod) {
            case RR:
                return isRRReady();
            case Custom1:
                return isCustom1Ready();
            case Custom2:
                return isCustom2Ready();
        }

        return true;
    }

    /**
     * RR 스케줄링이 가능한 상태인지 검사하여 반환
     *
     * @return RR 스케줄링 수행 가능 여부
     */
    private boolean isRRReady() {
        return currentTimeQuantum > 0;
    }

    /**
     * Custom 1 스케줄링이 가능한 상태인지 검사하여 반환
     *
     * @return Custom 1 스케줄링 수행 가능 여부
     */
    private boolean isCustom1Ready() {
        return currentTimeQuantum > 0 && currentQueueLimit > 0;
    }

    /**
     * Custom 2 스케줄링이 가능한 상태인지 검사하여 반환
     *
     * @return Custom 2 스케줄링 수행 가능 여부
     */
    private boolean isCustom2Ready() {
        return currentTimeQuantum > 0 && currentFlagLimit > 0;
    }

    /**
     * 현재 스케줄링 설정에 맞는 스케줄러 반환
     *
     * @return 현재 스케줄링 설정에 맞는 스케줄러
     */
    private AbstractScheduler getScheduler() {
        switch (currentScheduleMethod) {
            case FCFS:
                return new FCFSScheduler();
            case RR:
                return new RRScheduler(currentTimeQuantum);
            case SPN:
                return new SRTNScheduler();
            case SRTN:
                return new SPNScheduler();
            case HRRN:
                return new HRRNScheduler();
            case Custom1:
            case Custom2:
        }

        return new FCFSScheduler();
    }

    public GridPane getRoot() throws IOException {
        return root;
    }

    public GridPane getPane() {
        return pane;
    }

    public GridPane getProcessorsStatusPane() {
        return processorsStatus.getPane();
    }

    public ScheduleMethod getCurrentScheduleMethod() {
        return currentScheduleMethod;
    }

    public int getCurrentTimeQuantum() {
        return currentTimeQuantum;
    }

    public int getCurrentQueueLimit() {
        return currentQueueLimit;
    }

    public int getCurrentFlagLimit() {
        return currentFlagLimit;
    }
}
