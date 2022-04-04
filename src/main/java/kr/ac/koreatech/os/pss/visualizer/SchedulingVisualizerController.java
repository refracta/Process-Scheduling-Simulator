package kr.ac.koreatech.os.pss.visualizer;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * 스케쥴러와 비주얼라이저를 연결해주는 클래스
 *
 * @author unta1337
 */
public class SchedulingVisualizerController {
    @FXML

    /**
     * 프로세스 Arrival Time 입력 텍스트 필드
     */
    public TextField taskConfigATTextField;

    /**
     * 프로세스 Burst Time 입력 텍스트 필드
     */
    public TextField taskConfigBTTextField;

    /**
     * 프로세스 Arrival Time 확인 버튼
     */
    public Button taskConfigATButton;

    /**
     * 프로세스 Arrival Time 확인 버튼
     */
    public Button taskConfigBTButton;

    /**
     * 프로세스 Arrival Time 입력 시 입력 초기화
     *
     * @param e KeyEvent
     */
    public void onTaskConfigATKeyEnter(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER)
            taskConfigATTextField.setText("");
    }

    /**
     * 프로세스 Burst Time 입력 시 입력 초기화
     *
     * @param e KeyEvent
     */
    public void onTaskConfigBTKeyEnter(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER)
            taskConfigBTTextField.setText("");
    }

    /**
     * 프로세스 Arrival Time 입력 시 입력 초기화
     *
     * @param e MouseEvent
     */
    public void onTaskConfigATButtonClick(MouseEvent e) {
        taskConfigATTextField.setText("");
    }

    /**
     * 프로세스 Burst Time 입력 시 입력 초기화
     *
     * @param e MouseEvent
     */
    public void onTaskConfigBTButtonClick(MouseEvent e) {
        taskConfigBTTextField.setText("");
    }
}