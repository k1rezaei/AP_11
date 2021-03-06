import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

public class Focus {
    private static final int DIS_X = 50;
    private static final int ITEM_LENGTH = 25;
    private static final int DIS_Y = 10;
    private static final int UPGRADE_LENGTH = 20;
    private static final int CAPACITY_LENGTH = 20;
    private static final int FILL_LENGTH = 20;
    private static final int DO_LENGTH = 20;
    private static Image arrowImage = new Image("file:textures/arrow.png");
    private static Image upgradeImage = new Image("file:textures/upgradeIcon1.png");
    private static Image capacityImage = new Image("file:textures/cap.png");
    private static Image fillImage = new Image("file:textures/water1.png");
    private static Image doImage = new Image("file:textures/do.png");
    private Map<Upgradable, Boolean> active = new HashMap<>();
    private Map<Upgradable, Node> upgradableInfo = new HashMap<>();
    private Group focus = new Group();

    public Group getRoot() {
        return focus;
    }

    void add(Workshop workshop) {
        if (active.get(workshop) != null && active.get(workshop)) return;

        int x = GameView.getInstance().getWorkshop(workshop).getX();
        int y = GameView.getInstance().getWorkshop(workshop).getY();

        VBox vBox = new VBox();

        HBox hBox1 = getUpgradeBox(workshop);

        if (hBox1 != null) vBox.getChildren().add(hBox1);

        HBox hBox2 = new HBox();
        for (Map.Entry<String, Integer> pair : workshop.getInputs().entrySet()) {
            SpriteAnimation sprite = Images.getSpriteAnimation(pair.getKey());
            sprite.getImageView().setFitWidth(ITEM_LENGTH);
            sprite.getImageView().setFitHeight(ITEM_LENGTH);
            hBox2.getChildren().add(sprite.getImageView());
        }
        ImageView arrow = new ImageView(arrowImage);
        arrow.setFitWidth(ITEM_LENGTH);
        arrow.setFitHeight(ITEM_LENGTH);
        hBox2.getChildren().add(arrow);
        SpriteAnimation sprite = Images.getSpriteAnimation(workshop.getOutput());
        sprite.getImageView().setFitWidth(ITEM_LENGTH);
        sprite.getImageView().setFitHeight(ITEM_LENGTH);
        hBox2.getChildren().add(sprite.getImageView());
        vBox.getChildren().add(hBox2);
        HBox hBox3 = combiner(workshop.getStartCost() + "", "gold", doImage, DO_LENGTH);
        vBox.getChildren().add(hBox3);
        vBox.relocate(x + DIS_X, y - DIS_Y);
        vBox.setId("focus");
        focus.getChildren().add(vBox);

        upgradableInfo.put(workshop, vBox);
        active.put(workshop, true);
    }

    void add(Vehicle vehicle) {
        if (active.get(vehicle) != null && active.get(vehicle)) return;

        int x, y;
        if (vehicle.getName().equals("helicopter")) {
            x = GameView.getInstance().getHelicopter().getX() + 100;
            y = GameView.getInstance().getHelicopter().getY() + 50;
        } else {
            x = GameView.getInstance().getTruck().getX();
            y = GameView.getInstance().getTruck().getY();
        }

        VBox vBox = new VBox();
        vBox.setId("focus");

        Label name = new Label(vehicle.getName().substring(0, 1).toUpperCase() + vehicle.getName().substring(1));
        name.setId("name");

        HBox hBox1 = getUpgradeBox(vehicle);
        HBox hBox2 = getCapacityBox(vehicle);

        fix(vBox, name, hBox2, hBox1, x, y);

        active.put(vehicle, true);
        upgradableInfo.put(vehicle, vBox);

    }

    private void fix(VBox vBox, Label label, HBox hBox1, HBox hbox2, int x, int y) {
        vBox.getChildren().addAll(label, hBox1);
        if (hbox2 != null) vBox.getChildren().add(hbox2);
        vBox.relocate(x + DIS_X, y);
        focus.getChildren().add(vBox);
    }

    void add(Well well) {
        if (active.get(well) != null && active.get(well)) return;
        int x = GameView.getInstance().getWell().getX(), y = GameView.getInstance().getWell().getY();

        VBox vBox = new VBox();
        vBox.setId("focus");
        Label name = new Label("Well");
        name.setId("name");

        HBox hBox1 = getUpgradeBox(well);
        HBox hBox2 = getFillBox(well);

        fix(vBox, name, hBox2, hBox1, x, y);

        active.put(well, true);
        upgradableInfo.put(well, vBox);

    }

    void add(Warehouse warehouse) {
        if (active.get(warehouse) != null && active.get(warehouse)) return;
        int x = GameView.getInstance().getWarehouse().getX(), y = GameView.getInstance().getWarehouse().getY();

        VBox vBox = new VBox();
        vBox.setId("focus");
        Label name = new Label("Warehouse");

        HBox hBox1 = getUpgradeBox(warehouse);
        HBox hBox2 = getCapacityBoxForWarehouse(warehouse);
        if (hBox1 == null) vBox.getChildren().addAll(name, hBox2);
        else vBox.getChildren().addAll(name, hBox1, hBox2);
        vBox.relocate(x + DIS_X, y);
        focus.getChildren().add(vBox);

        active.put(warehouse, true);
        upgradableInfo.put(warehouse, vBox);

    }

    void remove(Upgradable u) {
        if (active.get(u) == null || !active.get(u)) return;
        VBox data = (VBox) upgradableInfo.remove(u);
        data.setId("focus");
        //System.err.print(focus.getChildren().size() + " : ");
        focus.getChildren().clear();
        //System.err.println(focus.getChildren().size());
        active.put(u, false);
    }

    private String getCost(Upgradable u) {
        String cost;
        if (u.canUpgrade()) cost = Integer.toString(u.getUpgradeCost());
        else cost = "oo";
        return cost;
    }

    private String getCap(Vehicle vehicle) {
        return Integer.toString(vehicle.getCapacity());
    }

    private HBox getUpgradeBox(Upgradable u) {
        String cost = getCost(u);
        if (cost.equals("oo")) return null;
        return combiner(cost, "gold", upgradeImage, UPGRADE_LENGTH);
    }

    private HBox getCapacityBox(Vehicle vehicle) {
        String cap = getCap(vehicle);
        return combiner(cap, "capacity", capacityImage, CAPACITY_LENGTH);
    }

    private HBox getCapacityBoxForWarehouse(Warehouse warehouse) {
        String cap = Integer.toString(warehouse.getCapacity());
        return combiner(cap, "capacity", capacityImage, CAPACITY_LENGTH);
    }

    private HBox combiner(String str, String id, Image image, int len) {
        HBox hBox = new HBox();
        Label label = new Label(str);
        label.setId(id);
        ImageView img = new ImageView(image);
        img.setFitHeight(len);
        img.setFitWidth(len);
        hBox.getChildren().addAll(label, img);
        return hBox;
    }

    private HBox getFillBox(Well well) {
        String fill = Integer.toString(well.getFillCost());
        return combiner(fill, "gold", fillImage, FILL_LENGTH);
    }

}
