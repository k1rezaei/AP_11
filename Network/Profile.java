import javafx.concurrent.Task;

import java.net.Socket;
import java.util.Formatter;
import java.util.Scanner;

public class Profile {
    String id;
    Socket socket;
    Formatter formatter;
    Scanner scanner;
    int level = 1;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Profile(String id, Socket socket, Formatter formatter, Scanner scanner) {
        this.id = id;
        this.socket = socket;
        this.formatter = formatter;
        this.scanner = scanner;
    }

    Task<Void> read = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
            while(socket.isConnected()) {
                String command = scanner.nextLine();
                process(command);
            }
            return null;
        }
    };

    public void command(String command) {
        formatter.format(command);
        formatter.flush();
    }

    private void process(String command) {
    }

    public void run() {
        new Thread(read).start();
    }

    Task<Void> getRead() {return read;};


}