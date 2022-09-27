import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket clientSocket;
    private BufferedReader input;
    private PrintWriter serverOutput;
    private String username;
    private ServerListener serverListener;

    public Client(Socket socket, String username) throws IOException {

        try {
            this.serverOutput = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
            this.clientSocket =socket;
            this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch (IOException e){
            System.out.println("input/output error");
            closeResources();
            System.exit(0);
        }

        this.username = username;
        this.serverListener = new ServerListener(input, socket);
    }

    public void startChatting() throws IOException {

        serverOutput.println(username);
        Scanner scanner = new Scanner(System.in);


        while (clientSocket.isConnected()){
            String message = scanner.nextLine();
            serverOutput.println(message);
        }
        closeResources();
    }

    private void closeResources() throws IOException {
        try{
            if(this.clientSocket!= null){
                this.clientSocket.close();
            }
            if(this.input != null){
                this.input.close();
            }
            if(this.serverOutput!= null){
                this.serverOutput.close();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }


    public static void main(String []args) throws IOException {
        try( Scanner scanner = new Scanner(System.in);
             Socket socket = new Socket("localhost",1234);
        )
        {
            String username = scanner.nextLine();
            Client client  = new Client(socket,username);
            client.startChatting();
        }
        catch (IOException e){
            System.out.println("Check if ip and port number are correct");
        }
    }
}