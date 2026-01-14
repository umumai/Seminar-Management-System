package database;
import java.sql.*;

public class DBConnection {
    private static final String URL = "jdbc:sqlite:seminar.db";

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL);
    }
}
