import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;


public class Intro {

    static final int DURATION = 4000;
    static final int fadeCanv = 6;
    Group introGroup = new Group();
    View view;
    static final ImageView BG = new ImageView(new Image("file:textures/intro.png"));

    static Interpolator interpolator = new Interpolator() {
        @Override
        protected double curve(double t) {
            double res = 1;
            for(int i = 0; i < fadeCanv; i++) res *= t;
            return res;
        }
    };

    Intro(View view){
        this.view = view;
        Rectangle bg = new Rectangle(0,0,800,600);
        bg.setFill(Color.BLACK);
        introGroup.getChildren().add(bg);

        BG.setFitWidth(800);
        BG.setFitHeight(600);
        introGroup.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                goToMenu();
            }
        });
        Label label = new Label("Felan SmsS Taghdim Mikonad");
        label.setId("intro");
        label.translateXProperty().bind(label.widthProperty().divide(2).negate());
        label.translateYProperty().bind(label.heightProperty().divide(2).negate());
        label.relocate(400,300);
        label.setMinSize(800,600);
        label.setMaxSize(800,600);
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
        ft.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                goToMenu();
            }
        });

        TextAnimation blinkAnimation = new TextAnimation(label);
        blinkAnimation.setFrom(255, 0, 0);
        blinkAnimation.setTo(0,0,255);
        blinkAnimation.play();


    }

    boolean inMenu = false;

    void goToMenu(){
        if(inMenu) return;
        inMenu = true;
        introGroup.setOnMouseClicked(null);
        view.setRoot(new Menu(view).getRoot());
    }

    public Group getRoot() {
        return introGroup;
    }


}
