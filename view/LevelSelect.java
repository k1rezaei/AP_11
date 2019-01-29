import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class LevelSelect {

    private static final int NUM_LEVELS = 9;
    private static final int SIZE = 100;
    private static final String BASE = "file:textures/level/";
    private static ArrayList<Image> images = new ArrayList<>();
    private static Image back = new Image(BASE + "back.png");
    private static Image lock = new Image(BASE + "lock.png");
    private static Image BG = new Image(BASE + "back.jpg");

    static {
        for (int i = 0; i < NUM_LEVELS; i++) {
            images.add(new Image(BASE + (i + 1) + ".png"));
        }
    }

    private ArrayList<Boolean> isLock = new ArrayList<>();
    private boolean[] levels = new boolean[NUM_LEVELS];
    private Group root = new Group();
    private View view;
    private FlowPane flowPane = new FlowPane(Orientation.HORIZONTAL);

    {
        try {
            InputStream inputStream = new FileInputStream("Levels");
            Scanner scanner = new Scanner(inputStream);
            while (scanner.hasNext()) {
                int level = scanner.nextInt();
                levels[level] = true;
            }
            scanner.close();
            inputStream.close();
        } catch (Exception ignored) {
            System.err.println("Level Select");
        }


        for (int i = 0; i < 1; i++) isLock.add(false);
        for (int i = 1; i < 9; i++) {
            boolean lock = false;
            for (int j = 0; j < i; j++)
                if (!levels[j]) lock = true;

            isLock.add(lock);
        }
    }

    LevelSelect(View view) {
        this.view = view;

        flowPane.setId("level_select");

        ImageView bg = new ImageView(BG);
        bg.setFitWidth(800);
        bg.setFitHeight(600);
        root.getChildren().add(bg);

        Label backLabel = new Label("BACK");
        backLabel.setId("label_button");
        //ImageView backImageView = new ImageView(new Image("file:textures/back_button.png"));
        //backLabel.setGraphic(backImageView);
        backLabel.relocate(30, 30);
        root.getChildren().add(backLabel);
        backLabel.setOnMouseClicked(event -> view.setRoot(new Menu(view).getRoot()));

        flowPane.setPrefWrapLength(350);

        flowPane.setPadding(new Insets(10));
        flowPane.setHgap(10);
        flowPane.setVgap(10);

        flowPane.relocate(400, 300);
        flowPane.translateXProperty().bind(flowPane.widthProperty().divide(2).negate());
        flowPane.translateYProperty().bind(flowPane.heightProperty().divide(2).negate());

        for (int i = 0; i < NUM_LEVELS; i++) {

            StackPane stackPane = new StackPane();

            Label label = new Label();
            ImageView imageView;
            if (isLock.get(i)) {
                imageView = new ImageView(lock);
            } else {
                imageView = new ImageView(images.get(i));
            }


            imageView.setFitWidth(SIZE);
            imageView.setFitHeight(SIZE);
            if(!isLock.get(i))label.setText(""+(i+1));
            else label.setGraphic(imageView);
            //label.setGraphic(imageView);
            label.setMaxSize(SIZE, SIZE);
            label.setMinSize(SIZE, SIZE);

            ImageView back = new ImageView(this.back);
            back.setFitHeight(SIZE);
            back.setFitWidth(SIZE);
            back.setOpacity(0.5);
            stackPane.getChildren().addAll(back, label);
            label.setId("label_button");

            final int finalI = i;
            if (!isLock.get(i)) stackPane.setOnMouseClicked(event -> {
                Game.runMap(Game.getLevel("level" + finalI));
                GameView.getInstance().runGame();
                view.setRoot(GameView.getInstance().getRoot());
            });
            flowPane.getChildren().add(stackPane);
        }

        root.getChildren().addAll(flowPane);
    }

    public Group getRoot() {
        return root;
    }

}
