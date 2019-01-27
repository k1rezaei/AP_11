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
                    System.err.println("connected new user");
                    Formatter formatter = new Formatter(socket.getOutputStream());
                    Scanner scanner = new Scanner(socket.getInputStream());
                    String id = scanner.nextLine();
                    System.err.println("userid is : " + id);
                    if (validId(id)) {
                        System.err.println("valid");
                        formatter.format("userName Valid\n");
                        formatter.flush();
                    } else {
                        System.err.println("not valid");
                        formatter.format("userName inValid\n");
                        formatter.flush();
                        socket.close();
                        continue;
                    }

                    formatter.format(Integer.toString(cnt) + '\n');
                    System.err.println("port is " + cnt);
                    formatter.flush();

                    System.err.println("waiting for finishing");
                    String connected = scanner.nextLine();
                    System.err.println("connected");

                    formatter.close();
                    scanner.close();

                    serverSocket.close();
                    serverSocket = new ServerSocket(cnt);
                    cnt++;
                    socket = serverSocket.accept();
                    System.err.println("User adding");
                    Profile profile = new Profile(id, id, socket, new Formatter(socket.getOutputStream()),
                            new Scanner(socket.getInputStream()));
                    profiles.add(profile);
                    System.err.println("User added");

                } catch (Exception e) {
                    System.err.println("Server's problem");
                    e.printStackTrace();
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
