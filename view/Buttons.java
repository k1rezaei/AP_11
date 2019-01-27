import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


public class Buttons {
    private static int WIDTH = 200;
    private static int HEIGHT = 100;
    private Node frost;
    private VBox vBox = new VBox();
    private StackPane stackPane = new StackPane();
    private Label[] labels = new Label[3];

    Buttons( Image bg, int cnt) {
        ImageView[] imageViews = new ImageView[cnt];
        vBox.setId("vBox_menu");

        for (int i = 0; i < cnt; i++) {
            labels[i] = new Label();
            labels[i].setGraphic(imageViews[i]);
            labels[i].setMinSize(WIDTH, HEIGHT);
            labels[i].setMaxSize(WIDTH, HEIGHT);
            labels[i].setId("label_button");
            vBox.getChildren().add(labels[i]);
        }

        VBox fake = new VBox();
        fake.setId("vBox_menu_fake");

        stackPane.relocate(400, 300);
        stackPane.translateXProperty().bind(stackPane.widthProperty().divide(2).negate());
        stackPane.translateYProperty().bind(stackPane.heightProperty().divide(2).negate());

        frost = Frost.freeze(bg);
        stackPane.getChildren().addAll(frost, fake, vBox);
    }

    public StackPane getStackPane() {
        return stackPane;
    }

    public VBox getVBox() {
        return vBox;
    }

    public Node getDisabler() {
        return frost;
    }

    public Label[] getLabels(){ return labels;}
}
