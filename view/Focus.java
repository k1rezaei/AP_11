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
    Map<Workshop, Boolean> active = new HashMap<>();
    Map<Workshop, Node> workshopNodeMap = new HashMap<>();
    private static Image arrowImage = new Image("file:textures/arrow.png");

    void add(Workshop workshop) {
        if(active.get(workshop) != null && active.get(workshop)) return ;

        int x = GameView.getInstance().getWorkshop(workshop).getX();
        int y = GameView.getInstance().getWorkshop(workshop).getY();

        String cost;
        if(workshop.canUpgrade()) cost = Integer.toString(workshop.getUpgradeCost());
        else cost = "oo";

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

        workshopNodeMap.put(workshop, vBox);

        active.put(workshop, true);
    }

    void remove(Workshop workshop) {
        if(active.get(workshop) == null || !active.get(workshop)) return;
        VBox upgrade = (VBox)workshopNodeMap.remove(workshop);
        GameView.getInstance().getRoot().getChildren().remove(upgrade);
        active.put(workshop, false);
    }



}
