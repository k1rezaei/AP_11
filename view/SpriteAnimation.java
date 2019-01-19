import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.ArrayList;

public class SpriteAnimation extends Transition {
    private final ArrayList<ImageView> imageView;
    private final ArrayList<Integer> count;
    private final ArrayList<Integer> columns;
    private final ArrayList<Integer> width;
    private final ArrayList<Integer> height;
    private int state = 0;

    private int lastIndex;

    public SpriteAnimation(
            ArrayList<ImageView> imageView,
            Duration duration,
            ArrayList<Integer> count, ArrayList<Integer> columns,
            ArrayList<Integer> width, ArrayList<Integer> height) {
        this.imageView = imageView;
        this.count = count;
        this.columns = columns;
        this.width = width;
        this.height = height;
        for (int i = 0; i < widths.size(); i++) {
            this.widths.set(i, widths.get(i) / (columns.get(i)));
            this.heights.set(i, heights.get(i) / ((counts.get(i) + columns.get(i) - 1) / columns.get(i)));

        }

        setCycleDuration(duration);
        setInterpolator(Interpolator.LINEAR);
    }

    public SpriteAnimation(LoadedImage loadedImage) {
        this(null, Duration.millis(1000), loadedImage.count, loadedImage.column, loadedImage.width, loadedImage.height);

        ArrayList<Image> images = loadedImage.images;

        for (Image image : images)
            imageViews.add(new ImageView(image));

    }
    @Override
    protected void interpolate(double k) {
        final int index = Math.min((int) Math.floor(k * count.get(state)), count.get(state) - 1);
        if (index != lastIndex) {
            final int x = (index % columns.get(state)) * width.get(state);
            final int y = (index / columns.get(state)) * height.get(state);
            imageView.get(state).setViewport(new Rectangle2D(x, y, width.get(state), height.get(state)));
            lastIndex = index;
        }
    }

    public void shutDown() {
        stop();
        imageViews.get(state).setViewport(new Rectangle2D(0, 0, widths.get(state), heights.get(state)));
    }

    public ImageView getImageView() {
        return imageViews.get(state);
    }

    public void setState(int state) {
        this.state = state;
    }
}