import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class EventHandlers {

    static View view;

    //static private Media sound = new Media(new File("sounds/alert.mp3").toURI().toString());
    //static private MediaPlayer mediaPlayer = new MediaPlayer(sound);
    static void setView(View view) {
        EventHandlers.view = view;
    }

    static void alert(){
    //    mediaPlayer.stop();
    //    mediaPlayer.play();
    }

    static EventHandler<MouseEvent> getOnMouseEnteredEventHandler(Workshop workshop) {
        return event ->  GameView.getInstance().getFocus().add(workshop);
    }

    static EventHandler<MouseEvent> getOnMouseExitedEventHandler(Workshop workshop) {
        return event -> GameView.getInstance().getFocus().remove(workshop);
    }

    static EventHandler<MouseEvent> getOnMouseEnteredEventHandler(Vehicle vehicle) {
        return event -> GameView.getInstance().getFocus().add(vehicle);
    }
    static EventHandler<MouseEvent> getOnMouseExitedEventHandler(Vehicle vehicle) {
        return event -> GameView.getInstance().getFocus().remove(vehicle);
    }

    static EventHandler<MouseEvent> getOnMouseClickedEventHandler(Entity entity) {
        return event -> {
            if(event.getButton()== MouseButton.PRIMARY) {
                if (entity instanceof WildAnimal) {
                    Game.getInstance().cage(entity.getCell().getX(), entity.getCell().getY());
                } else if (entity instanceof Item) {
                    Game.getInstance().pickUp(entity.getCell().getX(), entity.getCell().getY());
                }
            }
        };
    }

    static EventHandler<MouseEvent> getOnMouseClickedEventHandler(Workshop workshop) {
        return event -> {
            switch (event.getButton()) {
                case PRIMARY:
                    try {
                        Game.getInstance().startWorkshop(workshop.getName());
                        GameView.getInstance().getWorkshop(workshop).play();
                    } catch (Exception e) {
                        alert();
                    }
                    break;
                case SECONDARY:
                    try {
                        SpriteAnimation sprite = GameView.getInstance().getWorkshop(workshop);
                        Game.getInstance().upgrade(workshop.getName());

                        GameView.getInstance().update(sprite, workshop);
                    } catch (Exception e) {
                        alert();
                        System.out.println(e.getMessage());
                    }
                    break;
            }
        };
    }

    static EventHandler<MouseEvent> getOnMouseClickedEventHandler(Helicopter helicopter) {
        return event -> {
            switch (event.getButton()) {
                case PRIMARY:
                    GameView.getInstance().pause();
                    view.setRoot(new BuyMenu(view).getBuyGroup());
                    break;
                case SECONDARY:
                    try {
                        Game.getInstance().upgrade("helicopter");
                        SpriteAnimation sprite = GameView.getInstance().getHelicopter();
                        GameView.getInstance().update(sprite, helicopter);
                    } catch (Exception e) {
                        alert();
                        System.err.println(e.getMessage());
                    }
                    break;
            }

        };
    }

    static EventHandler<MouseEvent> getOnMouseClickedEventHandler(Truck truck) {
        return event -> {
            switch (event.getButton()) {
                case PRIMARY:
                    GameView.getInstance().pause();
                    new SellMenu(view).getSellGroup();
                    break;
                case SECONDARY:
                    try {
                        Game.getInstance().upgrade("truck");
                        SpriteAnimation sprite = GameView.getInstance().getTruck();
                        GameView.getInstance().update(sprite, truck);
                    } catch (Exception e) {
                        alert();
                        System.err.println(e.getMessage());
                    }
                    break;
            }
        };
    }

    static EventHandler<MouseEvent> getOnMouseClickedEventHandler(Warehouse warehouse) {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                switch (event.getButton()) {
                    case PRIMARY:
                        GameView.getInstance().pause();
                        view.setRoot(new SellMenu(view).getSellGroup());
                        break;
                    case SECONDARY:
                        try {
                            Game.getInstance().upgrade("warehouse");
                            SpriteAnimation sprite = GameView.getInstance().getWarehouse();
                            GameView.getInstance().update(sprite, warehouse);
                        } catch (Exception e) {
                            System.err.println(e.getMessage());
                            alert();;
                        }


                        break;
                }
            }
        };
    }

    static EventHandler<MouseEvent> getOnMouseClickedEventHandler(Well well) {
        return event -> {
            switch (event.getButton()) {
                case PRIMARY:
                    try {
                        Game.getInstance().well();
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                    break;
                case SECONDARY:
                    try {
                        Game.getInstance().upgrade("well");
                        SpriteAnimation sprite = GameView.getInstance().getWell();
                        GameView.getInstance().update(sprite, well);
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                    break;
            }
        };
    }

}
