import javafx.concurrent.Task;

import java.net.Socket;
import java.util.Scanner;

public class Client {
    Task task = new Task<Void>() {
        @Override
        public Void call() {
            try {
                Thread.sleep(1000);
                Socket socket = new Socket("localhost", 8050);
                Scanner scanner = new Scanner(socket.getInputStream());
                int x = scanner.nextInt();
                socket.close();
                socket = new Socket("localhost", x);
                scanner = new Scanner(socket.getInputStream());
                System.out.println(scanner.next());
            } catch (Exception e) {

            }
            return null;
        }
    };
            new Thread(task).start();

}
