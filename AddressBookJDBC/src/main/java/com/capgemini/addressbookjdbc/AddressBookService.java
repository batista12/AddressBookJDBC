package com.capgemini.addressbookjdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
}
