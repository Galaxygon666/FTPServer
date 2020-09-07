import java.net.*;
import java.io.*;

public class Server
{
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream input = null;


    public Server(int port)
    {
        try
        {
            server = new ServerSocket(port);
            System.out.println("Server started...");

            System.out.println("Waiting for connection...");

            socket = server.accept();
            System.out.println("Connection accepted!");

            input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            String line = "";

            while (!line.equals("Over"))
            {
                try
                {
                    line = input.readUTF();
                    System.out.println(line);
                }
                catch (IOException i)
                {
                    System.out.println(i);
                }
            }
            System.out.println("Connection closing");

            socket.close();
            input.close();
        }

        catch (IOException i)
        {
            System.out.println(i);
        }
    }



    public static void main(String args[])
    {
        Server server = new Server(5000);
    }
}
