/*TCSS 445
Hotel Management Database Project
05/31/2023
Nicholas Curwood
Anthony Owens
 */
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

//Purpose of HotelSystemGUI is facilitating IO
public class HotelSystemGUI extends JPanel implements ActionListener {

    private final JButton clockInButton;
    private final JButton clockOutButton;
    private final JButton makeBookingButton;
    private final JButton cancelBookingButton;
    private final JButton updateRoomStatusButton;
    private final JButton cashInLoyaltyPointsButton;
    private final JButton updatePersonalPhone;
    private final JButton changeShiftsButton;
    private final int employee_id;
    private final String url;
    private final String username;
    private final String password;

    public HotelSystemGUI(int employee, String url, String username_db, String password_db) {
        setLayout(new GridLayout(8, 1));
        employee_id = employee;
        this.url = url;
        this.username = username_db;
        this.password = password_db;

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
    private void clockIn(HotelSystem hotelSystem) throws SQLException {
        String password = JOptionPane.showInputDialog("Enter Password:");

        if (hotelSystem.authenticateEmployee(employee_id, password)) {
            LocalDate currentDate = LocalDate.now();
            LocalTime currentTime = LocalTime.now();
            hotelSystem.clockIn(employee_id, currentDate.toString(), currentTime.toString().substring(0, 5));
            JOptionPane.showMessageDialog(this, "Clock in successful");
        } else {
            JOptionPane.showMessageDialog(this, "Invalid employee password");
        }
    }
    //Gather Employee Password (Security)
    //Edge cases: authenticate passoword to employeeid
    private void clockOut(HotelSystem hotelSystem) throws SQLException {
        String password = JOptionPane.showInputDialog("Enter Password:");

        if (hotelSystem.authenticateEmployee(employee_id, password)) {
            LocalDate currentDate = LocalDate.now();
            LocalTime currentTime = LocalTime.now();
            hotelSystem.clockOut(employee_id, currentDate.toString(), currentTime.toString().substring(0, 5));
            JOptionPane.showMessageDialog(this, "Clock out successful");
        } else {
            JOptionPane.showMessageDialog(this, "Invalid employee password");
        }
    }
    //Gather Guest card Number, check in date, Building ID, Room, and package (user inputs computer ID)
    //Edge Cases: User is not Registered, user also does not have loyalty points initialized
    private void makeBooking(HotelSystem hotelSystem) throws SQLException {
        String customerCard = JOptionPane.showInputDialog("Enter Guest Card Number:");
        if (!hotelSystem.authenticateGuest(customerCard)) {
            String fName = JOptionPane.showInputDialog("Enter New Guest's First Name:");
            String lName = JOptionPane.showInputDialog("Enter New Guest's Last Name:");
            hotelSystem.saveGuest(customerCard, fName, lName);
            hotelSystem.initLoyalty(customerCard);
        }

        LocalDate checkInDate = LocalDate.parse(JOptionPane.showInputDialog("Enter Check-In Date (YYYY-MM-DD):"));

        String[] buildingIDs = hotelSystem.getBuildings();
        String building_id = (String) JOptionPane.showInputDialog(null, "Select Building ID:", "Building ID", JOptionPane.QUESTION_MESSAGE, null, buildingIDs, buildingIDs[0]);

        String[] roomNumbers = hotelSystem.getRooms(building_id, true);
        String room_number = (String) JOptionPane.showInputDialog(null, "Select Room Number:", "Room Number", JOptionPane.QUESTION_MESSAGE, null, roomNumbers, roomNumbers[0]);

        String[] computers = hotelSystem.getComputers();
        String computer_id = (String) JOptionPane.showInputDialog(null, "Select Computer ID:", "Computer ID", JOptionPane.QUESTION_MESSAGE, null, computers, computers[0]);

        String[] packages = hotelSystem.getPackages();
        String guest_package = (String) JOptionPane.showInputDialog(null, "Select a Guest Package:", "Guest Package", JOptionPane.QUESTION_MESSAGE, null, packages, packages[0]);

        int status = hotelSystem.makeBooking(customerCard, building_id, Integer.parseInt(room_number), checkInDate.toString(), Integer.parseInt(computer_id), employee_id, guest_package);
        hotelSystem.increaseLoyalty(customerCard);
        if (status != -1) {
            JOptionPane.showMessageDialog(this, "Booking successful!");
        } else {
            JOptionPane.showMessageDialog(this, "Booking failed! Room not available.");
        }
    }
    //Gather the Building ID, the Room Number, and the Date
    //Edge Cases: That specific room is not booked
    private void cancelBooking(HotelSystem hotelSystem) throws SQLException {
        String[] buildingIDs = hotelSystem.getBuildings();
        String building_id = (String) JOptionPane.showInputDialog(null, "Select Building ID:", "Building ID", JOptionPane.QUESTION_MESSAGE, null, buildingIDs, buildingIDs[0]);

        String[] roomNumbers = hotelSystem.getRooms(building_id, false);
        String room_number = (String) JOptionPane.showInputDialog(null, "Select Room Number:", "Room Number", JOptionPane.QUESTION_MESSAGE, null, roomNumbers, roomNumbers[0]);

        String[] datesBooked = hotelSystem.getBookingDates(building_id, Integer.parseInt(room_number));
        if (datesBooked.length == 0) JOptionPane.showMessageDialog(this, "No dates booked for this room");
        else {
            String date = (String) JOptionPane.showInputDialog(null, "Select Date:", "Booked Dates", JOptionPane.QUESTION_MESSAGE, null, datesBooked, datesBooked[0]);

            if (hotelSystem.cancelBooking(building_id, Integer.parseInt(room_number), date))
                JOptionPane.showMessageDialog(this, "Booking canceled successfully");
        }
    }
    //Gather Building ID, Room Number, and the New Room Status
    //Edge Cases: There should not be any way that the user could input garbage data
    private void updateRoomStatus(HotelSystem hotelSystem) throws SQLException {
        String[] buildingIDs = hotelSystem.getBuildings();
        String building_id = (String) JOptionPane.showInputDialog(null, "Select Building ID:", "Building ID", JOptionPane.QUESTION_MESSAGE, null, buildingIDs, buildingIDs[0]);

        String[] roomNumbers = hotelSystem.getRooms(building_id, false);
        String roomNumber = (String) JOptionPane.showInputDialog(null, "Select Room Number:", "Room Number", JOptionPane.QUESTION_MESSAGE, null, roomNumbers, roomNumbers[0]);

        String[] status = {"Vacant", "Occupied", "Unready"};
        String newStatus = (String) JOptionPane.showInputDialog(null, "Enter New Room Status:", "Room Status", JOptionPane.QUESTION_MESSAGE, null, status, status[0]);

        boolean updated = hotelSystem.updateRoomStatus(building_id, Integer.parseInt(roomNumber), newStatus);
        if (updated) {
            JOptionPane.showMessageDialog(this, "Room status updated successfully");

        } else {
            JOptionPane.showMessageDialog(this, "");
        }
    }
    //Gather just the card number
    //Edge Cases: Guest is not registered, Guest does not have enough points
    private void cashInLoyalty(HotelSystem hotelSystem) throws SQLException {
        String card = JOptionPane.showInputDialog("Enter Customer Card Number:");
        boolean cashedIn = false;
        int points;
        if (hotelSystem.authenticateGuest(card)) {
            points = hotelSystem.getLoyalty(card);
            cashedIn = hotelSystem.cashInLoyaltyPoints(card, points);
        }
        if (cashedIn) {
            JOptionPane.showMessageDialog(this, "Loyalty points cashed in successfully");
            LocalDate checkInDate = LocalDate.parse(JOptionPane.showInputDialog("Enter Check-In Date (YYYY-MM-DD):"));

            String[] buildingIDs = hotelSystem.getBuildings();
            String building_id = (String) JOptionPane.showInputDialog(null, "Select Building ID:", "Building ID", JOptionPane.QUESTION_MESSAGE, null, buildingIDs, buildingIDs[0]);

            String[] roomNumbers = hotelSystem.getRooms(building_id, true);
            String room_number = (String) JOptionPane.showInputDialog(null, "Select Room Number:", "Room Number", JOptionPane.QUESTION_MESSAGE, null, roomNumbers, roomNumbers[0]);

            String[] computers = hotelSystem.getComputers();
            String computer_id = (String) JOptionPane.showInputDialog(null, "Select Computer ID:", "Computer ID", JOptionPane.QUESTION_MESSAGE, null, computers, computers[0]);

            hotelSystem.makeBooking(card, building_id, Integer.parseInt(room_number), checkInDate.toString(), Integer.parseInt(computer_id), employee_id, "Standard");

        } else {
            JOptionPane.showMessageDialog(this, "Invalid customer ID or insufficient points");
        }
    }
    //Gather the Employee Password(security), and New Phone Number
    //Edge Cases: new phone length can not exceed 12 chars
    private void updatePhone(HotelSystem hotelSystem) throws SQLException {
        String password = JOptionPane.showInputDialog("Enter Employee Password:");
        boolean updated = false;
        String newPhone;
        newPhone = JOptionPane.showInputDialog("Enter New Phone XXX-XXX-XXX:");
        if (newPhone.length() == 12 && hotelSystem.authenticateEmployee(employee_id, password)) {
            updated = hotelSystem.updatePersonalPhone(employee_id, newPhone);
        }
        if (updated) {
            JOptionPane.showMessageDialog(this, "Personal information updated successfully");
        } else {
            JOptionPane.showMessageDialog(this, "Invalid employee password or phone number");
        }
    }
    //Gather the Shift, Employee ID, And Number
    //Critical Edge Cases: No employees work that day
    private void changeShift(HotelSystem hotelSystem) throws SQLException {
        boolean changed = false;
        String date = JOptionPane.showInputDialog("Enter Shift Date (YYYY-MM-DD):");

        String[] employees = hotelSystem.getEmployees(date);
        if (employees.length != 0) {
            String oldEmployeeID = (String) JOptionPane.showInputDialog(null, "Select Employee ID:", "EmployeeID", JOptionPane.QUESTION_MESSAGE, null, employees, employees[0]);

            String[] shifts = hotelSystem.getShifts(Integer.parseInt(oldEmployeeID), date);
            if (shifts.length != 0) {
                String startTime = (String) JOptionPane.showInputDialog(null, "Select Shift Time:", "Shift", JOptionPane.QUESTION_MESSAGE, null, shifts, shifts[0]);


                changed = hotelSystem.changeShifts(employee_id, Integer.parseInt(oldEmployeeID), date, startTime);
            }
        }
        if (changed) {
            JOptionPane.showMessageDialog(this, "Shifts changed successfully");
        } else {
            JOptionPane.showMessageDialog(this, "Invalid employee ID or Shift Date");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            HotelSystem hotelSystem = new HotelSystem(connection);

            //Each button triggers a corresponding reactionary function call
            if (e.getSource() == clockInButton) {
                clockIn(hotelSystem);
            } else if (e.getSource() == clockOutButton) {
                clockOut(hotelSystem);
            } else if (e.getSource() == makeBookingButton) {
                makeBooking(hotelSystem);
            } else if (e.getSource() == cancelBookingButton) {
                cancelBooking(hotelSystem);
            } else if (e.getSource() == updateRoomStatusButton) {
                updateRoomStatus(hotelSystem);
            } else if (e.getSource() == cashInLoyaltyPointsButton) {
                cashInLoyalty(hotelSystem);
            } else if (e.getSource() == updatePersonalPhone) {
                updatePhone(hotelSystem);
            } else if (e.getSource() == changeShiftsButton) {
               changeShift(hotelSystem);
            }

        } catch (SQLException ex) {
            Logger.getLogger(HotelSystemGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
