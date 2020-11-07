package com.capgemini.addressbookjdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressBookService {
	ContactDetails contactObj = null;

	/**
	 * @return
	 * @throws DBServiceException
	 */
	public List<ContactDetails> viewAddressBook() throws DBServiceException {
		List<ContactDetails> contactsList = new ArrayList<>();
		String query = "select * from address_book";
		try (Connection con = AddressBookJDBC.getConnection()) {
			Statement statement = con.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				int id = resultSet.getInt(1);
				String fisrtName = resultSet.getString(2);
				String lastName = resultSet.getString(3);
				String addressName = resultSet.getString(4);
				String addressType = resultSet.getString(5);
				String address = resultSet.getString(6);
				String city = resultSet.getString(7);
				String state = resultSet.getString(8);
				String zip = resultSet.getString(9);
				String phoneNumber = resultSet.getString(10);
				String email = resultSet.getString(11);
				contactObj = new ContactDetails(id, fisrtName, lastName, addressName, addressType, address, city, state,
						zip, phoneNumber, email);
				contactsList.add(contactObj);
			}
		} catch (Exception e) {
			throw new DBServiceException("SQL Exception", DBServiceExceptionType.SQL_EXCEPTION);
		}
		System.out.println(contactsList);
		return contactsList;
	}
	/**
	 * @param fName
	 * @return
	 * @throws DBServiceException
	 */
	public List<ContactDetails> viewContactsByName(String fName) throws DBServiceException {
		List<ContactDetails> contactsListByName = new ArrayList<>();
		String query = "select * from address_book where first_name = ?";
		try (Connection con = AddressBookJDBC.getConnection()) {
			PreparedStatement preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, fName);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				int id = resultSet.getInt(1);
				String fisrtName = resultSet.getString(2);
				String lastName = resultSet.getString(3);
				String addressName = resultSet.getString(4);
				String addressType = resultSet.getString(5);
				String address = resultSet.getString(6);
				String city = resultSet.getString(7);
				String state = resultSet.getString(8);
				String zip = resultSet.getString(9);
				String phoneNumber = resultSet.getString(10);
				String email = resultSet.getString(11);
				contactObj = new ContactDetails(id, fisrtName, lastName, addressName, addressType, address, city, state,
						zip, phoneNumber, email);
				contactsListByName.add(contactObj);
			}
		} catch (Exception e) {
			throw new DBServiceException("SQL Exception", DBServiceExceptionType.SQL_EXCEPTION);
		}
		System.out.println(contactsListByName);
		return contactsListByName;
	}

	/**
	 * @param state
	 * @param zip
	 * @param fName
	 * @throws DBServiceException
	 */
	public void updateContactDetails(String state, String zip, String fName) throws DBServiceException {
		String query = "update address_book set state = ? , zip = ? where first_name = ?";
		try (Connection con = AddressBookJDBC.getConnection()) {
			PreparedStatement preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, state);
			preparedStatement.setString(2, zip);
			preparedStatement.setString(3, fName);
			int result = preparedStatement.executeUpdate();
			contactObj = getContactDetails(fName);
			if (result > 0 && contactObj != null) {
				contactObj.setStateName(state);
				contactObj.setZipCode(zip);
			}
		} catch (Exception e) {
			throw new DBServiceException("SQL Exception", DBServiceExceptionType.SQL_EXCEPTION);
		}
	}

	public ContactDetails getContactDetails(String fName) throws DBServiceException {
		return viewAddressBook().stream().filter(e -> e.getFirstName().equals(fName)).findFirst().orElse(null);
	}

	public boolean isAddressBookSyncedWithDB(String fName) throws DBServiceException {
		try {
			return viewContactsByName(fName).get(0).equals(getContactDetails(fName));
		} catch (IndexOutOfBoundsException e) {
		} catch (Exception e) {
			throw new DBServiceException("SQL Exception", DBServiceExceptionType.SQL_EXCEPTION);
		}
		return false;
	}

	/**
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DBServiceException
	 */
	public List<ContactDetails> viewContactsByDateRange(LocalDate startDate, LocalDate endDate)
			throws DBServiceException {
		List<ContactDetails> contactsListByStartDate = new ArrayList<>();
		String query = "select * from address_book where date_added between ? and  ?";
		try (Connection con = AddressBookJDBC.getConnection()) {
			PreparedStatement preparedStatement = con.prepareStatement(query);
			preparedStatement.setDate(1, Date.valueOf(startDate));
			preparedStatement.setDate(2, Date.valueOf(endDate));
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				int id = resultSet.getInt(1);
				String fisrtName = resultSet.getString(2);
				String lastName = resultSet.getString(3);
				String addressName = resultSet.getString(4);
				String addressType = resultSet.getString(5);
				String address = resultSet.getString(6);
				String city = resultSet.getString(7);
				String state = resultSet.getString(8);
				String zip = resultSet.getString(9);
				String phoneNumber = resultSet.getString(10);
				String email = resultSet.getString(11);
				contactObj = new ContactDetails(id, fisrtName, lastName, addressName, addressType, address, city, state,
						zip, phoneNumber, email);
				contactsListByStartDate.add(contactObj);
			}
		} catch (Exception e) {
			throw new DBServiceException("SQL Exception", DBServiceExceptionType.SQL_EXCEPTION);
		}
		return contactsListByStartDate;
	}
	/**
	 * @param column
	 * @return
	 * @throws DBServiceException
	 */
	public Map<String, Integer> countContactsByCityOrState(String column) throws DBServiceException {
		Map<String, Integer> contactsCount = new HashMap<>();
		String query = String.format("select %s , count(%s) from address_book group by %s;", column, column, column);
		try (Connection con = AddressBookJDBC.getConnection()) {
			PreparedStatement preparedStatement = con.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				contactsCount.put(resultSet.getString(1), resultSet.getInt(2));
			}
		} catch (Exception e) {
			throw new DBServiceException("SQL Exception", DBServiceExceptionType.SQL_EXCEPTION);
		}
		return contactsCount;
	}
	public List<ContactDetails>  insertNewContacts(String firstName,String lastName,String address_name,String addressType,
			String address,String city,String state,String zip,String phoneNo,String email,String date) throws DBServiceException {
			String sql = String.format("insert into address_book (first_name,last_name,address_name,address_type,address,city,state,zip,phone_number,email,date_added)"+
			" values ('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s');",firstName,lastName,address_name,addressType,address,city,state,zip,phoneNo,email,date);
			try (Connection con = AddressBookJDBC.getConnection()) {
				PreparedStatement preparedStatement = con.prepareStatement(sql);
				int result = preparedStatement.executeUpdate();
				if (result == 1)
					contactObj = new ContactDetails(firstName,lastName,address_name,addressType,address,city,state,zip,phoneNo,email,date);
					viewAddressBook().add(contactObj);
			}catch (Exception e) {
				throw new DBServiceException("SQL Exception", DBServiceExceptionType.SQL_EXCEPTION);
			}
			return viewAddressBook();
		}
	/**
	 * @param contactList
	 * @throws DBServiceException
	 */
	public void addEmployeeListToEmployeeAndPayrollWithThreads(List<ContactDetails> contactList) throws DBServiceException {
		Map<Integer, Boolean> contactAditionStatus = new HashMap<>();
		contactList.forEach(contact -> {
			Runnable task = () -> {
				contactAditionStatus.put(contact.hashCode(), false);
				System.out.println("Contact being added : " + contact.getFirstName());
				try {
					insertNewContacts(contact.getFirstName(),contact.getLastName(),contact.getAddress_name(),contact.getAddressType(),
							contact.getAddress(),contact.getCityName(), contact.getStateName(), contact.getZipCode(),
							contact.getPhoneNumber(), contact.getEmailId(),contact.getDate());
				} catch (DBServiceException e) {
					e.printStackTrace();
				}
				contactAditionStatus.put(contact.hashCode(), true);
				System.out.println("Contact added : " + contact.getFirstName());
			};
			Thread thread = new Thread(task, contact.getFirstName());
			thread.start();
		});

		while (contactAditionStatus.containsValue(false)) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

}



