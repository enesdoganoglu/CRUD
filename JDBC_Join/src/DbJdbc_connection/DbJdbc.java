package DbJdbc_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbJdbc {


    private final String url = "jdbc:postgresql://localhost:5432/jdbc_join";
    private final String userName = "postgres";
    private final String userPass = "12345";
    private final String driver = "org.postgresql.Driver";
    public Connection connection = null;

    public DbJdbc() {
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
