import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.*;
import java.io.*;

import javax.swing.*;

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
        JFrame frame = new JFrame("Server client");
        JButton btn = new JButton("Connect");

        JLabel label = new JLabel("Enter ip below");
        JTextField txIn = new JTextField(16);
        
        label.setBounds(50, 100, 200, 30);
        txIn.setBounds(50, 130, 200, 30);
        btn.setBounds(50, 170, 200, 40);

        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Client client = new Client(txIn.getText(), 5000);
            }
        });


        frame.add(txIn);
        frame.add(label);

        frame.add(btn);

        frame.setSize(330, 400);
        frame.setLayout(null);
        frame.setVisible(true);
    }
}
