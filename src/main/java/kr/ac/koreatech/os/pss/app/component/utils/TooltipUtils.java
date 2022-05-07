package kr.ac.koreatech.os.pss.app.component.utils;

import javafx.scene.Node;
import javafx.scene.control.Tooltip;

public class TooltipUtils {
    public static Tooltip getDefaultTooltip(Node node) {
        Tooltip tooltip = new Tooltip();
        tooltip.setStyle("-fx-font-size: 14px; -fx-font-family: 'NanumSquare'; -fx-font-smoothing: gray;");
        Tooltip.install(node, tooltip);

        return tooltip;
    }
}
