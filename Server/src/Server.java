import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.net.*;
import java.io.*;


public class Server
{
    // Initiate server stuff
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream input = null;

    // GUI elements
    static JFrame frame;
    static JPanel panel;
    static JLabel title;
    static JLabel status;
    static JLabel ip;
    static JButton conn;

    public Server(int port) {
        try
        {
            server = new ServerSocket(port);
            socket = server.accept();

            // Gets client ip and turns it to a string
            String ipClient = socket.getInetAddress().toString();

            // "Prints" connection status and client ip in GUI
            status.setText("Connection made!");
            ip.setText("Connected to: " + ipClient.replace("/", ""));

            // sendfile
            File file = new File ("test.txt");
            byte[] mybytearray  = new byte[(int)file.length()];
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(mybytearray,0,mybytearray.length);
            OutputStream os = socket.getOutputStream();
            System.out.println("Sending...");
            os.write(mybytearray,0,mybytearray.length);
            os.flush();


            // Get data input stream
            input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            String line = "";

            while (!line.equals("Over"))
            {
                try
                {
                    line = input.readUTF();
                    System.out.println(line);
                    status.setText("Connection failed...");
                }
                catch (IOException i)
                {
                    System.out.println(i);
                    break;
                }
            }
            status.setText("Connection lost...");
            ip.setText("");

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
        frame = new JFrame("DragonFiles: Server side");
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage("./resources/Icon.png"));
        panel = new JPanel();
        panel.setBounds(0,0,400,400);
        panel.setBackground(new Color(49, 49, 49));

        // Input field Label
        title = new JLabel("Start the server on port 5000");
        title.setForeground(new Color(255, 255, 255));
        title.setFont(new Font("Arial", Font.PLAIN, 16));
        title.setBounds(90,70,200,30);

        // Server connect button
        conn = new JButton("Start");
        conn.setBackground(new Color(202, 62, 71));
        conn.setForeground(Color.white);
        conn.setBounds(90,100,200,30);

        // Connection made Lable
        status = new JLabel("Waiting for connection...");
        status.setBounds(90,135,200,30);
        status.setFont(new Font("Arial", Font.PLAIN, 14));
        status.setForeground(Color.white);

        // Display client ip
        ip = new JLabel();
        ip.setBounds(90,160,200,30);
        ip.setFont(new Font("Arial", Font.PLAIN, 14));
        ip.setForeground(Color.white);
        ip.setVisible(true);


        //Connect button connect function
        conn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                new Thread(() -> {
                        Server server = new Server( 5000);
                }).start();

            }
        });

        // Elements
        frame.add(title);
        frame.add(conn);
        frame.add(status);
        frame.add(ip);

        // Finalize
        frame.add(panel);
        frame.setSize(400, 400);
        frame.setLayout(null);
        frame.setVisible(true);

        // Exit code on window close
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });

    }
}
