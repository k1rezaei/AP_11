import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


public class YesNoCancel {
    private VBox vBox = new VBox();
    private Label disabler = new Label();
     private StackPane stackPane  = new StackPane();
    private Label[] labels = new Label[3];
    private static Image YES = new Image("file:textures/yes.png");
    private static Image NO = new Image("file:textures/no.png");
    private static Image CANCEL = new Image("file:textures/cancel.png");
    private static int WIDTH = 200;
    private static int HEIGHT = 100;

    YesNoCancel(String text) {
        ImageView[] imageViews = new ImageView[3];
        imageViews[0] = new ImageView(YES);
        imageViews[1] = new ImageView(NO);
        imageViews[2] = new ImageView(CANCEL);
        Label label = new Label(text);
        vBox.getChildren().add(label);
        vBox.setId("vBox_menu");
       /* vBox.setMaxSize(WIDTH,HEIGHT*4);
        vBox.setMinSize(WIDTH,HEIGHT*4);*/
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

      /*  vBox.relocate(400, 300);
        vBox.translateXProperty().bind(vBox.widthProperty().divide(2).negate());
        vBox.translateYProperty().bind(vBox.heightProperty().divide(2).negate());*/

        disabler.setMinSize(800, 600);
        disabler.setMaxSize(800, 600);



        stackPane.relocate(400,300);
        stackPane.translateXProperty().bind(stackPane.widthProperty().divide(2).negate());
        stackPane.translateYProperty().bind(stackPane.heightProperty().divide(2).negate());

        stackPane.getChildren().addAll(disabler,vBox);
    }


    public StackPane getStackPane() {
        return stackPane;
    }

    public VBox getvBox() {
        return vBox;
    }


    public Label getYes() {
        return labels[0];
    }

    public Label getNo() {
        return labels[1];
    }

    public Label getDisabler() {
        return disabler;
    }

    public Label getCancel() {
        return labels[2];
    }


}
