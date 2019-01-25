import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;



public class Pop {
    private static final double BLUR_AMOUNT = 7;
    private Label disabler;
    private StackPane stackPane = new StackPane();
    private Label content;
    public StackPane getStackPane() {
        return stackPane;
    }
    Image backgroundImage;

    Pop(String text, Image bg){
        if(bg != null) backgroundImage = bg;



        content = new Label();
        content.setId("pop");
        content.setText(text);

        stackPane.relocate(400,300);
        stackPane.translateXProperty().bind(stackPane.widthProperty().divide(2).negate());
        stackPane.translateYProperty().bind(stackPane.heightProperty().divide(2).negate());

        if(bg != null) {
            Node frost = Frost.freeze(bg);
            stackPane.getChildren().addAll(frost, content);

        }else{
            disabler = new Label();
            disabler.setMaxSize(800,800);
            disabler.setMinSize(800,800);
            stackPane.getChildren().addAll(disabler,content);
        }

    }





}
