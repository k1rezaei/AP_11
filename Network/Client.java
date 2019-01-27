import javafx.concurrent.Task;

import java.io.IOException;
import java.net.Socket;
import java.util.Formatter;
import java.util.Scanner;

public class Client {

    Socket socket;
    Scanner scanner;
    Formatter formatter;

    Task<Void> read = new Task<Void>() {
        @Override
        protected Void call() throws Exception {

            while (socket.isConnected()) {
                String command = scanner.nextLine();
                process(command);
            }
            return null;
        }
    };


    void initialize() {
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                try {
                    socket = new Socket("localhost", 8050);
                    scanner = new Scanner(socket.getInputStream());
                    formatter = new Formatter(socket.getOutputStream());
                } catch (Exception e) {
                    System.err.println("Can not connect to Server :/");
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    boolean checkId(String id) throws IOException {
        formatter.format(id + "\n");
        String result = scanner.nextLine();
        if (result.equals("userName inValid")) {
            scanner.close();
            formatter.close();
            return false;
        } else {
            int port = scanner.nextInt();
            formatter.format("Connected with port : " + port + '\n');
            formatter.close();
            scanner.close();
            while(true ){
                try {
                    socket = new Socket("localhost", port);
                    break ;
                } catch(Exception e) {};
            }
            scanner = new Scanner(socket.getInputStream());
            formatter = new Formatter(socket.getOutputStream());
            return true;
        }

    }

    public void run() {
        new Thread(read).start();
    }

    private void process(String command) {
    }
}
