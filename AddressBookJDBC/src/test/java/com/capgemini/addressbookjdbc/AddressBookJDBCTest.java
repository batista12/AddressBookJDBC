package com.capgemini.addressbookjdbc;
import static org.junit.Assert.assertEquals;

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
		assertEquals(6, contactsList.size());
	}

}

