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

    opens kr.ac.koreatech.os.pss.visualizer to javafx.graphics, javafx.fxml, com.jfoenix, org.kordamp.ikonli.javafx;
    opens kr.ac.koreatech.os.pss.visualizer.timeline to com.jfoenix, javafx.fxml, javafx.graphics, org.kordamp.ikonli.javafx;
    opens kr.ac.koreatech.os.pss.visualizer.timeline.impl to com.jfoenix, javafx.fxml, javafx.graphics, org.kordamp.ikonli.javafx;
}
