package Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
    private Connection connection;
    private static String Query = "select * from users";

    public DBConnector(){
    }

    public Connection GetConnection(String URL) throws SQLException {
        if (connection != null) {
            System.out.println("Подключено");
            return connection;
        }
        connection = DriverManager.getConnection(URL);
        System.out.println("Connection OK");
        DBCreator createDB = new DBCreator(Query, connection);
        return connection;
    }
}

