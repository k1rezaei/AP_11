import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.HashMap;
import java.util.Map;

public class SellMenu {
    public static final int BASE_X = 30;
    public static final int DIS_X = 240;
    public static final int NUM_IN_COL = 8;
    public static final int BASE_Y = 60;
    public static final int DIS_Y = 70;
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
    static Image one = new Image("file:textures/sell/one.png");
    static Image all = new Image("file:textures/sell/all.png");


    HashMap<String, Integer> truck = new HashMap<>();


    void update() {
        sellGroup.getChildren().clear();
        Map<String, Integer> storables = Game.getInstance().getWarehouse().getStorables();


        Button ok = new Button("Sell");
        Button cancel = new Button("Cancel");
        ok.relocate(350, 20);
        cancel.relocate(400, 20);


        //ok.setMinSize(50, HEIGHT);
        //cancel.setMinSize(50, HEIGHT);

        sellGroup.getChildren().add(ok);
        sellGroup.getChildren().add(cancel);


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

            ImageView sellOne = new ImageView(one); sellOne.setFitHeight(30); sellOne.setFitWidth(25);
            ImageView sellAll = new ImageView(all); sellAll.setFitHeight(30); sellAll.setFitWidth(25);

            int baseX = numberOfItems / NUM_IN_COL * DIS_X + BASE_X;
            int baseY = (numberOfItems % NUM_IN_COL) * DIS_Y + BASE_Y;

            imageView.relocate(baseX, baseY);
            label.relocate(baseX + 30, baseY);
            price.relocate(baseX + 95, baseY + 10);
            sellOne.relocate(baseX + 130, baseY);
            sellAll.relocate(baseX + 160, baseY);

            sellGroup.getChildren().addAll(imageView, label, price, sellAll, sellOne);

            numberOfItems ++;

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

        }
    }

}
