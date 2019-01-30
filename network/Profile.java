import javafx.animation.AnimationTimer;
import javafx.concurrent.Task;

import java.io.IOException;
import java.net.Socket;
import java.util.Formatter;
import java.util.Scanner;

public class Profile {

    private static final String end = "#";
    private static final String UPDATE_SCOREBOARD = "update_scoreboard";
    private static final String ADD_MESSAGE_TO_CHAT_ROOM = "add_message_to_chat_room";
    private static final String ADD_MESSAGE_TO_CHAT_ROOM_WITH_REPLY = "add_message_to_chat_room_with_reply";
    private static final String INIT_SCOREBOARD = "init_scoreboard";
    private static final String INIT_CHAT_ROOM = "init_chat_room";
    private static final String GET_ITEM_COST = "get_item_cost";
    private static final String BUY_ITEM = "buy_item";
    private static final String SELL_ITEM = "sell_item";
    private static final String SEND_PRIVATE_MESSAGE = "send_private_message";
    private static final String ADD_FRIEND_REQUEST = "add_friend_request";
    private static final String GET_PERSON = "get_person";
    private static final String ACCEPT_FRIEND_REQUEST = "accept_friend_request";
    private static final String GET_WAREHOUSE = "get_warehouse";
    private static final String SPLIT = "$$";
    private static final String CHECK_CONNECT = "check_connect";
    private static final String I_AM_CONNECTED = "i_am_connected";
    private static final String ADD_BEAR = "add_bear";
    private static final String CAN_YOU_ADD_BEAR = "can_you_add_bear";
    private static final String I_CAN_ADD_BEAR = "i_can_add_bear";
    private static final String I_CAN_NOT_ADD_BEAR = "i_can_not_add_bear";
    private static final long DURATION_IN_MILLISECOND = 1000 * 100 * 100 * 10;
    private static final int TURN_OUT = 30;
    private static final String BEAR_ADDED = "bear_added";
    private static final String BEAR_DID_NOT_ADD = "bear_did_not_add";
    private static final String ADD_BEAR_TO_YOUR_MAP = "add_bear_to_your_map";

    private int counter = 0;
    private boolean bucketSent = false;

    Person person;
    Socket socket;
    Formatter formatter;
    Scanner scanner;
    Server server;

    AnimationTimer connectionChecker = new AnimationTimer() {
        long lastTime = -1;
        @Override
        public void handle(long now) {
            if(lastTime == -1 || now > lastTime + DURATION_IN_MILLISECOND) {
                lastTime = now;
                counter ++;
                if(counter > TURN_OUT) {
                    checkConnection();
                }
            }
        }
    };

    public void setPerson(Person p) {
        person = p;
    }

    private void checkConnection() {
        counter = 0;
        if(bucketSent) disconnect();
        else {
            command(CHECK_CONNECT + "\n" + end + "\n");
            bucketSent = true;
        }
    }

    private void disconnect() {
        System.err.println("DISCONNECTED");
        try {
            //socket.close();
            scanner = null;
            socket.close();
        } catch(Exception e) {
            System.err.println("Cannot close connection :/");
        }
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Person getPerson() {
        return person;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Formatter getFormatter() {
        return formatter;
    }

    public void setFormatter(Formatter formatter) {
        this.formatter = formatter;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public Profile(Person person, Socket socket) throws IOException {
        this.person = person;
        this.socket = socket;
        this.formatter = new Formatter(socket.getOutputStream());
        this.scanner = new Scanner(socket.getInputStream());
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
            while (true) {
                try {
                    String command = scanner.nextLine();
                    clear();
                    System.err.println("Cleared");
                    process(command, getData(scanner));
                } catch (Exception e) {
                    break ;
                }
            }
            System.err.println("Disconnected");
            connectionChecker.stop();
            server.remove(person);
            return null;
        }
    };

    private void clear() {
        bucketSent = false;
        counter = 0;
    }


    //talk with client;
    synchronized public void command(String command) {
        formatter.format(command);
        formatter.flush();
    }

    //decoding what's client saying;.
    private void process(String command, String data) {
        System.err.println(command);
        String cmd, item, id, id1, id2;
        Scanner reader = new Scanner(data);
        switch (command) {
            case ADD_MESSAGE_TO_CHAT_ROOM:
                Talk talk = new Talk(person.getId(), data);
                server.addMessageToChatRoom(talk);
                break;
            case ADD_MESSAGE_TO_CHAT_ROOM_WITH_REPLY:
                StringBuilder txt = new StringBuilder();
                while(true) {
                    String line = reader.nextLine();
                    if(line.equals(SPLIT)) break ;
                    txt.append(line + "\n");
                }
                Talk talkWithReply = new Talk(person.getId(), txt.toString());
                txt = new StringBuilder();
                while(reader.hasNextLine()) {
                    String line = reader.nextLine();
                    txt.append(line + "\n");
                }
                talkWithReply.setRepliedText(txt.toString());
                server.addMessageToChatRoom(talkWithReply);
                break;
            case UPDATE_SCOREBOARD:
                String level = reader.nextLine();
                person.setLevel(level);
                server.updateScoreboard();
                break;
            case INIT_SCOREBOARD:
                command(server.getScoreboard());
                break;
            case INIT_CHAT_ROOM:
                command(server.getChatRoom());
                break;
            case GET_ITEM_COST:
                item = reader.nextLine();
                command(server.getItemCost(item));
                break;
            case BUY_ITEM:
                item = reader.nextLine();
                cmd = server.buyItem(item);
                if (cmd.length() > 0) command(cmd);
                break;
            case SELL_ITEM:
                item = reader.nextLine();
                cmd = server.sellItem(item);
                command(cmd);
                break;
            case SEND_PRIVATE_MESSAGE:
                id = reader.nextLine();
                data = data.substring(id.length() + 1);
                server.sendPrivateMessage(person.getId(), id, data);
                break;
            case ADD_FRIEND_REQUEST:
                id = reader.nextLine();
                server.addFriendRequest(person.getId(), id);
                break;
            case GET_PERSON:
                id = reader.nextLine();
                command(server.getPersonCommand(id));
                break;
            case ACCEPT_FRIEND_REQUEST :
                id = reader.nextLine();
                server.acceptFriendRequest(person.getId(), id);
                break;
            case GET_WAREHOUSE :
                command(server.getWarehouse());
                break;
            case I_AM_CONNECTED :
                break;
            case ADD_BEAR :
                id = reader.nextLine();
                server.command(CAN_YOU_ADD_BEAR + "\n" + person.getId() + "\n" + id + "\n" + end + "\n", id);
                break;
            case I_CAN_ADD_BEAR :
                id1 = reader.nextLine();
                id2 = reader.nextLine();
                server.command(BEAR_ADDED + "\n" + id2 + "\n" + end + "\n", id1);
                server.command(ADD_BEAR_TO_YOUR_MAP + "\n" + end + "\n", id2);
                break ;
            case I_CAN_NOT_ADD_BEAR :
                id1 = reader.nextLine();
                id2 = reader.nextLine();
                server.command(BEAR_DID_NOT_ADD + "\n" + id2 + "\n" + end + "\n", id1);
                break;
        }
    }

    //start listening client's commands;
    public void run() {
        //connectionChecker.start();
        new Thread(read).start();
    }

    Task<Void> getRead() {
        return read;
    }

}