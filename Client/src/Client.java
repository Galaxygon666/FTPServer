import java.net.*;
import java.io.*;

public class Client
{
    private Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream output = null;
    private BufferedReader br = null;

    public Client(String address, int port)
    {
        try
        {
            socket = new Socket(address, port);
            System.out.println("Connected to server!");


            input = new DataInputStream(socket.getInputStream());
            br = new BufferedReader(new InputStreamReader(input));
            output = new DataOutputStream(socket.getOutputStream());

        }
        catch(IOException u)
        {
            System.out.println(u);
        }

        String line = "";

        while (!line.equals("Over"))
        {
            try
            {
                line = br.readLine();
                output.writeUTF(line);

            }
            catch (IOException i)
            {
                System.out.println(i);
                System.out.println("Connection lost!");
                break;
            }
        }

        try
        {
            input.close();
            output.close();
            socket.close();

        }
        catch (IOException i)
        {
            System.out.println(i);
        }
    }



    public static void main(String[] args)
    {
        Client client = new Client("127.0.0.1", 5000);
    }
}
