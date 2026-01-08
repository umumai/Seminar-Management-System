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

