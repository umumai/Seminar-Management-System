package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import models.*;

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
            // evaluation table
            st.execute("CREATE TABLE IF NOT EXISTS evaluation(evaluation_id INTEGER PRIMARY KEY AUTOINCREMENT, submission_id INTEGER, evaluator_id TEXT, clarity_score INTEGER, methodology_score INTEGER, results_score INTEGER, presentation_score INTEGER, comments TEXT, FOREIGN KEY (submission_id) REFERENCES submission(submission_id), FOREIGN KEY (evaluator_id) REFERENCES users(id))");
            // appointments table
            st.execute("CREATE TABLE IF NOT EXISTS appointments(appointment_id INTEGER PRIMARY KEY AUTOINCREMENT, session_id INTEGER, student_id TEXT, evaluator_id TEXT, time TEXT, status TEXT, FOREIGN KEY (session_id) REFERENCES session(session_id), FOREIGN KEY (student_id) REFERENCES users(id), FOREIGN KEY (evaluator_id) REFERENCES users(id))");
            // award table (award_id, student_id, award_type, created_at)
            st.execute("CREATE TABLE IF NOT EXISTS award(award_id INTEGER PRIMARY KEY AUTOINCREMENT, student_id TEXT, award_type TEXT, score REAL, created_at TEXT DEFAULT CURRENT_TIMESTAMP)");

            // seed accounts for starters
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

    // True if coordinator has assigned an evaluator (and time) for this student
    public static boolean hasEvaluatorAssigned(String studentId) {
        String sql = "SELECT 1 FROM appointments WHERE student_id = ? AND evaluator_id IS NOT NULL AND trim(coalesce(evaluator_id, '')) != '' LIMIT 1";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Display status for student UI: "Submitted" | "Under evaluation" | "Completed"
    public static String getStudentDisplayStatus(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            return null;
        }
        String submissionStatus = getStudentSubmissionStatus(studentId);
        if ("Completed".equals(submissionStatus)) {
            return "Completed";
        }
        if (getSubmissionId(studentId) == null) {
            return null;
        }
        if (isStudentEvaluatedByAny(studentId)) {
            return "Completed";
        }
        if (hasEvaluatorAssigned(studentId)) {
            return "Under evaluation";
        }
        return "Submitted";
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

    // Insert session_id and student_id into appointments table 
    public static boolean insertAppointment(int sessionId, String studentId) throws SQLException {
        String sql = "INSERT INTO appointments(session_id, student_id, time, status) VALUES(?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sessionId);
            ps.setString(2, studentId);
            ps.setString(3, "0");
            ps.setString(4, "Unassigned"); // umu added status "Unassigned" to show in Appointment Management
            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }

    //  Get all appointments from database
    public static List<Appointment> getAllAppointments() {
        String sql = "SELECT session_id, student_id, evaluator_id, time, status FROM appointments";
        List<Appointment> appointments = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int sessionId = rs.getInt("session_id");
                    String studentId = rs.getString("student_id");
                    String evaluatorId = rs.getString("evaluator_id");
                    String timeSlot = rs.getString("time");
                    String status = rs.getString("status");
                    appointments.add(new Appointment(sessionId, studentId, evaluatorId, timeSlot, status));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

     // Get all appointments from database
    public static List<Appointment> getAppointmentsbySession(int sessionID) {
        String sql = "SELECT student_id, evaluator_id, time, status FROM appointments WHERE session_id = ?";
        List<Appointment> appointments = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sessionID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String studentId = rs.getString("student_id");
                    String evaluatorId = rs.getString("evaluator_id");
                    String timeSlot = rs.getString("time");
                    String status = rs.getString("status");

                    // Normalize for coordinator display: only Unassigned / Under Evaluation / Evaluated.
                    boolean evaluatorAssigned = evaluatorId != null && !evaluatorId.trim().isEmpty();
                    boolean timeAssigned = timeSlot != null && !timeSlot.trim().isEmpty() && !"0".equals(timeSlot);
                    boolean isEvaluated = status != null && "evaluated".equalsIgnoreCase(status);

                    if (isEvaluated) {
                        status = "Evaluated";
                    } else if (evaluatorAssigned && timeAssigned) {
                        status = "Under Evaluation";
                    } else {
                        status = "Unassigned";
                    }
                    appointments.add(new Appointment(sessionID, studentId, evaluatorId, timeSlot, status));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    // Get student_id from appointments table by sessionId and row number
    public static String getStudentIdFromAppointment(int sessionId, int rowNumber) {
        int offset = rowNumber - 1;
        String sql = "SELECT student_id FROM appointments WHERE session_id = ? LIMIT 1 OFFSET ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sessionId);
            ps.setInt(2, offset);  // Start from 0
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("student_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //update time slot
    public static boolean updateTimeSlot(String timeSlot, String studentID) throws SQLException {
    String sql = "UPDATE appointments SET time = ? WHERE student_id = ?";
    
    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, timeSlot);
        ps.setString(2, studentID);
        int affected = ps.executeUpdate();
        return affected > 0;
    }
}

    // Get name from users table by role
    public static String getUserByRole(String role, int rowNumber) {
        String sql = "SELECT name FROM users WHERE role = ? LIMIT 1 OFFSET ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, role);
            ps.setInt(2, rowNumber - 1);  // Start from 0
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

    //check each existing row
   public static boolean checkUserbyRow(int rowNumber) {
    String sql = "SELECT 1 FROM users LIMIT 1 OFFSET ?";

    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, rowNumber - 1);
        try (ResultSet rs = ps.executeQuery()) {
            return rs.next();
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return false;
}


    public static User getUserbyID(String id) {
    String sql = "SELECT id, name, role, password FROM users WHERE id = ?";
    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, id);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String name = rs.getString("name");
                String role = rs.getString("role");
                String password = rs.getString("password");
                return new User(id, name, role, password); // Use 4-parameter constructor
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}

    // Get name from users table by role
    public static String getNameByID(String ID) {
        String sql = "SELECT name FROM users WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ID);
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

    // Get name from users table by role
    public static String getIDbyName(String name) {
        String sql = "SELECT id FROM users WHERE name = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

     // Update evaluator and status by appointment_id
    public static boolean updateAppointment(String studentID, String evaluatorId, String time, String status) throws SQLException {
        String sql = "UPDATE appointments SET evaluator_id = ?, time = ? , status = ? WHERE student_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, evaluatorId);
            ps.setString(2, time);
            ps.setString(3, status);
            ps.setString(4, studentID);
            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }

    // Get student profile information
    public static StudentProfileData getStudentProfile(String studentId) {
        String sql = "SELECT sp.research_title, sp.abstract, sp.supervisor_name, sp.presentation_type, " +
                     "s.filepath FROM student_profile sp " +
                     "LEFT JOIN submission s ON sp.student_id = s.student_id " +
                     "WHERE sp.student_id = ? " +
                     "ORDER BY s.submission_id DESC LIMIT 1";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new StudentProfileData(
                        rs.getString("research_title"),
                        rs.getString("abstract"),
                        rs.getString("supervisor_name"),
                        rs.getString("presentation_type"),
                        rs.getString("filepath")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Inner class to hold student profile data
    public static class StudentProfileData {
        public final String researchTitle;
        public final String abstractText;
        public final String supervisorName;
        public final String presentationType;
        public final String filepath;

        public StudentProfileData(String researchTitle, String abstractText, String supervisorName, 
                                 String presentationType, String filepath) {
            this.researchTitle = researchTitle;
            this.abstractText = abstractText;
            this.supervisorName = supervisorName;
            this.presentationType = presentationType;
            this.filepath = filepath;
        }
    }

    // Get submission ID for a student
    public static Integer getSubmissionId(String studentId) {
        String sql = "SELECT submission_id FROM submission WHERE student_id = ? ORDER BY submission_id DESC LIMIT 1";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("submission_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get appointment time for a student
    public static String getAppointmentTime(String studentId) {
        String sql = "SELECT time FROM appointments WHERE student_id = ? LIMIT 1";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("time");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "0";
    }

    // Save evaluation to database
    public static boolean saveEvaluation(String studentId, String evaluatorId, int clarity, 
                                         int methodology, int results, int presentation, String comments) {
        Integer submissionId = getSubmissionId(studentId);
        if (submissionId == null) {
            System.err.println("No submission found for student: " + studentId);
            return false;
        }

        String sql = "INSERT INTO evaluation(submission_id, evaluator_id, clarity_score, " +
                     "methodology_score, results_score, presentation_score, comments) " +
                     "VALUES(?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, submissionId);
            ps.setString(2, evaluatorId);
            ps.setInt(3, clarity);
            ps.setInt(4, methodology);
            ps.setInt(5, results);
            ps.setInt(6, presentation);
            ps.setString(7, comments);
            int affected = ps.executeUpdate();
            
            // Update appointment status to "Evaluated" so coordinator's Status column is consistent
            if (affected > 0) {
                try {
                    String existingTime = getAppointmentTime(studentId);
                    updateAppointment(studentId, evaluatorId, existingTime, "Evaluated");
                } catch (SQLException e) {
                    System.err.println("Failed to update appointment status: " + e.getMessage());
                }
            }
            
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Check if evaluator has evaluated this student
    public static boolean isStudentEvaluated(String studentId, String evaluatorId) {
        Integer submissionId = getSubmissionId(studentId);
        if (submissionId == null) {
            return false;
        }

        String sql = "SELECT 1 FROM evaluation WHERE submission_id = ? AND evaluator_id = ? LIMIT 1";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, submissionId);
            ps.setString(2, evaluatorId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Check if student has been evaluated by any evaluator
    public static boolean isStudentEvaluatedByAny(String studentId) {
        Integer submissionId = getSubmissionId(studentId);
        if (submissionId == null) {
            return false;
        }

        String sql = "SELECT 1 FROM evaluation WHERE submission_id = ? LIMIT 1";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, submissionId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get all evaluations for a student
    public static List<EvaluationData> getAllEvaluationsForStudent(String studentId) {
        List<EvaluationData> evaluations = new ArrayList<>();
        Integer submissionId = getSubmissionId(studentId);
        if (submissionId == null) {
            return evaluations;
        }

        String sql = "SELECT clarity_score, methodology_score, results_score, presentation_score, comments " +
                     "FROM evaluation WHERE submission_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, submissionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    evaluations.add(new EvaluationData(
                        rs.getInt("clarity_score"),
                        rs.getInt("methodology_score"),
                        rs.getInt("results_score"),
                        rs.getInt("presentation_score"),
                        rs.getString("comments")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return evaluations;
    }

    // Inner class to hold evaluation data
    public static class EvaluationData {
        public final int clarityScore;
        public final int methodologyScore;
        public final int resultsScore;
        public final int presentationScore;
        public final String comments;

        public EvaluationData(int clarityScore, int methodologyScore, int resultsScore, 
                              int presentationScore, String comments) {
            this.clarityScore = clarityScore;
            this.methodologyScore = methodologyScore;
            this.resultsScore = resultsScore;
            this.presentationScore = presentationScore;
            this.comments = comments;
        }

        public int getTotalScore() {
            return clarityScore + methodologyScore + resultsScore + presentationScore;
        }
    }

    // Get all student IDs in a session
    public static List<String> getStudentsInSession(int sessionId) {
        List<String> studentIds = new ArrayList<>();
        String sql = "SELECT student_id FROM appointments WHERE session_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sessionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    studentIds.add(rs.getString("student_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return studentIds;
    }

    // Check if all students in a session have been evaluated
    public static boolean areAllStudentsEvaluated(int sessionId) {
        List<String> studentIds = getStudentsInSession(sessionId);
        if (studentIds.isEmpty()) {
            return false;
        }

        for (String studentId : studentIds) {
            if (!isStudentEvaluatedByAny(studentId)) {
                return false;
            }
        }
        return true;
    }

    // Calculate average total score for a student
    public static double getAverageTotalScore(String studentId) {
        List<EvaluationData> evaluations = getAllEvaluationsForStudent(studentId);
        if (evaluations.isEmpty()) {
            return 0.0;
        }

        int totalSum = 0;
        for (EvaluationData eval : evaluations) {
            totalSum += eval.getTotalScore();
        }
        return (double) totalSum / evaluations.size();
    }

    // Calculate average presentation score for a student
    public static double getAveragePresentationScore(String studentId) {
        List<EvaluationData> evaluations = getAllEvaluationsForStudent(studentId);
        if (evaluations.isEmpty()) {
            return 0.0;
        }

        int totalSum = 0;
        for (EvaluationData eval : evaluations) {
            totalSum += eval.presentationScore;
        }
        return (double) totalSum / evaluations.size();
    }

    // Calculate and save awards for a session
    public static void calculateAndSaveAwards(int sessionId) {
        List<String> studentIds = getStudentsInSession(sessionId);
        if (studentIds.isEmpty()) {
            return;
        }

        // Clear existing awards for students in this session
        try (Connection conn = getConnection()) {
            if (!studentIds.isEmpty()) {
                StringBuilder placeholders = new StringBuilder();
                for (int i = 0; i < studentIds.size(); i++) {
                    if (i > 0) placeholders.append(",");
                    placeholders.append("?");
                }
                String deleteSql = "DELETE FROM award WHERE student_id IN (" + placeholders.toString() + ")";
                try (PreparedStatement ps = conn.prepareStatement(deleteSql)) {
                    for (int i = 0; i < studentIds.size(); i++) {
                        ps.setString(i + 1, studentIds.get(i));
                    }
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        // Collect student data with scores
        List<StudentScoreData> studentScores = new ArrayList<>();
        for (String studentId : studentIds) {
            StudentProfileData profile = getStudentProfile(studentId);
            if (profile != null) {
                double avgTotal = getAverageTotalScore(studentId);
                double avgPresentation = getAveragePresentationScore(studentId);
                studentScores.add(new StudentScoreData(studentId, profile.presentationType, avgTotal, avgPresentation));
            }
        }

        // Calculate Best Oral
        String bestOralId = null;
        double bestOralScore = -1;
        for (StudentScoreData data : studentScores) {
            if ("Oral".equalsIgnoreCase(data.presentationType) && data.avgTotalScore > bestOralScore) {
                bestOralScore = data.avgTotalScore;
                bestOralId = data.studentId;
            }
        }

        // Calculate Best Poster
        String bestPosterId = null;
        double bestPosterScore = -1;
        for (StudentScoreData data : studentScores) {
            if ("Poster".equalsIgnoreCase(data.presentationType) && data.avgTotalScore > bestPosterScore) {
                bestPosterScore = data.avgTotalScore;
                bestPosterId = data.studentId;
            }
        }

        // Calculate People's Choice (highest presentation score overall)
        String peopleChoiceId = null;
        double peopleChoiceScore = -1;
        for (StudentScoreData data : studentScores) {
            if (data.avgPresentationScore > peopleChoiceScore) {
                peopleChoiceScore = data.avgPresentationScore;
                peopleChoiceId = data.studentId;
            }
        }

        // Save awards
        try (Connection conn = getConnection()) {
            String insertSql = "INSERT INTO award(student_id, award_type, score) VALUES(?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                if (bestOralId != null) {
                    ps.setString(1, bestOralId);
                    ps.setString(2, "Best Oral");
                    ps.setDouble(3, bestOralScore);
                    ps.executeUpdate();
                }

                if (bestPosterId != null) {
                    ps.setString(1, bestPosterId);
                    ps.setString(2, "Best Poster");
                    ps.setDouble(3, bestPosterScore);
                    ps.executeUpdate();
                }

                if (peopleChoiceId != null) {
                    ps.setString(1, peopleChoiceId);
                    ps.setString(2, "People's Choice");
                    ps.setDouble(3, peopleChoiceScore);
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Update submission statuses to "Completed"
        try (Connection conn = getConnection()) {
            if (!studentIds.isEmpty()) {
                StringBuilder placeholders = new StringBuilder();
                for (int i = 0; i < studentIds.size(); i++) {
                    if (i > 0) placeholders.append(",");
                    placeholders.append("?");
                }
                String updateSql = "UPDATE submission SET status = 'Completed' WHERE student_id IN (" + placeholders.toString() + ")";
                try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                    for (int i = 0; i < studentIds.size(); i++) {
                        ps.setString(i + 1, studentIds.get(i));
                    }
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Inner class to hold student score data
    private static class StudentScoreData {
        final String studentId;
        final String presentationType;
        final double avgTotalScore;
        final double avgPresentationScore;

        StudentScoreData(String studentId, String presentationType, double avgTotalScore, double avgPresentationScore) {
            this.studentId = studentId;
            this.presentationType = presentationType;
            this.avgTotalScore = avgTotalScore;
            this.avgPresentationScore = avgPresentationScore;
        }
    }

    // Inner class to hold student evaluation results
    public static class StudentEvaluationResults {
        public final int avgClarityScore;
        public final int avgMethodologyScore;
        public final int avgResultsScore;
        public final int avgPresentationScore;
        public final String comments;
        public final String award;

        public StudentEvaluationResults(int avgClarityScore, int avgMethodologyScore, int avgResultsScore,
                                       int avgPresentationScore, String comments, String award) {
            this.avgClarityScore = avgClarityScore;
            this.avgMethodologyScore = avgMethodologyScore;
            this.avgResultsScore = avgResultsScore;
            this.avgPresentationScore = avgPresentationScore;
            this.comments = comments;
            this.award = award;
        }
    }

    // Get student evaluation results (averaged scores, comments, award)
    public static StudentEvaluationResults getStudentEvaluationResults(String studentId) {
        List<EvaluationData> evaluations = getAllEvaluationsForStudent(studentId);
        if (evaluations.isEmpty()) {
            return null;
        }

        // Calculate averages
        double avgClarity = 0, avgMethodology = 0, avgResults = 0, avgPresentation = 0;
        List<String> commentsList = new ArrayList<>();
        
        for (EvaluationData eval : evaluations) {
            avgClarity += eval.clarityScore;
            avgMethodology += eval.methodologyScore;
            avgResults += eval.resultsScore;
            avgPresentation += eval.presentationScore;
            if (eval.comments != null && !eval.comments.trim().isEmpty()) {
                commentsList.add(eval.comments.trim());
            }
        }

        int count = evaluations.size();
        avgClarity /= count;
        avgMethodology /= count;
        avgResults /= count;
        avgPresentation /= count;

        String combinedComments = String.join("\n\n", commentsList);
        String award = getStudentAwardResult(studentId);
        if ("Pending".equals(award)) {
            award = "No award";
        }

        return new StudentEvaluationResults(
            (int) Math.round(avgClarity),
            (int) Math.round(avgMethodology),
            (int) Math.round(avgResults),
            (int) Math.round(avgPresentation),
            combinedComments,
            award
        );
    }

    // Get latest evaluation for a student (latest submission)
    public static Evaluation getEvaluationByStudentId(String studentId) {
        Integer submissionId = getSubmissionId(studentId);
        if (submissionId == null) {
            return null;
        }

        String sql = "SELECT clarity_score, methodology_score, results_score, presentation_score, comments " +
                     "FROM evaluation WHERE submission_id = ? ORDER BY evaluation_id DESC LIMIT 1";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, submissionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Evaluation(
                        rs.getInt("clarity_score"),
                        rs.getInt("methodology_score"),
                        rs.getInt("results_score"),
                        rs.getInt("presentation_score"),
                        rs.getString("comments")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    
}
