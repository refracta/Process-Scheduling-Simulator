package kr.ac.koreatech.os.pss.app.component.utils;

import javafx.scene.text.Text;

public class TextUtils {
    public static Text getDefaultText(String text, int size) {
        Text textNode = new Text();
        textNode.setText(text);
        textNode.setStyle("-fx-font-family: 'NanumSquare'; -fx-font-size: " + size + "px");
        return textNode;
    }
}
