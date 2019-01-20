import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Map;

public class BuyMenu {
    private Group buyGroup = new Group();
    final int WIDTH = 300;

    Group getBuyGroup() {
        return buyGroup;
    }

    static Image one = new Image("file:texture/sell/one");
    static Image all = newImage("file:texture/sell/all");



    void update() {
        buyGroup.getChildren().clear();
        Map<String, Integer> storables = Game.getInstance().getWarehouse().getStorables();
        VBox vBox = new VBox();
        vBox.setMinWidth(WIDTH);
        buyGroup.getChildren().add(vBox);

        {
            HBox hBox = new HBox();
            hBox.setMinWidth(WIDTH);
            hBox.setMinHeight(60);

            Label[] labels = new Label[3];

            labels[0] = new Label("Goods");
            labels[1] = new Label("Price");
            labels[2] = new Label("Ship");

            for (Label label : labels) label.setMaxWidth(WIDTH / 3);
            hBox.getChildren().addAll(labels);

            vBox.getChildren().add(hBox);
        }

        for (Map.Entry<String, Integer> pair : storables.entrySet()) {
            HBox hBox = new HBox();
            hBox.setMinHeight(60);
            hBox.setMinWidth(WIDTH);

            ImageView imageView = getSpriteAnimation(pair.getKey()).getImageView();
            Label label = new Label(pair.getValue().toString());
            label.setMinWidth(52);
            hBox.getChildren().add(imageView);
            hBox.getChildren().add(label);

            Label price = new Label(new Integer(Entity.getNewEntity(pair.getKey()).getSellPrice()).toString());
            price.setMinWidth(100);
            hBox.getChildren().add(price);

            ImageView sellOne = new ImageView(one);
            ImageView sellAll = new ImageView(all);

            sellOne.setFitWidth(50);
            sellAll.setFitWidth(50);
            sellOne.setFitHeight(100);
            sellAll.setFitHeight(100);

            sellOne.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Game.getInstance().getTruck().add(pair.getKey(), 1);
                }
            });

            sellAll.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Game.getInstance().getTruck().add(pair.getKey(), pair.getValue());
                }
            });

            hBox.getChildren().add(sellOne);
            hBox.getChildren().add(sellAll);


            vBox.getChildren().add(hBox);
        }
    }

}
