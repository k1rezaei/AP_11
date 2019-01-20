import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

public class SellMenu {
    private Group sellGroup = new Group();
    final int WIDTH = 300;
    final int HEIGHT = 70;

    View view;
    Group getSellGroup() {
        update();
        return sellGroup;
    }

    SellMenu(View view){
        this.view = view;
    }
    static Image one = new Image("file:textures/sell/one.png");
    static Image all = new Image("file:textures/sell/all.png");


    HashMap<String, Integer> truck = new HashMap<>();


    void update() {
        sellGroup.getChildren().clear();
        Map<String, Integer> storables = Game.getInstance().getWarehouse().getStorables();
        VBox vBox = new VBox();
        vBox.setMinWidth(WIDTH);
        sellGroup.getChildren().add(vBox);


        Button ok = new Button("OK");
        Button cancel = new Button("Cancel");
        ok.setMinSize(50, HEIGHT);
        cancel.setMinSize(50, HEIGHT);

        vBox.getChildren().add(ok);
        vBox.getChildren().add(cancel);

        cancel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Game.getInstance().getTruck().clear();
                truck.clear();
                view.setRoot(GameView.getInstance().getRoot());
            }
        });

        ok.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    Game.getInstance().go(Game.getInstance().getTruck());
                    GameView.getInstance().getTruck().getImageView().setVisible(false);
                    new AnimationTimer() {
                        @Override
                        public void handle(long now) {
                            if (Game.getInstance().getTruck().getRemainingTime() == 0 ){
                                GameView.getInstance().getTruck().getImageView().setVisible(true);
                            }
                        }
                    }.start();
                }catch (Exception e){

                }
                truck.clear();
                view.setRoot(GameView.getInstance().getRoot());
            }
        });
        {
            HBox hBox = new HBox();
            hBox.setMinWidth(WIDTH);
            hBox.setMinHeight(HEIGHT);

            Label[] labels = new Label[3];

            labels[0] = new Label("Goods");
            labels[1] = new Label("Price");
            labels[2] = new Label("Ship");

            for (Label label : labels){
                label.setMinWidth(WIDTH / 3);
                label.setMinHeight(HEIGHT);
            }
            hBox.getChildren().addAll(labels);

            vBox.getChildren().add(hBox);
        }

        for (Map.Entry<String, Integer> pair : storables.entrySet()) {
            int tmp = pair.getValue();
            if (truck.get(pair.getKey()) != null) {
                tmp -= truck.get(pair.getKey());
            }

            final int cnt = tmp;
            if (cnt == 0) continue;
            HBox hBox = new HBox();
            hBox.setMinHeight(HEIGHT);
            hBox.setMinWidth(WIDTH);

            ImageView imageView = Images.getSpriteAnimation(pair.getKey()).getImageView();
            imageView.setFitWidth(48);
            imageView.setFitHeight(HEIGHT);
            Label label = new Label(new Integer(cnt).toString());
            label.setMinWidth(52);
            label.setMinHeight(HEIGHT);
            hBox.getChildren().add(imageView);
            hBox.getChildren().add(label);

            Label price = new Label(new Integer(Entity.getNewEntity(pair.getKey()).getSellPrice()).toString());
            price.setMinWidth(100);
            price.setMinHeight(HEIGHT);
            hBox.getChildren().add(price);

            ImageView sellOne = new ImageView(one);
            ImageView sellAll = new ImageView(all);

            sellOne.setFitWidth(50);
            sellAll.setFitWidth(50);
            sellOne.setFitHeight(HEIGHT);
            sellAll.setFitHeight(HEIGHT);

            sellOne.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Game.getInstance().getTruck().add(pair.getKey(), 1);
                    if (truck.get(pair.getKey()) == null) truck.put(pair.getKey(), 0);
                    truck.put(pair.getKey(), truck.get(pair.getKey()) + 1);
                    update();
                }
            });

            sellAll.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Game.getInstance().getTruck().add(pair.getKey(), cnt);
                    if (truck.get(pair.getKey()) == null) truck.put(pair.getKey(), 0);
                    truck.put(pair.getKey(), truck.get(pair.getKey()) + cnt);
                    update();
                }
            });

            hBox.getChildren().add(sellOne);
            hBox.getChildren().add(sellAll);

            vBox.getChildren().add(hBox);
        }
    }

}
