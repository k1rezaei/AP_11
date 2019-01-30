import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.Socket;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Scanner;

public class Client {

    private static final String end = "#";
    private static final String DATA_CHAT_ROOM = "data_chat_room";
    private static final String DATA_SCOREBOARD = "data_scoreboard";
    private static final String DATA_ITEM_COST = "data_item_cost";
    private static final String UPDATE_SCOREBOARD = "update_scoreboard";
    private static final String ADD_MESSAGE_TO_CHAT_ROOM = "add_message_to_chat_room";
    private static final String ADD_MESSAGE_TO_CHAT_ROOM_WITH_REPLY = "add_message_to_chat_room_with_reply";
    private static final String INIT_SCOREBOARD = "init_scoreboard";
    private static final String INIT_CHAT_ROOM = "init_chat_room";
    private static final String GET_ITEM_COST = "get_item_cost";
    private static final String BUY_ITEM = "buy_item";
    private static final String BOUGHT_ITEM = "bought_item";
    private static final String SELL_ITEM = "sell_item";
    private static final String SEND_PRIVATE_MESSAGE = "send_private_message";
    private static final String DATA_INBOX = "data_inbox";
    private static final String SOLD_ITEM = "sold_item";
    private static final String ADD_FRIEND_REQUEST = "add_friend_request";
    private static final String ACCEPT_FRIEND_REQUEST = "accept_friend_request";
    private static final String DATA_FRIENDS = "data_friends";
    private static final String GET_PERSON = "get_person";
    private static final String DATA_PERSON = "data_person";
    private static final String LOG_IN = "I am in!";
    private static final String GET_WAREHOUSE = "get_warehouse";
    private static final String DATA_WAREHOUSE = "data_warehouse";
    private static final String SPLIT = "$$";
    private static final String CHECK_CONNECT = "check_connect";
    private static final String I_AM_CONNECTED = "i_am_connected";
    private static final String ADD_BEAR = "add_bear";
    private static final String CAN_YOU_ADD_BEAR = "can_you_add_bear";
    private static final String I_CAN_ADD_BEAR = "i_can_add_bear";
    private static final String I_CAN_NOT_ADD_BEAR = "i_can_not_add_bear";
    private static final String BEAR_ADDED = "bear_added";
    private static final String BEAR_DID_NOT_ADD = "bear_did_not_add";
    private static final String ADD_BEAR_TO_YOUR_MAP = "add_bear_to_your_map";
    private static final String UPDATE_MONEY = "update_money";
    private static final String GET_MONEY = "get_money";
    private static final String DATA_MONEY = "data_money";
    //TODO get bear cost from server
    private static final int BEAR_COST = 200;
    View view;
    Socket socket;
    Scanner scanner;
    Formatter formatter;

    private Chatroom chatroom;
    private Scoreboard scoreboard;
    private MultiPlayerMenu multiPlayerMenu;
    private Inbox inbox;
    private Shop shop;
    private String myId;
    //TODO make money
    private int money = 1000;
    private boolean inGame;

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public MultiPlayerMenu getMultiPlayerMenu() {
        return multiPlayerMenu;
    }

    public Chatroom getChatRoom() {
        return chatroom;
    }

    Client(View view) {
        this.view = view;
        chatroom = new Chatroom(view, this);
        multiPlayerMenu = new MultiPlayerMenu(view, this);
        scoreboard = new Scoreboard(view, this);
        inbox = new Inbox(view, this);
        shop = new Shop(view, this);
    }

    private String getData(Scanner scanner) {
        StringBuilder s = new StringBuilder();
        while (true) {
            String line = scanner.nextLine();
            if (line.equals(end)) break;
            s.append(line + "\n");
        }
        return s.toString();
    }

