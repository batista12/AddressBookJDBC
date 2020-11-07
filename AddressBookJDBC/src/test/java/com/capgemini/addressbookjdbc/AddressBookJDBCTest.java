package com.capgemini.addressbookjdbc;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

public class AddressBookJDBCTest {
	static AddressBookService serviceObj;
	static List<ContactDetails> contactsList;
	static Map<String, Integer> contactsCount;

	@BeforeClass
	public static void setUp() {
		serviceObj = new AddressBookService();
		contactsList = new ArrayList<>();
	}

	@Test
	public void givenAddressBookDB_WhenRetrieved_ShouldMatchContactsCount() throws DBServiceException {
		contactsList = serviceObj.viewAddressBook();
		assertEquals(3, contactsList.size());
	}

	@Test
	public void givenUpdatedContacts_WhenRetrieved_ShouldBeSyncedWithDB() throws DBServiceException {
		serviceObj.updateContactDetails("City D", "986754", "Ambani");
		boolean isSynced = serviceObj.isAddressBookSyncedWithDB("Ambani");
		assertTrue(isSynced);
	}

	@Test
	public void givenDateRange_WhenRetrieved_ShouldMatchContactsCount() throws DBServiceException {
		contactsList = serviceObj.viewContactsByDateRange(LocalDate.of(2015, 01, 01), LocalDate.now());
		assertEquals(3, contactsList.size());
	}

	@Test
	public void givenAddressDB_WhenRetrievedCountByCity_ShouldReturnCountGroupedByCity() throws DBServiceException {
		contactsCount = serviceObj.countContactsByCityOrState("city");
		assertEquals(1, contactsCount.get("City A"), 0);

	}

	@Test
	public void givenAddressDB_WhenRetrievedCountByCity_ShouldReturnCountGroupedByState() throws DBServiceException {
		contactsCount = serviceObj.countContactsByCityOrState("state");
		assertEquals(1, contactsCount.get("State A"), 0);

	}

	@Test
	public void givenContactData_WhenAddedToDB_ShouldSyncWithDB() throws DBServiceException {
		serviceObj.insertNewContacts("Riya", "Singh", "Friend_Book", "Friend", "Agrico", "Jhar", "Jhar", "789342",
				"9887223345", "riyasingh@gmail.com", "2017-05-09");
		boolean isSynced = serviceObj.isAddressBookSyncedWithDB("Riya");
		assertTrue(isSynced);
	}
	@Test
	public void givenMultipleContact_WhenAdded_ShouldSyncWithDB() throws DBServiceException {
		ContactDetails[] contArr= {
								new ContactDetails("Riya","Koyyani","Family_Book","Family","Agrico",
										"Jhar","Jhar","831009","9825678845", "mahi@gmail.com","2014-04-04"),
								new ContactDetails("Soni","Koyyani","Family_Book","Family","Agrico","Jhar",
										"Jhar","831009","9825678845","Soni@gmail.com","2016-07-19"),
								new ContactDetails("Juniper","Dokkala","Friend_Book","Friend","Agrico","Jhar",
										"Jhar", "831009","9825678845","Juniper@yahoo.com","2018-06-05"),
		};
		serviceObj.addEmployeeListToEmployeeAndPayrollWithThreads(Arrays.asList(contArr));
		boolean isSynced1 = serviceObj.isAddressBookSyncedWithDB("Mahi");
		boolean isSynced2 = serviceObj.isAddressBookSyncedWithDB("Soni");
		boolean isSynced3 = serviceObj.isAddressBookSyncedWithDB("Juniper");
		assertTrue(isSynced1);
		assertTrue(isSynced2);
		assertTrue(isSynced3);
	}

}
