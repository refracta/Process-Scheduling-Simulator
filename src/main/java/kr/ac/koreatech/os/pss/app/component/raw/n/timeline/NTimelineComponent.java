package kr.ac.koreatech.os.pss.app.component.raw.n.timeline;

import javafx.scene.layout.AnchorPane;

public abstract class NTimelineComponent extends AnchorPane {
    public NTimelineComponent() {
        AnchorPane.setRightAnchor(this, 0.0);
        AnchorPane.setLeftAnchor(this, 0.0);
        widthProperty().addListener((observable, oldValue, newValue) -> resized(newValue, getHeight()));
        heightProperty().addListener((observable, oldValue, newValue) -> resized(getWidth(), newValue));
    }

    public abstract void init();

    public void resized(Number width, Number height) {
        init();
    }
}
