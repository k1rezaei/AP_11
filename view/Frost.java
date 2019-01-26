import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Frost extends StackPane {

    private static final double BLUR_AMOUNT = 7;

    public static StackPane freeze(Image background) {
        ImageView frost = new ImageView(background);

        Rectangle filler = new Rectangle(0, 0, 800, 600);
        filler.setFill(Color.AZURE);

        Pane frostPane = new Pane(frost);
        Effect frostEffect = new BoxBlur(BLUR_AMOUNT, BLUR_AMOUNT, 3);
        frostPane.setEffect(frostEffect);

        StackPane frostView = new StackPane(
                filler,
                frostPane
        );


        Rectangle clipShape = new Rectangle(0, 0, 800, 600);
        frostView.setClip(clipShape);


        return frostView;
    }

}
