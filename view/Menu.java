import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;

import java.util.ArrayList;

public class Menu {
    private static final int NUM_SLIDES = 3;
    private VBox vBox = new VBox();
    private ArrayList<ImageView> slides = new ArrayList<>();
    private ArrayList<ArrayList<Label>> labels = new ArrayList<>();
    private View view;
    private Group menuGroup = new Group();
    private static boolean isHost = false;


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
        mute.setOnMouseClicked(event -> {
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
                Sounds.get("main_theme").setCycleCount(MediaPlayer.INDEFINITE);
                Sounds.get("main_theme").play();
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
      /*  VBox fake = new VBox();

        fake.relocate(400, 300);
        fake.translateXProperty().bind(fake.widthProperty().divide(2).negate());
        fake.translateYProperty().bind(fake.heightProperty().divide(2).negate());

        fake.setId("menuFake");*/
        vBox.setId("menu");

        //  menuGroup.getChildren().add(fake);
        menuGroup.getChildren().add(vBox);
    }

    long lastTry = 0;
    final static public long GAP_TIME = 5 * 1000000000L;

    private void connect(String userName, String ip, int port) {
        if (lastTry + GAP_TIME > System.nanoTime()) {
            new Pop("You should wait " + (1 + (GAP_TIME - System.nanoTime() + lastTry) / 1000000000) + " second before trying again", view.getSnap(), menuGroup);
            return;
        }
        Platform.runLater(() -> lastTry = System.nanoTime());
        Client client = new Client(view);
        client.initialize(ip, port);
        try {
            if (client.checkId(userName)) {
                view.setRoot(client.getMultiPlayerMenu().getRoot());
                client.run(new LevelSelect(view).getLevel());
                GameView.getInstance().setClient(client);
            } else {
                new Pop("Invalid Usernam", view.getSnap(), menuGroup);
            }
        } catch (Exception e) {
            Pop pop = new Pop("No one is Host", view.getSnap(), menuGroup);
            System.err.println("something is wrong");
            e.printStackTrace();
        }
    }

    private void setStart() {
        Label start = new Label("START");
        //start.setGraphic(new ImageView(new Image("file:textures/menu/start.png")));
        start.setId("label_button");
        vBox.getChildren().add(start);
        start.setOnMouseClicked(event -> {
            Buttons buttons = new Buttons(view.getSnap(), 4);
            buttons.getDisabler().setOnMouseClicked(event12 -> menuGroup.getChildren().remove(buttons.getStackPane()));
            buttons.getLabels()[3].setOnMouseClicked(event1 -> menuGroup.getChildren().remove(buttons.getStackPane()));

            Label[] labels = buttons.getLabels();
            Label cancel = labels[3];
            cancel.setText("CANCEL");
            cancel.setStyle("-fx-font-size: 50");
            //cancel.setGraphic(new ImageView(new Image("file:textures/cancel.png")));

            Label solo = labels[0];
            solo.setText("SOLO");
            //solo.setGraphic(new ImageView(new Image("file:textures/solo.png")));
            Label join = labels[1];
            join.setText("JOIN");
            //join.setGraphic(new ImageView(new Image("file:textures/join.png")));
            Label host = labels[2];
            host.setText("HOST");
            //host.setGraphic(new ImageView(new Image("file:textures/host.png")));
            menuGroup.getChildren().add(buttons.getStackPane());
            solo.setOnMouseClicked(event15 -> {
                GameView.getInstance().setClient(null);
                view.setRoot(new LevelSelect(view).getRoot());
            });
            join.setOnMouseClicked(event13 -> {
                LimitedTextField userName = new LimitedTextField(16);
                userName.setMaxWidth(200);
                userName.setPromptText("UserName");
                userName.setId("inputBox");
                TextField ipAddress = new TextField();
                ipAddress.setText("127.0.0.1");
                ipAddress.setMaxWidth(200);
                TextField port = new TextField();
                port.setMaxWidth(200);
                port.setText("8050");
                Label button = new Label("Join");
                button.setId("label_button");
                button.setMinWidth(200);
                Label cancel1 = new Label("Cancel");
                cancel1.setId("label_button");
                cancel1.setMinWidth(200);

                Buttons logIn = new Buttons(view.getSnap(), 0);
                logIn.getVBox().getChildren().addAll(userName, ipAddress, port, button, cancel1);

                logIn.getStackPane().relocate(400, 300);
                logIn.getStackPane().translateXProperty().bind(logIn.getStackPane().widthProperty().divide(2).negate());
                logIn.getStackPane().translateYProperty().bind(logIn.getStackPane().heightProperty().divide(2).negate());
                logIn.getDisabler().setOnMouseClicked(event131 -> menuGroup.getChildren().remove(logIn.getStackPane()));

                menuGroup.getChildren().addAll(logIn.getStackPane());
                logIn.getVBox().setOnKeyPressed(event1312 -> {
                    if (event1312.getCode() == KeyCode.ENTER)
                        connect(userName.getText(), ipAddress.getText(), Integer.parseInt(port.getText()));
                });

                button.setOnMouseClicked(event1313 ->
                        connect(userName.getText(), ipAddress.getText(), Integer.parseInt(port.getText())));
                cancel1.setOnMouseClicked(event1314 -> menuGroup.getChildren().remove(logIn.getStackPane()));
            });
            host.setOnMouseClicked(event14 -> {
                if (!isHost) {
                    isHost = true;
                    Server server = new Server();
                    server.run();
                    System.err.println("U R HOST");
                    new Pop("You are HOST now", view.getSnap(), menuGroup);
                } else {
                    new Pop("You or someone else is host", view.getSnap(), menuGroup);
                }
            });
        });

    }

    private void setLoad() {

    }

    private void setInfo() {
        Label info = new Label("INFO");
        //info.setGraphic(new ImageView(new Image("file:textures/menu/info.png")));
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


        Label guide = new Label("GUIDE");
        //guide.setGraphic(new ImageView(new Image("file:textures/menu/guide.png")));
        guide.setId("label_button");
        vBox.getChildren().add(guide);
        guide.setOnMouseClicked(event -> {
            menuGroup.getChildren().add(slides.get(0));
            menuGroup.getChildren().addAll(labels.get(0));
        });
    }

    private void setExit() {
        Label exit = new Label("EXIT");
        //exit.setGraphic(new ImageView(new Image("file:textures/menu/exit.png")));
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
