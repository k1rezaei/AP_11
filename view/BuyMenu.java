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
    public static final int BASE_Y_BOUGHT = 80;
    public static final int DIS_Y_BOUGHT = 30;
    public static final int BASE_X_BOUGHT = 600;
    public static final int NUM_OF_BOUGHT = 15;
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
    private final int WIDTH = 50, DIS_X = 270, DIS_Y = 100, NUM_OF_ROW = 5, BASE_X = 50, BASE_Y = 60;
    private final int HEIGHT = 50;

    Group getBuyGroup() {
        numBought = 0;
        init();
        return buyGroup;
    }

    static Image one = new Image("file:textures/sell/one.png");

    Label cap = new Label();

    {
        cap.setMinSize(50,HEIGHT);
        cap.setFont(Font.font(20));
        cap.setAlignment(Pos.CENTER);
        cap.relocate(600,20);
    }

    Label money = new Label();
    {

        money.setMinSize(50,HEIGHT);
        money.setFont(Font.font(20));
        money.setAlignment(Pos.CENTER);
        money.relocate(600,0);
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

        Button ok = new Button("Buy"); ok.relocate(70, 20);
        Button cancel = new Button("Cancel"); cancel.relocate(120, 20);

        buyGroup.getChildren().addAll(ok, cancel);

        cancel.setOnMouseClicked(event -> {
            Game.getInstance().getHelicopter().clear();
            view.setRoot(GameView.getInstance().getRoot());
        });

        ok.setOnMouseClicked(event -> {
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
            view.setRoot(GameView.getInstance().getRoot());
        });

        int numberOfItems = 0;

        for (String type : items) {

            int baseX = BASE_X + numberOfItems / NUM_OF_ROW * DIS_X;
            int baseY = BASE_Y + numberOfItems % NUM_OF_ROW * DIS_Y;

            ImageView imageView = Images.getSpriteAnimation(type).getImageView();
            imageView.setFitHeight(HEIGHT); imageView.setFitWidth(WIDTH);
            imageView.relocate(baseX, baseY);

            Label price = new Label("" + Entity.getNewEntity(type).getBuyPrice() + " (" + Entity.getNewEntity(type).getSize()+ ")");
            price.relocate(baseX + 70, baseY + 10);

            ImageView buyOne = new ImageView(one);
            buyOne.setFitWidth(WIDTH); buyOne.setFitHeight(HEIGHT);

            buyOne.relocate(baseX + 150, baseY);

            buyGroup.getChildren().addAll(imageView, price, buyOne);

            buyOne.setOnMouseClicked(event -> {
                if(Game.getInstance().getHelicopter().getCurrentCapacity() >= Entity.getNewEntity(type).getSize()) {
                    if(Game.getInstance().getHelicopter().getNeededMoney() + Entity.getNewEntity(type).getBuyPrice() <= Game.getInstance().getMoney()) {
                        Game.getInstance().getHelicopter().add(type, 1);
                        currentMoney -= Entity.getNewEntity(type).getBuyPrice();
                        ImageView bought = Images.getSpriteAnimation(type).getImageView();
                        bought.setFitWidth(30); bought.setFitHeight(30);
                        int x = numBought / NUM_OF_BOUGHT * DIS_X_BOUGHT + BASE_X_BOUGHT;
                        int y = numBought % NUM_OF_BOUGHT *  DIS_Y_BOUGHT + BASE_Y_BOUGHT;
                        bought.relocate(x, y);
                        numBought ++;
                        buyGroup.getChildren().add(bought);
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
            });

            numberOfItems ++;

        }
    }

}
