import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.ArrayList;

public class SpriteAnimation extends Transition {
    private final ArrayList<ImageView> imageViews;
    private final ArrayList<Integer> counts;
    private final ArrayList<Integer> columns;
    private final ArrayList<Integer> widths;
    private final ArrayList<Integer> heights;
    private int state = 0;

    private int lastIndex;

    public void setState(int state){ this.state = state; }

    public SpriteAnimation(
            ArrayList<ImageView> imageViews,
            Duration duration,
            ArrayList<Integer> counts, ArrayList<Integer> columns,
            ArrayList<Integer> widths, ArrayList<Integer> heights) {
        this.imageViews = imageViews;
        this.counts = counts;
        this.columns = columns;
        this.widths = widths;
        this.heights = heights;

        for(int i = 0; i < widths.size(); i++) {

            this.widths.set(i, widths.get(i) / (columns.get(i)));
            this.heights.set(i, heights.get(i) / ((counts.get(i) + columns.get(i) - 1) / columns.get(i)));

        }

        setCycleDuration(duration);
        setInterpolator(Interpolator.LINEAR);
    }

    @Override
    protected void interpolate(double k) {
        final int index = Math.min((int) Math.floor(k * counts.get(state)), counts.get(state) - 1);
        if (index != lastIndex) {
            final int x = (index % columns.get(state)) * widths.get(state);
            final int y = (index / columns.get(state)) * heights.get(state);
            imageViews.get(state).setViewport(new Rectangle2D(x, y, widths.get(state), heights.get(state)));
            lastIndex = index;
        }
    }
    void shutDown(){
        stop();
        imageViews.get(state).setViewport(new Rectangle2D(0, 0, widths.get(state), heights.get(state)));
    }
}