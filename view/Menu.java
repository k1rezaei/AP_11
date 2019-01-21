import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.Optional;

public class Menu {
    private View view;
    private Group menuGroup = new Group();

    {


        Media sound = new Media(new File("rabana.mp3").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
             mediaPlayer.play();





        VBox vBox = new VBox();

        vBox.setAlignment(Pos.CENTER);
        vBox.setPrefWidth(800);
        vBox.setPrefHeight(600);
        menuGroup.getChildren().add(vBox);

        ImageView background = new ImageView(new Image("file:textures/menu/back.jpg"));
        menuGroup.getChildren().add(background);


        setStart();

        setLoad();

        setInfo();

        setExit();

    }

    private void setStart() {
        Label start = new Label();
        start.setGraphic(new ImageView(new Image("file:textures/menu/start.png"))); start.relocate(350, 100);
        menuGroup.getChildren().add(start);
        start.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                GameView gameView = GameView.getInstance();
                gameView.initGame();
                gameView.setView(view);
                view.setRoot(gameView.getRoot());
            }
        });
    }

    private void setLoad() {
        Label load = new Label();
        load.setGraphic(new ImageView(new Image("file:textures/menu/load.png"))); load.relocate(350, 200);
        menuGroup.getChildren().add(load);
        load.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    Game.getInstance().loadGame("SaveGame");
                    GameView gameView = GameView.getInstance();
                    for (Workshop workshop : Game.getInstance().getWorkshops())
                        System.out.println(workshop.getName() + "," + workshop.getLevel());
                    gameView.initGame();
                    gameView.setView(view);
                    view.setRoot(gameView.getRoot());

                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Can't LoadGame");
                    alert.setHeaderText(null);
                    alert.setContentText("Can't Find SaveGame");
                    alert.showAndWait();
                }
            }
        });
    }

    private void setInfo() {
        Label info = new Label();
        info.setGraphic(new ImageView(new Image("file:textures/menu/info.png"))); info.relocate(350, 300);
        menuGroup.getChildren().add(info);
        info.setOnMouseClicked(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setHeaderText(null);
            alert.setContentText("Designed By\n" +
                    "Seyed Mahdi Sadegh Shobeiri\n" +
                    "Mohammad Mahdavi\n" +
                    "Keivan Rezaei");
            alert.showAndWait();
        });
    }

    private void setExit() {
        Label exit = new Label();
        exit.setGraphic(new ImageView(new Image("file:textures/menu/exit.png"))); exit.relocate(350, 400);
        menuGroup.getChildren().add(exit);
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

    Menu(View view) {
        this.view = view;
    }

    Group getRoot() {
        return menuGroup;
    }

}
