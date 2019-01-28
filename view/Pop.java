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
    private Label disabler;
    private StackPane stackPane = new StackPane();
    private Label content;

    Pop(String text, Image bg) {
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

    Pop(VBox vBox, Image bg){
        vBox.setId("popVBox");
        stackPane.relocate(400, 300);
        stackPane.translateXProperty().bind(stackPane.widthProperty().divide(2).negate());
        stackPane.translateYProperty().bind(stackPane.heightProperty().divide(2).negate());
        stackPane.setId("pop_stackPane");

        if (bg != null) {
            Node frost = Frost.freeze(bg);
            stackPane.getChildren().addAll(frost, vBox);

        } else {
            disabler = new Label();
            disabler.setMaxSize(800, 800);
            disabler.setMinSize(800, 800);
            stackPane.getChildren().addAll(disabler, vBox);
        }
    }

    Pop(VBox vBox, Image bg, Group root){
        this(vBox, bg);
        root.getChildren().add(stackPane);
        stackPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                root.getChildren().remove(stackPane);
            }
        });
    }

    Pop(String text, Image bg, Group root){
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


}
