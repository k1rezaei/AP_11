import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class EventHandlers {

    static EventHandler<MouseEvent> getOnMouseClickedEventHandler(Entity entity) {
        return event -> {
            if (entity instanceof WildAnimal) {
                Game.getInstance().cage(entity.getCell().getX(), entity.getCell().getY());
            } else if (entity instanceof Item) {
                Game.getInstance().pickUp(entity.getCell().getX(), entity.getCell().getY());
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
                    } catch (Exception e) {}
                    break;
                case SECONDARY:
                    try {
                        Game.getInstance().upgrade(workshop.getName());
                        //GameView.getInstance().
                        GameView.getInstance().getWorkshop(workshop).setState(workshop.getLevel() - 1); //TODO level 1 based.
                    } catch(Exception e) {}
                    break;
            }
        };
    }

    static EventHandler<MouseEvent> getOnMouseClickedEventHandler(Helicopter helicopter) {
        return event -> {
            switch (event.getButton()) {
                case PRIMARY:
                    break;
                case SECONDARY:
                    break;
            }
            /**
             *
             *
             * */
        };
    }

    static EventHandler<MouseEvent> getOnMouseClickedEventHandler(Truck truck) {
        return event -> {
            switch (event.getButton()) {
                case PRIMARY:
                    break;
                case SECONDARY:
                    break;
            }
            /**
             *
             *
             * */
        };
    }

    static EventHandler<MouseEvent> getOnMouseClickedEventHandler(Warehouse warehouse) {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                switch (event.getButton()) {
                    case PRIMARY:
                        break;
                    case SECONDARY:
                        break;
                }
                /**
                 *
                 *
                 * */
            }
        };
    }

    static EventHandler<MouseEvent> getOnMouseClickedEventHandler(Well well) {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                switch (event.getButton()) {
                    case PRIMARY:
                        try {
                            Game.getInstance().well();

                        }catch(Exception e) {
                            System.err.println(e.getMessage());
                        }

                        break;
                    case SECONDARY:
                        try {
                            Game.getInstance().upgrade("well");
                            GameView.getInstance().getWell().setState(well.getLevel());
                        } catch(Exception e) {
                            System.err.println(e.getMessage());
                        }
                        break;
                }
            }
        };
    }

}
