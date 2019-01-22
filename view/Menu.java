import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Menu {
    static int OFFSET_X = 320;
    private View view;
    private Group menuGroup = new Group();

    Menu(View view) {
        initializeMenu();
        this.view = view;
    }

    private void initializeMenu() {
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
        start.setGraphic(new ImageView(new Image("file:textures/menu/start.png")));
        start.relocate(OFFSET_X, 100);
        start.setId("label_button");
        menuGroup.getChildren().add(start);
        start.setOnMouseClicked(event -> {
            GameView gameView = GameView.getInstance();
            List<String> choices = new ArrayList<>();
            choices.add("level0");
            choices.add("level1");
            ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
            dialog.setTitle("Choose Level");
            dialog.setHeaderText(null);
            dialog.setContentText(null);
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(s -> {
                Game.getInstance().runMap(Game.getInstance().getLevel(s));
                gameView.runGame();
                view.setRoot(gameView.getRoot());
            });
        });
    }

    private void setLoad() {
        Label load = new Label();
        load.setGraphic(new ImageView(new Image("file:textures/menu/load.png")));
        load.relocate(OFFSET_X, 200);
        load.setId("label_button");
        menuGroup.getChildren().add(load);
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
        menuGroup.getChildren().add(info);
        info.setOnMouseClicked(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setHeaderText(null);
            alert.setContentText("Designed By\n" +
                    "Seyed Mahdi Sadegh Shobeiri\n" +
                    "Mohammad Mahdavi\n" +
                    "Keivan Rezaei\n" +
                    "Music : Hope Prevails (By Jesper Kyd)");
            alert.showAndWait();
        });
    }

    private void setExit() {
        Label exit = new Label();
        exit.setGraphic(new ImageView(new Image("file:textures/menu/exit.png")));
        exit.relocate(OFFSET_X, 400);
        exit.setId("label_button");
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

    Group getRoot() {
        return menuGroup;
    }

}
