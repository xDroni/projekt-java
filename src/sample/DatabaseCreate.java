package sample;

import java.sql.*;

public class DatabaseCreate {
    public static void connect(String databaseFileName) {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:" +  getPath() + "\\data\\" + databaseFileName;
            conn = DriverManager.getConnection(url);

            System.out.println("Connected to the database");
            Statement stmt = conn.createStatement();

            ResultSet result = stmt.executeQuery("SELECT * FROM highscores");
            if (result.next()) {
                System.out.println(result.getString("Nickname") + ": " + result.getInt("Points"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static String getPath() {
        return System.getProperty("user.dir");

    }

    public static void main(String[] args){
        connect("main.db");
    }


}
