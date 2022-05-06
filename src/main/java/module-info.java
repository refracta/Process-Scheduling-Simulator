/**
 * Default module-info.java
 */
module kr.ac.koreatech.os.pss {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires java.logging;
    requires com.jfoenix;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;

    opens kr.ac.koreatech.os.pss.app to javafx.graphics, javafx.fxml, com.jfoenix, org.kordamp.ikonli.javafx, org.kordamp.ikonli.fontawesome5;
    opens kr.ac.koreatech.os.pss.app.loader to com.jfoenix, javafx.fxml, javafx.graphics, org.kordamp.ikonli.javafx, org.kordamp.ikonli.fontawesome5;
    opens kr.ac.koreatech.os.pss.app.component.pane to com.jfoenix, javafx.fxml, javafx.graphics, org.kordamp.ikonli.javafx, org.kordamp.ikonli.fontawesome5;
    opens kr.ac.koreatech.os.pss.app.loader.annotation to com.jfoenix, javafx.fxml, javafx.graphics, org.kordamp.ikonli.javafx, org.kordamp.ikonli.fontawesome5;
    opens kr.ac.koreatech.os.pss.app.loader.utils to com.jfoenix, javafx.fxml, javafx.graphics, org.kordamp.ikonli.javafx, org.kordamp.ikonli.fontawesome5;
}
