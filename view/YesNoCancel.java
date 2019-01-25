import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


public class YesNoCancel {
    private static Image YES = new Image("file:textures/yes.png");
    private static Image NO = new Image("file:textures/no.png");
    private static Image CANCEL = new Image("file:textures/cancel.png");
    private static int WIDTH = 200;
    private static int HEIGHT = 100;
    private Node frost;
    private VBox vBox = new VBox();
    private StackPane stackPane = new StackPane();
    private Label[] labels = new Label[3];

    YesNoCancel(String text, Image bg) {
        ImageView[] imageViews = new ImageView[3];
        imageViews[0] = new ImageView(YES);
        imageViews[1] = new ImageView(NO);
        imageViews[2] = new ImageView(CANCEL);
        Label label = new Label(text);
        vBox.getChildren().add(label);
        vBox.setId("vBox_menu");

        for (int i = 0; i < 3; i++) {
            labels[i] = new Label();
            labels[i].setGraphic(imageViews[i]);
            imageViews[i].setFitWidth(WIDTH);
            imageViews[i].setFitHeight(HEIGHT);
            labels[i].setMinSize(WIDTH, HEIGHT);
            labels[i].setMaxSize(WIDTH, HEIGHT);
            labels[i].setId("label_button");
            vBox.getChildren().add(labels[i]);
        }

        stackPane.relocate(400, 300);
        stackPane.translateXProperty().bind(stackPane.widthProperty().divide(2).negate());
        stackPane.translateYProperty().bind(stackPane.heightProperty().divide(2).negate());

        frost = Frost.freeze(bg);
        stackPane.getChildren().addAll(frost, vBox);
    }


    public StackPane getStackPane() {
        return stackPane;
    }

    public VBox getVBox() {
        return vBox;
    }

    public Label getYes() {
        return labels[0];
    }

    public Label getNo() {
        return labels[1];
    }

    public Node getDisabler() {
        return frost;
    }

    public Label getCancel() {
        return labels[2];
    }


}
