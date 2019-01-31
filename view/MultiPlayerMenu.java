import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class MultiPlayerMenu {
    private static String BASE = "file:textures/multiplayer/";
    private Group root = new Group();
    private Client client;
    private View view;
    private String id;
    private String[] itemList = new String[]{"Adornment", "CheeseFerment", "Cookie", "Souvenir",
            "bear", "Cheese", "Horn",
            "BrightHorn", "ColoredPlume", "Intermediate",
            "Curd", "lion",
            "Egg", "Milk",
            "EggPowder", "Plume",
            "Cake", "Fabric", "Sewing", "Varnish",
            "CarnivalDress", "Flour", "SourCream", "Wool"};

    MultiPlayerMenu(View view, Client client, boolean isHost) {
        this.view = view;
        this.client = client;
        ImageView imageView = new ImageView(new Image(BASE + "back.jpg"));
        imageView.setFitWidth(800);
        imageView.setFitHeight(600);

        root.getChildren().add(imageView);
        Label start = new Label("START");
        start.setId("label_button");


        Label chat = new Label("CHAT");
        chat.setId("label_button");

        Label rank = new Label("RANK");
        rank.setId("label_button");

        Label profile = new Label("PROFILE");
        profile.setId("label_button");

        Label logOut = new Label("LOGOUT");
        logOut.setId("label_button");

        Label setPrice = new Label("SET PRICE");
        setPrice.setId("label_button");
        setPrice.setOnMouseClicked(mouseEvent -> {
            VBox vBox = new VBox();
            HBox select = new HBox();
            TextField price = new TextField();
            price.setMaxWidth(100);
            TextField itemName = new TextField();
            itemName.setDisable(true);
            itemName.setMaxWidth(100);
            ImageView currentItem = new ImageView();
            Label send = new Label("Send");
            send.setId("label_button");
            select.setSpacing(20);
            Pop pop = new Pop(vBox, view.getSnap(), root, Pop.AddType.BUTTONS_TEXT);
            send.setOnMouseClicked(mouseEvent1 -> sendItemPrice(price, itemName, pop));
            price.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ENTER)
                    sendItemPrice(price, itemName, pop);
            });
            select.getChildren().addAll(currentItem, itemName, price, send);
            FlowPane items = new FlowPane();
            for (String item : itemList) {
                ImageView itemImage = Images.getSpriteAnimation(item).getImageView();
                itemImage.setFitWidth(50);
                itemImage.setFitHeight(50);
                itemImage.setOnMouseClicked(mouseEvent1 -> {
                    currentItem.setImage(itemImage.getImage());
                    currentItem.setViewport(itemImage.getViewport());
                    currentItem.setFitWidth(50);
                    currentItem.setFitHeight(50);
                    itemName.setText(item);
                });
                items.getChildren().add(itemImage);
            }
            vBox.getChildren().addAll(items, select);
        });

        VBox vBox = new VBox(start, rank, chat, profile, logOut);
        if (isHost) {
            vBox.getChildren().add(setPrice);
            logOut.toFront();
        }
        vBox.setId("menu");
        vBox.setAlignment(Pos.CENTER);
        vBox.relocate(400, 300);
        vBox.translateXProperty().bind(vBox.widthProperty().divide(2).negate());
        vBox.translateYProperty().bind(vBox.heightProperty().divide(2).negate());
        root.getChildren().addAll(vBox);

        chat.setOnMouseClicked(event -> view.setRoot(client.getChatroom().getRoot()));

        rank.setOnMouseClicked(event -> view.setRoot(client.getScoreboard().getRoot()));

        logOut.setOnMouseClicked(event -> {
            client.closeSocket();
            view.setRoot(new Menu(view).getRoot());
        });
        profile.setOnMouseClicked(event -> client.getPerson(client.getMyId()));

        start.setOnMouseClicked(event -> view.setRoot(new LevelSelect(view).getRoot()));
    }

    private void sendItemPrice(TextField price, TextField itemName, Pop pop) {
        if (!itemName.getText().equals("")) {
            try {
                client.setPrice(itemName.getText(), Integer.parseInt(price.getText()));
                root.getChildren().remove(pop.getStackPane());
                new Pop(new Label("Price Sent"), view.getSnap(), root, Pop.AddType.ALERT);
            } catch (Exception e) {
                new Pop(new Label("Invalid Request"), view.getSnap(), root, Pop.AddType.ALERT);
            }
        }
    }

    public Group getRoot() {
        return root;
    }
}
