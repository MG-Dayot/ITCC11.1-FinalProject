import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserManager {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/FlashCard_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "xxxx";

    private Connection connection;

    public UserManager() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("MySQL JDBC Driver not found");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to the database");
        }
    }

    public boolean addUser(String username, String password) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean validateUser(String username, String password) {
        if (username == null || password == null) return false;

        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
        
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Returns true if a user with the given username and password exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed())
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}