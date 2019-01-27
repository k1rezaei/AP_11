import javafx.concurrent.Task;

import java.io.IOException;
import java.net.Socket;
import java.util.Formatter;
import java.util.Scanner;

public class Client {

    public static String end = "#";

    View view;
    Socket socket;
    Scanner scanner;
    Formatter formatter;

    Chatroom chatroom = new Chatroom(view);
    Scoreboard scoreboard = new Scoreboard(view);
    MultiPlayerMenu multiPlayerMenu = new MultiPlayerMenu(view);

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public MultiPlayerMenu getMultiPlayerMenu() {
        return multiPlayerMenu;
    }

    public Chatroom getChatroom() {
        return chatroom;
    }

    Client(View view){
        this.view = view;
    }


    Task<Void> read = new Task<Void>() {
        @Override
        protected Void call() throws Exception {

            while (socket.isConnected()) {
                String command = scanner.nextLine();
                StringBuilder s = new StringBuilder();
                while(true) {
                    String line = scanner.nextLine();
                    if(line.equals(end)) break;
                    s.append(line + "\n");
                }
                process(command, s.toString());
            }
            return null;
        }
    };



    void initialize() {
        try {
            socket = new Socket("localhost", 8050);
            scanner = new Scanner(socket.getInputStream());
            formatter = new Formatter(socket.getOutputStream());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    boolean checkId(String id) throws IOException {
        System.err.println(socket.isConnected());
        System.err.println("id is :" + id);
        formatter.format(id + "\n");
        formatter.flush();
        String result = scanner.nextLine();
        System.err.println("result is :" + result);
        if (result.equals("userName inValid")) {
            scanner.close();
            formatter.close();
            return false;
        } else {
            int port = scanner.nextInt();
            System.err.println("port is " + port);
            formatter.format("Connected with port : " + port + '\n');
            formatter.flush();
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

    void command(String commond) {
        formatter.format(commond + "\n");
        formatter.flush();
    }

    private void process(String command, String text) {
        if(command.equals("set_text")) {
            //chatroom.setText(text);
        }
    }
}
