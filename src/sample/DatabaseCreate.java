package sample;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseCreate {
    public static void createNewDatabase(String fileName) {

        Connection conn;
        String url = getPath() + "\\database\\" + fileName;

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
            DatabaseMetaData meta = conn.getMetaData();
            System.out.println("The driver name is " + meta.getDriverName());
            System.out.println("A new database has been created.");


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String getPath() {
        return System.getProperty("user.dir");

    }

    public static void main(String[] args){
        createNewDatabase("test.db");
    }


}
