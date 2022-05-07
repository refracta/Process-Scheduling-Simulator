package kr.ac.koreatech.os.pss.app.component.raw.n.timeline;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.function.Function;

public class NTimelineScalePlatePane extends NTimelineComponent {
    Function<Double, String> scalerPlateLabelMapper = aDouble -> aDouble.intValue() + "T";

    protected int LEFT_MARGIN = 8 + 24;
    protected int RIGHT_MARGIN = 14;
    protected int MAX_HEIGHT = 40;
    protected int SCALE_HEIGHT = 10;
//    protected int LEFT

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

    public double getScalePlateWidth(){
        return getWidth() - LEFT_MARGIN - RIGHT_MARGIN;
    }


    public void drawScale(double start, double range, double endpoint) {
// 640 -> 640
        getChildren().clear();
        System.out.println(start+"/"+range+"/"+endpoint);
        double startValue = start * endpoint;
        // 3.4
        double rangeValue = range * endpoint;
        double endValue = startValue + rangeValue;

        int samplingInterval = 3;
        // 7.4
        int firstSamplingPoint = (int) Math.ceil(startValue / samplingInterval) * samplingInterval;
        //2
        int endSamplingPoint = (int) Math.floor(endValue / samplingInterval) * samplingInterval;

        int outerEndSamplingPoint = (int) Math.ceil(endValue / samplingInterval) * samplingInterval;

        int numInterval = (int) Math.floor(endValue / samplingInterval) - (int) Math.ceil(startValue / samplingInterval) + 1;
        for (int i = firstSamplingPoint; i < outerEndSamplingPoint; i += samplingInterval) {
            Line line = new Line(0, 0, 0, LEFT_MARGIN);
            double v = i / endpoint * getScalePlateWidth();
            line.setLayoutX(v);
            getChildren().add(line);
        }
    }
}
