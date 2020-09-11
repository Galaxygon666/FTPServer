import java.awt.*;
import java.awt.color.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;


import java.net.*;
import java.io.*;



public class Client
{
    private Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream output = null;
    private BufferedReader br = null;
    private BufferedOutputStream bos = null;
    private FileOutputStream fos = null;

    static JFrame frame;
    static JPanel panel;
    static JLabel title;
    static JLabel status;
    static JTextField text;
    static JButton conn;

    public Client(String address, int port)
    {
        int filesize=6022386;
        int bytesRead;
        int current = 0;

        try
        {
            socket = new Socket(address, port);
            System.out.println("Connected to server!");
            status.setVisible(true);


            input = new DataInputStream(socket.getInputStream());
            br = new BufferedReader(new InputStreamReader(input));
            output = new DataOutputStream(socket.getOutputStream());

            // receive file
            byte[] bytes  = new byte[filesize];
            InputStream is = socket.getInputStream();
            fos = new FileOutputStream("test2.txt");
            bos = new BufferedOutputStream(fos);
            bytesRead = is.read(bytes, 0, bytes.length);
            current = bytesRead;
            // thanks to A. Cdiz for the bug fix
            do {
                bytesRead =
                        is.read(bytes, current, (bytes.length-current));
                if(bytesRead >= 0) current += bytesRead;
            } while(bytesRead > -1);
            bos.write(bytes, 0 , current);
            bos.flush();

        }
        catch(IOException u)
        {
            System.out.println(u);
            status.setText("There was an error connecting");
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
            bos.close();

        }
        catch (IOException i)
        {
            System.out.println(i);
        }
    }



    public static void main(String[] args)
    {
        frame = new JFrame("DragonFiles: Client side");
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage("./resources/Icon.png"));
        panel = new JPanel();
        panel.setBounds(0,0,400,400);
        panel.setBackground(new Color(49, 49, 49));

        // Input field Label
        title = new JLabel("Enter ip");
        title.setForeground(new Color(255, 255, 255));
        title.setFont(new Font("Arial", Font.PLAIN, 18));
        title.setBounds(90,20,200,30);

        // Ip input field
        text = new JTextField();
        text.setBounds(90,55,200,30);

        // Server connect button
        conn = new JButton("Connect");
        conn.setBackground(new Color(202, 62, 71));
        conn.setForeground(Color.white);
        conn.setBounds(90,100,200,30);

        // Connection made Lable
        status = new JLabel();
        status.setBounds(90,130,200,30);
        title.setFont(new Font("Arial", Font.PLAIN, 14));
        status.setForeground(Color.white);
        status.setVisible(false);


        //Connect button connect function
        conn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                new Thread(() -> {
                    Client client = new Client(text.getText(), 5000);
                }).start();

                status.setText("Connected to: " + text.getText());
            }
        });

        // Elements
        frame.add(title);
        frame.add(text);
        frame.add(conn);
        frame.add(status);

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
