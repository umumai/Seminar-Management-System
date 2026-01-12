/*
 * SubmissionDAO.java
 * -------------------
 * Handles saving student submission data into the database.
 *
 * When a student uploads a file, saveSubmission() is called
 * to insert the record into the submission table in seminar.db.
 *
 * This class only contains database code (no UI logic).
 */

package Database;
import java.sql.*;

public class SubmissionDAO {

    public void saveSubmission(int studentId, String filePath) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "INSERT INTO submission(student_id, filepath, status) VALUES(?,?, 'Submitted')";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, studentId);
            ps.setString(2, filePath);
            ps.executeUpdate();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

