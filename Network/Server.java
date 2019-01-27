import javafx.concurrent.Task;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Scanner;

public class Server {
    ArrayList<Profile> profiles = new ArrayList<>();

    Task<Void> task = new Task<Void>() {
        @Override
        public Void call() throws IOException {
            int cnt = 8051;
            while (true) {
                ServerSocket serverSocket;
                try {
                    serverSocket = new ServerSocket(8050);
                    Socket socket = serverSocket.accept();
                    Formatter formatter = new Formatter(socket.getOutputStream());
                    Scanner scanner = new Scanner(socket.getInputStream());
                    String id = scanner.nextLine();
                    if (validId(id)) {
                        formatter.format("userName Valid\n");
                        formatter.flush();
                    } else {
                        formatter.format("userName inValid\n");
                        formatter.flush();
                    }

                    formatter.format(Integer.toString(cnt) + '\n');
                    formatter.flush();

                    String connected = scanner.nextLine();

                    formatter.close();
                    scanner.close();

                    serverSocket.close();
                    serverSocket = new ServerSocket(cnt);
                    cnt++;
                    socket = serverSocket.accept();

                    Profile profile = new Profile(id, id, socket, new Formatter(socket.getOutputStream()),
                            new Scanner(socket.getInputStream()));
                    profiles.add(profile);


                } catch (Exception e) {
                }
            }
        }
    };

    void run() {
        new Thread(task).start();
    }

    private boolean validId(String id) {
        return true;
    }


}
