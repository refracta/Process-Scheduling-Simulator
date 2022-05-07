package kr.ac.koreatech.os.pss.app.component.raw.n.timeline;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class NTimelineScalePlatePane extends NTimelineComponent {

    protected int MAX_HEIGHT = 40;

    public NTimelineScalePlatePane() {
        setMinHeight(MAX_HEIGHT);
    }

    protected Color backgroundColor = Color.rgb(230, 230, 230);

    @Override
    public void init() {
        setBackground(new Background(new BackgroundFill(backgroundColor, new CornerRadii(0), Insets.EMPTY)));


    }
}
