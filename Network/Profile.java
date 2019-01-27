import javafx.concurrent.Task;

import java.net.Socket;
import java.util.Formatter;
import java.util.Scanner;

public class Profile {

    private static final String end = "#";
    private static final String UPDATE_SCOREBOARD = "update_scoreboard";
    private static final String ADD_MESSAGE_TO_CHAT_ROOM = "add_message_to_chat_room";
    private static final String INIT_SCOREBOARD = "init_scoreboard";
    private static final String INIT_CHAT_ROOM = "init_chat_room";



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

    public Profile(Person person, Socket socket) {
        this.person = person;
        this.socket = socket;
        this.formatter = new Formatter(socket.getOutputStream());
        this.scanner = new Scanner(socket.getInputStream());
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
            while(socket.isConnected()) {
                String command = scanner.nextLine();
                process(command, getData(scanner));
            }
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
        switch (command) {
            case ADD_MESSAGE_TO_CHAT_ROOM:
                server.addMessageToChatRoom(person.getId() + " : " + data);
                break;
            case UPDATE_SCOREBOARD:
                person.setLevel(data);
                server.updateScoreboard();
                break;
            case INIT_SCOREBOARD:
                command(server.getScoreboard());
                break;
            case INIT_CHAT_ROOM:
                command(server.getChatRoom());
                break;
        }
    }

    //start listening client's commands;
    public void run() {
        new Thread(read).start();
    }

    Task<Void> getRead() {return read;};

}
