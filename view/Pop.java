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
    private Node content;
    AddType addType;

    enum AddType {
        ALERT, WINDOW, BUTTONS, BUTTONS_TEXT;
    }

    {
        stackPane.relocate(400, 300);
        stackPane.translateXProperty().bind(stackPane.widthProperty().divide(2).negate());
        stackPane.translateYProperty().bind(stackPane.heightProperty().divide(2).negate());
        stackPane.setId("pop_stackPane");
    }

    Pop(String text, Image bg) {
        content = new Label();
        content.setId("pop");
        ((Label) (content)).setText(text);
        frost = Frost.freeze(bg);
        stackPane.getChildren().addAll(frost, content);
    }

    Pop(Node node, Image bg) {
        content = node;
        node.setId("popVBox");
        frost = Frost.freeze(bg);
        stackPane.getChildren().addAll(frost, node);
    }

    Pop(Node node, Image bg, Group root, AddType addType) {
        this(node, bg);
        this.addType = addType;
        root.getChildren().add(stackPane);
        if (addType == AddType.ALERT) stackPane.setOnMouseClicked(event -> root.getChildren().remove(stackPane));
        else {
            stackPane.setOnMouseClicked(event -> root.getChildren().remove(stackPane));
            node.setId("vBox_menu");
        }
        if (addType == AddType.BUTTONS || addType == AddType.BUTTONS_TEXT) {
            setButtonsID();
        }
    }

    Pop(String text, Image bg, Group root, AddType addType) {
        this(text, bg);
        this.addType = addType;
        root.getChildren().add(stackPane);
        stackPane.setOnMouseClicked(event -> root.getChildren().remove(stackPane));
    }


    public StackPane getStackPane() {
        return stackPane;
    }

    public StackPane getDisabler() {
        return frost;
    }

    public Node getContent() {
        return content;
    }

    public void setButtonsID() {
        boolean first = true;
        if (content instanceof VBox) {
            for (Node node : ((VBox) content).getChildren()) {
                if (node instanceof Label){
                    if(addType == AddType.BUTTONS_TEXT && first){
                        first = false;
                        continue;
                    }
                    node.setId("label_button");
                }
            }
        } else {
            throw new RuntimeException("This is not button pop");
        }

    }
}
