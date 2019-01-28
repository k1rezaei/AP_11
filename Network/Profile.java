import javafx.concurrent.Task;

import java.io.IOException;
import java.net.Socket;
import java.util.Formatter;
import java.util.Scanner;

public class Profile {

    private static final String end = "#";
    private static final String UPDATE_SCOREBOARD = "update_scoreboard";
    private static final String ADD_MESSAGE_TO_CHAT_ROOM = "add_message_to_chat_room";
    private static final String INIT_SCOREBOARD = "init_scoreboard";
    private static final String INIT_CHAT_ROOM = "init_chat_room";
    private static final String GET_ITEM_COST = "get_item_cost";
    private static final String BUY_ITEM = "buy_item";
    private static final String SELL_ITEM = "sell_item";
    private static final String SEND_PRIVATE_MESSAGE = "send_private_message";
    private static final String ADD_FRIEND_REQUEST = "add_friend_request";
    private static final String GET_PERSON = "get_person";
    private static final String ACCEPT_FRIEND_REQUEST = "accept_friend_request";


    Person person;
    Socket socket;
    Formatter formatter;
    Scanner scanner;
    Server server;

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
            while (socket.isConnected()) {
                String command = scanner.nextLine();
                process(command, getData(scanner));
            }
            server.remove(person);
            return null;
        }
    };


    //talk with client;
    synchronized public void command(String command) {
        formatter.format(command);
        formatter.flush();
    }

    //decoding what's client saying;.
    private void process(String command, String data) {
        String cmd, item, id;
        Scanner reader = new Scanner(data);

        switch (command) {
            case ADD_MESSAGE_TO_CHAT_ROOM:
                Talk talk = new Talk(person, data);
                server.addMessageToChatRoom(talk);
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
            case BUY_ITEM:
                item = reader.nextLine();
                cmd = server.buyItem(item);
                if (cmd.length() > 0) command(cmd);
            case SELL_ITEM:
                item = reader.nextLine();
                cmd = server.sellItem(item);
                command(cmd);
            case SEND_PRIVATE_MESSAGE:
                id = reader.nextLine();
                data = data.substring(id.length() + 1);
                server.sendPrivateMessage(person.getId(), id, data);
            case ADD_FRIEND_REQUEST:
                id = reader.nextLine();
                server.addFriendRequest(person.getId(), id);
            case GET_PERSON:
                id = reader.nextLine();
                command(server.getPersonCommand(id));
            case ACCEPT_FRIEND_REQUEST :
                id = reader.nextLine();
                server.acceptFriendRequest(person.getId(), id);

        }
    }

    //start listening client's commands;
    public void run() {
        new Thread(read).start();
    }

    Task<Void> getRead() {
        return read;
    }

}
