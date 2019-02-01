import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

public class BuyMenu {
    private static final int BASE_Y_BOUGHT = 80;
    private static final int DIS_Y_BOUGHT = 30;
    private static final int BASE_X_BOUGHT = 600;
    private static final int NUM_OF_BOUGHT = 5;
    private static final int DIS_X_BOUGHT = 30;
    private static Image one = new Image("file:textures/one.png");
    private static Image BG = new Image("file:textures/bglemon.gif");
    private static Image COIN = new Image("file:textures/coin.png");
    private static Image BOX = new Image("file:textures/cap.png");
    private final int WIDTH = 50, DIS_X = 270, DIS_Y = 95, NUM_OF_ROW = 5, BASE_X = 50, BASE_Y = 80;
    private final int HEIGHT = 50;
    private View view;
    private int currentMoney = Game.getInstance().getMoney();
    private int numBought = 0;
    private Label cap = new Label();
    private Label money = new Label();
    private Group buyGroup = new Group();

    {
        cap.setMinSize(50, HEIGHT);
        cap.setAlignment(Pos.CENTER);
        cap.relocate(600, 20);
    }

    {

        money.setMinSize(50, HEIGHT);
        money.setAlignment(Pos.CENTER);
        money.relocate(600, 0);
    }

    BuyMenu(View view) {
        this.view = view;
    }

    Group getBuyGroup() {
        numBought = 0;
        init();
        return buyGroup;
    }

    void update() {
        cap.setText("Capacity : " + Game.getInstance().getHelicopter().getCurrentCapacity());
        money.setText("Money : " + currentMoney);
    }

    void init() {

        ImageView bg = new ImageView(BG);
        bg.setFitHeight(600);
        bg.setFitWidth(800);
        buyGroup.getChildren().add(bg);
        buyGroup.getChildren().add(money);
        buyGroup.getChildren().add(cap);
        update();
        Label rectangle = new Label();

        rectangle.relocate(BASE_X - 10, BASE_Y - 10);
        rectangle.setMinSize(DIS_X * 2 - 10, DIS_Y * 5);
        //rectangle.setFill(Color.BLACK);
        // rectangle.setOpacity(0.5);
        //rectangle.setFill(Color.TRANSPARENT);
        rectangle.setId("recBG");

        Label stack = new Label();
        stack.relocate(DIS_X * 2 - 20 + BASE_X + 10, BASE_Y - 10);
        stack.setMinSize(200, 350);
        stack.setId("recBG");


        //stack.setFill(Color.BLACK);
        // stack.setOpacity(0.5);

        buyGroup.getChildren().add(stack);
        buyGroup.getChildren().add(rectangle);


        ArrayList<String> items = Game.getInstance().getLevel().getItemList();


        Label ok = new Label("OK");
        ok.relocate(BASE_X, 5);
        ImageView okImage = new ImageView(new Image("file:textures/buy.png"));
        okImage.setFitHeight(60);
        okImage.setFitWidth(100);
        ok.setId("label_button");

        ImageView cancelImage = new ImageView(new Image("file:textures/cancel.png"));
        cancelImage.setFitHeight(60);
        cancelImage.setFitWidth(100);

        //ok.setGraphic(okImage);
        Label cancel = new Label("CANCEL");
        cancel.relocate(BASE_X + 110, 5);
        //cancel.setGraphic(cancelImage);
        cancel.setId("label_button");

      /*  Label clear = new Label();
        clear.relocate(BASE_X + 220, 10 );
        ImageView clearImage = new ImageView(new Image("file:textures/clear.png"));
        clearImage.setFitHeight(60);
        clearImage.setFitWidth(100);
        clear.setGraphic(cancelImage);
        clear.setId("label_button");*/

        buyGroup.getChildren().addAll(ok, cancel);

        cancel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Game.getInstance().getHelicopter().clear();
                GameView.getInstance().resume();
                view.setRoot(GameView.getInstance().getRoot());
            }
        });

        ok.setOnMouseClicked(event -> {
            try {
                Game.getInstance().go(Game.getInstance().getHelicopter());
            } catch (Exception e) {
                Game.getInstance().getHelicopter().clear();
            }
            GameView.getInstance().resume();
            view.setRoot(GameView.getInstance().getRoot());
        });

      /*  clear.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

            }
        });*/

        int numberOfItems = 0;

        for (String type : items) {

            int baseX = BASE_X + numberOfItems / NUM_OF_ROW * DIS_X;
            int baseY = BASE_Y + numberOfItems % NUM_OF_ROW * DIS_Y;

            ImageView imageView = Images.getSpriteAnimation(type).getImageView();
            imageView.setFitHeight(HEIGHT);
            imageView.setFitWidth(WIDTH);
            imageView.relocate(baseX, baseY);

            Label priceLabel = new Label("" + Entity.getNewEntity(type).getBuyPrice());
            //priceLabel.relocate(baseX + 70, baseY + 10);
            ImageView coin = new ImageView(COIN);
            coin.setFitWidth(20);
            coin.setFitHeight(20);
            Label size = new Label(Entity.getNewEntity(type).getSize() + "");
            ImageView box = new ImageView(BOX);
            box.setFitHeight(20);
            box.setFitWidth(20);

            HBox price = new HBox(priceLabel, coin, size, box);
            price.relocate(baseX + 70, baseY + 10);
            price.setSpacing(5);


            ImageView buyOneImage = new ImageView(one);
            buyOneImage.setFitWidth(WIDTH);
            buyOneImage.setFitHeight(HEIGHT);
            Label buyOne = new Label();
            buyOne.setId("label_button_small");
            buyOne.relocate(baseX + 190, baseY);
            buyOne.setGraphic(buyOneImage);

            buyGroup.getChildren().addAll(imageView, price, buyOne);


            buyOne.setOnMouseClicked(event -> {
                if (Game.getInstance().getHelicopter().getCurrentCapacity() >= Entity.getNewEntity(type).getSize()) {
                    if (Game.getInstance().getHelicopter().getNeededMoney() + Entity.getNewEntity(type).getBuyPrice() <= Game.getInstance().getMoney()) {
                        Game.getInstance().getHelicopter().add(type, 1);
                        currentMoney -= Entity.getNewEntity(type).getBuyPrice();
                        ImageView bought = Images.getSpriteAnimation(type).getImageView();
                        bought.setFitWidth(30);
                        bought.setFitHeight(30);
                        // TODO flowPane
                        int x = numBought % NUM_OF_BOUGHT * DIS_X_BOUGHT + BASE_X_BOUGHT;
                        int y = numBought / NUM_OF_BOUGHT * DIS_Y_BOUGHT + BASE_Y_BOUGHT;
                        bought.relocate(x, y);
                        numBought++;
                        buyGroup.getChildren().add(bought);
                        update();
                    } else {
                        Pop pop = new Pop("Not Enough Money", view.getSnap());
                        buyGroup.getChildren().add(pop.getStackPane());
                        pop.getStackPane().setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                buyGroup.getChildren().remove(pop.getStackPane());
                            }
                        });
                    }
                } else {
                    Pop pop = new Pop("Not Enough Space", view.getSnap());
                    buyGroup.getChildren().add(pop.getStackPane());
                    pop.getStackPane().setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            buyGroup.getChildren().remove(pop.getStackPane());
                        }
                    });
                }
            });

            numberOfItems++;

        }
    }

}
