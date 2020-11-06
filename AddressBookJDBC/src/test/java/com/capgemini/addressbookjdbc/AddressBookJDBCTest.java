package com.capgemini.addressbookjdbc;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class AddressBookJDBCTest {
	static AddressBookService serviceObj;
	static List<ContactDetails> contactsList;

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
	public void givenDateRange_WhenRetrieved_ShouldMatchContactsCount() throws DBServiceException{
		contactsList = serviceObj.viewContactsByDateRange(LocalDate.of(2015,01,01), LocalDate.now() );
		assertEquals(3, contactsList.size());
	}

}
