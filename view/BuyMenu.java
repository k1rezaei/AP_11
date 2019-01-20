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

import java.util.ArrayList;

public class BuyMenu {
    View view;

    BuyMenu(View view) {
        this.view = view;
    }

    private Group buyGroup = new Group();
    final int WIDTH = 100;
    final int HEIGHT = 70;

    Group getBuyGroup() {
        init();
        return buyGroup;
    }

    static Image one = new Image("file:textures/sell/one.png");

    void init() {
        ArrayList<String> items = Game.getInstance().getLevel().getItemList();
        VBox vBox = new VBox();
        vBox.setMinWidth(WIDTH*3);

        Button ok = new Button("OK");
        Button cancel = new Button("Cancel");
        ok.setMinSize(50, HEIGHT);
        cancel.setMinSize(50, HEIGHT);

        vBox.getChildren().add(ok);
        vBox.getChildren().add(cancel);

        cancel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Game.getInstance().getHelicopter().clear();
                view.setRoot(GameView.getInstance().getRoot());
            }
        });

        ok.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    Game.getInstance().go(Game.getInstance().getHelicopter());
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
                    Game.getInstance().getHelicopter().clear();
                }
                view.setRoot(GameView.getInstance().getRoot());
            }
        });

        for (String type : items) {
            HBox hBox = new HBox();
            hBox.setMinWidth(WIDTH );
            ImageView imageView = Images.getSpriteAnimation(type).getImageView();
            imageView.setFitHeight(HEIGHT);
            imageView.setFitWidth(WIDTH);
            hBox.getChildren().add(imageView);

            Label price = new Label("" + Entity.getNewEntity(type).getBuyPrice());
            price.setMinSize(WIDTH, HEIGHT);
            hBox.getChildren().add(price);

            ImageView buyOne = new ImageView(one);
            buyOne.setFitWidth(WIDTH);
            buyOne.setFitHeight(HEIGHT);
            buyOne.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Game.getInstance().getHelicopter().add(type, 1);
                }
            });
            hBox.getChildren().add(buyOne);

            vBox.getChildren().add(hBox);
        }
        buyGroup.getChildren().add(vBox);
    }
}
