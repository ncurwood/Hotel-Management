package com.company;

import java.io.IOException;

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