import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

public class ServerListener  implements Runnable {

    private BufferedReader fromServer;
    private Socket socket;

    public ServerListener(BufferedReader bufferedReader,Socket socket){
        this.fromServer = bufferedReader;
        this.socket = socket;
        Thread thread = new Thread(this);
        thread.start();

    }

    @Override
    public void run() {
        String message;
        while (socket.isConnected()){
            try {
                message = fromServer.readLine();
                System.out.println(message);
            }catch (IOException e){
                System.out.println("Check if server is up ");
                System.exit(0);
            }
        }
    }

}
