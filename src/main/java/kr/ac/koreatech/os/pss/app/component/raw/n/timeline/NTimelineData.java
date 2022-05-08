package kr.ac.koreatech.os.pss.app.component.raw.n.timeline;

public record NTimelineData(int value, double renderX) {
    @Override
    public String toString() {
        return "NTimelineData{" +
                "value=" + value +
                ", renderX=" + renderX +
                '}';
    }
}
