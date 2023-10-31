package org.example;

import org.postgresql.gss.GSSOutputStream;

import java.sql.*;

public class Main {


    private static final String url = "jdbc:postgresql://localhost:5432/postgres";
    private static final String user = "postgres";
    private static final String pass = "postgres";

    private static Connection connection;

    public static void main(String[] args) {

        connection = DBConnection.connect();
        inserData("Francia", "Paris", "23988499");

    }

    private static void inserData(String country, String capital, String population) {
        String insertQuery = "INSERT INTO countries (country, capital, population) VALUES (?, ?, ?) RETURNING id;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, country);
            preparedStatement.setString(2, capital);
            preparedStatement.setString(3, population);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                int idCountry = resultSet.getInt(1);
                System.out.println("El id del pais inserta es: " + idCountry);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}