package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import models.Session;
import models.User;

public class DBHelper {
    private static final String DB_URL = "jdbc:sqlite:seminar.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initDatabase() {
        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
            // Create all tables 
            // users table (id, name, password, role)
            st.execute("CREATE TABLE IF NOT EXISTS users(id TEXT PRIMARY KEY, name TEXT, password TEXT, role TEXT)");
            // session table (session_id, session_date, venue, session_type, time_slot)
            st.execute("CREATE TABLE IF NOT EXISTS session(session_id INTEGER PRIMARY KEY AUTOINCREMENT, session_date TEXT, venue TEXT, session_type TEXT, time_slot TEXT)");
            // student_profile table
            st.execute("CREATE TABLE IF NOT EXISTS student_profile(student_id TEXT PRIMARY KEY, supervisor_name TEXT, research_title TEXT, abstract TEXT, presentation_type TEXT, FOREIGN KEY (student_id) REFERENCES users(id))");
            // submission table
            st.execute("CREATE TABLE IF NOT EXISTS submission(submission_id INTEGER PRIMARY KEY AUTOINCREMENT, student_id TEXT, filepath TEXT, status TEXT)");
            // session for presenter (session_id, student_id)
            st.execute("CREATE TABLE IF NOT EXISTS session_presenter(session_id INTEGER, student_id TEXT, PRIMARY KEY (session_id, student_id))");
            // award table (award_id, student_id, award_type, created_at)
            st.execute("CREATE TABLE IF NOT EXISTS award(award_id INTEGER PRIMARY KEY AUTOINCREMENT, student_id TEXT, award_type TEXT, created_at TEXT DEFAULT CURRENT_TIMESTAMP)");

            // Seed accounts if not present
            seedIfMissing(conn, "STU001", "Student One", "student1", "STUDENT");
            seedIfMissing(conn, "EVA001", "Evaluator One", "eval1", "EVALUATOR");
            seedIfMissing(conn, "COO001", "Coordinator One", "coord1", "COORDINATOR");

