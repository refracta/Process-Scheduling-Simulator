/**
 * Default module-info.java
 */
module kr.ac.koreatech.os.pss {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires java.logging;
    requires com.jfoenix;

    opens kr.ac.koreatech.os.pss.app to javafx.graphics, javafx.fxml, com.jfoenix;
    opens kr.ac.koreatech.os.pss.app.legacy to com.jfoenix, javafx.fxml, javafx.graphics;
    opens kr.ac.koreatech.os.pss.app.loader to com.jfoenix, javafx.fxml, javafx.graphics;
    opens kr.ac.koreatech.os.pss.app.component.pane to com.jfoenix, javafx.fxml, javafx.graphics;
    opens kr.ac.koreatech.os.pss.app.loader.annotation to com.jfoenix, javafx.fxml, javafx.graphics;
    opens kr.ac.koreatech.os.pss.app.loader.utils to com.jfoenix, javafx.fxml, javafx.graphics;
}
