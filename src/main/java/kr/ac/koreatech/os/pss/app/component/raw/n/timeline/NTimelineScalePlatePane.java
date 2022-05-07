package kr.ac.koreatech.os.pss.app.component.raw.n.timeline;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import kr.ac.koreatech.os.pss.app.component.utils.TextUtils;

import java.util.function.Function;

public class NTimelineScalePlatePane extends NTimelineComponent {
    Function<Integer, String> scalerPlateLabelMapper = integer -> String.valueOf(integer);

    protected int LEFT_MARGIN = 8 + 24;
    protected int RIGHT_MARGIN = 14;
    protected int MAX_HEIGHT = 40;
    protected int SCALE_HEIGHT = 10;
//    protected int LEFT

    protected int SCALER_PLATE_LABEL_INTERVAL_MARGIN = 10;
    protected int SCALER_PLATE_LABEL_TOP_MARGIN = 6;
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

    public double getScalePlateWidth() {
        return getWidth() - LEFT_MARGIN - RIGHT_MARGIN;
    }


    public void drawScale(double start, double range, double endpoint) {
        double startValue = start * endpoint;
        // 3.4
        double rangeValue = range * endpoint;
        double endValue = startValue + rangeValue;

        samplingLoop:
        for (int samplingInterval = 1; ; samplingInterval++) {
            getChildren().clear();
            int firstSamplingPoint = (int) Math.ceil(startValue / samplingInterval) * samplingInterval;
            int endSamplingPoint = (int) Math.floor(endValue / samplingInterval) * samplingInterval;
            int outerEndSamplingPoint = (int) Math.ceil(endValue / samplingInterval) * samplingInterval;
                outerEndSamplingPoint += endSamplingPoint == outerEndSamplingPoint ? 1 : 0;

            double prevHalfX = 0;
            double scaleY = MAX_HEIGHT - SCALE_HEIGHT;
            for (int i = firstSamplingPoint; i < outerEndSamplingPoint; i += samplingInterval) {
                Line line = new Line(0, 0, 0, SCALE_HEIGHT);
                double x = LEFT_MARGIN + ((i - firstSamplingPoint) / endpoint * getScalePlateWidth()) * (1 / range);
                line.setLayoutX(x);
                line.setLayoutY(scaleY);
                Text text = TextUtils.getDefaultText(scalerPlateLabelMapper.apply(i), 14);
                getChildren().add(text);
                text.applyCss();
                double textWidth = text.getBoundsInLocal().getWidth();

                text.setX(x - textWidth / 2);
                if (prevHalfX + SCALER_PLATE_LABEL_INTERVAL_MARGIN > text.getX()) {
                    continue samplingLoop;
                }
                prevHalfX = x + textWidth / 2;
                text.setY(SCALER_PLATE_LABEL_TOP_MARGIN + text.getBoundsInLocal().getHeight() + 2);
                getChildren().add(line);
            }
            break;
        }
    }
}
