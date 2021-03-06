import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class GameView {
    public static final int BASE_X = 260;
    public static final int BASE_Y = 210;
    public static final String FINISH = "finish";
    public static final Image BOX = new Image("file:textures/box.png");
    public static final int REWARD_MULTIPLIER = 3000;
    private static final int INFO_LENGTH = 20;
    private static final int ONE_SECOND = 1000 * 1000 * 1000;
    private static final GameView gameView = new GameView();
    private static final int WELL_X = 360;
    private static final int WELL_Y = 20;
    private static final int TRUCK_X = 200;
    private static final int TRUCK_Y = 460;
    private static final int HELICOPTER_X = 520;
    private static final int HELICOPTER_Y = 430;
    private static final int LEFT_WORKSHOP_X = 50;
    private static final int RIGHT_WORKSHOP_X = 620;
    private static final int BASE_WORKSHOP = 80;
    private static final int WORKSHOP_DIS = 150;
    private static final int BUY_ANIMAL_Y = 20;
    private static final int BUY_ANIMAL_BASE_X = 20;
    private static final int BUY_ANIMAL_X_DIFF = 60;
    private static final int GOALS_WIDTH = 70;
    private static final int GOALS_HEIGHT = 50;
    private static final int GOALS_X = 640;
    private static final int GOALS_Y = 560;
    private static final int FF_HEIGHT = 50;
    private static final int FF_WIDTH = 100;
    private static final int FF_X = 360;
    private static final int FF_Y = 15;
    private static final int SAVE_X = 450;
    private static final int SAVE_Y = 30;
    private static final int EXIT_X = 10;
    private static final int EXIT_Y = 550;
    private static final int MONEY_X = 600;
    private static final int MONEY_Y = 35;
    private static final int MENU_X = 75;
    private static final int MENU_Y = 550;
    private static final int MENU_WIDTH = 100;
    private static final int MENU_HEIGHT = 50;
    private static final int WAREHOUSE_X = 360;
    private static final int WAREHOUSE_Y = 460;
    private static final int WAREHOUSE_CNT_X = 7;
    private static final int WAREHOUSE_CNT_Y = 5;
    private static final double SOUND_PROP = 0.01;
    private static final int GOAL_ICONS_LENGTH = 70;
    private static final String[] NON_WILD = {"chicken", "sheep", "cow", "dog", "cat"};
    private static final double EPS = 0.0001;
    private static final Rectangle REFRESHER = new Rectangle(0, 0, 1000, 1000);
    private static final int VEHICLE_MINI_X = 600;
    private static final int ROAD_X = 520;
    private static final int HELICOPTER_MINI_Y = 10;
    private static final int ROAD_Y = 10;
    private static final int TRUCK_MINI_Y = 35;
    private static final int VEHICLE_MINI_TRAVEL = 130;
    private static final int CHAT_X = 150;
    private static final int CHAT_Y = 550;
    private static final String LABEL_BUTTON = "label_button";
    private static final float ITEM_FADE_TIME = 100;
    private static final Image info = new Image("file:textures/info.png");
    private static final Image END_GIF = new Image("file:textures/end3.gif");
    private static final Image COIN_GIF = new Image("file:textures/coin3.gif");
    private static final Image pashImage = new Image("file:textures/mosquito.gif");
    private static final Image ROAD = new Image("file:textures/road.png");
    private static final Image FF1 = new Image("file:textures/fastForward/fastForward1.png");
    private static final Image FF2 = new Image("file:textures/fastForward/fastForward2.png");
    private static final Image BACK = new Image("file:textures/back.png");
    private static final Image UPGRADE_ICON = new Image("file:textures/upgradeIcon1.png");
    private static final String SMALL_BUTTON_ID = "label_button_small";

    static {
        REFRESHER.setVisible(false);
    }

    private ImageView pashe = null;
    private int pasheX;
    private int pasheY;
    private int pasheDisX;
    private int pasheDisY;
    private ArrayList<SpriteAnimation> deadSprites = new ArrayList<>();
    private double speed = 1;
    private boolean paused = false;
    private AnimationTimer game;
    private Label truckInfo;
    private Label helicopterInfo, warehouseInfo;
    private FlowPane stored = new FlowPane();
    private Group root = new Group();
    private HashMap<Entity, SpriteAnimation> sprites = new HashMap<>();
    private Group entityRoot = new Group();
    private Group infoRoot = new Group();
    private View view;
    private Label filled = new Label();
    private HashMap<Workshop, SpriteAnimation> workshops = new HashMap<>();
    private SpriteAnimation well;
    private SpriteAnimation warehouse;
    private SpriteAnimation truck;
    private SpriteAnimation helicopter;
    private SpriteAnimation truckMini;
    private SpriteAnimation helicopterMini;
    private Label moneyLabel;
    private Focus focus;
    private Label fastForward;
    private Label save;
    private Client client;
    private Label clock;
    private VBox catInfoBox;
    private VBox goalsVBox;
    private ImageView[] buyAnimalIcons = new ImageView[5];

    private GameView() {
    }

    public static GameView getInstance() {
        return gameView;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void pause() {
        paused = true;
        game.stop();
    }

    public void resume() {
        paused = false;
        game.start();
    }


    void pash() {
        if (pashe != null) {
            if (pasheX != pasheDisX) pasheX += (pasheDisX - pasheX) / Math.abs(pasheDisX - pasheX);
            if (pasheY != pasheDisY) pasheY += (pasheDisY - pasheY) / Math.abs(pasheDisY - pasheY);
            pashe.relocate(pasheX, pasheY);
            if (pasheX == pasheDisX && pasheY == pasheDisY) {
                root.getChildren().remove(pashe);
                pashe = null;
            }
        }
        if (Math.random() * 100 <= 1 && pashe == null) {
            pashe = new ImageView(pashImage);
            pashe.setFitHeight(20);
            pashe.setFitWidth(20);
            root.getChildren().add(pashe);
            pasheX = (int) (Math.random() * 800);
            pasheY = (int) (Math.random() * 600);
            pasheDisX = (int) (Math.random() * 800);
            if (pasheX < pasheDisX) {
                int temp = pasheDisX;
                pasheDisX = pasheX;
                pasheX = temp;
            }
            pasheDisY = (int) (Math.random() * 600);
            pashe.relocate(pasheX, pasheY);

            System.err.println("pashe oomad");
        }
    }

    String getXX(long t) {
        if (t < 10) return "0" + t;
        else return "" + t;
    }

    void updateClock(long second) {
        clock.setText(getXX(second / 60) + ":" + getXX(second % 60));
    }

    public void runGame() {
        initializeGame();

        game = new AnimationTimer() {
            private static final int SECOND = 1000000000;
            private long lastTime;
            private long startTime;

            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    startTime = now;
                }
                if (now > lastTime + SECOND / (48 * speed)) {
                    pash();
                    lastTime = now;
                    catInfoBox.getChildren().clear();
                    catInfoBox.getChildren().addAll(getBuyAnimalFocus("cat").getChildren());
                    handleOverlaps();
                    updateWarehouse();
                    refreshScreen();
                    updateTruck();
                    updateHelicopter();
                    Game.getInstance().turn();
                    renderEntities();
                    stopWorkshops();
                    updateClock(Game.getInstance().getCurrentTurn() / 40);
                    updateWellFilledBar();
                    for (int i = 0; i < 5; i++) {
                        if (Game.getInstance().getMoney() < Entity.getNewEntity(NON_WILD[i]).getBuyPrice()) {
                            applyGrayscale(buyAnimalIcons[i]);
                            buyAnimalIcons[i].setId(null);
                        } else {
                            buyAnimalIcons[i].setEffect(null);
                            buyAnimalIcons[i].setId("glow");
                        }
                    }
                    moneyLabel.setText(Integer.toString(Game.getInstance().getMoney()));
                    if (Game.getInstance().checkLevel()) endGame();
                }
            }
        };
        resume();
        game.start();
    }

    private void endGame() {
        pause();
        if (client != null && client.isInTeamGame()) {
            HashMap<String, Integer> goals = Game.getInstance().getLevel().getGoalEntity();
            for (Map.Entry<String, Integer> goal : goals.entrySet()) {
                for (int i = 0; i < goal.getValue(); i++) {
                    client.sendForServerMultiPlayer(goal.getKey());
                }
            }
        }
        if(view.BUG_KHORD) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        root.getChildren().clear();
        ImageView imageView = new ImageView(END_GIF);
        imageView.setFitHeight(600);
        imageView.setFitWidth(800);
        root.getChildren().add(imageView);
        Label finish = new Label("You Win :D");
        finish.translateXProperty().bind(finish.widthProperty().divide(2).negate());
        finish.translateYProperty().bind(finish.heightProperty().divide(2).negate());
        finish.setId(FINISH);
        finish.relocate(400, 300);
        TextAnimation textAnimation = new TextAnimation(finish);
        textAnimation.setFrom(0, 255, 0);
        textAnimation.setTo(255, 0, 255);
        textAnimation.setBlinkTime(100);
        textAnimation.play();
        root.getChildren().add(finish);
        saveLevel();
        AnimationTimer animationTimer = new AnimationTimer() {
            long last = -1;
            int cnt = 0;

            @Override
            public void handle(long now) {
                if (last == -1) last = now;
                if (now - last > ONE_SECOND || last == -1) {
                    cnt++;
                    last = now;
                    if (finish.getId().equals("finish")) finish.setId("finish2");
                    else finish.setId("finish");
                    if (cnt == 5) {
                        Menu menu = new Menu(view);
                        //TODO fix Back to MENU.
                        if (client != null) client.updateScoreboard(new LevelSelect(view).getLevel());
                        System.err.println(new LevelSelect(view).getLevel());
                        if (client == null) view.setRoot(menu.getRoot());
                        else {
                            client.setInGame(false);
                            int reward = Math.max(Game.getInstance().getLevel().getGoalMoney() * REWARD_MULTIPLIER
                                    / Game.getInstance().getCurrentTurn(), 50);
                            client.setMoney(client.getMoney() + reward);
                            view.setRoot(client.getMultiPlayerMenu().getRoot());
                        }
                        game.stop();
                        this.stop();
                    }
                }
            }
        };
        animationTimer.start();
    }

    private void saveLevel() {
        System.out.println(Game.getInstance().getLevel().getGoalMoney());
        for (String name : Game.getLevels().keySet()) {
            if (Game.getLevels().get(name).equals(Game.getInstance().getLevel())) {
                try {
                    FileWriter fw = new FileWriter("Levels", true);
                    fw.write(name.substring("level".length()) + "\n");
                    fw.close();
                } catch (Exception ignored) {
                }
                return;
            }
        }
    }


    private void initializeGame() {
        if (client != null) client.setInGame(true);
        root = new Group();
        entityRoot = new Group();
        infoRoot = new Group();
        focus = new Focus();
        workshops.clear();
        sprites.clear();
        deadSprites.clear();
        initializeNodes();
    }


    private void updateWellFilledBar() {
        int h = (int) (70 * (1.0 * Game.getInstance().getWell().getCurrentAmount()
                / Game.getInstance().getWell().getCapacity()));

        filled.setMaxHeight(h);
        filled.setMinHeight(h);

        filled.relocate(WELL_X, WELL_Y + 50 + 70 - h);
    }

    private void stopWorkshops() {
        for (Workshop workshop : Game.getInstance().getWorkshops()) {

            if (workshop.getRemainTime() == 0) {
                getWorkshop(workshop).shutDown();
            } else if (workshop.getRemainTime() == -1) {
                getWorkshop(workshop).getProgressBar(0);
            } else getWorkshop(workshop).getProgressBar(1 - (double) workshop.getRemainTime() / workshop.getDuration());
        }
    }

    private void renderEntities() {
        for (Entity entity : Game.getInstance().getMap().getEntities())
            if (entity.getCell() != null) {
                if (entity instanceof Animal && (Math.random() < SOUND_PROP))
                    if (view.getMute() == false) Sounds.play(entity.getType() + "_voice");
                if (!sprites.containsKey(entity)) addSprite(entity);
                renderSprite(entity);
            } else if (sprites.containsKey(entity)) killSprite(entity);
        removeFinishedDead();
        Game.getInstance().getMap().relax();
    }

    private void refreshScreen() {
        root.getChildren().add(REFRESHER);
        root.getChildren().remove(REFRESHER);
    }

    private void handleOverlaps() {
        entityRoot.toFront();
        fastForward.toFront();
        save.toFront();
        infoRoot.toFront();
        focus.getRoot().toFront();
    }

    public FlowPane getStored() {
        return stored;
    }

    public void updateTruck() {
        int remainingTime = Game.getInstance().getTruck().getRemainingTime();
        int goTime = Game.getInstance().getTruck().getGoTime();
        setUpVehicleMini(remainingTime, goTime, truckMini, truck);
        truck.getImageView().setVisible(remainingTime == 0);
        truckInfo.setVisible(Game.getInstance().getTruck().getRemainingTime() == 0);
        /*int rem = Game.getInstance().getTruck().getRemainingTime();
        int tim = Game.getInstance().getTruck().getGoTime();
        if (rem == 0)
            truck.setOnMouseClicked(EventHandlers.getOnMouseClickedEventHandler(Game.getInstance().getTruck()));
        else
            truck.setOnMouseClicked(null);

        truck.getImageView().setOpacity(canv(1 - (double) rem / tim));*/

    }

    public void updateHelicopter() {
        int remainingTime = Game.getInstance().getHelicopter().getRemainingTime();
        int goTime = Game.getInstance().getHelicopter().getGoTime();
        setUpVehicleMini(remainingTime, goTime, helicopterMini, helicopter);
        helicopter.getImageView().setVisible(remainingTime == 0);
        helicopterInfo.setVisible(Game.getInstance().getHelicopter().getRemainingTime() == 0);
        /*int rem = Game.getInstance().getHelicopter().getRemainingTime();
        int tim = Game.getInstance().getHelicopter().getGoTime();
        if (rem == 0)
            helicopter.setOnMouseClicked(EventHandlers.getOnMouseClickedEventHandler(Game.getInstance().getHelicopter()));
        else
            helicopter.setOnMouseClicked(null);

        helicopter.getImageView().setOpacity(canv(1 - (double) rem / tim));*/
    }

    private void setUpVehicleMini(int remainingTime, int goTime, SpriteAnimation vehicleMini, SpriteAnimation vehicle) {
        if (vehicleMini.getState() != vehicle.getState()) {
            root.getChildren().remove(vehicleMini.getImageView());
            vehicleMini.setState(vehicle.getState());
            if (remainingTime != 0) root.getChildren().add(vehicleMini.getImageView());
        }
        if (remainingTime != 0) {
            if (!root.getChildren().contains(vehicleMini.getImageView())) {
                root.getChildren().add(vehicleMini.getImageView());
                vehicleMini.play();
            }
            if (2 * remainingTime > goTime) vehicleMini.getImageView().setScaleX(-1);
            else vehicleMini.getImageView().setScaleX(1);
            vehicleMini.getImageView().setX(VEHICLE_MINI_X + VEHICLE_MINI_TRAVEL -
                    VEHICLE_MINI_TRAVEL * Math.abs(2 * remainingTime - goTime) / (2.0 * goTime));
        } else {
            root.getChildren().remove(vehicleMini.getImageView());
            vehicleMini.stop();
        }
    }

    public void setUpDead(Entity entity) {
        if (entity instanceof Animal && view.getMute() == false) Sounds.play(entity.getType() + "_die");
        if ((entity instanceof Animal) && !(entity instanceof WildAnimal)) {
            SpriteAnimation spriteAnimation = Images.getSpriteAnimation(entity.getType());
            spriteAnimation.setState(4);
            fixSprite(spriteAnimation, entity.getDeadCell().getX() + BASE_X - spriteAnimation.getWidth() / 2,
                    entity.getDeadCell().getY() + BASE_Y - spriteAnimation.getHeight() / 2);
            spriteAnimation.setCycleCount(1);
            spriteAnimation.play();
            deadSprites.add(spriteAnimation);
        }
    }

    public void removeFinishedDead() {
        for (int i = deadSprites.size() - 1; i >= 0; i--) {
            if (deadSprites.get(i).getLastIndex() == 23) {
                root.getChildren().remove(deadSprites.get(i).getImageView());
                deadSprites.remove(i);
            }
        }
    }

    private void killSprite(Entity entity) {
        setUpDead(entity);
        SpriteAnimation sprite = sprites.get(entity);
        sprite.stop();
        sprite.getImageView().setVisible(false);
        entityRoot.getChildren().remove(sprite.getImageView());
        entityRoot.getChildren().remove(sprite.getProgressBar(0));
        sprites.remove(entity);
    }

    private void renderSprite(Entity entity) {

        SpriteAnimation sprite = sprites.get(entity);
        if (entity instanceof FarmAnimal) {
            int x = entity.getCell().getX() + BASE_X - 25;
            int y = entity.getCell().getY() + BASE_Y + sprite.getHeight() / 2;
            sprite.getProgressBar(((FarmAnimal) entity).getHunger()).relocate(x, y);

        }
        if (sprite.getState() != entity.getState()) {
            entityRoot.getChildren().remove(sprite.getImageView());
            sprite.setState(entity.getState());
            entityRoot.getChildren().add(sprite.getImageView());
        }
        sprite.getImageView().setTranslateX(-sprite.getWidth() / 2);
        sprite.getImageView().setTranslateY(-sprite.getHeight() / 2);
        sprite.getImageView().relocate(BASE_X + entity.getCell().getX(), BASE_Y + entity.getCell().getY());
        if (entity instanceof Item) {
            int remainTime = ((Item) entity).getRemainTime();
            sprite.getImageView().setOpacity(Math.min(1.0, remainTime / ITEM_FADE_TIME));
        }
    }

    private void addSprite(Entity entity) {
        SpriteAnimation newSprite = Images.getSpriteAnimation(entity);
        sprites.put(entity, newSprite);
        newSprite.setOnMouseClicked(EventHandlers.getOnMouseClickedEventHandler(entity));
        newSprite.play();
        entityRoot.getChildren().add(newSprite.getImageView());
        if (entity instanceof FarmAnimal) {
            entityRoot.getChildren().add(newSprite.getProgressBar(0));
        }
        if (entity.getType().equalsIgnoreCase("plant")) {
            ImageView plant = newSprite.getImageView();
            plant.toBack();
            plant.setOnMouseClicked(EventHandlers.getOnMouseClickedPlant(
                    entity.getCell().getX() + BASE_X - newSprite.getWidth() / 2,
                    entity.getCell().getY() + BASE_Y - newSprite.getHeight() / 2));
        }
    }

    public Focus getFocus() {
        return focus;
    }

    private void initializeNodes() {
        setUpBackground();
        setUpClock();
        setUpRoadImage();
        setUpBuyIcons();
        setUpMoneyLabel();
        setUpWell();
        setUpTruck();
        setUpWarehouse();
        setUpWorkshops();
        setUpHelicopter();
        setUpSaveButton();
        setUpFastForward();
        setUpExitButton();
        setUpGoals();
        setUpHelicopterMini();
        setUpTruckMini();
        setUpMenuButton();
        setUpStructrues();
        setUpChatButton();
        root.getChildren().add(entityRoot);
        root.getChildren().add(infoRoot);
        root.getChildren().add(focus.getRoot());
    }

    private void setUpChatButton() {
        if (client != null && client.isInTeamGame()) {
            Label chat = new Label("CHAT");
            chat.setId(SMALL_BUTTON_ID);
            chat.setOnMouseClicked(mouseEvent -> view.setRoot(client.getInGameChat().getRoot()));
            chat.relocate(CHAT_X, CHAT_Y);
            root.getChildren().add(chat);
        }
    }

    private void setStyle(SpriteAnimation spriteAnimation) {
        for (ImageView imageView : spriteAnimation.getImageViews()) {
            imageView.setId("glow");
        }
    }

    private void setUpStructrues() {
        setStyle(warehouse);
        setStyle(well);
        setStyle(truck);
        setStyle(helicopter);
        for (Map.Entry<Workshop, SpriteAnimation> entry : workshops.entrySet()) setStyle(entry.getValue());

    }

    private void setUpRoadImage() {
        ImageView imageView = new ImageView(ROAD);
        imageView.relocate(ROAD_X, ROAD_Y);
        imageView.setScaleX(0.8);
        imageView.setScaleY(0.8);
        root.getChildren().add(imageView);
    }

    private void setUpTruckMini() {
        truckMini = Images.getSpriteAnimation("truckMini");
        for (ImageView imageView : truckMini.getImageViews()) {
            imageView.setY(TRUCK_MINI_Y);
        }
    }

    private void setUpHelicopterMini() {
        helicopterMini = Images.getSpriteAnimation("helicopterMini");
        for (ImageView imageView : helicopterMini.getImageViews()) {
            imageView.setY(HELICOPTER_MINI_Y);
        }
    }

    private void setUpGoals() {
        Level level = Game.getInstance().getLevel();
        Label goals = new Label("GOALS");
        goals.setId("label_button_small");
       /* ImageView goal = new ImageView(new Image("file:textures/goals.png"));
        goal.setFitWidth(GOALS_WIDTH);
        goal.setFitHeight(GOALS_HEIGHT);*/
        //goals.setGraphic(goal);
        goals.relocate(GOALS_X, GOALS_Y);
        root.getChildren().add(goals);
        goalsVBox = new VBox();
        goals.setOnMouseClicked(event -> {
            refreshGoals(level.toString());
            pop(goalsVBox);
        });
    }

    public void refreshGoals(String data) {
        Scanner scanner = new Scanner(data);
        goalsVBox.getChildren().clear();
        int cnt = 0;
        while (scanner.hasNext()) {
            cnt++;
            String line = scanner.nextLine();

            HBox hBox = new HBox();

            String[] s = line.split(" ");
            String type = s[0].split(":")[0];

            //System.out.println(type + " , " + s[1]);
            if (type.startsWith("Req") ) {

                Label label = new Label(s[2]);
                label.setId("money");

                ImageView img = Images.getImageForGoal("money");
                img.setFitHeight(GOAL_ICONS_LENGTH);
                img.setFitWidth(GOAL_ICONS_LENGTH);

                if(!s[2].equals("0"))hBox.getChildren().addAll(img, label);
            } else {
                ImageView img = Images.getImageForGoal(type);
                img.setFitHeight(GOAL_ICONS_LENGTH);
                img.setFitWidth(GOAL_ICONS_LENGTH);
                Label number = new Label(s[1]);
                Label add = new Label("ADD");
                add.setId("label_button");
                add.setStyle("-fx-text-fill: rgb(63, 72, 204);");
                add.setOnMouseClicked(event -> {
                    try {
                        Game.getInstance().getWarehouse().remove(type);
                        client.sendForServerMultiPlayer(type);
                    } catch (Exception e) {
                        new Pop("You don't have any", view.getSnap(), root, Pop.AddType.ALERT);
                    }
                });
                if (client != null && client.isInTeamGame()) hBox.getChildren().addAll(img, number, add);
                else hBox.getChildren().addAll(img, number);
            }
            goalsVBox.getChildren().add(hBox);
        }
        goalsVBox.setMaxHeight(cnt * GOAL_ICONS_LENGTH);
        goalsVBox.setMaxWidth(300);
    }

    private void setUpFastForward() {
        fastForward = new Label();
        fastForward.setId("label_button");
        ImageView ff1 = new ImageView(FF1);
        ff1.setFitHeight(FF_HEIGHT);
        ff1.setFitWidth(FF_WIDTH);
        ImageView ff2 = new ImageView(FF2);
        ff2.setFitHeight(FF_HEIGHT);
        ff2.setFitWidth(FF_WIDTH);
        fastForward.setGraphic(ff1);
        fastForward.relocate(FF_X, FF_Y);
        fastForward.setOnMouseClicked(event -> {
            if (speed < 1 + EPS) {
                speed = 2;
                fastForward.setGraphic(ff2);
            } else {
                speed = 1;
                fastForward.setGraphic(ff1);
            }
        });
        root.getChildren().add(fastForward);
    }

    void pop(String text) {
        pause();
        Pop pop = new Pop(text, view.getSnap());
        root.getChildren().add(pop.getStackPane());
        pop.getStackPane().setOnMouseClicked(event -> {
            root.getChildren().remove(pop.getStackPane());
            resume();
        });
    }

    void pop(VBox vBox) {
        pause();
        Pop pop = new Pop(vBox, view.getSnap());
        root.getChildren().add(pop.getStackPane());
        pop.getDisabler().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                root.getChildren().remove(pop.getStackPane());
                resume();
            }
        });
    }

    private void setUpSaveButton() {
        save = new Label("SAVE");

        //save.setGraphic(new ImageView(new Image("file:textures/save.png")));
        save.relocate(SAVE_X, SAVE_Y);
        save.setId("label_button_small");
        save.setOnMouseClicked(event -> {
            try {
                Game.getInstance().saveGame("SaveGame");
                pop("Saved Successful\nClick To Continue");
                /* OLD VERSION
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("^_^");
                alert.setContentText(null);
                alert.setHeaderText("Saved Successful");
                alert.showAndWait();
                resume();*/
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        });
        root.getChildren().add(save);
    }

    private void setUpExitButton() {


        Label exit = new Label("EXIT");
        //exit.setGraphic(new ImageView(new Image("file:textures/exit.png")));
        exit.relocate(EXIT_X, EXIT_Y);
        exit.setId("label_button_small");
        exit.setOnMouseClicked(event -> {
            pause();
            VBox vBox = new VBox();
            Label text = new Label("Do you want to save before exit?");
            Label no = new Label("NO");
            Label yes = new Label("YES");
            Label cancel = new Label("CANCEL");
            vBox.getChildren().addAll(text, yes, no, cancel);
            Pop menu = new Pop(vBox, view.getSnap(), root, Pop.AddType.BUTTONS_TEXT);

            no.setOnMouseClicked(event1 -> {
                root.getChildren().remove(menu.getStackPane());
                view.close();
            });
            yes.setOnMouseClicked(event12 -> {
                try {
                    Game.getInstance().saveGame("SaveGame");
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                root.getChildren().remove(menu.getStackPane());
                view.close();
            });
            cancel.setOnMouseClicked(event13 -> {
                resume();
                root.getChildren().remove(menu.getStackPane());
            });
            menu.getDisabler().setOnMouseClicked(event14 -> {
                resume();
                root.getChildren().remove(menu.getStackPane());
            });

        /*  OLD_VERSION  ButtonType buttonTypeOne = new ButtonType("Save & Exit");
            ButtonType buttonTypeTwo = new ButtonType("Exit");
            ButtonType buttonTypeCancel = new ButtonType("Cancel");

            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeOne) {
                try {
                    Game.getInstance().saveGame("SaveGame");
                } catch (Exception e) {

                }
                view.close();
            } else if (result.get() == buttonTypeTwo) {
                view.close();
            } else {
                resume();
            }
*/

        });
        root.getChildren().add(exit);
    }

    private void setUpMenuButton() {


        Label menuButton = new Label("MENU");
        menuButton.setId("label_button_small");
        //menuButton.setGraphic(mn);
        menuButton.relocate(MENU_X, MENU_Y);
        menuButton.setOnMouseClicked(event -> {
            pause();

            VBox vBox = new VBox();
            Label text = new Label("Do you want to save before going to menu?");
            Label yes = new Label("YES");
            Label no = new Label("NO");
            Label cancel = new Label("CANCEL");
            vBox.getChildren().addAll(text, yes, no, cancel);

            Pop menu = new Pop(vBox, view.getSnap(), root, Pop.AddType.BUTTONS_TEXT);

            no.setOnMouseClicked(event1 -> backToMenu());

            yes.setOnMouseClicked(event12 -> {
                try {
                    Game.getInstance().saveGame("SaveGame");
                } catch (Exception e) {
                }
                backToMenu();
            });

            cancel.setOnMouseClicked(event13 -> {
                root.getChildren().remove(menu.getStackPane());
                resume();
            });

            menu.getDisabler().setOnMouseClicked(event14 -> {
                resume();
                root.getChildren().remove(menu.getStackPane());
            });

          /* OLD_VERISON Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setTitle("Back To Menu");
            pause();

            ButtonType buttonTypeOne = new ButtonType("Save");
            ButtonType buttonTypeTwo = new ButtonType("Do Not Save");
            ButtonType buttonTypeThree = new ButtonType("Cancel");

            // TODO  ButtonType buttonTypeThree = new ButtonType("Go to menu");

            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree);

            Optional<ButtonType> result = alert.showAndWait();


            if (result.get() == buttonTypeOne) {
                try {
                    Game.getInstance().saveGame("SaveGame");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            if (result.get() != buttonTypeThree) {
                root.getChildren().clear();
                Menu backMenu = new Menu(view);
                view.setRoot(backMenu.getRoot());
                game.stop();
                //TODO fix Back to MENU.
            } else resume();*/

        });
        root.getChildren().add(menuButton);
    }

    private void backToMenu() {
        root.getChildren().clear();
        Menu backMenu = new Menu(view);
        if (client == null) view.setRoot(backMenu.getRoot());
        else {
            if (client.isInTeamGame()) client.suddenGameEnd();
            client.setInGame(false);
            view.setRoot(client.getMultiPlayerMenu().getRoot());
        }
        game.stop();
    }

    private void setUpWorkshops() {
        workshops.clear();
        for (int i = 0; i < Game.getInstance().getWorkshops().size(); i++) {
            Workshop workshop = Game.getInstance().getWorkshops().get(i);
            workshops.put(workshop, Images.getSpriteAnimation(workshop.getName()));
            SpriteAnimation sprite = getWorkshop(workshop);
            sprite.setOnMouseClicked(EventHandlers.getOnMouseClickedEventHandler(workshop));
            sprite.setState(workshop.getLevel());
            int x, y;
            if (i <= 2) {
                x = LEFT_WORKSHOP_X;
                y = BASE_WORKSHOP + WORKSHOP_DIS * i;
                sprite.getProgressBar().relocate(x, y + sprite.getHeight());
            } else {
                x = RIGHT_WORKSHOP_X;
                y = BASE_WORKSHOP + WORKSHOP_DIS * (i - 3);
                sprite.getProgressBar().relocate(x + 120, y + sprite.getHeight() + (i - 4) * 10);
            }
            for (int j = 0; j < sprite.getImageViews().size(); j++) {
                sprite.getImageViews().get(j).setFitHeight(520 / 4);
                sprite.getImageViews().get(j).setFitWidth(600 / 4);
            }
            root.getChildren().add(sprite.getProgressBar(0));
            fixSprite(sprite, x, y);
            setUpWorkshopInfo(workshop, x, y);
        }
    }

    private void setUpWorkshopInfo(Workshop workshop, int x, int y) {
        Label workshopInfo = new Label();
        ImageView img = new ImageView(info);
        img.setFitHeight(INFO_LENGTH);
        img.setFitWidth(INFO_LENGTH);
        workshopInfo.setGraphic(img);
        workshopInfo.relocate(x + 10, y + 10);
        infoRoot.getChildren().add(workshopInfo);
        workshopInfo.setOnMouseEntered(EventHandlers.getOnMouseEnteredEventHandler(workshop));
        workshopInfo.setOnMouseExited(EventHandlers.getOnMouseExitedEventHandler(workshop));
    }

    private void setUpTruck() {
        //if(client != null) return;
        truck = Images.getSpriteAnimation("truck");
        truck.setOnMouseClicked(EventHandlers.getOnMouseClickedEventHandler(Game.getInstance().getTruck()));
        truck.setState(Game.getInstance().getTruck().getLevel());
        fixSprite(truck, TRUCK_X, TRUCK_Y);

        setUpTruckInfo();
    }

    private void setUpTruckInfo() {
        //if(client != null) return;
        ImageView img = new ImageView(info);
        img.setFitHeight(INFO_LENGTH);
        img.setFitWidth(INFO_LENGTH);

        truckInfo = new Label();
        truckInfo.setGraphic(img);
        truckInfo.relocate(TRUCK_X - 10, TRUCK_Y);
        truckInfo.setOnMouseEntered(EventHandlers.getOnMouseEnteredEventHandler(Game.getInstance().getTruck()));
        truckInfo.setOnMouseExited(EventHandlers.getOnMouseExitedEventHandler(Game.getInstance().getTruck()));
        infoRoot.getChildren().add(truckInfo);
    }

    private void setUpWarehouse() {
        warehouse = Images.getSpriteAnimation("warehouse");
        warehouse.setState(Game.getInstance().getWarehouse().getLevel());
        warehouse.setOnMouseClicked(EventHandlers.getOnMouseClickedEventHandler(Game.getInstance().getWarehouse()));
        fixSprite(warehouse, WAREHOUSE_X, WAREHOUSE_Y);
        setUpWarehouseInfo();
        root.getChildren().add(stored);
    }

    private void setUpWarehouseInfo() {
        ImageView img = new ImageView(info);
        img.setFitHeight(INFO_LENGTH);
        img.setFitWidth(INFO_LENGTH);

        warehouseInfo = new Label();
        warehouseInfo.setGraphic(img);
        warehouseInfo.relocate(WAREHOUSE_X - 10, WAREHOUSE_Y);
        warehouseInfo.setOnMouseEntered(EventHandlers.getOnMouseEnteredEventHandler(Game.getInstance().getWarehouse()));
        warehouseInfo.setOnMouseExited(EventHandlers.getOnMouseExitedEventHandler(Game.getInstance().getWarehouse()));
        infoRoot.getChildren().add(warehouseInfo);
    }

    private void setUpHelicopter() {
        //if(client != null) return;
        helicopter = Images.getSpriteAnimation("helicopter");
        helicopter.setOnMouseClicked(EventHandlers.getOnMouseClickedEventHandler(Game.getInstance().getHelicopter()));
        helicopter.setState(Game.getInstance().getHelicopter().getLevel());
        fixSprite(helicopter, HELICOPTER_X, HELICOPTER_Y);

        setUpHelicopterInfo();
    }

    private void setUpHelicopterInfo() {
        //if(client != null) return;
        ImageView img = new ImageView(info);
        img.setFitWidth(INFO_LENGTH);
        img.setFitHeight(INFO_LENGTH);

        helicopterInfo = new Label();
        helicopterInfo.setGraphic(img);
        helicopterInfo.relocate(HELICOPTER_X + 120, HELICOPTER_Y + 100);
        helicopterInfo.setOnMouseEntered(EventHandlers.getOnMouseEnteredEventHandler(Game.getInstance().getHelicopter()));
        helicopterInfo.setOnMouseExited(EventHandlers.getOnMouseExitedEventHandler(Game.getInstance().getHelicopter()));
        infoRoot.getChildren().add(helicopterInfo);
    }

    private void setUpWell() {
        well = Images.getSpriteAnimation("well");
        for (ImageView iv : well.getImageViews()) {
            //iv.setFitHeight(100);
        }
        well.setOnMouseClicked(EventHandlers.getOnMouseClickedEventHandler(Game.getInstance().getWell()));
        well.setState(Game.getInstance().getWell().getLevel());
        fixSprite(well, WELL_X, WELL_Y);
        Label waterBar = new Label();
        waterBar.setId("baseBar");
        waterBar.relocate(WELL_X, WELL_Y + 50);
        waterBar.setMinSize(15, 70);
        waterBar.setMaxSize(15, 70);
        root.getChildren().add(waterBar);


        filled.relocate(WELL_X, WELL_Y + 50);
        filled.setMinWidth(15);
        filled.setMaxWidth(15);
        filled.setId("bar");
        root.getChildren().add(filled);
        setUpWellInfo();
    }

    private void setUpWellInfo() {
        ImageView img = new ImageView(info);
        img.setFitHeight(INFO_LENGTH);
        img.setFitWidth(INFO_LENGTH);
        Label wellInfo = new Label();
        wellInfo.setGraphic(img);
        wellInfo.relocate(WELL_X - 10, WELL_Y + 10);
        wellInfo.setOnMouseEntered(EventHandlers.getOnMouseEnteredEventHandler(Game.getInstance().getWell()));
        wellInfo.setOnMouseExited(EventHandlers.getOnMouseExitedEventHandler(Game.getInstance().getWell()));
        infoRoot.getChildren().add(wellInfo);
    }

    private void setUpBackground() {
        Image backgroundImage = BACK;
        ImageView imageView = new ImageView(backgroundImage);
        imageView.setOnMouseClicked(EventHandlers.getOnMouseClickedPlant(0, 0));
        root.getChildren().add(imageView);
    }

    private void setUpClock() {
        clock = new Label();
        clock.setId("clock");
        clock.relocate(GOALS_X + 80, GOALS_Y - 10);
        root.getChildren().add(clock);
    }

    private void setUpBuyIcons() {
        for (int i = 0; i < NON_WILD.length; i++) {
            String animalName = NON_WILD[i];
            ImageView buyAnimal = Images.getIcon(animalName);
            buyAnimal.setId("glow");
            buyAnimal.setOnMouseClicked(EventHandlers.getOnMouseClicked(animalName));
            Label priceLabel = new Label("" + Entity.getNewEntity(animalName).getBuyPrice());
            if (priceLabel.getText().length() < 4) priceLabel.setText(" " + priceLabel.getText());
            priceLabel.setId("buyAnimal");
            buyAnimal.relocate(BUY_ANIMAL_BASE_X + BUY_ANIMAL_X_DIFF * i, BUY_ANIMAL_Y);
            priceLabel.relocate(BUY_ANIMAL_BASE_X + BUY_ANIMAL_X_DIFF * i + 10, BUY_ANIMAL_Y + 34);
            root.getChildren().add(buyAnimal);
            root.getChildren().add(priceLabel);
            VBox infoBox = getBuyAnimalFocus(animalName);
            if (animalName.equalsIgnoreCase("cat")) catInfoBox = infoBox;
            infoBox.relocate(BUY_ANIMAL_BASE_X + BUY_ANIMAL_X_DIFF * i - 20, BUY_ANIMAL_Y + 70);
            priceLabel.setOnMouseEntered(mouseEvent -> focus.getRoot().getChildren().add(infoBox));
            priceLabel.setOnMouseExited(mouseEvent -> focus.getRoot().getChildren().remove(infoBox));
            buyAnimal.setOnMouseEntered(mouseEvent -> focus.getRoot().getChildren().add(infoBox));
            buyAnimal.setOnMouseExited(mouseEvent -> focus.getRoot().getChildren().remove(infoBox));
            buyAnimalIcons[i] = buyAnimal;
        }
    }

    private void applyGrayscale(ImageView imageView) {
        ColorAdjust grayscale = new ColorAdjust();
        grayscale.setSaturation(-1);
        grayscale.setBrightness(-0.5);
        imageView.setEffect(grayscale);
    }


    private VBox getBuyAnimalFocus(String animalName) {
        VBox infoBox = new VBox();
        infoBox.setId("focus");
        Label name = new Label(animalName);
        name.setId("name");
        HBox price = new HBox();
        Label buyPriceLabel = new Label(Integer.toString(Entity.getNewEntity(animalName).getBuyPrice()));
        buyPriceLabel.setId("gold");
        price.getChildren().add(buyPriceLabel);
        ImageView coin = new ImageView(COIN_GIF);
        coin.setFitHeight(18);
        coin.setFitWidth(18);
        price.getChildren().add(coin);
        infoBox.getChildren().add(name);
        infoBox.getChildren().add(price);
        if (animalName.equalsIgnoreCase("cat") && Game.getInstance().getCatLevel() == 0) {
            HBox upgrade = new HBox();
            Label upgradeLabel = new Label(Integer.toString(Cat.getUpgradeCost()));
            upgradeLabel.setId("gold");
            ImageView upgradeIcon = new ImageView(UPGRADE_ICON);
            upgradeIcon.setFitHeight(18);
            upgradeIcon.setFitWidth(18);
            upgrade.getChildren().add(upgradeLabel);
            upgrade.getChildren().add(upgradeIcon);
            infoBox.getChildren().add(upgrade);
        }
        return infoBox;
    }

    private void setUpMoneyLabel() {
        HBox hBox = new HBox();
        moneyLabel = new Label(Integer.toString(Game.getInstance().getMoney()));
        moneyLabel.setId("gold");
        hBox.getChildren().add(moneyLabel);
        ImageView coin = new ImageView(COIN_GIF);
        coin.setFitHeight(20);
        coin.setFitWidth(20);
        hBox.getChildren().add(coin);
        hBox.setScaleX(0.8);
        hBox.setScaleY(0.8);
        hBox.translateXProperty().bind(hBox.widthProperty().divide(2).negate());
        hBox.relocate(MONEY_X, MONEY_Y);
        root.getChildren().add(hBox);
    }

    private void fixSprite(SpriteAnimation sprite, int x, int y) {
        for (ImageView img : sprite.getImageViews())
            img.relocate(x, y);
        root.getChildren().add(sprite.getImageView());
        sprite.setX(x);
        sprite.setY(y);
    }

    public void updateWarehouse() {
        Map<String, Integer> storables = Game.getInstance().getWarehouse().getStorables();
        stored.getChildren().clear();

        int offsetY = Game.getInstance().getWarehouse().getLevel() * 5;
        stored.relocate(WAREHOUSE_X + 30, offsetY + WAREHOUSE_Y + 40);
        stored.setMinSize(130, 80);
        stored.setMaxSize(130, 80);
        int cur = 0;
        int cnt = 0;
        for (Map.Entry<String, Integer> pair : storables.entrySet()) {
            Entity entity = Entity.getNewEntity(pair.getKey());
            cur += entity.getSize() * pair.getValue();
            while (cur * (WAREHOUSE_CNT_X * WAREHOUSE_CNT_Y) > cnt * (Game.getInstance().getWarehouse().getMaximumCapacity())) {
                cnt++;
                ImageView imageView = Images.getSpriteAnimation(pair.getKey()).getImageView();
                imageView.setFitHeight(80 / WAREHOUSE_CNT_Y);
                imageView.setFitWidth(120 / WAREHOUSE_CNT_X);
                StackPane stackPane = new StackPane();
                ImageView box = new ImageView(BOX);
                box.setFitHeight(80 / WAREHOUSE_CNT_Y);
                box.setFitWidth(120 / WAREHOUSE_CNT_X);
                box.setOpacity(0.5);
                stackPane.getChildren().addAll(box, imageView);
                stackPane.setMaxSize(120 / WAREHOUSE_CNT_X, 80 / WAREHOUSE_CNT_Y);
                stored.getChildren().add(stackPane);
            }
        }
        stored.setOnMouseClicked(EventHandlers.getOnMouseClickedEventHandler(Game.getInstance().getWarehouse()));

    }

    public void update(SpriteAnimation sprite, Upgradable upgradable) {
        root.getChildren().remove(sprite.getImageView());
        sprite.setState(upgradable.getLevel());
        root.getChildren().add(sprite.getImageView());
    }

    public Group getRoot() {
        return root;
    }

    public SpriteAnimation getWorkshop(Workshop workshop) {
        return workshops.get(workshop);
    }

    public SpriteAnimation getWarehouse() {
        return warehouse;
    }

    public SpriteAnimation getWell() {
        return well;
    }

    public SpriteAnimation getTruck() {
        return truck;
    }

    public SpriteAnimation getHelicopter() {
        return helicopter;
    }

    public boolean getPaused() {
        return paused;
    }

    public void setView(View view) {
        this.view = view;

    }
}
