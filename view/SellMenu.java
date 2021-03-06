import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.util.HashMap;
import java.util.Map;

public class SellMenu {
    private static final int BASE_X = 30;
    private static final int DIS_X = 260;
    private static final int NUM_IN_ROW = 8;
    private static final int BASE_Y = 70;
    private static final int DIS_Y = 65;
    private static final Image BG = new Image("file:textures/shelf.jpg");
    private static Image one = new Image("file:textures/one.png");
    private static Image all = new Image("file:textures/all.png");
    private static Image COIN = new Image("file:textures/coin.png");
    private final int WIDTH = 300;
    private final int HEIGHT = 70;
    private View view;
    private HashMap<String, Integer> truck = new HashMap<>();
    private Group sellGroup = new Group();

    SellMenu(View view) {
        this.view = view;
    }

    Group getSellGroup() {
        view.setRoot(sellGroup);
        update();
        return sellGroup;
    }

    private void setCapAndMoney() {

        Label cap = new Label("Capacity : " + Game.getInstance().getTruck().getCurrentCapacity());
        Label money = new Label("Money : " + Game.getInstance().getTruck().getResultMoneyWithoutClear());

        cap.setMinSize(50, HEIGHT);
        cap.setFont(Font.font(20));
        cap.setAlignment(Pos.CENTER);
        cap.relocate(600, 20);

        money.setMinSize(50, HEIGHT);
        money.setFont(Font.font(20));
        money.setAlignment(Pos.CENTER);
        money.relocate(600, 0);

        sellGroup.getChildren().add(cap);
        sellGroup.getChildren().add(money);
    }

    private void update() {
        sellGroup.getChildren().clear();

        ImageView bg = new ImageView(BG);
        bg.setFitWidth(800);
        bg.setFitHeight(600);
        sellGroup.getChildren().add(bg);

        setCapAndMoney();

        Map<String, Integer> storables = Game.getInstance().getWarehouse().getStorables();

        Label ok = new Label();
        ok.relocate(BASE_X, 5);
        ok.setId("label_button");
        ImageView okImage = new ImageView(new Image("file:textures/sell.png"));
        okImage.setFitHeight(60);
        okImage.setFitWidth(100);

        ImageView cancelImage = new ImageView(new Image("file:textures/cancel.png"));
        cancelImage.setFitHeight(60);
        cancelImage.setFitWidth(100);

        //ok.setGraphic(okImage);
        Label cancel = new Label();
        cancel.relocate(BASE_X + 110, 5);
        cancel.setId("label_button");
        //cancel.setGraphic(cancelImage);
        ok.setText("OK");
        cancel.setText("CANCEL");

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
                } catch (Exception e) {
                    System.err.println("Truck problem");
                    e.printStackTrace();
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
            imageView.setFitWidth(30);
            imageView.setFitHeight(30);

            Label label = new Label(Integer.toString(cnt) + "x");
            label.setScaleX(0.7);
            label.setScaleY(0.7);

            Label price = new Label(Integer.toString(Entity.getNewEntity(pair.getKey()).getSellPrice()));

            ImageView so = new ImageView(one);
            so.setFitHeight(30 * 7 / 5);
            so.setFitWidth(25 * 7 / 5);
            Label sellOne = new Label();
            sellOne.setGraphic(so);

            ImageView sa = new ImageView(all);
            sa.setFitHeight(30 * 7 / 5);
            sa.setFitWidth(25 * 7 / 5);
            Label sellAll = new Label();
            sellAll.setGraphic(sa);
            sellAll.setId("label_button_small");
            sellOne.setId("label_button_small");
            sellOne.setStyle("-fx-font-size: 20");
            sellAll.setStyle("-fx-font-size: 20");

            int baseX = numberOfItems / NUM_IN_ROW * DIS_X + BASE_X;
            int baseY = (numberOfItems % NUM_IN_ROW) * DIS_Y + BASE_Y;

            imageView.relocate(baseX, baseY);
            label.relocate(baseX + 30, baseY-5);
            sellOne.relocate(baseX + 130, baseY);
            sellAll.relocate(baseX + 165, baseY);
            ImageView coin = new ImageView(COIN);
            coin.setFitWidth(20);
            coin.setFitHeight(20);
            HBox priceBox = new HBox(price, coin);
            priceBox.setSpacing(5);
            priceBox.relocate(baseX + 50, baseY + 5);
            sellGroup.getChildren().addAll(imageView, label, priceBox, sellAll, sellOne);

            numberOfItems++;

            sellOne.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {

                    if (Game.getInstance().getTruck().getCurrentCapacity() < Entity.getNewEntity(pair.getKey()).getSize()) {
                        Pop pop = new Pop("Not Enough Space", view.getSnap());
                        sellGroup.getChildren().add(pop.getStackPane());
                        pop.getStackPane().setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                sellGroup.getChildren().remove(pop.getStackPane());
                                sellGroup.getChildren().remove(pop.getStackPane());
                            }
                        });

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
                    if (Entity.getNewEntity(pair.getKey()).getSize() == 0) k = cnt;
                    else if (k >= Game.getInstance().getTruck().getCurrentCapacity() / Entity.getNewEntity(pair.getKey()).getSize())
                        k = Game.getInstance().getTruck().getCurrentCapacity() / Entity.getNewEntity(pair.getKey()).getSize();

                    if (k == 0) {
                        Pop pop = new Pop("Not Enough Space", view.getSnap());
                        sellGroup.getChildren().add(pop.getStackPane());
                        pop.getStackPane().setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                sellGroup.getChildren().remove(pop.getStackPane());
                                sellGroup.getChildren().remove(pop.getStackPane());
                            }
                        });
                    } else {
                        Game.getInstance().getTruck().add(pair.getKey(), k);
                        if (truck.get(pair.getKey()) == null) truck.put(pair.getKey(), 0);
                        truck.put(pair.getKey(), truck.get(pair.getKey()) + k);
                        update();
                    }
                }
            });

        }
    }

}
