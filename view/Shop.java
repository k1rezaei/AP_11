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

public class Shop {
    private static final int BASE_X = 30;
    private static final int DIS_X = 260;
    private static final int NUM_IN_ROW = 8;
    private static final int BASE_Y = 70;
    private static final int DIS_Y = 65;
    private static final Image BG = new Image("file:textures/multiplayer/shelf.jpg");
    private static final Image COIN = new Image("file:textures/coin.png");
    private final int WIDTH = 300;
    private final int HEIGHT = 70;
    private int numberOfItems = 0;
    private Client client;
    private View view;
    private Group root = new Group();
    private Group itemsGroup = new Group();
    private HashMap<String, Double> items = new HashMap<>();
    private HashMap<String, Double> prices = new HashMap<>();
    private Label cap;
    private Label money;

    Shop(View view, Client client) {
        this.client = client;
        this.view = view;
        ImageView bg = new ImageView(BG);
        bg.setFitWidth(800);
        bg.setFitHeight(600);
        root.getChildren().add(bg);
        setCapAndMoney();
        Label donate = new Label("donate");
        donate.relocate(BASE_X, 5);
        donate.setId("label_button");
        Label cancel = new Label("BACK");
        cancel.relocate(BASE_X + 300, 5);
        cancel.setId("label_button");
        root.getChildren().addAll(cancel, donate);
        cancel.setOnMouseClicked(event -> {
            view.setRoot(GameView.getInstance().getRoot());
            GameView.getInstance().resume();
        });
        donate.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                for (Map.Entry<String, Integer> pair : Game.getInstance().getWarehouse().getStorables().entrySet()) {
                    for (int i = 0; i < pair.getValue(); i++) {
                        client.sellItem(pair.getKey());
                        Game.getInstance().getWarehouse().remove(pair.getKey());
                    }
                }
                update();
            }
        });
        root.getChildren().add(itemsGroup);
    }

    private void setCapAndMoney() {

        cap = new Label("Capacity : " + Game.getInstance().getWarehouse().getCapacity());
        money = new Label("Money : " + Game.getInstance().getMoney());

        cap.setMinSize(50, HEIGHT);
        cap.setFont(Font.font(20));
        cap.setAlignment(Pos.CENTER);
        cap.relocate(600, 20);

        money.setMinSize(50, HEIGHT);
        money.setFont(Font.font(20));
        money.setAlignment(Pos.CENTER);
        money.relocate(600, 0);

        root.getChildren().add(cap);
        root.getChildren().add(money);


    }

    public void update() {
        numberOfItems = 0;
        itemsGroup.getChildren().clear();
        for (Map.Entry<String, Double> pair : items.entrySet()) {
            show(pair.getKey(), pair.getValue(), prices.get(pair.getKey()));
        }/*
        for (Map.Entry<String, Integer> pair : Game.getInstance().getWarehouse().getStorables().entrySet()){
            System.err.println(pair.getKey());
            System.err.println(pair.getValue());
            System.err.println(prices.get(pair.getKey()));
            System.err.println(pair.getKey());

            show(pair.getKey(), pair.getValue(), prices.get(pair.getKey()), true);
        }*/
        cap.setText("Capacity : " + Game.getInstance().getWarehouse().getCapacity());
        money.setText("Money : " + Game.getInstance().getMoney());
    }

    void show(String type, double doubleCnt, double doubleCost) {
        HBox hBox = new HBox();
        int cnt = (int) (doubleCnt);
        int cost = (int) (doubleCost);
        ImageView imageView = Images.getSpriteAnimation(type).getImageView();
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);

        Label serverCnt = new Label("" + cnt + "x");
        Label clientCnt = new Label();
        Integer cntWarehouse = Game.getInstance().getWarehouse().getStorables().get(type);
        if (cntWarehouse != null && cntWarehouse != 0) {
            clientCnt.setText(cntWarehouse.toString() + "x");
        } else clientCnt.setText("0x");

        HBox priceBox = new HBox();
        Label price = new Label("" + cost);
        ImageView coin = new ImageView(COIN);
        coin.setFitHeight(20);
        coin.setFitWidth(20);
        priceBox.getChildren().addAll(price, coin);
        //priceBox.setSpacing(5);


        Label buy = new Label("buy");
        Label sell = new Label("sell");

        buy.setId("label_button_small");
        buy.setStyle("-fx-font-size: 20");


        sell.setId("label_button_small");
        sell.setStyle("-fx-font-size: 20");

        int baseX = numberOfItems / NUM_IN_ROW * DIS_X + BASE_X;
        int baseY = (numberOfItems % NUM_IN_ROW) * DIS_Y + BASE_Y;

        imageView.relocate(baseX, baseY);
        hBox.relocate(baseX - 30, baseY);
        /* serverCnt.relocate(baseX + 30, baseY);*/

       /* price.relocate(baseX + 75, baseY + 5);
        buy.relocate(baseX + 165, baseY);
        sell.relocate(baseX + 165, baseY);*/
        hBox.setSpacing(10);
        hBox.setMinWidth(230);

        hBox.setStyle("-fx-scale-x: 0.8");
        hBox.getChildren().addAll(imageView, priceBox, serverCnt, clientCnt, buy, sell);

        buy.setOnMouseClicked(event -> {
            if (Game.getInstance().getMoney() < cost) {
                new Pop("Not Enough Money", view.getSnap(), root, Pop.AddType.ALERT);
                return;
            }
            if (Game.getInstance().getWarehouse().getCapacity() < Entity.getNewEntity(type).getSize()) {
                new Pop("Not Enough Space In Warehouse", view.getSnap(), root, Pop.AddType.ALERT);
                return;
            }
            if (serverCnt.getText().equalsIgnoreCase("0")) {
                new Pop("Khak bar Saret Baw :D", view.getSnap(), root, Pop.AddType.ALERT);
                return;
            }
            Game.getInstance().setMoney(Game.getInstance().getMoney() - cost);
            client.buyItem(type);
            Game.getInstance().getWarehouse().add(Entity.getNewEntity(type));
            update();
        });

        sell.relocate(baseX + 130, baseY);
        sell.setOnMouseClicked(event -> {
            System.err.println(clientCnt.getText());
            if (clientCnt.getText().equalsIgnoreCase("0")) {
                new Pop("Nadari Baw :D", view.getSnap(), root, Pop.AddType.ALERT);
            } else {
                client.sellItem(type);
                Game.getInstance().getWarehouse().remove(type);
                Game.getInstance().setMoney(Game.getInstance().getMoney() + cost);
                update();
            }

        });

        if (serverCnt.getText().equals("0x")) {
            buy.setStyle("-fx-text-fill: grey;");
            buy.setOnMouseClicked(null);
        }
        if (clientCnt.getText().equals("0x")) {
            sell.setStyle("-fx-text-fill: grey;");
            sell.setOnMouseClicked(null);
        }
        itemsGroup.getChildren().add(hBox);

        numberOfItems++;

    }

    public void update(HashMap<String, Double> items, HashMap<String, Double> prices) {
        System.err.println("Update Shop");
        this.items = items;
        this.prices = prices;
        update();
    }

    public Group getRoot() {
        update();
        return root;
    }
}