package db_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {

    private final String url = "jdbc:mysql://localhost:3306/bilgeadam_uygulama?useUnicode=true&useLegacyDatetimeCode=false&serverTimezone=Turkey&useSSL=false";
    private final String userName = "root";
    private final String userPass = "12345";
    private final String driver = "com.mysql.cj.jdbc.Driver";
    public Connection connection = null;

    public DB() {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url,userName,userPass);
            if (!connection.isClosed()){
                System.out.println("Connection Success");
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}