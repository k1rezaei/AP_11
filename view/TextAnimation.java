import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;


public class TextAnimation extends Transition {

    int blinkTime = 500;
    Label label;
    ;

    TextAnimation(Label label) {
        this.label = label;
        setInterpolator(Interpolator.LINEAR);
        setCycleDuration(Duration.millis(blinkTime));
        setCycleCount(Animation.INDEFINITE);
        setAutoReverse(true);
    }

    ArrayList<Integer> from = new ArrayList<>();
    ArrayList<Integer> to = new ArrayList<>();

    int diff(int idx) {
        return to.get(idx) - from.get(idx);
    }

    void setFrom(int r, int g, int b) {
        from.add(r);
        from.add(g);
        from.add(b);
    }

    void setTo(int r, int g, int b) {
        to.add(r);
        to.add(g);
        to.add(b);
    }

    void setBlinkTime(int blinkTime){
        this.blinkTime = blinkTime;
    }

    @Override
    protected void interpolate(double k) {

        int r = (int) (from.get(0) + diff(0) * k);
        int g = (int) (from.get(1) + diff(1) * k);
        int b = (int) (from.get(2) + diff(2) * k);

        label.setTextFill(Color.rgb(r, g, b));
        //label.getText().
    }

}
