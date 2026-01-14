package database;
import java.nio.file.*;
import java.sql.*;

public class DBSetup {
    public static void main(String[] args) {
        try {
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();

            String sqlFile = Files.readString(Paths.get("Database/schema.sql"));
            String[] queries = sqlFile.split(";");

            for (String q : queries) {
                String query = q.trim();
                if (!query.isEmpty()) {
                    stmt.execute(query);
                }
            }

            con.close();
            System.out.println("All tables created successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

