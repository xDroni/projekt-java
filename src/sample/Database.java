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
        return executeQuery("SELECT * FROM highscores;");
        //        try {
//            if (this.conn != null) {
//                this.conn.close();
//            }
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }

    }

    public void addNewScore(String nickname, int score, int time) {
        executeQuery(   "INSERT INTO highscores (Nickname, Points, Time)" +
                        "VALUES ("+nickname+", "+score+", "+time+")");
    }

    private ResultSet executeQuery(String query) {
        try {
            Statement stmt = this.conn.createStatement();
            return stmt.executeQuery(query);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private static String getPath() {
        return System.getProperty("user.dir");

    }
}
