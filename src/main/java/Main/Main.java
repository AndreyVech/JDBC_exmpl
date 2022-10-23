package Main;

import java.sql.*;

public class Main {
    private static final String URL = "jdbc:postgresql://localhost/db_exmpl_test1?user=postgres&password=***";

    public static void main(String[] args) throws SQLException {
        DBConnector DB = new DBConnector();
        Connection connection = DB.GetConnection(URL);

        connection.close();
    }
}