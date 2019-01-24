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
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.HashMap;
import java.util.Map;

public class SellMenu {
    public static final int BASE_X = 30;
    public static final int DIS_X = 260;
    public static final int NUM_IN_ROW = 8;
    public static final int BASE_Y = 70;
    public static final int DIS_Y = 65;
    private static final Image BG = new Image("file:textures/shelf.jpg");
    private Group sellGroup = new Group();
    final int WIDTH = 300;
    final int HEIGHT = 70;

    View view;
    Group getSellGroup() {
        view.setRoot(sellGroup);
        update();
        return sellGroup;
    }

    SellMenu(View view){
        this.view = view;
    }
    static Image one = new Image("file:textures/one.png");
    static Image all = new Image("file:textures/all.png");


    HashMap<String, Integer> truck = new HashMap<>();

    void setCapAndMoney(){

        Label cap = new Label("Capacity : " + Game.getInstance().getTruck().getCurrentCapacity());
        Label money = new Label("Money : " + Game.getInstance().getTruck().getResultMoneyWithoutClear());


        cap.setMinSize(50,HEIGHT);
        cap.setFont(Font.font(20));
        cap.setAlignment(Pos.CENTER);
        cap.relocate(600,20);

        money.setMinSize(50,HEIGHT);
        money.setFont(Font.font(20));
        money.setAlignment(Pos.CENTER);
        money.relocate(600,0);

        sellGroup.getChildren().add(cap);
        sellGroup.getChildren().add(money);
    }

    void update() {
        sellGroup.getChildren().clear();

        ImageView bg = new ImageView(BG);
        bg.setFitWidth(800);
        bg.setFitHeight(600);
        sellGroup.getChildren().add(bg);

        setCapAndMoney();

        Map<String, Integer> storables = Game.getInstance().getWarehouse().getStorables();


        Label ok = new Label(); ok.relocate(BASE_X, 10);
        ok.setId("label_button");
        ImageView okImage = new ImageView(new Image("file:textures/sell.png"));
        okImage.setFitHeight(60);
        okImage.setFitWidth(100);

        ImageView cancelImage = new ImageView(new Image("file:textures/cancel.png"));
        cancelImage.setFitHeight(60);
        cancelImage.setFitWidth(100);

        ok.setGraphic(okImage);
        Label cancel = new Label(); cancel.relocate(BASE_X+110, 10);
        cancel.setId("label_button");
        cancel.setGraphic(cancelImage);


        sellGroup.getChildren().add(ok);
        sellGroup.getChildren().add(cancel);


        cancel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Game.getInstance().getTruck().clear();
                truck.clear();
                GameView.getInstance().resume();
                view.setRoot(GameView.getInstance().getRoot());
            }
        });

        ok.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    Game.getInstance().go(Game.getInstance().getTruck());
                }catch (Exception e){
                }
                truck.clear();
                GameView.getInstance().resume();
                view.setRoot(GameView.getInstance().getRoot());
            }
        });

        int numberOfItems = 0;

        for (Map.Entry<String, Integer> pair : storables.entrySet()) {
            int tmp = pair.getValue();
            if (truck.get(pair.getKey()) != null) {
                tmp -= truck.get(pair.getKey());
            }

            final int cnt = tmp;
            if (cnt == 0) continue;

            ImageView imageView = Images.getSpriteAnimation(pair.getKey()).getImageView();
            imageView.setFitWidth(30); imageView.setFitHeight(30);

            Label label = new Label(Integer.toString(cnt));

            Label price = new Label(Integer.toString(Entity.getNewEntity(pair.getKey()).getSellPrice()));

            ImageView so = new ImageView(one); so.setFitHeight(30*7/5); so.setFitWidth(25*7/5);
            Label sellOne = new Label(); sellOne.setGraphic(so);

            ImageView sa = new ImageView(all); sa.setFitHeight(30*7/5); sa.setFitWidth(25*7/5);
            Label sellAll = new Label(); sellAll.setGraphic(sa);
            sellAll.setId("label_button");
            sellOne.setId("label_button");

            int baseX = numberOfItems / NUM_IN_ROW * DIS_X + BASE_X;
            int baseY = (numberOfItems % NUM_IN_ROW) * DIS_Y + BASE_Y;

            imageView.relocate(baseX, baseY);
            label.relocate(baseX + 30, baseY);
            price.relocate(baseX + 75, baseY + 5);
            sellOne.relocate(baseX + 130, baseY);
            sellAll.relocate(baseX + 165, baseY);

            //Rectangle rectangle = new Rectangle(baseX, baseY, DIS_X, DIS_Y);
            //rectangle.setStroke(Color.GOLD); rectangle.setFill(Color.TRANSPARENT);


            sellGroup.getChildren().addAll(imageView, label, price, sellAll, sellOne);

            numberOfItems ++;

            sellOne.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {

                    if(Game.getInstance().getTruck().getCurrentCapacity() < Entity.getNewEntity(pair.getKey()).getSize()){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Not Enough Space");
                        alert.setHeaderText(null);
                        alert.setTitle("Oops");
                        alert.showAndWait();
                        return;
                    }

                    Game.getInstance().getTruck().add(pair.getKey(), 1);
                    if (truck.get(pair.getKey()) == null) truck.put(pair.getKey(), 0);
                    truck.put(pair.getKey(), truck.get(pair.getKey()) + 1);
                    update();
                }
            });

            sellAll.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {

                    int k = cnt;
                    if(Entity.getNewEntity(pair.getKey()).getSize() == 0) k = cnt;
                    else if(k >= Game.getInstance().getTruck().getCurrentCapacity()/Entity.getNewEntity(pair.getKey()).getSize())
                        k = Game.getInstance().getTruck().getCurrentCapacity()/Entity.getNewEntity(pair.getKey()).getSize();

                    if(k == 0){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Not Enough Space");
                        alert.setHeaderText(null);
                        alert.setTitle("Oops");
                        alert.showAndWait();
                    }
                    Game.getInstance().getTruck().add(pair.getKey(), k);
                    if (truck.get(pair.getKey()) == null) truck.put(pair.getKey(), 0);
                    truck.put(pair.getKey(), truck.get(pair.getKey()) + k);
                    update();
                }
            });

        }
    }

}
