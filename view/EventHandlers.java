import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import static javafx.scene.input.MouseButton.*;

public class EventHandlers {

    static EventHandler<MouseEvent> getOnMouseClickedEventHandler(Entity entity) {
        return event -> {
            if (entity instanceof WildAnimal) {
                Game.getInstance().cage(entity.getCell().getX(), entity.getCell().getY());
            }else if(entity instanceof Item){
                Game.getInstance().pickUp(entity.getCell().getX(), entity.getCell().getY());
            }
        };
    }

    static EventHandler<MouseEvent> getOnMouseClickedEventHandler(Workshop workshop) {
        return event -> {
            switch (event.getButton()){
                case PRIMARY:
                    Game.getInstance().startWorkshop(workshop.getName());
                    break;
                case SECONDARY:
                    Game.getInstance().upgrade(workshop.getName());
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
                        Game.getInstance().well();
                        break;
                    case SECONDARY:
                        Game.getInstance().upgrade("well");
                        break;
                }
            }
        };
    }

}
