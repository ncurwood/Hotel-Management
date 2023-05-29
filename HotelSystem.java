import java.sql.*;

public class HotelSystem {
    private final Connection connection;

    public HotelSystem(String url, String username, String password) throws SQLException {
        connection = DriverManager.getConnection(url, username, password);
    }

    public void clockIn(int employeeId, String date, String startTime) throws SQLException {
        String query = "INSERT INTO Shifts (`Employee-ID`, Date, `Start Time`) " +
                "VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, employeeId);
            statement.setString(2, date);
            statement.setString(3, startTime);
            statement.executeUpdate();
        }
    }

    public void clockOut(int employeeId, String date, String endTime) throws SQLException {
        String query = "UPDATE Shifts " +
                "SET `End Time` = ? " +
                "WHERE `Employee-ID` = ? AND Date = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, endTime);
            statement.setInt(2, employeeId);
            statement.setString(3, date);
            statement.executeUpdate();
        }
    }

    public void makeBooking(int roomNumber, String buildingId, String date, String guestCardNumber, int computerId, int employeeId, String packageName) throws SQLException {
        String query = "INSERT INTO Booking (`Room Number`, `Building-ID`, Date, `Guest Card Number`, `Computer-ID`, `Employee-ID`, `Package Name`) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, roomNumber);
            statement.setString(2, buildingId);
            statement.setString(3, date);
            statement.setString(4, guestCardNumber);
            statement.setInt(5, computerId);
            statement.setInt(6, employeeId);
            statement.setString(7, packageName);
            statement.executeUpdate();
        }
    }

    public void cancelBooking(int roomNumber, String buildingId, String date) throws SQLException {
        String query = "DELETE FROM Booking " +
                "WHERE `Building-ID` = ? AND Date = ? AND `Room Number` = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, buildingId);
            statement.setString(2, date);
            statement.setInt(3, roomNumber);
            statement.executeUpdate();
        }
    }

    public void updateRoomStatus(int roomNumber, String buildingId, String roomStatus) throws SQLException {
        String query = "UPDATE Room " +
                "SET `Room Status` = ? " +
                "WHERE `Room Number` = ? AND `Building-ID` = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, roomStatus);
            statement.setInt(2, roomNumber);
            statement.setString(3, buildingId);
            statement.executeUpdate();
        }
    }

    public void cashInLoyaltyPoints(String guestCardNumber) throws SQLException {
        String query = "UPDATE Loyalty_Program " +
                "SET `Loyalty Points` = 0 " +
                "WHERE `Guest Card Number` = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, guestCardNumber);
            statement.executeUpdate();
        }
    }

    public void updateEmployeePhone(int employeeId, String phone) throws SQLException {
        String query = "UPDATE Employee " +
                "SET `Employee Phone` = ? " +
                "WHERE `Employee-ID` = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, phone);
            statement.setInt(2, employeeId);
            statement.executeUpdate();
        }
    }

    public void changeShift(int oldEmployeeId, String date, int newEmployeeId) throws SQLException {
        String query = "UPDATE Shifts " +
                "SET `Employee-ID` = ? " +
                "WHERE `Employee-ID` = ? AND Date = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, newEmployeeId);
            statement.setInt(2, oldEmployeeId);
            statement.setString(3, date);
            statement.executeUpdate();
        }
    }

    public void close() throws SQLException {
        connection.close();
    }
}