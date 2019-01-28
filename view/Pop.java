import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


public class Pop {
    private StackPane frost;
    private StackPane stackPane = new StackPane();
    private Label content;

    {

        stackPane.relocate(400, 300);
        stackPane.translateXProperty().bind(stackPane.widthProperty().divide(2).negate());
        stackPane.translateYProperty().bind(stackPane.heightProperty().divide(2).negate());
        stackPane.setId("pop_stackPane");
    }

    Pop(String text, Image bg) {
        content = new Label();
        content.setId("pop");
        content.setText(text);
        frost = Frost.freeze(bg);
        stackPane.getChildren().addAll(frost, content);
    }

    Pop(Node node, Image bg) {
        node.setId("popVBox");
        frost = Frost.freeze(bg);
        stackPane.getChildren().addAll(frost, node);
    }

    Pop(Group root, Image bg) {
        root.setId("popVBox");
        Node frost = Frost.freeze(bg);
        stackPane.getChildren().addAll(frost, root);
    }

    Pop(Node node, Image bg, Group root) {
        this(node, bg);
        root.getChildren().add(stackPane);
        stackPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                root.getChildren().remove(stackPane);
            }
        });
    }

    Pop(String text, Image bg, Group root) {
        this(text, bg);
        root.getChildren().add(stackPane);
        stackPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                root.getChildren().remove(stackPane);
            }
        });
    }

    public StackPane getStackPane() {
        return stackPane;
    }

    public StackPane getDisabler() {
        return frost;
    }

}
