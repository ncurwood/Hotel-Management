package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HotelSystemGUI extends JPanel implements ActionListener {

    private HotelSystem hotelSystem;

    private JButton clockInButton;
    private JButton clockOutButton;
    private JButton makeBookingButton;
    private JButton cancelBookingButton;
    private JButton updateRoomStatusButton;
    private JButton cashInLoyaltyPointsButton;
    private final JButton updatePersonalPhone;
    private final JButton changeShiftsButton;
    private final int employeeid;
    private final String url;
    private final String usernamedb;
    private final String passworddb;

    public HotelSystemGUI(int employee, String url, String usernamedb, String passworddb) {
        setLayout(new GridLayout(8, 1));
        employeeid = employee;
        this.url = url;
        this.usernamedb = usernamedb;
        this.passworddb = passworddb;

        clockInButton = new JButton("Clock In");
        clockOutButton = new JButton("Clock Out");
        makeBookingButton = new JButton("Make Booking");
        cancelBookingButton = new JButton("Cancel Booking");
        updateRoomStatusButton = new JButton("Update Room Status");
        cashInLoyaltyPointsButton = new JButton("Cash In Loyalty Points");
        updatePersonalPhone = new JButton("Update Personal Phone");
        changeShiftsButton = new JButton("Change Shifts");

        clockInButton.addActionListener(this);
        clockOutButton.addActionListener(this);
        makeBookingButton.addActionListener(this);
        cancelBookingButton.addActionListener(this);
        updateRoomStatusButton.addActionListener(this);
        cashInLoyaltyPointsButton.addActionListener(this);
        updatePersonalPhone.addActionListener(this);
        changeShiftsButton.addActionListener(this);

        add(clockInButton);
        add(clockOutButton);
        add(makeBookingButton);
        add(cancelBookingButton);
        add(updateRoomStatusButton);
        add(cashInLoyaltyPointsButton);
        add(updatePersonalPhone);
        add(changeShiftsButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        try (Connection connection = DriverManager.getConnection(url, usernamedb, passworddb)) {
            hotelSystem = new HotelSystem(connection);

            if (e.getSource() == clockInButton) {
                String password = JOptionPane.showInputDialog("Enter Password:");

                if (hotelSystem.authenticateEmployee(employeeid, password)) {
                    LocalDate currentDate = LocalDate.now();
                    LocalTime currentTime = LocalTime.now();
                    hotelSystem.clockIn(employeeid, currentDate.toString(), currentTime.toString().substring(0, 5));
                    JOptionPane.showMessageDialog(this, "Clock in successful");
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid employee password");
                }
            } else if (e.getSource() == clockOutButton) {
                String password = JOptionPane.showInputDialog("Enter Password:");

                if (hotelSystem.authenticateEmployee(employeeid, password)) {
                    LocalDate currentDate = LocalDate.now();
                    LocalTime currentTime = LocalTime.now();
                    hotelSystem.clockOut(employeeid, currentDate.toString(), currentTime.toString().substring(0, 5));
                    JOptionPane.showMessageDialog(this, "Clock out successful");
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid employee password");
                }
            } else if (e.getSource() == makeBookingButton) {

                String customerCard = JOptionPane.showInputDialog("Enter Guest Card Number:");
                if (!hotelSystem.authenticateGuest(customerCard)) {
                    String fname = JOptionPane.showInputDialog("Enter New Guest's First Name:");
                    String lname = JOptionPane.showInputDialog("Enter New Guest's Last Name:");
                    hotelSystem.saveGuest(customerCard, fname, lname);
                    hotelSystem.initLoyalty(customerCard);
                }

                LocalDate checkInDate = LocalDate.parse(JOptionPane.showInputDialog("Enter Check-In Date (YYYY-MM-DD):"));

                String[] buildingIDs = hotelSystem.getBuildings();
                String buildingid = (String) JOptionPane.showInputDialog(null, "Select Building ID:", "Building ID", JOptionPane.QUESTION_MESSAGE, null, buildingIDs, buildingIDs[0]);

                String[] roomNumbers = hotelSystem.getRooms(buildingid, true);
                String roomnumber = (String) JOptionPane.showInputDialog(null, "Select Room Number:", "Room Number", JOptionPane.QUESTION_MESSAGE, null, roomNumbers, roomNumbers[0]);

                String[] computers = hotelSystem.getComputers();
                String computerid = (String) JOptionPane.showInputDialog(null, "Select Computer ID:", "Computer ID", JOptionPane.QUESTION_MESSAGE, null, computers, computers[0]);

                String[] pkgs = hotelSystem.getPackages();
                String packagename = (String) JOptionPane.showInputDialog(null, "Select a Guest Package:", "Guest Package", JOptionPane.QUESTION_MESSAGE, null, pkgs, pkgs[0]);

                int status = hotelSystem.makeBooking(customerCard, buildingid, Integer.parseInt(roomnumber), checkInDate.toString(), Integer.parseInt(computerid), employeeid, packagename);
                hotelSystem.increaseLoyalty(customerCard, 100);
                if (status != -1) {
                    JOptionPane.showMessageDialog(this, "Booking successful!");
                } else {
                    JOptionPane.showMessageDialog(this, "Booking failed! Room not available.");
                }
            } else if (e.getSource() == cancelBookingButton) {

                String[] buildingIDs = hotelSystem.getBuildings();
                String buildingid = (String) JOptionPane.showInputDialog(null, "Select Building ID:", "Building ID", JOptionPane.QUESTION_MESSAGE, null, buildingIDs, buildingIDs[0]);

                String[] roomNumbers = hotelSystem.getRooms(buildingid, false);
                String roomnumber = (String) JOptionPane.showInputDialog(null, "Select Room Number:", "Room Number", JOptionPane.QUESTION_MESSAGE, null, roomNumbers, roomNumbers[0]);

                String[] datesBooked = hotelSystem.getBookingDates(buildingid, Integer.parseInt(roomnumber));
                if (datesBooked.length == 0) JOptionPane.showMessageDialog(this, "No dates booked for this room");
                else {
                    String date = (String) JOptionPane.showInputDialog(null, "Select Date:", "Booked Dates", JOptionPane.QUESTION_MESSAGE, null, datesBooked, datesBooked[0]);

                    if (hotelSystem.cancelBooking(buildingid, Integer.parseInt(roomnumber), date))
                        JOptionPane.showMessageDialog(this, "Booking canceled successfully");
                }
            } else if (e.getSource() == updateRoomStatusButton) {
                String[] buildingIDs = hotelSystem.getBuildings();
                String buildingid = (String) JOptionPane.showInputDialog(null, "Select Building ID:", "Building ID", JOptionPane.QUESTION_MESSAGE, null, buildingIDs, buildingIDs[0]);

                String[] roomNumbers = hotelSystem.getRooms(buildingid, false);
                String roomNumber = (String) JOptionPane.showInputDialog(null, "Select Room Number:", "Room Number", JOptionPane.QUESTION_MESSAGE, null, roomNumbers, roomNumbers[0]);

                String[] status = {"Vacant", "Occupied", "Unready"};
                String newStatus = (String) JOptionPane.showInputDialog(null, "Enter New Room Status:", "Room Status", JOptionPane.QUESTION_MESSAGE, null, status, status[0]);

                boolean updated = hotelSystem.updateRoomStatus(buildingid, Integer.parseInt(roomNumber), newStatus);
                if (updated) {
                    JOptionPane.showMessageDialog(this, "Room status updated successfully");

                } else {
                    JOptionPane.showMessageDialog(this, "");
                }
            } else if (e.getSource() == cashInLoyaltyPointsButton) {
                String card = JOptionPane.showInputDialog("Enter Customer Card Number:");
                boolean cashedIn = false;
                int points = 0;
                if (hotelSystem.authenticateGuest(card)) {
                    points = hotelSystem.getLoyalty(card);
                    cashedIn = hotelSystem.cashInLoyaltyPoints(card, points);
                }
                if (cashedIn) {
                    JOptionPane.showMessageDialog(this, "Loyalty points cashed in successfully");
                    LocalDate checkInDate = LocalDate.parse(JOptionPane.showInputDialog("Enter Check-In Date (YYYY-MM-DD):"));

                    String[] buildingIDs = hotelSystem.getBuildings();
                    String buildingid = (String) JOptionPane.showInputDialog(null, "Select Building ID:", "Building ID", JOptionPane.QUESTION_MESSAGE, null, buildingIDs, buildingIDs[0]);

                    String[] roomNumbers = hotelSystem.getRooms(buildingid, true);
                    String roomnumber = (String) JOptionPane.showInputDialog(null, "Select Room Number:", "Room Number", JOptionPane.QUESTION_MESSAGE, null, roomNumbers, roomNumbers[0]);

                    String[] computers = hotelSystem.getComputers();
                    String computerid = (String) JOptionPane.showInputDialog(null, "Select Computer ID:", "Computer ID", JOptionPane.QUESTION_MESSAGE, null, computers, computers[0]);

                    hotelSystem.makeBooking(card, buildingid, Integer.parseInt(roomnumber), checkInDate.toString(), Integer.parseInt(computerid), employeeid, "Standard");

                } else {
                    JOptionPane.showMessageDialog(this, "Invalid customer ID or insufficient points");
                }
            } else if (e.getSource() == updatePersonalPhone) {
                String password = JOptionPane.showInputDialog("Enter Employee Password:");
                boolean updated = false;
                String newPhone = new String();
                newPhone = JOptionPane.showInputDialog("Enter New Phone XXX-XXX-XXX:");
                if (newPhone.length() == 12 && hotelSystem.authenticateEmployee(employeeid, password)) {
                    updated = hotelSystem.updatePersonalPhone(employeeid, newPhone);
                }
                if (updated) {
                    JOptionPane.showMessageDialog(this, "Personal information updated successfully");
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid employee password or phone number");
                }
            } else if (e.getSource() == changeShiftsButton) {
                boolean changed = false;
                String date = JOptionPane.showInputDialog("Enter Shift Date (YYYY-MM-DD):");

                String[] employees = hotelSystem.getEmployees(date);
                if (employees.length != 0) {
                    String oldEmployeeID = (String) JOptionPane.showInputDialog(null, "Select Employee ID:", "EmployeeID", JOptionPane.QUESTION_MESSAGE, null, employees, employees[0]);

                    String[] shifts = hotelSystem.getShifts(Integer.parseInt(oldEmployeeID), date);
                    if (shifts.length != 0) {
                        String startTime = (String) JOptionPane.showInputDialog(null, "Select Shift Time:", "Shift", JOptionPane.QUESTION_MESSAGE, null, shifts, shifts[0]);


                        changed = hotelSystem.changeShifts(employeeid, Integer.parseInt(oldEmployeeID), date, startTime);
                    }
                }
                if (changed) {
                    JOptionPane.showMessageDialog(this, "Shifts changed successfully");
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid employee ID or Shift Date");
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(HotelSystemGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
