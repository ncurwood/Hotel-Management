package com.company;

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
    JTextField employeeid;
    JLabel label_password, label_username;
    JButton btn;
    HotelFrame currentFrame;

    public LoginPanel(HotelFrame frame) throws IOException {
        currentFrame = frame;
        setLayout(null);

        label_username = new JLabel("ID Number");
        label_username.setBounds(200, 300, 100, 40);

        employeeid = new JTextField();
        employeeid.setBounds(300, 300, 300, 40);

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
        add(employeeid);
        add(label_username);
        add(label_password);
        add(password);

        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String url = "jdbc:mysql://localhost:3306/myHotelSystem";
        String usernamedb = "root";
        String passworddb = "password";
        HotelSystem hotelSystem = null;
        boolean authenticateEmployee = false;
        try (Connection connection = DriverManager.getConnection(url, usernamedb, passworddb)) {
            hotelSystem = new HotelSystem(connection);

            try {
                authenticateEmployee = hotelSystem.authenticateEmployee(Integer.parseInt(employeeid.getText()), password.getText());
            } catch (SQLException ex) {
                Logger.getLogger(LoginPanel.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Login Error");
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginPanel.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Connection Error");
        }
        if (authenticateEmployee) {
            try {
                currentFrame.menu(Integer.parseInt(employeeid.getText()),url, usernamedb, passworddb);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Login Error");
        }
    }
}
