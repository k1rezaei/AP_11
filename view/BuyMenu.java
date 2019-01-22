import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class BuyMenu {
    public static final int BASE_Y_BOUGHT = 80;
    public static final int DIS_Y_BOUGHT = 30;
    public static final int BASE_X_BOUGHT = 600;
    public static final int NUM_OF_BOUGHT = 5;
    public static final int DIS_X_BOUGHT = 30;
    View view;

    int currentMoney;

    {
        currentMoney = Game.getInstance().getMoney();
    }

    int numBought = 0;

    BuyMenu(View view) {
        this.view = view;
    }

    private Group buyGroup = new Group();
    private final int WIDTH = 50, DIS_X = 270, DIS_Y = 95, NUM_OF_ROW = 5, BASE_X = 50, BASE_Y = 80;
    private final int HEIGHT = 50;

    Group getBuyGroup() {
        numBought = 0;
        init();
        return buyGroup;
    }

    static Image one = new Image("file:textures/one.png");

    Label cap = new Label();

    {
        cap.setMinSize(50, HEIGHT);
        cap.setFont(Font.font(20));
        cap.setAlignment(Pos.CENTER);
        cap.relocate(600, 20);
    }

    Label money = new Label();

    {

        money.setMinSize(50, HEIGHT);
        money.setFont(Font.font(20));
        money.setAlignment(Pos.CENTER);
        money.relocate(600, 0);
    }

    void update() {
        cap.setText("Capacity : " + Game.getInstance().getHelicopter().getCurrentCapacity());
        money.setText("Money : " + currentMoney);
    }

    FlowPane buyList = new FlowPane();

    {
        buyGroup.getChildren().add(buyList);
        buyGroup.getChildren().add(money);
        buyGroup.getChildren().add(cap);
        buyList.relocate(400, 70);

        update();
    }

    static private Image BG = new Image("file:textures/bglemon.gif");
    static private Image SHELF = new Image("file:textures/3x5shelf.png");

    void init() {

        ImageView bg = new ImageView(BG);
        bg.setFitHeight(600);
        bg.setFitWidth(800);
        buyGroup.getChildren().add(bg);
        Rectangle rectangle = new Rectangle(BASE_X-10,BASE_Y-10,DIS_X*2-10,DIS_Y*5);
        rectangle.setFill(Color.BLACK);
        rectangle.setOpacity(0.5);

        Rectangle stack = new Rectangle(DIS_X*2-20+BASE_X+10,BASE_Y-10,200,300);
        stack.setFill(Color.BLACK);
        stack.setOpacity(0.5);

        buyGroup.getChildren().add(stack);
        buyGroup.getChildren().add(rectangle);
   /*     {
            ImageView imageView = new ImageView(SHELF);
            imageView.setFitWidth(470);
            imageView.setFitHeight(500);
            imageView.relocate(BASE_X-10, BASE_Y-35);
            buyGroup.getChildren().add(imageView);
        }*/


        ArrayList<String> items = Game.getInstance().getLevel().getItemList();

        Label ok = new Label();
        ok.relocate(BASE_X, 10);
        ImageView okImage = new ImageView(new Image("file:textures/buy.png"));
        okImage.setFitHeight(60);
        okImage.setFitWidth(100);
        ok.setId("label_button");

        ImageView cancelImage = new ImageView(new Image("file:textures/cancel.png"));
        cancelImage.setFitHeight(60);
        cancelImage.setFitWidth(100);

        ok.setGraphic(okImage);
        Label cancel = new Label();
        cancel.relocate(BASE_X + 110, 10);
        cancel.setGraphic(cancelImage);
        cancel.setId("label_button");

        buyGroup.getChildren().addAll(ok, cancel);

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
                } catch (Exception e) {
                    Game.getInstance().getHelicopter().clear();
                }
                GameView.getInstance().resume();
                view.setRoot(GameView.getInstance().getRoot());
            }
        });

        int numberOfItems = 0;

        for (String type : items) {

            int baseX = BASE_X + numberOfItems / NUM_OF_ROW * DIS_X;
            int baseY = BASE_Y + numberOfItems % NUM_OF_ROW * DIS_Y;

            ImageView imageView = Images.getSpriteAnimation(type).getImageView();
            imageView.setFitHeight(HEIGHT);
            imageView.setFitWidth(WIDTH);
            imageView.relocate(baseX, baseY);

            Label price = new Label("" + Entity.getNewEntity(type).getBuyPrice() + " (" + Entity.getNewEntity(type).getSize() + ")");
            price.relocate(baseX + 70, baseY + 10);

            ImageView buyOneImage = new ImageView(one);
            buyOneImage.setFitWidth(WIDTH);
            buyOneImage.setFitHeight(HEIGHT);
            Label buyOne = new Label();
            buyOne.setId("label_button");
            buyOne.relocate(baseX + 150, baseY);
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
                        int x = numBought % NUM_OF_BOUGHT * DIS_X_BOUGHT + BASE_X_BOUGHT;
                        int y = numBought / NUM_OF_BOUGHT * DIS_Y_BOUGHT + BASE_Y_BOUGHT;
                        bought.relocate(x, y);
                        numBought++;
                        buyGroup.getChildren().add(bought);
                        update();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Oooops");
                        alert.setHeaderText(null);
                        alert.setContentText("Not Enough Money");
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Oooops");
                    alert.setHeaderText(null);
                    alert.setContentText("Not Enough Space");
                    alert.showAndWait();
                }
            });

            numberOfItems++;

        }
    }

}
