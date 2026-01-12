import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBHelper {
    private static final String DB_URL = "jdbc:sqlite:seminar.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initDatabase() {
        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS users(id TEXT PRIMARY KEY, name TEXT, password TEXT, role TEXT)");

            // Seed accounts if not present
            seedIfMissing(conn, "STU001", "Student One", "student1", "STUDENT");

            seedIfMissing(conn, "EVA001", "Evaluator One", "eval1", "EVALUATOR");

            seedIfMissing(conn, "COO001", "Coordinator One", "coord1", "COORDINATOR");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void seedIfMissing(Connection conn, String id, String name, String password, String role) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE id = ?")) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    try (PreparedStatement insert = conn.prepareStatement("INSERT INTO users(id,name,password,role) VALUES(?,?,?,?)")) {
                        insert.setString(1, id);
                        insert.setString(2, name);
                        insert.setString(3, password);
                        insert.setString(4, role);
                        insert.executeUpdate();
                    }
                }
            }
        }
    }

    public static User authenticate(String id, String password) {
    String sql = "SELECT id, name, role FROM users WHERE id = ? AND password = ?";
    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, id);
        ps.setString(2, password);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String name = rs.getString("name");
                String role = rs.getString("role");
                return new User(id, name, role, password); // Use 4-parameter constructor
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}

    public static String createStudent(String name, String password) throws SQLException {
        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
            try (ResultSet rs = st.executeQuery("SELECT id FROM users WHERE id LIKE 'STU%'") ) {
                int max = 0;
                while (rs.next()) {
                    String id = rs.getString(1);
                    try {
                        int n = Integer.parseInt(id.substring(3));
                        if (n > max) max = n;
                    } catch (Exception ignored) {}
                }
                int next = max + 1;
                String newId = String.format("STU%03d", next);
                try (PreparedStatement ps = conn.prepareStatement("INSERT INTO users(id,name,password,role) VALUES(?,?,?,?)")) {
                    ps.setString(1, newId);
                    ps.setString(2, name);
                    ps.setString(3, password);
                    ps.setString(4, "STUDENT");
                    ps.executeUpdate();
                }
                return newId;
            }
        }
    }
} 

