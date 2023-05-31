/*TCSS 445
Hotel Management Database Project
05/31/2023
Nicholas Curwood
Anthony Owens
 */
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginPanel extends JPanel implements ActionListener {

    JPasswordField password;
    JTextField employee_id;
    JLabel label_password, label_username;
    JButton btn;
    HotelFrame currentFrame;

    public LoginPanel(HotelFrame frame) throws IOException {
        currentFrame = frame;
        setLayout(null);

        label_username = new JLabel("ID Number");
        label_username.setBounds(200, 300, 100, 40);

        employee_id = new JTextField();
        employee_id.setBounds(300, 300, 300, 40);

        label_password = new JLabel("Password");
        label_password.setBounds(200, 350, 100, 40);

        password = new JPasswordField();
        password.setBounds(300, 350, 300, 40);

        btn = new JButton("Sign in");
        btn.setBounds(300, 400, 100, 40);
        btn.addActionListener(this);

        BufferedImage myPicture = ImageIO.read(new File("profile.png"));
        JLabel image = new JLabel(new ImageIcon(myPicture));

        image.setBounds(250, 100, 300, 200);

        add(image);
        add(btn);
        add(employee_id);
        add(label_username);
        add(label_password);
        add(password);

        setVisible(true);

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        //Once we click sign in, we want to open our connection to the database
        String url = "jdbc:mysql://localhost:3306/myHotelSystem";
        String username = "root";
        String password = "password";
        //Create our hotelSystem(the class that interacts directly with the database)
        HotelSystem hotelSystem;
        //We need to make sure the username and password match to a real employee
        boolean authenticateEmployee = false;
        //Create our connection to the database for our hotelSystem
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            hotelSystem = new HotelSystem(connection);

            try {
                //Check hotelSystem if this employee exists
                authenticateEmployee = hotelSystem.authenticateEmployee(Integer.parseInt(employee_id.getText()), this.password.getText());
            } catch (SQLException ex) {//Our sql statements were bad
                Logger.getLogger(LoginPanel.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Login Error");
            } catch (NumberFormatException ex){// The user input a letter for our ID, this will cause an exception when we try to parseInt
                JOptionPane.showMessageDialog(this, "Login Error, ID is a number");
            }
        } catch (SQLException ex) {// Failed to connect to the database
            Logger.getLogger(LoginPanel.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Connection Error");
        }
        if (authenticateEmployee) {//Once the user puts the correct username password pair, continue to menu (HotelSystemGUI)
            try {
                currentFrame.menu(Integer.parseInt(employee_id.getText()),url, username, password);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Login Error, Username or Password is Incorrect");
        }
    }
}
