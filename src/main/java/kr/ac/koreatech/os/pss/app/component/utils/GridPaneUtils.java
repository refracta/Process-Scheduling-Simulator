package kr.ac.koreatech.os.pss.app.component.utils;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class GridPaneUtils {
    public static GridPane wrap(Node node) {
        GridPane wrapPane = new GridPane();
        wrapPane.setAlignment(Pos.CENTER);
        wrapPane.add(node, 0, 0);
        return wrapPane;
    }
}
