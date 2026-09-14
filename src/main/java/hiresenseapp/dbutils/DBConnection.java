package hiresenseapp.dbutils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static String url;
    private static String user;
    private static String pass;

    public static void openConnection(String dbUrl, String username, String password) {
        url = dbUrl;
        user = username;
        pass = password;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("DB config loaded successfully");
        } catch (ClassNotFoundException ex) {
            System.out.println("MySQL Driver not found");
            ex.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }

    public static void closeConnection() {
        // not needed anymore — each DAO method closes its own connection
    }
}