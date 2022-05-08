package kr.ac.koreatech.os.pss.app.component.raw.n.timeline;

import java.util.List;

public interface NTimelineUpdateListener {
    void update(double start, double range, double endpoint, double samplingInterval, double unitStep, List<NTimelineData> timelineData);

}
