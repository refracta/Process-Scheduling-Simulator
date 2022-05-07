package kr.ac.koreatech.os.pss.app.component.raw.n.timeline;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

import java.util.function.Function;

public class NTimelineScalePlatePane extends NTimelineComponent {
    Function<Double, String> scalerPlateLabelMapper = aDouble -> aDouble.intValue() + "T";

    protected int MAX_HEIGHT = 40;
    protected int SCALE_HEIGHT = 10;

    public NTimelineScalePlatePane() {
        setMinHeight(MAX_HEIGHT);
    }

    protected Color backgroundColor = Color.rgb(230, 230, 230);
    protected Color scaleColor = Color.rgb(76, 76, 76);

    @Override
    public void init() {
        setBackground(new Background(new BackgroundFill(backgroundColor, new CornerRadii(0), Insets.EMPTY)));
    }

    protected final int LABEL_FONT_SIZE = 18;


    public void drawScale(double start, double range, double endpoint) {
// 640 -> 640

        double startValue = start * endpoint;
        // 3.4
        double rangeValue = range * endpoint;
        double endValue = startValue + rangeValue;

        int samplingInterval = 3;
        double firstSamplingPoint = Math.ceil(startValue / samplingInterval) * samplingInterval;
        double endSamplingPoint = Math.floor(endValue / samplingInterval) * samplingInterval;

    }
}
