package com.bridgelabz.AddressBook;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

public class AddressBookTestCases {
    private static final String SQL_READ  = "select * from PersonData";
    private static final String SQL_READ_WRONG_QUERY = "select * from employee";
    private static final String SQL_UPDATE  = "UPDATE PersonData "
            + "SET city = ? "
            + "SET state = ? "
            + "SET zipCode = ? "
            + "WHERE firstName = ?";
    private static final String SQL_UPDATE_WRONG_QUERY = "UPDATE employee "
            + "SET city = ? "
            + "SET state = ? "
            + "SET zipCode = ? "
            + "WHERE firstName = ?";

    AddressBookDB addressBookDB;

    @Before
    public void setUp()  {
        addressBookDB = new AddressBookDB();
    }

    @Test
    public void givenAddressBookDB_whenRetrievedData_MatchCountWithDB() throws InvalidException {
        List<AddressBookData> addressBookData = addressBookDB.readAddressBookData(SQL_READ);
        Assert.assertEquals(4, addressBookData.size());
    }

    @Test
    public void givenAddressBookDB_whenPassWrongTableNameAtRetrieve_ShouldThrowException() {
        try {
            List<AddressBookData> addressBookData = addressBookDB.readAddressBookData(SQL_READ_WRONG_QUERY);
        } catch (InvalidException invalidException) {
            System.out.println(invalidException.getMessage());
            Assert.assertEquals("JDBC_TABLE_NAME_WRONG", invalidException.getMessage());
        }
    }

    @Test
    public void givenAddressBookDB_whenUpdated_syncWithDB() throws InvalidException {
       int i= addressBookDB.updateAddressBookData(SQL_UPDATE);
        boolean result = addressBookDB.checkAddressBookDataSyncWithDB("Manish");
        Assert.assertTrue(result);
         }

    @Test
    public void givenAddressBookDB_whenPassAtUpdate_ShouldThrowException() {
        try {
            addressBookDB.updateAddressBookData(SQL_UPDATE_WRONG_QUERY);
        } catch (InvalidException invalidException) {
            System.out.println(invalidException.getMessage());
            Assert.assertEquals("JDBC_UPDATE_ERROR", invalidException.getMessage());
        }
    }


    @Test
    public void givenAddressBookDB_whenRetrievedForParticularPeriod_shouldMatchCount() throws InvalidException {
        LocalDate startDate = LocalDate.of(2018, 1, 1);
        LocalDate endDate = LocalDate.now();
        List<AddressBookData> addressBookData = addressBookDB.readAddressBookDataForDateRange(startDate, endDate);
        Assert.assertEquals(4, addressBookData.size());
    }

    @Test
    public void givenAddressBookDB_whenRetrievedForParticularCity_shouldMatchCount() throws InvalidException {
        String city = "pune";
        List<AddressBookData> addressBookData = addressBookDB.readAddressBookDataForCity(city);
        Assert.assertEquals(2, addressBookData.size());
    }

    @Test
    public void givenAddressBookDB_whenRetrievedForParticularState_shouldMatchCount() throws InvalidException {
        String state = "Maharashtra";
        List<AddressBookData> addressBookDataList = addressBookDB.readAddressBookDataForState(state);
        Assert.assertEquals(3, addressBookDataList.size());
    }

    @Test
    public void givenNewAddressData_whenAdded_shouldSyncWithDB() throws InvalidException {
        addressBookDB.addNewAddressBookData("Manish","Patil","Surat","Gujarat",234563,9665353267L,LocalDate.now());
        boolean result = addressBookDB.checkAddressBookDataSyncWithDB("Manish");
        Assert.assertTrue(result);
    }


    @Test
    public void givenAddressData_whenDelete_shouldSyncWithDB() throws InvalidException {
        addressBookDB.deleteAddressBookData("Manish");
        boolean result = addressBookDB.checkAddressBookDataSyncWithDB("Manish");
        Assert.assertFalse(result);
    }

    @Test
    public void givenAddressData_whenDeleteNotFoundData_shouldThrowException() throws InvalidException {
        try {
            addressBookDB.deleteAddressBookData("Manoj");
        }catch (InvalidException invalidException) {
            System.out.println(invalidException.getMessage());
            Assert.assertEquals("DATA_NOT_FOUND", invalidException.getMessage());
        }
    }


      @Test
    public void givenNewAddressData_whenAdded_shouldThrowException() throws InvalidException {
        try {
            addressBookDB.addNewAddressBookData("Manish", "Patil", "Surat", "Gujarat", 234563, 9665353267L, LocalDate.now());
        } catch (InvalidException invalidException) {
            System.out.println(invalidException.getMessage());
            Assert.assertEquals("DATA_NOT_ADDED", invalidException.getMessage());
        }
    }

}
