package kr.ac.koreatech.os.pss.app.component.utils;

import javafx.scene.control.TextField;

/**
 * 텍스트 필드 유틸리티
 *
 * @author refracta
 */
public class TextFieldUtils {
    public static void convertNumericTextField(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    public static int getNumericValue(TextField textField, int defaultValue) {
        try {
            return Integer.parseInt(textField.getText());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
