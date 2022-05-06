package kr.ac.koreatech.os.pss.app.component.structure;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 단일 생성 컴포넌트 클래스
 * @author refracta
 */
public abstract class SingleComponent implements Initializable {
    private static Map<Class<?>, SingleComponent> componentMap = new ConcurrentHashMap<>();

    public static <T> T getInstance(Class<T> type) {
        return (T) componentMap.get(type);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (componentMap.containsKey(getClass())) {
            throw new RuntimeException("Only one SingleComponent instance can be created.");
        }
        componentMap.put(getClass(), this);
    }

}
