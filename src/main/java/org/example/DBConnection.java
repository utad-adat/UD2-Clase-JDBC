package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    private static final String url = "jdbc:postgresql://localhost:5432/postgres";
    private static final String user = "postgres";
    private static final String pass = "postgres";

    public static Connection connect() {
        try {
            Connection connection = DriverManager.getConnection(url, user, pass);
            String tableCountries = "CREATE TABLE IF NOT EXISTS countries (id SERIAL PRIMARY KEY, country VARCHAR(255), capital VARCHAR(255), population VARCHAR(255));";
            Statement statement = connection.createStatement();
            statement.executeUpdate(tableCountries);

            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
