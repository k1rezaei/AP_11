import javafx.concurrent.Task;

import java.net.Socket;
import java.util.Formatter;
import java.util.Scanner;

public class Profile {

    private static String end = "#";

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

    Task<Void> read = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
            while(socket.isConnected()) {
                String command = scanner.nextLine();
                StringBuilder s = new StringBuilder();
                while(true) {
                    String line = scanner.nextLine();
                    if(line.equals(end)) {
                        //process(command, s.toString());
                        break ;
                    }
                    s.append(line + "\n");
                }
                process(command, s.toString());
            }
            return null;
        }
    };


    //talk with client;
    public void command(String command) {
        formatter.format(command);
        formatter.flush();
    }

    //decoding what's client saying;.
    private void process(String command, String data) {
        if(command.equals("add_text")) {
            server.addMessageToChatRoom(id + " : " + data);
        }else if(command.equals("upd_scoreboard")) {
            server.updateScoreboard(id, data);
        }
    }

    //start listening client's commands;
    public void run() {
        new Thread(read).start();
    }

    Task<Void> getRead() {return read;};

}
