import db_credentials.credentials;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHelper {
    private static final credentials credentials = new credentials();
    static{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Drivers loaded successfully");
        } catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
    }
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                credentials.getDbUrl(),      // Retrieve the database URL
                credentials.getDbUsername(), // Retrieve the database username
                credentials.getDbPassword()  // Retrieve the database password
        );
    }
}
