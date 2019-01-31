import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class Scoreboard {
    View view;
    Group root = new Group();
    Label rank = new Label();
    BorderPane borderPane = new BorderPane();
    private static final int HEIGHT = 450;
    //VBox vBox = new VBox();
    Client client;
    static private final int WIDTH = 200;
    ScrollPane scrollPane = new ScrollPane();
    enum SortType {
        ID, LEVEL, GOLD;
    }
    SortType sortType = SortType.ID;

    Scoreboard(View view, Client client) {
        this.view = view;
        this.client = client;
        ImageView bg = new ImageView(new Image("file:textures/multiplayer/cup2.jpg"));
        bg.setFitWidth(800);
        bg.setFitHeight(600);
        root.getChildren().add(bg);
        //vBox.setSpacing(20);
        Label back = new Label("BACK");
        back.relocate(20, 10);
        back.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                view.goBack();
            }
        });
        root.getChildren().add(back);
        back.setId("label_button");

        scrollPane.setId("null");
        scrollPane.relocate(100, 100);
        scrollPane.setContent(borderPane);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        borderPane.setId("rank");
        scrollPane.setMaxHeight(HEIGHT);
        scrollPane.setMinHeight(HEIGHT);

        root.getChildren().add(scrollPane);

    }

    private void sortByLevel(Person[] persons) {
        for (int i = 0; i < persons.length; i++) {
            for (int j = 0; j < persons.length - 1; j++) {
                if (Integer.parseInt(persons[j].getLevel()) < Integer.parseInt(persons[j + 1].getLevel())) {
                    Person temp = persons[j];
                    persons[j] = persons[j + 1];
                    persons[j + 1] = temp;
                }
            }
        }
    }

    private void sortById(Person[] persons) {
        for (int i = 0; i < persons.length; i++) {
            for (int j = 0; j < persons.length - 1; j++) {
                if (persons[j].getId().compareToIgnoreCase(persons[j+1].getId()) > 0) {
                    Person temp = persons[j];
                    persons[j] = persons[j + 1];
                    persons[j + 1] = temp;
                }
            }
        }
    }

    private void sortByMoney(Person[] persons) {
        for (int i = 0; i < persons.length; i++) {
            for (int j = 0; j < persons.length - 1; j++) {
                if (persons[j].getMoney() < persons[j + 1].getMoney()) {
                    Person temp = persons[j];
                    persons[j] = persons[j + 1];
                    persons[j + 1] = temp;
                }
            }
        }
    }


    public void setContent(Person[] persons) {
        switch (sortType){
            case ID:
                sortById(persons);
                break;
            case GOLD:
                sortByMoney(persons);
                break;
            case LEVEL:
                sortByLevel(persons);
                break;
        }
        borderPane.getChildren().clear();
        VBox IDS = new VBox();
        VBox levels = new VBox();
        VBox golds = new VBox();
        Label idInfo = new Label("ID");
        idInfo.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                sortById(persons);
                sortType = SortType.ID;
                setContent(persons);
            }
        });
        Label levelInfo = new Label("Level");
        levelInfo.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                sortByLevel(persons);
                sortType = SortType.LEVEL;
                setContent(persons);
            }
        });
        Label moenyInfo = new Label("Money");
        moenyInfo.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                sortByMoney(persons);
                sortType = SortType.GOLD;
                setContent(persons);
            }
        });
        idInfo.setId("rank_info");
        levelInfo.setId("rank_info");
        moenyInfo.setId("rank_info");
        IDS.getChildren().add(idInfo);
        levels.getChildren().add(levelInfo);
        golds.getChildren().add(moenyInfo);

        for (int i = 0; i < persons.length; i++) {
            BorderPane borderPane = new BorderPane();
            borderPane.setMinWidth(WIDTH);
            Label id = new Label(persons[i].getName());
            Label rank = new Label(persons[i].getLevel());
            Label money = new Label("" + persons[i].getMoney());

            int finalI = i;
            id.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    client.getPerson(persons[finalI].getId());
                }
            });
            id.setId("rank_name");
            rank.setId("rank_level");
            money.setId("rank_money");

            if(idInfo.getWidth() < id.getWidth()){
                idInfo.setMinWidth(id.getWidth());
            }
            IDS.getChildren().add(id);
            levels.getChildren().add(rank);
            golds.getChildren().add(money);

            /*vBox.setSpacing(25);
            vBox.getChildren().add(borderPane);
            vBox.setId("rank");*/
        }

        borderPane.setLeft(IDS);
        borderPane.setCenter(levels);
        borderPane.setRight(golds);

        borderPane.getLeft().setId("rank_");
        borderPane.getRight().setId("rank_");
        borderPane.getCenter().setId("rank_");
    }


    Group getRoot() {
        return root;
    }
}
