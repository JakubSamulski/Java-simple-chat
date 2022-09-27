import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{


    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket clientSocket;
    private PrintWriter output;
    private BufferedReader input;
    private String username;

    public ClientHandler(Socket clientSocket) throws IOException {
        this.clientSocket =clientSocket;
        this.output = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()),true);
        this.input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.username = input.readLine();
        clientHandlers.add(this);
        broadcast(this.username+" has connected","server");

    }

    public void broadcast(String message,String sender){
        for (ClientHandler clientHandler:clientHandlers){
            if(clientHandler!=this){
                clientHandler.output.println(sender+"> "+message);
            }
        }
    }



    @Override
    public void run() {
        String message;
        while (clientSocket.isConnected()&&clientHandlers.contains(this)){
            try{
                message = input.readLine();
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
            if(this.input != null){
                this.input.close();
            }
            if(this.output!= null){
                this.output.close();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}