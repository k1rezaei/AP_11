import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

import java.util.HashMap;
import java.util.Map;

public class Shop {
    private Client client;
    private View view;
    private Group root = new Group();
    private Group itemsGroup = new Group();

    private static final int BASE_X = 30;
    private static final int DIS_X = 260;
    private static final int NUM_IN_ROW = 8;
    private static final int BASE_Y = 70;
    private static final int DIS_Y = 65;
    private static final Image BG = new Image("file:textures/multiplayer/shelf.jpg");
    private final int WIDTH = 300;
    private final int HEIGHT = 70;
    HashMap<String, Integer> items = new HashMap<>();
    HashMap<String, Integer> prices = new HashMap<>();
    Label cap;
    Label money;
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

    Shop(View view, Client client) {
        this.client = client;
        this.view = view;



        ImageView bg = new ImageView(BG);
        bg.setFitWidth(800);
        bg.setFitHeight(600);
        root.getChildren().add(bg);
        setCapAndMoney();

        Label cancel = new Label();
        cancel.relocate(BASE_X + 110, 10);
        cancel.setId("label_button");
        cancel.setText("CANCEL");
        root.getChildren().add(cancel);
        cancel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                GameView.getInstance().resume();
                view.setRoot(GameView.getInstance().getRoot());
            }
        });
        root.getChildren().add(itemsGroup);
    }

    public void update(){
        numberOfItems = 0;
        itemsGroup.getChildren().clear();
        for (Map.Entry<String, Integer> pair : items.entrySet()) {
            show(pair.getKey(), pair.getValue(), prices.get(pair.getKey()), false);
        }
        for (Map.Entry<String, Integer> pair : Game.getInstance().getWarehouse().getStorables().entrySet()){
            show(pair.getKey(), pair.getValue(), prices.get(pair.getKey()), true);
        }
        cap.setText("Capacity : " + Game.getInstance().getWarehouse().getCapacity());
        money.setText("Money : " + Game.getInstance().getMoney());
    }

    int numberOfItems = 0;

    void show(String type, int cnt, int cost, boolean forSell){

        ImageView imageView = Images.getSpriteAnimation(type).getImageView();
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);

        Label label = new Label("" + cnt);

        Label price = new Label("" + cost);


        Label buy = new Label("buy");
        Label sell = new Label("sell");

        buy.setId("label_button_small");
        buy.setStyle("-fx-font-size: 20");


        sell.setId("label_button_small");
        sell.setStyle("-fx-font-size: 20");

        int baseX = numberOfItems / NUM_IN_ROW * DIS_X + BASE_X;
        int baseY = (numberOfItems % NUM_IN_ROW) * DIS_Y + BASE_Y;

        imageView.relocate(baseX, baseY);
        label.relocate(baseX + 30, baseY);
        price.relocate(baseX + 75, baseY + 5);
        buy.relocate(baseX + 165, baseY);
        sell.relocate(baseX + 165, baseY);

        itemsGroup.getChildren().addAll(imageView, label, price);
        if(!forSell) {
            itemsGroup.getChildren().add(buy);
        }else{
            itemsGroup.getChildren().add(sell);
        }
        numberOfItems++;
        buy.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (Game.getInstance().getMoney() < cost) {
                    new Pop("Not Enough Money", view.getSnap(), root);
                } else {

                }
            }
        });

        sell.relocate(baseX + 130, baseY);
        sell.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (Game.getInstance().getWarehouse().getCapacity() < Entity.getNewEntity(type).getSize()) {
                    new Pop("Not Enough Space In Warehouse", view.getSnap(), root);
                } else {

                }

            }
        });
    }

    public void update(HashMap<String, Integer> items, HashMap<String, Integer> prices) {

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