            // Seed a sample seminar/session
            seedSampleSession(conn);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private static void seedSampleSession(Connection conn) throws SQLException {
        // Check if any sessions exist
        try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM session")) {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    // Insert a sample session if no sessions exist
                    try (PreparedStatement insert = conn.prepareStatement(
                        "INSERT INTO session(session_date, venue, session_type, time_slot) VALUES(?, ?, ?, ?)")) {
                        insert.setString(1, "2024-03-15");
                        insert.setString(2, "Hall A");
                        insert.setString(3, "Oral Presentation");
                        insert.setString(4, "10:00 AM - 12:00 PM");
                        insert.executeUpdate();
                    }
                }
            }
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
    
    // Get all available sessions (seminars) from database
    public static List<Session> getAvailableSessions(String studentId) {
        List<Session> sessions = new ArrayList<>();
        String sql = "SELECT session_id, session_date, venue, session_type, time_slot " +
                     "FROM session " +
                     "WHERE session_id NOT IN (SELECT session_id FROM session_presenter WHERE student_id = ?) " +
                     "ORDER BY session_date";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId != null ? studentId : "");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int sessionId = rs.getInt("session_id");
                    String date = rs.getString("session_date");
                    String venue = rs.getString("venue");
                    String type = rs.getString("session_type");
                    
                    // Format session ID as SEM###
                    String sessionIdStr = String.format("SEM%03d", sessionId);
                    
                    Session session = new Session(sessionIdStr, date, venue, type);
                    sessions.add(session);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sessions;
    }
    
    // Get all sessions (for when studentId is not needed)
    public static List<Session> getAllSessions() {
        return getAvailableSessions(null);
    }
    
    // Save student registration (student profile and session presenter)
    public static void saveStudentRegistration(String studentId, String researchTitle, 
            String abstractText, String supervisorName, String presentationType, 
            String materialPath, int sessionId) throws SQLException {
        try (Connection conn = getConnection()) {
            // Save or update student profile
            try (PreparedStatement ps = conn.prepareStatement(
                "INSERT OR REPLACE INTO student_profile(student_id, supervisor_name, research_title, abstract, presentation_type) VALUES(?, ?, ?, ?, ?)")) {
                ps.setString(1, studentId);
                ps.setString(2, supervisorName);
                ps.setString(3, researchTitle);
                ps.setString(4, abstractText);
                ps.setString(5, presentationType);
                ps.executeUpdate();
            }
            
            // Save session presenter relationship
            try (PreparedStatement ps = conn.prepareStatement(
                "INSERT OR REPLACE INTO session_presenter(session_id, student_id) VALUES(?, ?)")) {
                ps.setInt(1, sessionId);
                ps.setString(2, studentId);
                ps.executeUpdate();
            }
            
            // Save submission with material file path
            try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO submission(student_id, filepath, status) VALUES(?, ?, 'Submitted')")) {
                ps.setString(1, studentId);
                ps.setString(2, materialPath);
                ps.executeUpdate();
            }
        }
    }
    
    // Get student's registered seminar
    public static Session getStudentRegisteredSeminar(String studentId) {
        try (Connection conn = getConnection()) {
            // Get session_id from session_presenter
            String sql = "SELECT s.session_id, s.session_date, s.venue, s.session_type " +
                        "FROM session s " +
                        "INNER JOIN session_presenter sp ON s.session_id = sp.session_id " +
                        "WHERE sp.student_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, studentId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int sessionId = rs.getInt("session_id");
                        String date = rs.getString("session_date");
                        String venue = rs.getString("venue");
                        String type = rs.getString("session_type");
                        String sessionIdStr = String.format("SEM%03d", sessionId);
                        return new Session(sessionIdStr, date, venue, type);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Get student's submission status
    public static String getStudentSubmissionStatus(String studentId) {
        try (Connection conn = getConnection()) {
            String sql = "SELECT status FROM submission WHERE student_id = ? ORDER BY submission_id DESC LIMIT 1";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, studentId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("status");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Get student's award result
    public static String getStudentAwardResult(String studentId) {
        try (Connection conn = getConnection()) {
            String sql = "SELECT award_type FROM award WHERE student_id = ? ORDER BY award_id DESC LIMIT 1";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, studentId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String awardType = rs.getString("award_type");
                        return awardType != null ? awardType : "Pending";
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Pending";
    }

    // Create a new session with only date and venue, returns generated session_id or -1 on failure
    public static int createSession(String sessionDate, String venue) throws SQLException {
        String sql = "INSERT INTO session(session_date, venue) VALUES(?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, sessionDate);
            ps.setString(2, venue);
            int affected = ps.executeUpdate();
            if (affected == 0) return -1;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    // Check whether a session with the given session_id exists
    public static boolean sessionExists(int sessionId) {
        String sql = "SELECT 1 FROM session WHERE session_id = ? LIMIT 1";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sessionId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Return the total number of sessions in the database
    public static int getSessionCount() {
        String sql = "SELECT COUNT(*) FROM session";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    // Get session data by session_id (row number)
    public static Session getSession(int sessionId) {
        String sql = "SELECT session_id, session_date, venue, session_type FROM session WHERE session_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sessionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String date = rs.getString("session_date");
                    String venue = rs.getString("venue");
                    String type = rs.getString("session_type");
                    String sessionIdStr = String.format("SEM%03d", sessionId);
                    return new Session(sessionIdStr, date, venue, type);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Insert student_id into student_profile table
    public static boolean insertStudentProfile(String studentId) throws SQLException {
        String sql = "INSERT INTO student_profile(student_id) VALUES(?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }

    // Insert student_id and session_id into session_presenter table
    public static boolean insertSessionPresenter(int sessionId) throws SQLException {
        String sql = "INSERT INTO session_presenter(session_id) VALUES(?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sessionId);
            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }

    // Get student name from users table by student_id
    public static String getStudentNameById(String studentId) {
        String sql = "SELECT name FROM users WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    
} 

