import java.sql.*;

public class HotelSystem {
    private Connection connection;

    public HotelSystem(String url, String username, String password) throws SQLException {
        //connection = DriverManager.getConnection(url, username, password);
    }

    public void clockIn(int employeeId, String date, String startTime) throws SQLException {

    }

    public void clockOut(int employeeId, String date, String endTime) throws SQLException {

    }

    public void makeBooking(int roomNumber, String buildingId, String date, String guestCardNumber, int computerId, int employeeId, String packageName) throws SQLException {

    }

    public void cancelBooking(int roomNumber, String buildingId, String date) throws SQLException {

    }

    public void updateRoomStatus(int roomNumber, String buildingId, String roomStatus) throws SQLException {

    }

    public void cashInLoyaltyPoints(String guestCardNumber) throws SQLException {

    }

    public void updateEmployeePhone(int employeeId, String phone) throws SQLException {
    }
    }
