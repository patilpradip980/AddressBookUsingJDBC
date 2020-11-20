package com.bridgelabz.AddressBook;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class AddressBookTestCases {
    String sqlRead = "select * from PersonData";
    AddressBookDB addressBookDB;

    @Before
    public void setUp() throws Exception {
        addressBookDB = new AddressBookDB();
    }

    @Test
    public void givenAddressBookDB_whenRetrievedData_MatchCountWithDB() {
        List<AddressBookData> addressBookData = addressBookDB.readAddressBookData(sqlRead);
        Assert.assertEquals(4, addressBookData.size());
 }
}
