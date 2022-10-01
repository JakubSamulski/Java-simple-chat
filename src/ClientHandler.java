import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{


    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket clientSocket;
    private PrintWriter streamToClients;
    private BufferedReader streamFromClients;
    private String username;

    public ClientHandler(Socket clientSocket) throws IOException {
        this.clientSocket =clientSocket;
        this.streamToClients = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()),true);
        this.streamFromClients = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.username = streamFromClients.readLine();
        clientHandlers.add(this);
        broadcast(this.username+" has connected","server");

    }

    public void broadcast(String message,String sender){
        for (ClientHandler clientHandler:clientHandlers){
            if(clientHandler!=this){
                clientHandler.streamToClients.println(sender+"> "+message);
            }
        }
    }



    @Override
    public void run() {
        String message;
        while (clientSocket.isConnected()&&clientHandlers.contains(this)){
            try{
                message = streamFromClients.readLine();
                broadcast(message,this.username);
            } catch (IOException e) {
                System.out.println(username+" has disconected");
                broadcast(username+" has disconected","server");
                closeResources();
                break;
            }
        }
        closeResources();
    }


    private void closeResources()  {
        clientHandlers.remove(this);
        try{
            if(this.clientSocket!= null){
                this.clientSocket.close();
            }
            if(this.streamFromClients != null){
                this.streamFromClients.close();
            }
            if(this.streamToClients != null){
                this.streamToClients.close();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}