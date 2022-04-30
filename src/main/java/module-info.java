/**
 * Default module-info.java
 */
module kr.ac.koreatech.os.pss {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires java.logging;

    opens kr.ac.koreatech.os.pss.visualizer to javafx.graphics, javafx.fxml;
}
