package com.bridgelabz.AddressBook;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddressBookDB {

    public Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/AddressBook_Services?useSSL=false";
        String username = "root";
        String password = "root";
        Connection connection;
        connection = DriverManager.getConnection(jdbcURL, username, password);
        return connection;
    }

    public List<AddressBookData> readAddressBookData(String sql) {
        return this.getAddressBookDataFromDB(sql);
    }

    public List<AddressBookData> getAddressBookDataFromDB(String sql) {
        List<AddressBookData> addressBookDataList = new ArrayList<>();
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            addressBookDataList = this.getData(resultSet);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return addressBookDataList;
    }

    public List<AddressBookData> getData(ResultSet resultSet) throws SQLException {
        List<AddressBookData> addressBookDataList = new ArrayList<>();
        while (resultSet.next()) {
            int personId = resultSet.getInt("personId");
            String firstName = resultSet.getString("firstName");
            String lastName = resultSet.getString("lastName");
            String city = resultSet.getString("city");
            String state = resultSet.getString("state");
            int zipCode = resultSet.getInt("zipCode");
            long phoneNUmber = resultSet.getLong("phoneNumber");
            addressBookDataList.add(new AddressBookData(personId, firstName, lastName, city, state, zipCode, phoneNUmber));
        }
        return addressBookDataList;
    }
}
