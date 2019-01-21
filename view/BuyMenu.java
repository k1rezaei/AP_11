import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class BuyMenu {
    View view;

    int currentMoney;
    {
        currentMoney = Game.getInstance().getMoney();
    }

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

    Label cap = new Label();
    {
        cap.setMinSize(50,HEIGHT);
        cap.setFont(Font.font(20));
        cap.setAlignment(Pos.CENTER);
        cap.relocate(400,20);
    }

    Label money = new Label();
    {

        money.setMinSize(50,HEIGHT);
        money.setFont(Font.font(20));
        money.setAlignment(Pos.CENTER);
        money.relocate(400,0);
    }
    void update(){
        cap.setText("Capacity : " + Game.getInstance().getHelicopter().getCurrentCapacity());
        money.setText("Money : " + currentMoney);
    }

    FlowPane buyList = new FlowPane();
    {
        buyGroup.getChildren().add(buyList);
        buyGroup.getChildren().add(money);
        buyGroup.getChildren().add(cap);
        buyList.relocate(400,70);
        update();
    }

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
                GameView.getInstance().resume();
                view.setRoot(GameView.getInstance().getRoot());
            }
        });

        ok.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    Game.getInstance().go(Game.getInstance().getHelicopter());
                    GameView.getInstance().getHelicopter().getImageView().setVisible(false);
                    new AnimationTimer() {
                        @Override
                        public void handle(long now) {
                            if (Game.getInstance().getHelicopter().getRemainingTime() == 0 ){
                                GameView.getInstance().getHelicopter().getImageView().setVisible(true);
                            }
                        }
                    }.start();
                }catch (Exception e){
                    Game.getInstance().getHelicopter().clear();
                }
                GameView.getInstance().resume();
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

            Label price = new Label("" + Entity.getNewEntity(type).getBuyPrice() + " (" + Entity.getNewEntity(type).getSize()+ ")");
            price.setMinSize(WIDTH, HEIGHT);
            price.setAlignment(Pos.CENTER);
            hBox.getChildren().add(price);

            ImageView buyOne = new ImageView(one);
            buyOne.setFitWidth(WIDTH);
            buyOne.setFitHeight(HEIGHT);
            buyOne.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(Game.getInstance().getHelicopter().getCurrentCapacity() >= Entity.getNewEntity(type).getSize()) {
                        if(Game.getInstance().getHelicopter().getNeededMoney() + Entity.getNewEntity(type).getBuyPrice() <= Game.getInstance().getMoney()) {
                            Game.getInstance().getHelicopter().add(type, 1);
                            currentMoney -= Entity.getNewEntity(type).getBuyPrice();
                            buyList.getChildren().add(Images.getSpriteAnimation(type).getImageView());
                            update();
                        }else {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Oooops");
                            alert.setHeaderText(null);
                            alert.setContentText("Not Enough Money");
                            alert.showAndWait();
                        }
                    }else{
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Oooops");
                        alert.setHeaderText(null);
                        alert.setContentText("Not Enough Space");
                        alert.showAndWait();
                    }
                }
            });
            hBox.getChildren().add(buyOne);

            vBox.getChildren().add(hBox);
        }
        buyGroup.getChildren().add(vBox);
    }

}
