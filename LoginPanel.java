import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LoginPanel extends JPanel implements ActionListener{

    JPasswordField password;
    JTextField username;
    JLabel label_password,label_username,message,title;
    JButton btn;

    public LoginPanel() throws IOException {
        setLayout(null);

        label_username = new JLabel("ID Number");
        label_username.setBounds(200,300,100,40);

        username = new JTextField();
        username.setBounds(300,300,300,40);

        label_password = new JLabel("Password");
        label_password.setBounds(200,350,100,40);

        password = new JPasswordField();
        password.setBounds(300,350,300,40);

        btn = new JButton("Sign in");
        btn.setBounds(300,400,100,40);
        btn.addActionListener(this);

        BufferedImage myPicture = ImageIO.read(new File("profile.png"));
        JLabel image = new JLabel(new ImageIcon( myPicture));

        image.setBounds(250, 100, 300, 200);

        add(image);
        add(btn);
        add(username);
        add(label_username);
        add(label_password);
        add(password);

        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}

