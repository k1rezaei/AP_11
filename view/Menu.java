import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Optional;

public class Menu {
    static int OFFSET_X = 320;
    private View view;
    private Group menuGroup = new Group();

    Menu(View view) {
        initializeMenu();
        this.view = view;
    }

    VBox vBox;

    private void initializeMenu() {
        vBox = new VBox();
        vBox.setId("null");

        ImageView background = new ImageView(new Image("file:textures/menu/back.jpg"));
        background.setFitWidth(800); background.setFitHeight(600);
        menuGroup.getChildren().add(background);
        setStart();
        setLoad();
        setGuide();
        setInfo();
        setExit();

        vBox.relocate(400,300);
        vBox.translateXProperty().bind(vBox.widthProperty().divide(2).negate());
        vBox.translateYProperty().bind(vBox.heightProperty().divide(2).negate());
        menuGroup.getChildren().add(vBox);
    }

    private void setStart() {
        Label start = new Label();
        start.setGraphic(new ImageView(new Image("file:textures/menu/start.png")));
        start.relocate(OFFSET_X, 100);
        start.setId("label_button");
        vBox.getChildren().add(start);
        start.setOnMouseClicked(event -> {
            view.setRoot(new LevelSelect(view).getRoot());
        });
    }

    private void setLoad() {
        Label load = new Label();
        load.setGraphic(new ImageView(new Image("file:textures/menu/load.png")));
        load.relocate(OFFSET_X, 200);
        load.setId("label_button");
        vBox.getChildren().add(load);
        load.setOnMouseClicked(event -> {
            try {
                Game.getInstance().loadGame("SaveGame");
                GameView gameView = GameView.getInstance();
                for (Workshop workshop : Game.getInstance().getWorkshops())
                    System.out.println(workshop.getName() + "," + workshop.getLevel());
                gameView.runGame();
                view.setRoot(gameView.getRoot());
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Can't LoadGame");
                alert.setHeaderText(null);
                alert.setContentText("Can't Find SaveGame");
                alert.showAndWait();
            }
        });
    }

    private void setInfo() {
        Label info = new Label();
        info.setGraphic(new ImageView(new Image("file:textures/menu/info.png")));
        info.relocate(OFFSET_X, 300);
        info.setId("label_button");
        vBox.getChildren().add(info);
        info.setOnMouseClicked(event -> {
            Pop pop = new Pop("Designed By\n" +
                    "Seyed Mahdi Sadegh Shobeiri\n" +
                    "Mohammad Mahdavi\n" +
                    "Keivan Rezaei\n" +
                    "Music : Hope Prevails (By Jesper Kyd)", view.getSnap());
            menuGroup.getChildren().add(pop.getStackPane());
            pop.getStackPane().setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    menuGroup.getChildren().remove(pop.getStackPane());
                }
            });
        });
    }

    private static final int NUM_SLIDES = 3;
    ArrayList<ImageView> slides = new ArrayList<>();
    ArrayList<ArrayList<Label>> labels = new ArrayList<>();

    EventHandler<MouseEvent> getOnMouseClickedEventHandler(int i) {
        return event -> {
            menuGroup.getChildren().remove(slides.get(i));
            menuGroup.getChildren().removeAll(labels.get(i));
            if(i+1 != NUM_SLIDES){
                menuGroup.getChildren().add(slides.get(i+1));
                menuGroup.getChildren().addAll(labels.get(i+1));
            }
        };
    }


    void setUpLabels(int i) {
        ArrayList<Label> labels = new ArrayList<>();

        switch (i) {
            case 0: {
                Label label0 = new Label(
                        "you can buy animals from here\n" +
                                "dog attacks lion and bear\n" +
                                "cat collect items\n" +
                                "lion and bear attack chicken, sheep and cow\n" +
                                "chicken lay eggs\n" +
                                "sheep produce wool\n" +
                                "cow produce milk\n");
                label0.relocate(20, 100);

                Label label1 = new Label(
                        "this shows how much money you have"
                );
                label1.relocate(420, 70);

                Label label2 = new Label(
                        "you can sell items with truck"
                );
                label2.relocate(20, 400);

                Label label3 = new Label("" +
                        "you can buy items with helicopter");
                label3.relocate(450, 380);

                Label label4 = new Label("click on screen to go to next slide");
                label4.relocate(400,300);

                labels.add(label0);
                labels.add(label1);
                labels.add(label2);
                labels.add(label3);
                labels.add(label4);
                break;
            }
            case 1: {
                Label label0 = new Label("you need water for planting\n" +
                        "you can refill well by pressing left click on it");
                label0.relocate(300, 170);

                Label label1 = new Label("workshop can convers item(s) to item");
                label1.relocate(200, 300);

                Label label2 = new Label("items go to warehouse when you click on them");
                label2.relocate(300, 400);

                labels.add(label0);
                labels.add(label1);
                labels.add(label2);

                break;
            }
            case 2: {
                Label label0 = new Label("you can cage lion and bear by clicking on them");
                label0.relocate(2,200);

                Label label1 = new Label("you can plant by clicking on ground");
                label1.relocate(400,450);

                Label label2 = new Label("you can upgrade cat, workshop, truck, well,\n" +
                        "helicopter and warehouse just press right click on them!\n" +
                        "and don't forget to collect items from ground");
                label2.relocate(10,10);
                labels.add(label0);
                labels.add(label1);
                labels.add(label2);
                break;
            }
        }
        for(Label label: labels){
            label.setId("tutorial");
            label.setOnMouseClicked(getOnMouseClickedEventHandler(i));
        }
        this.labels.add(labels);
    }



    private void setGuide() {

        for (int i = 0; i < NUM_SLIDES; i++) {
            ImageView imageView = new ImageView(new Image("file:textures/slides/slide" + i + ".png"));
            imageView.setFitHeight(600);
            imageView.setFitWidth(800);
            imageView.setOnMouseClicked(getOnMouseClickedEventHandler(i));
            slides.add(imageView);
            setUpLabels(i);
        }


        Label guide = new Label();
        guide.setGraphic(new ImageView(new Image("file:textures/menu/guide.png")));
        guide.relocate(OFFSET_X, 400);
        guide.setId("label_button");
        vBox.getChildren().add(guide);
        guide.setOnMouseClicked(event -> {
            menuGroup.getChildren().add(slides.get(0));
            menuGroup.getChildren().addAll(labels.get(0));
        });
    }

    private void setExit() {
        Label exit = new Label();
        exit.setGraphic(new ImageView(new Image("file:textures/menu/exit.png")));
        exit.relocate(OFFSET_X, 500);
        exit.setId("label_button");
        vBox.getChildren().add(exit);
        exit.setOnMouseClicked(event -> {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Exit");
            alert.setContentText("Are You Sure?");
            alert.setHeaderText(null);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                view.close();
            }
        });
    }

    Group getRoot() {
        return menuGroup;
    }

}
