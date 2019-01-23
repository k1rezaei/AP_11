import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.HashMap;
import java.util.Map;

public class Focus {
    private static final int DIS_X = 50;
    private static final int ITEM_LENGTH = 25;
    public static final int DIS_Y = 10;
    Map<Upgradable, Boolean> active = new HashMap<>();
    Map<Upgradable, Node> UpgradableInfo = new HashMap<>();
    private static Image arrowImage = new Image("file:textures/arrow.png");

    void add(Workshop workshop) {
        if(active.get(workshop) != null && active.get(workshop)) return ;

        int x = GameView.getInstance().getWorkshop(workshop).getX();
        int y = GameView.getInstance().getWorkshop(workshop).getY();

        String cost = getCost(workshop);

        VBox vBox = new VBox();

        Label upgrade = new Label(cost); upgrade.setFont(Font.font(2));
        HBox hBox1 = new HBox();
        hBox1.getChildren().add(upgrade);
        vBox.getChildren().add(hBox1);

        HBox hBox2 = new HBox();
        for (Map.Entry<String, Integer> pair : workshop.getInputs().entrySet()) {
            SpriteAnimation sprite = Images.getSpriteAnimation(pair.getKey());
            sprite.getImageView().setFitWidth(ITEM_LENGTH);
            sprite.getImageView().setFitHeight(ITEM_LENGTH);
            hBox2.getChildren().add(sprite.getImageView());
        }
        ImageView arrow = new ImageView(arrowImage);
        arrow.setFitWidth(ITEM_LENGTH); arrow.setFitHeight(ITEM_LENGTH);
        hBox2.getChildren().add(arrow);
        SpriteAnimation sprite = Images.getSpriteAnimation(workshop.getOutput());
        sprite.getImageView().setFitWidth(ITEM_LENGTH);
        sprite.getImageView().setFitHeight(ITEM_LENGTH);
        hBox2.getChildren().add(sprite.getImageView());
        vBox.getChildren().add(hBox2);
        vBox.relocate(x + DIS_X, y - DIS_Y);
        GameView.getInstance().getRoot().getChildren().add(vBox);

        UpgradableInfo.put(workshop, vBox);
        active.put(workshop, true);
    }

    void add(Vehicle vehicle) {
        if(active.get(vehicle) != null && active.get(vehicle)) return ;

        int x, y;
        if(vehicle.getName().equals("helicopter")) {
            x = GameView.getInstance().getHelicopter().getX();
            y = GameView.getInstance().getHelicopter().getY();
        }else {
            x = GameView.getInstance().getTruck().getX();
            y = GameView.getInstance().getTruck().getY();
        }

        String cost = getCost(vehicle);
        String cap = getCap(vehicle);

        VBox vBox = new VBox();
        Label upgrade = new Label(cost);
        Label capacity = new Label(cap);

        vBox.getChildren().add(upgrade);
        vBox.getChildren().add(capacity);
        vBox.relocate(x + DIS_X, y);
        GameView.getInstance().getRoot().getChildren().add(vBox);

        active.put(vehicle, true);
        UpgradableInfo.put(vehicle, vBox);

    }

    void remove(Upgradable u) {
        if(active.get(u) == null || !active.get(u)) return;
        VBox data = (VBox) UpgradableInfo.remove(u);
        GameView.getInstance().getRoot().getChildren().remove(data);
        active.put(u, false);
    }

    String getCost(Upgradable u) {
        String cost;
        if(u.canUpgrade()) cost = Integer.toString(u.getUpgradeCost());
        else cost = "oo";
        return  cost;
    }

    String getCap(Vehicle vehicle) {
        return Integer.toString(vehicle.getCapacity());
    }

}
