import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;


public class Pop {
    private static final double BLUR_AMOUNT = 7;
    private Image backgroundImage;
    private Label disabler;
    private StackPane stackPane = new StackPane();
    private Label content;

    Pop(String text, Image bg) {
        if (bg != null) backgroundImage = bg;

        content = new Label();
        content.setId("pop");
        content.setText(text);

        stackPane.relocate(400, 300);
        stackPane.translateXProperty().bind(stackPane.widthProperty().divide(2).negate());
        stackPane.translateYProperty().bind(stackPane.heightProperty().divide(2).negate());
        stackPane.setId("pop_stackPane");

        if (bg != null) {
            Node frost = Frost.freeze(bg);
            stackPane.getChildren().addAll(frost, content);

        } else {
            disabler = new Label();
            disabler.setMaxSize(800, 800);
            disabler.setMinSize(800, 800);
            stackPane.getChildren().addAll(disabler, content);
        }

    }

    public StackPane getStackPane() {
        return stackPane;
    }


}
