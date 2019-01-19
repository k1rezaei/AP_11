import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Optional;

public class Menu {
    View view;

    Menu(View view){
        this.view = view;
    }

    private Group menuGroup = new Group();




    {

        VBox vBox = new VBox();
        {
            vBox.setAlignment(Pos.CENTER);
            menuGroup.getChildren().addAll(vBox);
        }

        Button start = new Button();
        start.setText("Start");
        Button load = new Button();
        load.setText("Load");
        Button info = new Button();
        info.setText("Info");
        Button exit = new Button();
        exit.setText("Exit");
        /*
        ArrayList<Button> buttons = new ArrayList<>();
        buttons.add(start);
        buttons.add(load);
        buttons.add(info);
        buttons.add(exit);*/

        vBox.getChildren().addAll(start);
        vBox.getChildren().addAll(load);
        vBox.getChildren().addAll(info);
        vBox.getChildren().addAll(exit);

        start.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                /**
                 *
                 *
                 *
                 */
            }
        });
        load.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                /**
                 *
                 *
                 *
                 */
            }
        });
        info.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Info");
                alert.setHeaderText(null);
                alert.setContentText("Head : Seyed Mahdi Sadegh Shobeiri\n" +
                                     "Coder : Mohammad Mahdavi\n"+
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
                if (result.get() == ButtonType.OK){
                    view.close();
                }
            }
        });
    }
}
