/*TCSS 445
Hotel Management Database Project
05/31/2023
Nicholas Curwood
Anthony Owens
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HotelSystem {
    private final Connection connection;

    public HotelSystem(Connection connection) throws SQLException {
        this.connection = connection;
    }

    //Start the shift of the current employee
    public void clockIn(int employeeId, String date, String startTime) throws SQLException {
        // Update the employee's shift start time in the database
        String query = "INSERT INTO Shifts (`Employee-ID`, Date, `Start Time`) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, employeeId);
            statement.setString(2, date);
            statement.setString(3, startTime);
            statement.executeUpdate();
        }
    }

    //End the shift of the current employee
    public void clockOut(int employeeId, String date, String endTime) throws SQLException {
        // Update the employee's shift end time in the database
        String query = "UPDATE Shifts SET `End Time` = ? WHERE `Employee-ID` = ? AND Date = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, endTime);
            statement.setInt(2, employeeId);
            statement.setString(3, date);
            statement.executeUpdate();
        }
    }

    //Make a new booking
    public int makeBooking(String guestCardNumber, String buildingId, int roomNumber, String date, int computerId, int employeeId, String packageName) throws SQLException {
        String query = "INSERT INTO Booking (`Room Number`, `Building-ID`, Date, `Guest Card Number`, `Computer-ID`, `Employee-ID`, `Package Name`) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, roomNumber);
            statement.setString(2, buildingId);
            statement.setString(3, date);
            statement.setString(4, guestCardNumber);
            statement.setInt(5, computerId);
            statement.setInt(6, employeeId);
            statement.setString(7, packageName);

            int affectedRows = statement.executeUpdate();

            //If this query worked, return 1 (success)
            if (affectedRows > 0) {
                return 1;
            }
        }

        return -1; // Return -1 if the booking was not successful
    }

    //remove and existing booking
    public boolean cancelBooking(String buildingId, int roomNumber, String date) throws SQLException {
        String query = "DELETE FROM Booking WHERE `Building-ID` = ? AND `Room Number` = ? AND Date = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, buildingId);
            statement.setInt(2, roomNumber);
            statement.setString(3, date);

            int affectedRows = statement.executeUpdate();

            return affectedRows > 0;
        }
    }

    //Update room service after cleaning, booking, or checking out
    public boolean updateRoomStatus(String buildingID, int roomNumber, String newStatus) throws SQLException {
        String query = "UPDATE Room SET `Room Status` = ? WHERE `Building-ID` = ? AND `Room Number` = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, newStatus);
            statement.setString(2, buildingID);
            statement.setInt(3, roomNumber);
            int affectedRows = statement.executeUpdate();

            return affectedRows > 0;
        }
    }

    //Update a phone number
    public boolean updatePersonalPhone(int employeeId, String newPhone) throws SQLException {
        String query = "UPDATE Employee SET `Employee Phone` = ? WHERE `Employee-ID` = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, newPhone);
            statement.setInt(2, employeeId);

            int affectedRows = statement.executeUpdate();

            return affectedRows > 0;
        }
    }

    //Swap employees for an existing shift
    public boolean changeShifts(int newEmployeeID, int oldEmployeeID, String date, String startTime) throws SQLException {
        String query = "UPDATE Shifts SET `Employee-ID` = ? WHERE `Employee-ID` = ? AND `Date` = ? AND `Start Time` = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, newEmployeeID);
            statement.setInt(2, oldEmployeeID);
            statement.setString(3, date);
            statement.setString(4, startTime);

            int affectedRows = statement.executeUpdate();

            return affectedRows > 0;
        }
    }

    //Make sure points are enough
    public Boolean cashInLoyaltyPoints(String card, int points) {
        // Update the guest's loyalty points in the database
        String query = "UPDATE Loyalty_Program SET `Loyalty Points` = ? WHERE `Guest Card Number` = ?";
        if(points<1000)  return false;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, points-1000);
            statement.setString(2, card);
            statement.executeUpdate();
        } catch (SQLException ex) {

            Logger.getLogger(HotelSystem.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;

    }

    //Make sure the ID Password pair exist to an actual employee
    public boolean authenticateEmployee(int employeeId, String password) throws SQLException {
        String query = "SELECT COUNT(*) FROM Employee WHERE `Employee-ID` = ? AND `Employee Password` = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, employeeId);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }

    //Make sure that the guest is registered in the system for loyalty points
    public boolean authenticateGuest(String card) throws SQLException {
        String query = "SELECT COUNT(*) FROM Guest WHERE `Guest Card Number` = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, card);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }

    //Register a new card in the guest registry with their full name
    protected void saveGuest(String card, String fName, String lName){
        String query = "INSERT INTO Guest(`Guest Card Number`, `Guest First Name`, `Guest Last Name`) VALUES(?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, card);
            statement.setString(2, fName);
            statement.setString(3, lName);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Typically follows the saveGuest, new guests need to start will loyalty
    protected void initLoyalty(String card){
        String query = "INSERT INTO Loyalty_Program(`Guest Card Number`,`Loyalty Points`) VALUES(?,0)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, card);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Increase the loyalty of a guest by 100
    protected void increaseLoyalty(String card) throws SQLException {
        int currentLoyalty = getLoyalty(card);
        int newLoyalty = currentLoyalty + 100;

        String query = "UPDATE Loyalty_Program SET `Loyalty Points` = ? WHERE `Guest Card Number` = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, newLoyalty);
            statement.setString(2, card);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Obtain the loyalty points of a costumer
    protected int getLoyalty(String card) throws SQLException {
        String query = "SELECT `Loyalty Points` FROM Loyalty_Program WHERE `Guest Card Number` = ?";
        ResultSet resultSet;
        int result = 0;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, card);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getInt("Loyalty Points");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    //Obtain all of the existing buildings
    protected String[] getBuildings() throws SQLException {
        String[] buildings;
        String query = "SELECT DISTINCT `Building-ID`FROM Building";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        ArrayList<String> buildingList = new ArrayList<>();

        while (resultSet.next()) {
            String building = resultSet.getString("Building-ID");
            buildingList.add(building);
        }

        buildings = buildingList.toArray(new String[0]);


        return buildings;
    }

    //Obtain all the rooms within a building
    //Filter the results based on vacant rooms or just rooms in general
    protected String[] getRooms(String buildingID, boolean filterRoomStatus) throws SQLException {
        String[] rooms;
        String query1 = "SELECT DISTINCT `Room Number`FROM Room WHERE `Building-ID` = ? AND `Room Status` = 'Vacant'";
        String query2 = "SELECT DISTINCT `Room Number`FROM Room WHERE `Building-ID` = ?";

        String query = filterRoomStatus? query1:query2;
        ArrayList<String> roomList = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, buildingID);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int room = resultSet.getInt("Room Number");
                    roomList.add("" + room);
                }
            }
        }
        rooms = roomList.toArray(new String[0]);

        return rooms;
    }

    //Get all the computers
    protected String[] getComputers() throws SQLException {
        String[] computers;
        String query = "SELECT DISTINCT `Computer-ID`FROM Computer";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        ArrayList<String> computerList = new ArrayList<>();

        while (resultSet.next()) {
            String computer = resultSet.getString("Computer-ID");
            computerList.add(computer);
        }

        computers = computerList.toArray(new String[0]);
        return computers;
    }

    //Get the existing packages
    protected String[] getPackages() throws SQLException {
        String[] packages;
        String query = "SELECT DISTINCT `Package Name` FROM Guest_Package";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        ArrayList<String> packageList = new ArrayList<>();

        while (resultSet.next()) {
            String pkg = resultSet.getString("Package Name");
            packageList.add(pkg);
        }

        packages = packageList.toArray(new String[0]);
        return packages;
    }

    //Get the dates that a specific room is booked in the future
    protected String[] getBookingDates(String buildingID, int roomNumber) throws SQLException {
        String[] dates;
        String query = "SELECT DISTINCT `Date` FROM Booking WHERE `Building-ID` = ? AND `Room Number` = ?";

        ArrayList<String> dateList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, buildingID);
            statement.setInt(2, roomNumber);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String date = resultSet.getString("Date");
                    dateList.add(date);
                }
            }
        }
        dates = dateList.toArray(new String[0]);
        return dates;
    }

    //Get employees that worked on a certain date
    protected String[] getEmployees(String date) throws SQLException {
        String[] employees;
        String query = "SELECT DISTINCT `Employee-ID` FROM Shifts WHERE `Date` = ?";

        ArrayList<String> employeeList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, date);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String employee = resultSet.getString("Employee-ID");
                    employeeList.add(employee);
                }
            }
        }
        employees = employeeList.toArray(new String[0]);
        return employees;
    }

    //Get the shifts an employee worked on a certain date
    protected String[] getShifts(int employeeID, String date) throws SQLException {
        String[] shifts;
        String query = "SELECT DISTINCT `Start Time` FROM Shifts WHERE `Date` = ? AND `Employee-ID` = ?";

        ArrayList<String> shiftList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, date);
            statement.setInt(2, employeeID);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String shift = resultSet.getString("Start Time");
                    shiftList.add(shift);
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        shifts = shiftList.toArray(new String[0]);
        return shifts;
    }

    //Close the connection
    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
