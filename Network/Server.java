import javafx.concurrent.Task;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Formatter;

public class Server {
    Task task = new Task<Void>() {
        @Override
        public Void call() {
            int cnt = 8051;
            while (true) {
                ServerSocket serverSocket;
                try {
                    serverSocket = new ServerSocket(8050);
                    Socket socket = serverSocket.accept();
                    Formatter formatter = new Formatter(socket.getOutputStream());
                    formatter.format(Integer.toString(cnt) + '\n');
                    formatter.flush();
                    formatter.close();
                    serverSocket.close();
                    serverSocket = new ServerSocket(cnt);
                    cnt++;
                    socket = serverSocket.accept();
                    formatter = new Formatter(socket.getOutputStream());
                    formatter.format("OK\n");
                    formatter.flush();
                } catch (Exception e) {
                }

            }
        }
    };
            new Thread(task).start();

}
