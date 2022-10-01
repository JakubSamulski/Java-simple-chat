import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {

    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }
    public void startServer(){
        try{
            while (!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                System.out.println("new client connected");
                ClientHandler clientHandler = new ClientHandler(socket);

                Thread thread = new Thread(clientHandler);
                thread.start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        closeServerSocket();
    }

    public void closeServerSocket(){
        try {
            if(serverSocket !=null){
                serverSocket.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private static int parsePortNumber(String arg0){
        try{
            return Integer.parseInt(arg0);
        }catch (NumberFormatException e){
            System.out.println("check if port number is valid");
            return -1;
        }
    }

    public static void main(String args []) throws IOException {

        int portNumber= parsePortNumber(args[0]);
        if(portNumber==-1){
            System.exit(0);
        }

        ServerSocket serverSocket = new ServerSocket(portNumber);
        Server server = new Server(serverSocket);
        server.startServer();

    }
}