/*TCSS 445
Hotel Management Database Project
05/31/2023
Nicholas Curwood
Anthony Owens
 */
import java.io.IOException;

//Initialize the frame and login sequence
public class Controller {
    HotelFrame frame;
    public Controller() throws IOException {
        frame = new HotelFrame();
        initLogin();
    }

    private void initLogin() throws IOException {
        frame.login();
        
    }
}