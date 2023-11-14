package org.example;

import org.example.models.Countries;
import org.example.models.Country;
import org.postgresql.gss.GSSOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {


    private static final String url = "jdbc:postgresql://localhost:5432/postgres";
    private static final String user = "postgres";
    private static final String pass = "postgres";

    private static String path = "./src/main/resources/countries.xml";

    private static Connection connection;

    public static void main(String[] args) {

        connection = DBConnection.connect();
       // insertData("Francia", "Paris", "23988499");

        readData();

    }

    private static void insertData(String country, String capital, String population) {
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

    private static void readData() {
        String selectQuery = "SELECT country, capital, population FROM countries;";
        try {
            List<Country> countryList = new ArrayList<>();
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Country country = new Country();
                country.setName(resultSet.getString(1));
                country.setCapital(resultSet.getString(2));
                country.setPopulation(resultSet.getString(3));

                countryList.add(country);
            }

            Countries countries = new Countries();
            countries.setCountryList(countryList);
            addNewCountry(countries);
        } catch (SQLException | JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addNewCountry(Countries countries) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Countries.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(countries, new File(path));
    }


}