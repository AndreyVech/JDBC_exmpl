package Main;

import java.sql.*;

public class DBCreator {
    private final String createUsers = "CREATE TABLE IF NOT EXISTS users (\n" +
            "  id SERIAL PRIMARY KEY, \n" +
            "  first_name character varying(200) NOT NULL, \n" +
            "  second_name character varying(200) NOT NULL, \n" +
            "  email character varying(200) NOT NULL,\n" +
            "  status character varying(100) NOT NULL CHECK (\n" +
            "    status IN (\n" +
            "      'work', 'delete'\n" +
            "    )\n" +
            "  )   \t\n" +
            ");";
    private final String createPass = "CREATE TABLE IF NOT EXISTS passwords (\n" +
            "  id SERIAL PRIMARY KEY, \n" +
            "  user_id integer NOT NULL REFERENCES users(id), \n" +
            "  pass_hash character varying(200), \n" +
            "  status character varying(100) NOT NULL CHECK (\n" +
            "    status IN (\n" +
            "      'work', 'delete'\n" +
            "    )\n" +
            "  )  \t\n" +
            ");";
    private final String createMess = "CREATE TABLE IF NOT EXISTS messages (\n" +
            "  id SERIAL PRIMARY KEY, \n" +
            "  sender_id integer NOT NULL REFERENCES users(id), \n" +
            "  receiver_id integer NOT NULL REFERENCES users(id), \t\n" +
            "  message_text character varying(2000), \n" +
            "  date_send date NOT NULL, \n" +
            "  status character varying(100) NOT NULL CHECK (\n" +
            "    status IN (\n" +
            "      'send', 'read', 'delete'\n" +
            "    )\n" +
            "  )\n" +
            ");";
    private final String createPassFunction = "CREATE OR REPLACE FUNCTION newPass() RETURNS TRIGGER\n" +
            "AS $newPass$\n" +
            "\tBEGIN\n" +
            "\t\tinsert into \"passwords\"(\"user_id\",\"pass_hash\",\"status\") values (New.id, Null, New.Status);\n" +
            "\t\treturn new;\n" +
            "\tEND;\n" +
            "$newPass$ LANGUAGE plpgsql;";
    private final String createPassTrigger = "CREATE TRIGGER newPass \n" +
            "AFTER INSERT ON users\n" +
            "FOR EACH ROW\n" +
            "EXECUTE PROCEDURE newPass();";
    private final String deletePassFunction = "CREATE OR REPLACE FUNCTION delPass() RETURNS TRIGGER\n" +
            "AS $delPass$\n" +
            "\tBEGIN\n" +
            "\t\tupdate \"passwords\" set \"status\" = \'delete\';\n" +
            "\t\treturn new;\n" +
            "\tEND;\n" +
            "$delPass$ LANGUAGE plpgsql;";
    private final String deletePassTrigger = "CREATE TRIGGER delPass \n" +
            "AFTER update ON users\n" +
            "FOR EACH ROW\n" +
            "EXECUTE PROCEDURE delPass();";


    public DBCreator(String query, Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet res = statement.executeQuery(query);
            System.out.println("Tables created");
        } catch (SQLException ex) {
            System.out.println("Tables not created. Create...");
            connection.createStatement().execute(createUsers);
            connection.createStatement().execute(createPass);
            connection.createStatement().execute(createMess);
            System.out.println("Done");
            connection.createStatement().execute(createPassFunction);
            connection.createStatement().execute(deletePassFunction);
            connection.createStatement().execute(createPassTrigger);
            connection.createStatement().execute(deletePassTrigger);
            System.out.println("Triggers done");
        }
    }
}

