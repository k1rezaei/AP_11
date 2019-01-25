import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;


public class Intro {

    private static final int DURATION = 4000;
    private static final int fadeCanv = 6;
    private static final ImageView BG = new ImageView(new Image("file:textures/intro.png"));
    private static Interpolator interpolator = new Interpolator() {
        @Override
        protected double curve(double t) {
            double res = 1;
            for (int i = 0; i < fadeCanv; i++) res *= t;
            return res;
        }
    };
    private boolean inMenu = false;
    private Group introGroup = new Group();
    private View view;

    Intro(View view) {
        this.view = view;
        Rectangle bg = new Rectangle(0, 0, 800, 600);
        bg.setFill(Color.BLACK);
        introGroup.getChildren().add(bg);

        BG.setFitWidth(800);
        BG.setFitHeight(600);
        introGroup.setOnMouseClicked(event -> goToMenu());
        Label label = new Label("Dr. Keivan - SmsS - Banana");
        label.setId("intro");
        label.relocate(400, 300);
        label.translateXProperty().bind(label.widthProperty().divide(2).negate());
        label.translateYProperty().bind(label.heightProperty().divide(2).negate());

        Reflection r = new Reflection();
        r.setFraction(1);
        label.setEffect(r);

        introGroup.getChildren().addAll(BG, label);

        FadeTransition ft = new FadeTransition(Duration.millis(DURATION), BG);
        ft.setFromValue(1.0);
        ft.setToValue(0);
        FadeTransition ft2 = new FadeTransition(Duration.millis(DURATION), label);
        ft2.setFromValue(1.0);
        ft2.setToValue(0);

        ft2.setInterpolator(interpolator);
        ft.setInterpolator(interpolator);


        ft.play();
        ft2.play();
        ft.setOnFinished(event -> goToMenu());

        TextAnimation blinkAnimation = new TextAnimation(label);
        blinkAnimation.setFrom(255, 0, 0);
        blinkAnimation.setTo(0, 0, 255);
        blinkAnimation.play();


    }

    private void goToMenu() {
        if (inMenu) return;
        inMenu = true;
        introGroup.setOnMouseClicked(null);
        view.setRoot(new Menu(view).getRoot());
    }

    public Group getRoot() {
        return introGroup;
    }


}
