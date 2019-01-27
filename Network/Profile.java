import java.net.Socket;
import java.util.Formatter;
import java.util.Scanner;

public class Profile {
    String id, name;
    Socket socket;
    Formatter formatter;
    Scanner scanner;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Profile(String id, String name, Socket socket, Formatter formatter, Scanner scanner) {
        this.id = id;
        this.name = name;
        this.socket = socket;
        this.formatter = formatter;
        this.scanner = scanner;
    }
}
