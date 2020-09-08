import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.*;
import java.io.*;

public class Server
{
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream input = null;

    static JFrame frame;
    static JButton btn;
    static JLabel label;
    static JLabel start;
    static JLabel wait;


    public Server(int port)
    {
        try
        {
            server = new ServerSocket(port);
            System.out.println("Server started...");
            System.out.println("Waiting for connection...");

            socket = server.accept();
            System.out.println("Connection accepted!");
            start.setText("Connection made!");

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
                    break;
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
        frame = new JFrame("Server client");
        btn = new JButton("Start server");


        label = new JLabel("Start the server");
        start = new JLabel("Waiting for connection...");

        start.setVisible(false);

        label.setBounds(50, 100, 200, 30);
        start.setBounds(50, 170, 200, 30);
        btn.setBounds(50, 130, 200, 40);

        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                new Thread(() -> {
                    Server server = new Server(5000);
                }).start();

                start.setVisible(true);
            }
        });

        frame.add(label);
        frame.add(btn);
        frame.add(start);

        frame.setSize(330, 400);
        frame.setLayout(null);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });

    }
}
