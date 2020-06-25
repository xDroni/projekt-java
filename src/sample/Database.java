package sample;

import java.sql.*;

public class Database {
    private String databaseFileName;
    private Connection conn = null;

    public Database(String databaseFileName) {
        this.databaseFileName = databaseFileName;
    }

    public void connect() {
        try {
            String url = "jdbc:sqlite:" + getPath() + "\\data\\" + this.databaseFileName;
            this.conn = DriverManager.getConnection(url);
            System.out.println("Connected to the database");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ResultSet getHighscores() {
        try {
            Statement stmt = this.conn.createStatement();
            return stmt.executeQuery("SELECT * FROM highscores;");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
        //        try {
//            if (this.conn != null) {
//                this.conn.close();
//            }
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }

    }

    public void addNewScore(String nickname, int score, int time) {
        try {
            String query = "INSERT INTO highscores(Nickname,Points,Time) VALUES(?,?,?)";
            PreparedStatement pstmt = this.conn.prepareStatement(query);
            pstmt.setString(1, nickname);
            pstmt.setInt(2, score);
            pstmt.setInt(3, time);
            pstmt.executeUpdate();
            System.out.println("Successfully added new score to the database");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static String getPath() {
        return System.getProperty("user.dir");

    }
}
