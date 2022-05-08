package kr.ac.koreatech.os.pss.app.loader;

import javafx.scene.text.Font;
import kr.ac.koreatech.os.pss.app.PSSApplication;

public class FontLoader {
    /**
     * 외부 폰트 목록
     */
    private static final String[] FONTS = {"NanumSquareB.ttf", "NanumSquareEB.ttf", "NanumSquareL.ttf", "NanumSquareR.ttf", "NanumSquare_acB.ttf", "NanumSquare_acEB.ttf", "NanumSquare_acL.ttf", "NanumSquare_acR.ttf"};

    /**
     * 폰트 및 이미지 등 리소스를 불러옴
     */
    public static void load() {
        for (String font : FONTS) {
            Font.loadFont(PSSApplication.class.getResourceAsStream("font/" + font), -1);
        }
    }
}
