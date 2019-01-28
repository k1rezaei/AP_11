import com.google.gson.Gson;
import com.sun.media.jfxmedia.events.PlayerEvent;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.io.IOException;
import java.net.Socket;
import java.util.Formatter;
import java.util.Scanner;

public class Client {

    private static final String end = "#";
    private static final String DATA_CHAT_ROOM = "data_chat_room";
    private static final String DATA_SCOREBOARD = "data_scoreboard";
    private static final String DATA_ITEM_COST = "data_item_cost";
    private static final String UPDATE_SCOREBOARD = "update_scoreboard";
    private static final String ADD_MESSAGE_TO_CHAT_ROOM = "add_message_to_chat_room";
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
    private static final String DATA_FRIENDS = "data_friends";
    private static final String GET_PERSON = "get_person";
    private static final String DATA_PERSON = "data_person";


    View view;
    Socket socket;
    Scanner scanner;
    Formatter formatter;

    Chatroom chatroom ;
    Scoreboard scoreboard ;
    MultiPlayerMenu multiPlayerMenu ;

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public MultiPlayerMenu getMultiPlayerMenu() {
        return multiPlayerMenu;
    }

    public Chatroom getChatRoom() {
        return chatroom;
    }

    Client(View view){
        this.view = view;
        chatroom = new Chatroom(view, this);
        multiPlayerMenu = new MultiPlayerMenu(view, this);
        scoreboard = new Scoreboard(view);
    }

    private String getData(Scanner scanner) {
        StringBuilder s = new StringBuilder();
        while(true) {
            String line = scanner.nextLine();
            if(line.equals(end)) break;
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

    void initialize() {
        try {
            socket = new Socket("localhost", 8050);
            scanner = new Scanner(socket.getInputStream());
            formatter = new Formatter(socket.getOutputStream());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    boolean checkId(String id) throws IOException {
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
            int port = scanner.nextInt();
            System.err.println("port is " + port);
            formatter.format("Connected with port : " + port + '\n');
            formatter.flush();
            formatter.close();
            scanner.close();
            while(true ){
                try {
                    socket = new Socket("localhost", port);
                    break ;
                } catch(Exception e) {}
            }
            scanner = new Scanner(socket.getInputStream());
            formatter = new Formatter(socket.getOutputStream());
            return true;
        }

    }

    public void run() {
        new Thread(read).start();
        initChatRoom();
        initScoreboard();
    }

    //talk to server.
    synchronized void command(String command) {
        formatter.format(command);
        formatter.flush();
    }

    //decoding what's server saying.
    private void process(String command, String text) {
        Scanner reader;
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
                //scoreboard.setContent(people);
                scoreboard.setContent(text);
                break;
            }
            case DATA_ITEM_COST:
                reader = new Scanner(text);
                item = reader.nextLine();
                price = reader.nextLine();
                int cost = Integer.parseInt(price);
                if(cost >= Game.getInstance().getMoney()) buyItem(item);
                else System.err.println("not enough money");//TODO throw new Runtime exception
                break;
            case BOUGHT_ITEM :
                reader = new Scanner(text);
                item = reader.nextLine();
                price = reader.nextLine();
                cost = Integer.parseInt(price);
                Game.getInstance().setMoney(Game.getInstance().getMoney() - cost);
                Game.getInstance().addEntity(Entity.getNewEntity(item));
                //todo
            case SOLD_ITEM :
                reader = new Scanner(text);
                item = reader.nextLine();
                price = reader.nextLine();
                cost = Integer.parseInt(price);
                Game.getInstance().setMoney(Game.getInstance().getMoney()+ cost);
             case DATA_INBOX :
                String json = text;
                Talk[] inbox = new Gson().fromJson(text, Talk[].class);
                //todo
            case DATA_FRIENDS :
                reader = new Scanner(text);
                Person[] followers = new Gson().fromJson(reader.nextLine(), Person[].class);
                Person[] friends = new Gson().fromJson(reader.nextLine(), Person[].class);
                Person[] followings = new Gson().fromJson(reader.nextLine(), Person[].class);
                //todo
            case DATA_PERSON :
                reader = new Scanner(text);
                id = reader.nextLine();
                Person person = new Gson().fromJson(reader.nextLine(), Person.class);
                //todo
            default:
                System.err.println("FFFF");
        }
    }

    public void addMessageToChatRoom(String text) {
        String command = ADD_MESSAGE_TO_CHAT_ROOM + "\n" + text + '\n' + end + "\n";
        command(command);
    }

    public void updateScoreboard(String level) {
        String command = UPDATE_SCOREBOARD + "\n" + level + '\n' + end + "\n";
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


    public void closeSocket(){
        try {
            socket.close();
        }catch (Exception e){}
    }
}
