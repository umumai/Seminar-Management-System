package Database;
import java.nio.file.*;
import java.sql.*;

public class DBSetup {
    public static void main(String[] args) {
        try {
            String sql = Files.readString(Paths.get("Database/schema.sql"));
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();
            stmt.execute(sql);
            con.close();
            System.out.println("Database ready.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

