import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class Menu {
    private static final int NUM_SLIDES = 3;
    private VBox vBox = new VBox();
    private ArrayList<ImageView> slides = new ArrayList<>();
    private ArrayList<ArrayList<Label>> labels = new ArrayList<>();
    private View view;
    private Group menuGroup = new Group();


    Menu(View view) {
        this.view = view;
        initializeMenu();
    }

    void setMute() {
        int x = 0;
        if (view.getMute()) x = 1;
        ImageView imageView = new ImageView(new Image("file:textures/mute" + (x) + ".png"));

        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        Label mute = new Label();
        mute.setId("label_button");
        mute.setGraphic(imageView);

        mute.relocate(20, 20);
        mute.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                view.setMute(!view.getMute());
                String path = "file:textures/mute";
                if (view.getMute() == false) {
                    path += "0";
                } else path += "1";
                path += ".png";
                Image image = new Image(path);
                imageView.setImage(image);
                if (view.getMute()) {
                    Sounds.mute();
                } else {
                    Sounds.init();
                    Sounds.play("main_theme");
                }
            }
        });
        menuGroup.getChildren().add(mute);

    }

    private void initializeMenu() {
        ImageView background = new ImageView(new Image("file:textures/menu/back.jpg"));
        background.setFitHeight(600);
        background.setFitWidth(800);
        menuGroup.getChildren().add(background);

        setMute();
        setStart();
        setLoad();
        setGuide();
        setInfo();
        setExit();
        vBox.relocate(400, 300);
        vBox.translateXProperty().bind(vBox.widthProperty().divide(2).negate());
        vBox.translateYProperty().bind(vBox.heightProperty().divide(2).negate());
        VBox fake = new VBox();

        fake.relocate(400, 300);
        fake.translateXProperty().bind(fake.widthProperty().divide(2).negate());
        fake.translateYProperty().bind(fake.heightProperty().divide(2).negate());

        fake.setId("menuFake");
        vBox.setId("menu");

        menuGroup.getChildren().add(fake);
        menuGroup.getChildren().add(vBox);
    }

    private void setStart() {
        Label start = new Label();
        start.setGraphic(new ImageView(new Image("file:textures/menu/start.png")));
        start.setId("label_button");
        vBox.getChildren().add(start);
        start.setOnMouseClicked(event -> {
            Buttons buttons = new Buttons(view.getSnap(), 3);
            Label[] labels = buttons.getLabels();
            Label solo = labels[0];
            solo.setText("solo");
            Label join = labels[1];
            join.setText("join");
            Label host = labels[2];
            host.setText("host");
            menuGroup.getChildren().add(buttons.getStackPane());
            solo.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    view.setRoot(new LevelSelect(view).getRoot());
                }
            });
            join.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    TextField userName = new TextField();
                    Button button = new Button();
                    HBox hBox = new HBox();
                    hBox.getChildren().addAll(userName, button);
                    menuGroup.getChildren().addAll(hBox);
                    button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            Client client = new Client();
                            client.initialize();
                            System.err.println(client.formatter == null);
                            try {
                                System.err.println(client.formatter == null);
                                if (client.checkId(userName.getText())) {

                                } else {
                                    userName.setText("");
                                }
                            }catch (Exception e){
                                System.err.println("something is wrong");
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
            host.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Server server = new Server();
                    server.run();
                    System.err.println("Hey");
                }
            });
        });

    }

    private void setLoad() {
        Label load = new Label();
        load.setGraphic(new ImageView(new Image("file:textures/menu/load.png")));
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
                Pop pop = new Pop("Can't Find SaveGame", view.getSnap());
                menuGroup.getChildren().add(pop.getStackPane());
                pop.getStackPane().setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        menuGroup.getChildren().remove(pop.getStackPane());
                    }
                });
            }
        });
    }

    private void setInfo() {
        Label info = new Label();
        info.setGraphic(new ImageView(new Image("file:textures/menu/info.png")));
        info.setId("label_button");
        vBox.getChildren().add(info);
        info.setOnMouseClicked(event -> {
            Pop pop = new Pop("Designed By\n" +
                    "Seyed Mahdi Sadegh Shobeiri\n" +
                    "Mohammad Mahdavi\n" +
                    "Keivan Rezaei\n" +
                    "Music : Hope Prevails (By Jesper Kyd)", view.getSnap());
            menuGroup.getChildren().add(pop.getStackPane());
            pop.getStackPane().setOnMouseClicked(event1 -> menuGroup.getChildren().remove(pop.getStackPane()));
        });
    }

    private EventHandler<MouseEvent> getOnMouseClickedEventHandler(int i) {
        return event -> {
            menuGroup.getChildren().remove(slides.get(i));
            menuGroup.getChildren().removeAll(labels.get(i));
            if (i + 1 != NUM_SLIDES) {
                menuGroup.getChildren().add(slides.get(i + 1));
                menuGroup.getChildren().addAll(labels.get(i + 1));
            }
        };
    }


    private void setUpLabels(int i) {
        ArrayList<Label> labels = new ArrayList<>();

        switch (i) {
            case 0: {
                Label label0 = new Label(
                        "You can buy animals here.\n" +
                                "Dogs attack lions and bears.\n" +
                                "Cats collect items.\n" +
                                "Lions and bears attack chickens, sheep, and cows.\n" +
                                "Chickens lay eggs.\n" +
                                "Sheep produce wool.\n" +
                                "Cows produce milk.\n");
                label0.relocate(20, 120);

                Label label1 = new Label(
                        "This shows how much money you have."
                );
                label1.relocate(400, 70);

                Label label2 = new Label(
                        "You can sell your items using the truck."
                );
                label2.relocate(20, 400);

                Label label3 = new Label("" +
                        "You can buy items using the helicopter.");
                label3.relocate(370, 400);

                Label label4 = new Label("Click on the screen to move to next slide.");
                label4.relocate(400, 320);

                labels.add(label0);
                labels.add(label1);
                labels.add(label2);
                labels.add(label3);
                labels.add(label4);
                break;
            }
            case 1: {
                Label label0 = new Label("You need water to plant plants.\n" +
                        "You can refill  the well by left-clicking on it.");
                label0.relocate(300, 170);

                Label label1 = new Label("Workshops can convert items to other items");
                label1.relocate(200, 300);

                Label label2 = new Label("Items are put in the warehouse when you click on them.");
                label2.relocate(250, 400);

                labels.add(label0);
                labels.add(label1);
                labels.add(label2);

                break;
            }
            case 2: {
                Label label0 = new Label("You can cage lions and bears by clicking on them.");
                label0.relocate(2, 200);

                Label label1 = new Label("You can plant by clicking on the ground.");
                label1.relocate(400, 450);

                Label label2 = new Label("You can upgrade the cat, workshops, the truck, the well,\n" +
                        "the helicopter and the warehouse. Just press right click on them!\n" +
                        "And don't forget to collect items from the ground.");
                label2.relocate(10, 10);
                labels.add(label0);
                labels.add(label1);
                labels.add(label2);
                break;
            }
        }
        for (Label label : labels) {
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
        exit.setId("label_button");
        vBox.getChildren().add(exit);
        exit.setOnMouseClicked(event -> {

            YesNoCancel yesNoCancel = new YesNoCancel("Are you sure you want to exit?", view.getSnap());

            yesNoCancel.getVBox().getChildren().remove(yesNoCancel.getCancel());

            menuGroup.getChildren().add(yesNoCancel.getStackPane());

            yesNoCancel.getYes().setOnMouseClicked(event13 -> {
                menuGroup.getChildren().clear();
                view.close();
            });

            yesNoCancel.getNo().setOnMouseClicked(event1 -> menuGroup.getChildren().remove(yesNoCancel.getStackPane()));

            yesNoCancel.getDisabler().setOnMouseClicked(event12 -> menuGroup.getChildren().remove(yesNoCancel.getStackPane()));
        });
    }

    Group getRoot() {
        return menuGroup;
    }

}