    Task<Void> read = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
            while (socket.isConnected()) {
                String command = scanner.nextLine();
                process(command, getData(scanner));
            }
            return null;
        }
    };

    void initialize(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            scanner = new Scanner(socket.getInputStream());
            formatter = new Formatter(socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean checkId(String id, String ip) throws IOException {
        System.err.println(socket.isConnected());
        System.err.println("id is :" + id);
        formatter.format(id + "\n");
        formatter.flush();
        String result = scanner.nextLine();
        System.err.println("result is :" + result);
        if (result.equals("userName inValid")) {
            scanner.close();
            formatter.close();
            return false;
        } else {
            myId = id;
            int port = scanner.nextInt();
            System.err.println("port is " + port);
            formatter.format("Connected with port : " + port + '\n');
            formatter.flush();
            formatter.close();
            scanner.close();
            while (true) {
                try {
                    socket = new Socket(ip, port);
                    break;
                } catch (Exception e) {
                }
            }
            scanner = new Scanner(socket.getInputStream());
            formatter = new Formatter(socket.getOutputStream());
            return true;
        }

    }

    public void run() {
        run("1");
    }

    public void run(String level) {
        new Thread(read).start();
        addMessageToChatRoom(LOG_IN);
        updateScoreboard(level);
        getWarehouse();
    }

    //talk to server.
    synchronized void command(String command) {
        formatter.format(command);
        formatter.flush();
    }

    //decoding what's server saying.
    private void process(String command, String text) {

        Scanner reader = new Scanner(text);
        String item, price, id;
        switch (command) {
            case DATA_CHAT_ROOM: {
                Gson gson = new Gson();
                Talk[] talks = gson.fromJson(text, Talk[].class);
                Platform.runLater(() -> chatroom.setContent(talks));
                break;
            }
            case DATA_SCOREBOARD: {
                Gson gson = new Gson();
                Person[] people = gson.fromJson(text, Person[].class);
                Platform.runLater(() -> scoreboard.setContent(people));
                //scoreboard.setContent(text);
                break;
            }
            case DATA_ITEM_COST:
                item = reader.nextLine();
                price = reader.nextLine();/*
                int cost = Integer.parseInt(price);
                if (cost >= Game.getInstance().getMoney()) buyItem(item);
                else System.err.println("not enough money");//TODO throw new Runtime exception*/
                break;
            case BOUGHT_ITEM:
                item = reader.nextLine();
                price = reader.nextLine();
                int cost = Integer.parseInt(price);
                System.err.println("bought " + item);
                //todo
                break;
            case SOLD_ITEM:
                item = reader.nextLine();
                price = reader.nextLine();
                cost = Integer.parseInt(price);
                System.err.println("sold " + item);
                break;
            case DATA_INBOX:
                String json = text;
                Talk[] messages = new Gson().fromJson(text, Talk[].class);
                System.err.println(text);
                Platform.runLater(() -> inbox.setContent(messages));
                break;
            case DATA_FRIENDS:
                String[] followers = new Gson().fromJson(reader.nextLine(), String[].class);
                String[] friends = new Gson().fromJson(reader.nextLine(), String[].class);
                String[] followings = new Gson().fromJson(reader.nextLine(), String[].class);
                //todo
                break;
            case DATA_PERSON:
                id = reader.nextLine();
                Person person = new Gson().fromJson(reader.nextLine(), Person.class);
                System.err.println("BUG " + person.getId());
                System.err.println("BUG " + person.getInbox().size());
                Platform.runLater(() -> {
                    ViewProfile viewProfile = new ViewProfile(view, Client.this, person);
                    view.setRoot(viewProfile.getRoot());
                });
                //todo
                break;
            case DATA_WAREHOUSE:
                HashMap items = new Gson().fromJson(reader.nextLine(), HashMap.class);
                HashMap prices = new Gson().fromJson(reader.nextLine(), HashMap.class);
                Platform.runLater(() -> shop.update(items, prices));
                //todo
                break;
            case CHECK_CONNECT:
                iAmConnected();
                break;
            case CAN_YOU_ADD_BEAR:
                String id1 = reader.nextLine();
                String id2 = reader.nextLine();
                if (inGame)
                    command(I_CAN_ADD_BEAR + "\n" + id1 + "\n" + id2 + "\n" + end + "\n");
                else command(I_CAN_NOT_ADD_BEAR + "\n" + id1 + "\n" + id2 + "\n" + end + "\n");
                //todo sharte if bayad tabdil she be inke dare baazi mikone ya na?
                break;
            case BEAR_ADDED:
                id = reader.nextLine();
                //TODO DISABLE INGAME?
                if (inGame) Game.getInstance().setMoney(Game.getInstance().getMoney() - BEAR_COST);
                else money -= BEAR_COST;
                showMessage("Sent a  bear to " + id + ".");
                break;
            case BEAR_DID_NOT_ADD:
                id = reader.nextLine();
                showMessage("Failed to send bear. " + id + " is not online");
                break;
            case ADD_BEAR_TO_YOUR_MAP:
                if (!inGame) System.err.println("NOT IN GAME");
                Game.getInstance().addEntity(Entity.getNewEntity("bear"));
                break;
            case DATA_MONEY:
                int money = reader.nextInt();
                //todo
                break;
            default:
                System.err.println(command);
        }
    }

    private void iAmConnected() {
        String command = I_AM_CONNECTED + "\n" + end + "\n";
        command(command);
    }

    public void addMessageToChatRoom(String text, String repliedText) {
        String command = ADD_MESSAGE_TO_CHAT_ROOM_WITH_REPLY + "\n" +
                text + "\n"
                + SPLIT + "\n"
                + repliedText + "\n"
                + end + "\n";
        command(command);
    }

    public void addMessageToChatRoom(String text) {
        String command = ADD_MESSAGE_TO_CHAT_ROOM + "\n" + text + '\n' + end + "\n";
        command(command);
    }

    public void updateScoreboard(String level) {
        String command = UPDATE_SCOREBOARD + "\n" + level + '\n' + end + "\n";
        command(command);
    }

    public void updateMoney() {
        command(UPDATE_MONEY + "\n" + money + "\n" + end + "\n");
    }

    public void getMoneyFromServer() {
        String command = GET_MONEY + "\n" + end + "\n";
        command(command);
    }

    public void upgradeMoney() {
        String command = UPDATE_MONEY + "\n" + money + "\n" + end + "\n";
        command(command);
    }

    public void initScoreboard() {
        String command = INIT_SCOREBOARD + "\n" + end + "\n";
        command(command);
    }

    public void initChatRoom() {
        String command = INIT_CHAT_ROOM + "\n" + end + "\n";
        command(command);
    }

    public void getItemCost(String item) {
        String command = GET_ITEM_COST + "\n" + item + "\n" + end + "\n";
        command(command);
    }

    public void buyItem(String item) {
        String command = BUY_ITEM + "\n" + item + "\n" + end + "\n";
        command(command);
    }

    public void sellItem(String item) {
        String command = SELL_ITEM + "\n" + item + "\n" + end + "\n";
        command(command);
    }

    public void sendPrivateMessage(String id, String text) {
        String command = SEND_PRIVATE_MESSAGE + "\n" + id + "\n" + text + "\n" + end + "\n";
        command(command);
    }

    public void addFriendRequest(String id) {
        String command = ADD_FRIEND_REQUEST + "\n" + id + "\n" + end + "\n";
        command(command);
    }

    public void getPerson(String id) {
        String command = GET_PERSON + "\n" + id + "\n" + end + "\n";
        command(command);
    }

    public void acceptFriendRequest(String id) {
        String command = ACCEPT_FRIEND_REQUEST + "\n" + id + "\n" + end + "\n";
        command(command);
    }

    public void getWarehouse() {
        String command = GET_WAREHOUSE + "\n" + end + "\n";
        command(command);
    }

    public void addBear(String id) {
        if ((inGame && Game.getInstance().getMoney() < BEAR_COST) || (!inGame && money < BEAR_COST)) {
            showMessage("Not enough Money.");
            return;
        }
        String command = ADD_BEAR + "\n" + id + "\n" + end + "\n";
        command(command);
    }

    private void showMessage(String message) {
        Platform.runLater(() -> new Pop(new Label(message), view.getSnap(), (Group) view.getScene().getRoot()));
    }

    public Chatroom getChatroom() {
        return chatroom;
    }

    public void setChatroom(Chatroom chatroom) {
        this.chatroom = chatroom;
    }

    public void setScoreboard(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    public void setMultiPlayerMenu(MultiPlayerMenu multiPlayerMenu) {
        this.multiPlayerMenu = multiPlayerMenu;
    }

    public void closeSocket() {
        try {
            socket.close();
        } catch (Exception e) {
        }
    }

    public String getMyId() {
        return myId;
    }

    public Inbox getInbox() {
        return inbox;
    }

    public void setInbox(Inbox inbox) {
        this.inbox = inbox;
    }

    public Shop getShop() {
        return shop;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public boolean isInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }
}
