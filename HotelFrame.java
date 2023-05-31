/*TCSS 445
Hotel Management Database Project
05/31/2023
Nicholas Curwood
Anthony Owens
 */
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

//Make our frame
public class HotelFrame extends JFrame {

    JPanel MAIN_PANEL;
    HotelFrame() {
        MAIN_PANEL = new JPanel();
        MAIN_PANEL.setBackground(Color.WHITE);
        MAIN_PANEL.setLayout(new BorderLayout());

        setSize(800, 600);
        setTitle("Hotel Management");
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        add(MAIN_PANEL,BorderLayout.CENTER);
    }

    //Create the login panel
    protected void login() throws IOException {
        clearPanels();
        //We pass this frame to log in so that login may call menu after an action is performed
        MAIN_PANEL.add(new LoginPanel(this), BorderLayout.CENTER);
        setVisible(true);
    }

    //Create the menu panel, this will be called from login
    protected void menu(int employee_id, String url, String username, String password) throws IOException {
        clearPanels();
        MAIN_PANEL.add(new HotelSystemGUI(employee_id,url, username, password));
        setVisible(true);
    }

    public void clearPanels() {
        MAIN_PANEL.removeAll();
    }

}