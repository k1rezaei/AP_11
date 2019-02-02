import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.util.ArrayList;

public class SpriteAnimation extends Transition {
    private final ArrayList<Integer> counts;
    private final ArrayList<Integer> columns;
    private final ArrayList<Integer> widths = new ArrayList<>();
    private final ArrayList<Integer> heights = new ArrayList<>();
    private ArrayList<ImageView> imageViews = new ArrayList<>();
    private int state = 0, x, y;
    private int lastIndex;


    private ProgressBar progressBar = new ProgressBar();
    {
        progressBar.setMaxHeight(10);
        progressBar.setMaxWidth(50);
        progressBar.setProgress(0);
    }

    public SpriteAnimation(Duration duration,
                           ArrayList<Integer> counts, ArrayList<Integer> columns,
                           ArrayList<Integer> widths, ArrayList<Integer> heights) {
        this.counts = counts;
        this.columns = columns;
        for (int i = 0; i < widths.size(); i++) {
            this.widths.add(widths.get(i) / (columns.get(i)));
            this.heights.add(heights.get(i) / ((counts.get(i) + columns.get(i) - 1) / columns.get(i)));
        }

        setCycleDuration(duration);
        setCycleCount(Animation.INDEFINITE);
        setInterpolator(Interpolator.LINEAR);

    }

    public SpriteAnimation(LoadedImage loadedImage) {
        this(Duration.millis(1000), loadedImage.getCount(), loadedImage.getColumn(),
                loadedImage.getWidth(), loadedImage.getHeight());
        ArrayList<Image> images = loadedImage.getImages();
        for (int i = 0; i < images.size(); i++) {
            ImageView imageView = new ImageView(images.get(i));
            imageViews.add(imageView);
            imageView.setViewport(new Rectangle2D(0, 0, widths.get(i), heights.get(i)));
        }
    }

    public int getLastIndex() {
        return lastIndex;
    }

    @Override
    protected void interpolate(double k) {
        if (GameView.getInstance().getPaused()) {
            return;
        }
        final int index = Math.min((int) Math.floor(k * counts.get(state)), counts.get(state) - 1);
        if (index != lastIndex) {
            int x = (index % columns.get(state)) * widths.get(state);
            int y = (index / columns.get(state)) * heights.get(state);
            if(state == 1 && counts.get(state) == 24) x = (columns.get(state)  - 1) * widths.get(state) - x;


            imageViews.get(state).setViewport(new Rectangle2D(x, y, widths.get(state), heights.get(state)));
            lastIndex = index;
        }
    }

    public void shutDown() {
        stop();
        imageViews.get(state).setViewport(new Rectangle2D(0, 0, widths.get(state), heights.get(state)));
    }

    public void setOnMouseClicked(EventHandler<MouseEvent> eventHandler) {
        for (ImageView imageView : imageViews) imageView.setOnMouseClicked(eventHandler);
    }

    public void setOnMouseEntered(EventHandler<MouseEvent> eventHandler) {
        for (ImageView imageView : imageViews) imageView.setOnMouseEntered(eventHandler);
    }

    public void setOnMouseExited(EventHandler<MouseEvent> eventHandler) {
        for (ImageView imageView : imageViews) imageView.setOnMouseExited(eventHandler);
    }

    public ImageView getImageView() {
        return imageViews.get(state);
    }

    public ArrayList<ImageView> getImageViews() {
        return imageViews;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return widths.get(state);
    }

    public int getHeight() {
        return heights.get(state);
    }

    public ProgressBar getProgressBar(double prog) {
        progressBar.setProgress(prog);
        return progressBar;
    }
    public ProgressBar getProgressBar() {
        return progressBar;
    }
}