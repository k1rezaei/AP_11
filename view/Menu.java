import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.Optional;

public class Menu {
    private View view;
    private Group menuGroup = new Group();

    {

        VBox vBox = new VBox();

        vBox.setAlignment(Pos.CENTER);
        vBox.setPrefWidth(800);
        vBox.setPrefHeight(600);
        menuGroup.getChildren().add(vBox);

        Button start = new Button();
        start.setText("Start");
        Button load = new Button();
        load.setText("Load");
        Button info = new Button();
        info.setText("Info");
        Button exit = new Button();
        exit.setText("Exit");

        vBox.getChildren().add(start);
        vBox.getChildren().add(load);
        vBox.getChildren().add(info);
        vBox.getChildren().add(exit);

        start.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                GameView gameView = GameView.getInstance();
                gameView.initGame();
                gameView.setView(view);
                view.setRoot(gameView.getRoot());
            }
        });
        load.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    Game.getInstance().loadGame("SaveGame");
                    GameView.getInstance().initGame();
                    view.setRoot(GameView.getInstance().getRoot());

                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Can't LoadGame");
                    alert.setHeaderText(null);
                    alert.setContentText("Can't Find SaveGame");
                    alert.showAndWait();
                }
            }
        });
        info.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Info");
                alert.setHeaderText(null);
                alert.setContentText("Head : Seyed Mahdi Sadegh Shobeiri\n" +
                        "Coder : Mohammad Mahdavi\n" +
                        "Copy Paster : Keyvan Rezayi");
                alert.showAndWait();
            }
        });

        exit.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Exit");
                alert.setContentText("Are You Sure?");
                alert.setHeaderText(null);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    view.close();
                }
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
