package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

//    Connect SQLServer
//    private String jdbcURL = "jdbc:sqlserver://localhost:1433;databaseName=BTL_LTM;encrypt=true;trustServerCertificate=true";
//    private String jdbcUsername = "sa";
//    private String jdbcPassword = "12345";

//  Connect MySQL
    private String jdbcURL = "jdbc:mysql://localhost:3306/btl_ltm?useSSL=false";
    private String jdbcUsername = "root";
    private String jdbcPassword = "123456";
    
   

    private static DatabaseConnection instance;
    private Connection connection;

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    private DatabaseConnection() {

    }

    public Connection getConnection() {
        try {
//            SQLServer 
//            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); 

//          MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Connected to Database.");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
