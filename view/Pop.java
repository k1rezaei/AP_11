import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class Pop {
    private Label disabler;
    private StackPane stackPane = new StackPane();
    private Label content;
    public StackPane getStackPane() {
        return stackPane;
    }

    Pop(String text){
        disabler = new Label();
        disabler.setMaxSize(800,800);
        disabler.setMinSize(800,800);

        content = new Label();
        content.setId("pop");
        content.setText(text);

        stackPane.relocate(400,300);
        stackPane.translateXProperty().bind(stackPane.widthProperty().divide(2).negate());
        stackPane.translateYProperty().bind(stackPane.heightProperty().divide(2).negate());
        stackPane.getChildren().addAll(disabler,content);
    }
}
