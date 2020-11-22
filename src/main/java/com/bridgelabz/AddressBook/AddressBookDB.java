package com.bridgelabz.AddressBook;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddressBookDB {

    public Connection getConnection() throws InvalidException {
        String jdbcURL = "jdbc:mysql://localhost:3306/AddressBook_Services?useSSL=false";
        String username = "root";
        String password = "root";
        Connection connection;
        try {
            connection = DriverManager.getConnection(jdbcURL, username, password);
            return connection;
        } catch (SQLException exception) {
            throw new InvalidException("SQL_CONNECTION_ERROR",
                    InvalidException.ExceptionType.SQL_EXCEPTION);
        }
    }

    public List<AddressBookData> readAddressBookData(String sql) throws InvalidException {
        return this.getAddressBookDataFromDB(sql);
    }

    public List<AddressBookData> getAddressBookDataFromDB(String sql) throws InvalidException {
        List<AddressBookData> addressBookDataList = new ArrayList<>();
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            addressBookDataList = this.getData(resultSet);
        } catch (SQLException exception) {
            throw new InvalidException("JDBC_TABLE_NAME_WRONG",
                    InvalidException.ExceptionType.SQL_EXCEPTION);
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

    public int updateAddressBookData(String sql) throws InvalidException {

        try (Connection connection = this.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            String city = "Surat";
            String state = "GJ";
            int zipCode = 564534;
            String firstName = "pradip";
            preparedStatement.setString(1, city);
            preparedStatement.setString(2, state);
            preparedStatement.setInt(3, zipCode);
            preparedStatement.setString(4, firstName);
            int rowAffected = preparedStatement.executeUpdate();
            System.out.println(String.format("Row affected %d", rowAffected));
            return rowAffected;
        } catch (SQLException exception) {
            throw new InvalidException("JDBC_UPDATE_ERROR",
                    InvalidException.ExceptionType.SQL_EXCEPTION);
        }
    }

    public List<AddressBookData> readAddressBookDataForDateRange(LocalDate startDate, LocalDate endDate) throws InvalidException {
        String sql = String.format("SELECT * FROM personData WHERE dateAdded BETWEEN '%S' AND '%S';",
                Date.valueOf(startDate), Date.valueOf(endDate));
        return this.getAddressBookDataFromDB(sql);

    }

    public List<AddressBookData> readAddressBookDataForCity(String city) throws InvalidException {
        String sql = String.format("SELECT * FROM personData WHERE city = '%S';", city);
        return this.getAddressBookDataFromDB(sql);
    }

    public List<AddressBookData> readAddressBookDataForState(String state) throws InvalidException {
        String sql = String.format("SELECT * FROM personData WHERE state = '%S';", state);
        return this.getAddressBookDataFromDB(sql);

    }

    public void addNewAddressBookData(String firstName, String lastName, String city, String state,
                                      int zipCode, long phoneNumber, LocalDate date) throws InvalidException {
        int person_id = -1;
        AddressBookData addressBookData = null;
        String sql = String.format("Insert into personData(firstName,lastName,city,state,zipcode,phoneNumber,dateAdded)" +
                "values('%s','%s','%s','%s',%s,%s,'%s')", firstName, lastName, city, state, zipCode, phoneNumber, Date.valueOf(date));
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            int rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) person_id = resultSet.getInt(1);
            }
            addressBookData = new AddressBookData(person_id, firstName, lastName, city, state, zipCode, phoneNumber, date);
        } catch (SQLException | InvalidException exception) {
            throw new InvalidException("DATA_NOT_ADDED",
                    InvalidException.ExceptionType.SQL_EXCEPTION);
        }
    }

    public void deleteAddressBookData(String name) throws InvalidException {
        String sql = "DELETE FROM PERSONDATA WHERE FIRSTNAME=?";
        try (Connection connection = this.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            int row = preparedStatement.executeUpdate();
            System.out.println("deleted row : " + row);
        } catch (SQLException exception) {
            throw new InvalidException("DATA_NOT_FOUND",
                    InvalidException.ExceptionType.SQL_EXCEPTION);
        }
    }

    public boolean checkAddressBookDataSyncWithDB(String name) throws InvalidException {
        String sql = "select * from persondata where firstName= ?";
        String fetchedName = null;
        try (Connection connection = this.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                fetchedName = rs.getString("firstName");
                return fetchedName.equalsIgnoreCase(name);
            }
            preparedStatement.close();
        } catch (SQLException | InvalidException exception) {
            throw new InvalidException("data not found",
                    InvalidException.ExceptionType.SQL_EXCEPTION);

        }
        return false;
    }

}
