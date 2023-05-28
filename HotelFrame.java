import javax.swing.*;
import java.awt.*;
import java.io.IOException;

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
        //pack();
        add(MAIN_PANEL,BorderLayout.CENTER);
    }

    protected void login() throws IOException {
        clearPanels();
        MAIN_PANEL.add(new LoginPanel(), BorderLayout.CENTER);
        setVisible(true);
    }

    public void clearPanels() {
        MAIN_PANEL.removeAll();
    }

}