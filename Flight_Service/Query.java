import java.sql.*;
import java.util.ArrayList;

public class Query extends QuerySearchOnly {

	// Logged In User
	private String username; // customer username is unique

	// transactions
	private static final String BEGIN_TRANSACTION_SQL = "SET TRANSACTION ISOLATION LEVEL SERIALIZABLE; BEGIN TRANSACTION;";
	protected PreparedStatement beginTransactionStatement;

	private static final String COMMIT_SQL = "COMMIT TRANSACTION";
	protected PreparedStatement commitTransactionStatement;

	private static final String ROLLBACK_SQL = "ROLLBACK TRANSACTION";
	protected PreparedStatement rollbackTransactionStatement;
	
	private static final String CLEAR_TABLES = "DELETE FROM reservation";
	protected PreparedStatement clearTablesStatement1;
	
	private static final String CLEAR_TABLES2 = "DELETE FROM users";
	protected PreparedStatement clearTablesStatement2;
	
	private static final String CREATE_CUSTOMER = "INSERT INTO users VALUES (?,?,?)";
	protected PreparedStatement createCustomerStatement;
	
	private static final String LOG_IN = "SELECT userName FROM users WHERE userName = ? and password = ?";
	protected PreparedStatement logInStatement;
	
	private static final String MAKE_RESERVATION = "INSERT INTO reservation VALUES (?,?,?,?,?,?,?,?)";
	protected PreparedStatement makeReservationStatement;
	
	private static final String CHECK_DOUBLE_BOOK = "SELECT day FROM reservation WHERE day = ?";
	protected PreparedStatement checkDoubleBookStatement; 
	
	private static final String COUNT_TUPLES = "SELECT * FROM reservation";
	protected PreparedStatement countTuplesStatement;
	
	private static final String PAY = "SELECT id FROM reservation WHERE id = ?";
	protected PreparedStatement payStatement;
	
	private static final String CHECK_BALANCE = "SELECT balance FROM users WHERE userName = ?";
	protected PreparedStatement checkBalanceStatement;
	
	private static final String IS_PAID = "UPDATE reservation SET isPaid = ? WHERE id = ?";
	protected PreparedStatement isPaidStatement;
	
	private static final String LOOKUP_PRICE = "SELECT price FROM reservation WHERE id = ?";
	protected PreparedStatement lookupPriceStatement;
	
	private static final String LOOKUP_FID = "SELECT id, fid1, fid2, isPaid FROM reservation";
	protected PreparedStatement lookupFidStatement;
	
	private static final String FIND_FLIGHT = "SELECT fid, day_of_month, carrier_id, flight_num, origin_city, dest_city, actual_time, capacity, price FROM Flights WHERE fid = ?";
	protected PreparedStatement findFlightStatement;
	
	private static final String TO_BE_CANCELED = "SELECT id, price, isPaid FROM reservation WHERE id = ?";
	protected PreparedStatement toBeCanceledStatement;
	
	private static final String UPDATE_BALANCE = "UPDATE users SET balance = balance + ?";
	protected PreparedStatement updateBalanceStatement;
	
	private static final String CANCEL_RESERVATION = "DELETE FROM reservation WHERE id = ?";
	protected PreparedStatement cancelReservationStatement;
	
	private static final String PREVIOUS_ID = "SELECT MAX(id) AS prev FROM reservation";
	protected PreparedStatement previousIDStatement;
	
	private static final String CHECK_PAID_STATUS = "SELECT isPaid FROM reservation WHERE id = ?";
	protected PreparedStatement checkPaidStatusStatement;
	public Query(String configFilename) {
		super(configFilename);
	}
	
	


	/**
	 * Clear the data in any custom tables created. Do not drop any tables and do not
	 * clear the flights table. You should clear any tables you use to store reservations
	 * and reset the next reservation ID to be 1.
	 */
	public void clearTables() throws Exception {
		//This means that your clearTables() method 
		// should execute some PreparedStatement queries 
		//that delete all of the data from your created tables (i.e. not Flights)
		// your code here
		clearTablesTransaction();
		clearTables2Transaction();
		
	}


	/**
	 * prepare all the SQL statements in this method.
	 * "preparing" a statement is almost like compiling it.
	 * Note that the parameters (with ?) are still not filled in
	 */
	@Override
	public void prepareStatements() throws Exception
	{
		super.prepareStatements();
		beginTransactionStatement = conn.prepareStatement(BEGIN_TRANSACTION_SQL);
		commitTransactionStatement = conn.prepareStatement(COMMIT_SQL);
		rollbackTransactionStatement = conn.prepareStatement(ROLLBACK_SQL);
		clearTablesStatement1 = conn.prepareStatement(CLEAR_TABLES);
		clearTablesStatement2 = conn.prepareStatement(CLEAR_TABLES2);
		createCustomerStatement = conn.prepareStatement(CREATE_CUSTOMER);
		logInStatement = conn.prepareStatement(LOG_IN);
		makeReservationStatement = conn.prepareStatement(MAKE_RESERVATION);
		countTuplesStatement = conn.prepareStatement(COUNT_TUPLES);
		checkDoubleBookStatement = conn.prepareStatement(CHECK_DOUBLE_BOOK);
		payStatement = conn.prepareStatement(PAY);
		checkBalanceStatement = conn.prepareStatement(CHECK_BALANCE);
		isPaidStatement = conn.prepareStatement(IS_PAID);
		lookupPriceStatement = conn.prepareStatement(LOOKUP_PRICE);
		lookupFidStatement = conn.prepareStatement(LOOKUP_FID);
		findFlightStatement = conn.prepareStatement(FIND_FLIGHT);
		toBeCanceledStatement = conn.prepareStatement(TO_BE_CANCELED);
		updateBalanceStatement = conn.prepareStatement(UPDATE_BALANCE);
		cancelReservationStatement = conn.prepareStatement(CANCEL_RESERVATION);
		previousIDStatement = conn.prepareStatement(PREVIOUS_ID);
		checkPaidStatusStatement = conn.prepareStatement(CHECK_PAID_STATUS);
		/* add here more prepare statements for all the other queries you need */
		/* . . . . . . */
	}


	/**
	 * Takes a user's username and password and attempts to log the user in.
	 *
	 * @return If someone has already logged in, then return "User already logged in\n"
	 * For all other errors, return "Login failed\n".
	 *
	 * Otherwise, return "Logged in as [username]\n".
	 */
	public String transaction_login(String username, String password) {
		try {
			conn.setAutoCommit(false);
			beginTransaction();
			if(this.username != null) {
				rollbackTransaction();
				return "User already logged in\n";
			} else if(logInTransaction(username, password)) {
				this.username = username;
				commitTransaction();
				return "Logged in as " + this.username + "\n";	
			} else {
				// wrong password
				rollbackTransaction();
				return "Login failed\n";
			}
		} catch (SQLException e) {
			try {
				rollbackTransaction();
				return "Login failed\n";
			} catch(SQLException f) {
				return "Login failed\n";
			}			
		}		
	}

	/**
	 * Implement the create user function.
	 *
	 * @param username new user's username. User names are unique the system.
	 * @param password new user's password.
	 * @param initAmount initial amount to deposit into the user's account, should be >= 0 (failure otherwise).
	 *
	 * @return either "Created user {@code username}\n" or "Failed to create user\n" if failed.
	 */
	public String transaction_createCustomer (String username, String password, int initAmount) {
		
		try {
			conn.setAutoCommit(false);
			beginTransaction();
			createCustomerTransaction(username, password, initAmount);
			commitTransaction();
			return "Created user " + username + "\n";
		} catch (SQLException e) {
			try {
				rollbackTransaction();
				return "Failed to create user\n";
			} catch(SQLException f) {
				return "Failed to create user\n";
			}
		}
		
	}

	/**
	 * Implements the book itinerary function.
	 *
	 * @param itineraryId ID of the itinerary to book. This must be one that is returned by search in the current session.
	 *
	 * @return If the user is not logged in, then return "Cannot book reservations, not logged in\n".
	 * If try to book an itinerary with invalid ID, then return "No such itinerary {@code itineraryId}\n".
	 * If the user already has a reservation on the same day as the one that they are trying to book now, then return
	 * "You cannot book two flights in the same day\n".
	 * For all other errors, return "Booking failed\n".
	 *
	 * And if booking succeeded, return "Booked flight(s), reservation ID: [reservationId]\n" where
	 * reservationId is a unique number in the reservation system that starts from 1 and increments by 1 each time a
	 * successful reservation is made by any user in the system.
	 */
	public String transaction_book(int itineraryId)
	{
		try {
			conn.setAutoCommit(false);
			beginTransaction();
			if (this.username == null) {
				rollbackTransaction();
				return "Cannot book reservations, not logged in\n";
			} else if(flightItineraries.isEmpty() || itineraryId > flightItineraries.size() - 1) {
				rollbackTransaction();
				return "No such itinerary " + itineraryId + "\n";
			} else if (checkDoubleBookTransaction(flightItineraries.get(itineraryId).f1.dayOfMonth)) {
				rollbackTransaction();
				return "You cannot book two flights in the same day\n";
			} else {
				
				int numFlights;
				int fid1 = flightItineraries.get(itineraryId).f1.fid; 
				int fid2;
				int price = flightItineraries.get(itineraryId).f1.price;
				if(flightItineraries.get(itineraryId).f2 != null) {
					price = price + flightItineraries.get(itineraryId).f2.price;
				}
				int isPaid = 0;
				int day = flightItineraries.get(itineraryId).f1.dayOfMonth;
				
				if(flightItineraries.get(itineraryId).isDirect){
					numFlights = 1;
					fid2 = -1;
				} else {
					numFlights = 2;
					fid2 = flightItineraries.get(itineraryId).f2.fid;
				}
				
				
				// if empty table
				
				if(countTuplesTransaction() == false) { //adding to reservation table
					makeReservationTransaction(1, numFlights, fid1, fid2, price, 0, day);
					commitTransaction();
					return "Booked flight(s), reservation ID: " + 1 + "\n"; 
				} else { // non-empty table
					int index = previousIDTransaction() + 1;
					makeReservationTransaction(index, numFlights, fid1, fid2, price, 0, day);
					commitTransaction();
					return "Booked flight(s), reservation ID: " + index + "\n"; 
				}
				
			}
		} catch (SQLException e) {
			try {
				rollbackTransaction();
				return "Booking failed\n";
			} catch (SQLException f) {
				return "Booking failed\n";
			}
		}		
	}

	/**
	 * Implements the pay function.
	 *
	 * @param reservationId the reservation to pay for.
	 *
	 * @return If no user has logged in, then return "Cannot pay, not logged in\n"
	 * If the reservation is not found / not under the logged in user's name, then return
	 * "Cannot find unpaid reservation [reservationId] under user: [username]\n"
	 * If the user does not have enough money in their account, then return
	 * "User has only [balance] in account but itinerary costs [cost]\n"
	 * For all other errors, return "Failed to pay for reservation [reservationId]\n"
	 *
	 * If successful, return "Paid reservation: [reservationId] remaining balance: [balance]\n"
	 * where [balance] is the remaining balance in the user's account.
	 */
	public String transaction_pay (int reservationId)
	{
		try {
			conn.setAutoCommit(false);
			beginTransaction();
			if(this.username == null) {
				rollbackTransaction();
				return "Cannot pay, not logged in\n";
			} else if (flightItineraries == null 
					   || reservationId > flightItineraries.size() 
					   || checkPaidStatusTransaction(reservationId) == 1) {
				rollbackTransaction();
				return "Cannot find unpaid reservation " + reservationId + " under user: " + this.username + "\n";
			} else {
				int currentBalance = checkBalanceTransaction();
				int price = lookupPriceTransaction(reservationId); // look up price in reservation table
				if (currentBalance < price) {
					rollbackTransaction();
					return "User has only " + currentBalance + " in account but itinerary costs " + price + "\n";
				}
				// Update reservation table
				isPaidTransaction(1, reservationId);
				// Update balance
				updateBalanceTransaction(-1*price);
				commitTransaction();
				return "Paid reservation: " + reservationId + " remaining balance: " + (currentBalance - price) + "\n";
			}
		} catch (SQLException e) {
			//e.printStackTrace();
			try {
				rollbackTransaction();
				return "Failed to pay for reservation " + reservationId + "\n";
			} catch (SQLException f) {
				return "Failed to pay for reservation " + reservationId + "\n";
			}
			
		}
		
	}

	/**
	 * Implements the reservations function.
	 *
	 * @return If no user has logged in, then return "Cannot view reservations, not logged in\n"
	 * If the user has no reservations, then return "No reservations found\n"
	 * For all other errors, return "Failed to retrieve reservations\n"
	 *
	 * Otherwise return the reservations in the following format:
	 *
	 * Reservation [reservation ID] paid: [true or false]:\n"
	 * [flight 1 under the reservation]
	 * [flight 2 under the reservation]
	 * Reservation [reservation ID] paid: [true or false]:\n"
	 * [flight 1 under the reservation]
	 * [flight 2 under the reservation]
	 * ...
	 *
	 * Each flight should be printed using the same format as in the {@code Flight} class.
	 *
	 * @see Flight#toString()
	 */
	public String transaction_reservations()
	{
		
		try {
			conn.setAutoCommit(false);
			beginTransaction();
			if(this.username == null) {
				rollbackTransaction();
				return "Cannot view reservations, not logged in\n";
			} else if (countTuplesTransaction() == false) {
				rollbackTransaction();
				return "No reservations found\n";
			} else {
				
				ResultSet itineraryInfo = lookupFidTransaction();
				StringBuffer sb = new StringBuffer();
				while(itineraryInfo.next()) {
					int reservationId = itineraryInfo.getInt("id");
					int fid1 = itineraryInfo.getInt("fid1");
					int fid2 = itineraryInfo.getInt("fid2");
					int isPaid = itineraryInfo.getInt("isPaid");
					sb.append("Reservation " + reservationId + " paid: ");
					if(isPaid == 0) {
						sb.append("false:\n");
					} else {
						sb.append("true:\n");
					}
					Flight f1 = findFlightTransaction(fid1);
					sb.append(f1.toString() + "\n");
					if(fid2 != -1) {
						Flight f2 = findFlightTransaction(fid2);
						sb.append(f2.toString() + "\n");
					}	
				}
				itineraryInfo.close();
				commitTransaction();
				return sb.toString();				
			}
		} catch(SQLException e) {
			try {
				rollbackTransaction();
				return "Failed to retrieve reservations\n";
			} catch(SQLException f) {
				return "Failed to retrieve reservations\n";
			}
			//e.printStackTrace();			
		}
		
	}

	/**
	 * Implements the cancel operation.
	 *
	 * @param reservationId the reservation ID to cancel
	 *
	 * @return If no user has logged in, then return "Cannot cancel reservations, not logged in\n"
	 * For all other errors, return "Failed to cancel reservation [reservationId]"
	 *
	 * If successful, return "Canceled reservation [reservationId]"
	 *
	 * Even though a reservation has been canceled, its ID should not be reused by the system.
	 */
	public String transaction_cancel(int reservationId)
	{
		try {
			conn.setAutoCommit(false);
			beginTransaction();
			if(this.username == null) {
				rollbackTransaction();
				return "Cannot cancel reservations, not logged in\n";
			} else {
				// cancel reservation
				ResultSet toBeCanceled = toBeCanceledTransaction(reservationId);
				toBeCanceled.next();
				int price = toBeCanceled.getInt("price");
				int isPaid = toBeCanceled.getInt("isPaid");
				if(isPaid == 1) {
					updateBalanceTransaction(price);
				}
				isPaidTransaction(0, reservationId);
				cancelReservationTransaction(reservationId);
				toBeCanceled.close();
				commitTransaction();
				return "Canceled reservation " + reservationId + "\n";				
			}
		} catch (SQLException e) {
			try {
				rollbackTransaction();
				return "Failed to cancel reservation " + reservationId + "\n";
			} catch (SQLException f) {
				return "Failed to cancel reservation " + reservationId + "\n";
			}
			//e.printStackTrace();
			
		}
	}


	/* some utility functions below */

	public void beginTransaction() throws SQLException
	{
		conn.setAutoCommit(false);
		beginTransactionStatement.executeUpdate();
	}

	public void commitTransaction() throws SQLException
	{
		commitTransactionStatement.executeUpdate();
		conn.setAutoCommit(true);
	}

	public void rollbackTransaction() throws SQLException
	{
		rollbackTransactionStatement.executeUpdate();
		conn.setAutoCommit(true);
	}
	
	public void clearTablesTransaction() throws SQLException 
	{
		clearTablesStatement1.executeUpdate();	
	}
	
	public void clearTables2Transaction() throws SQLException {
		clearTablesStatement2.executeUpdate();
	}
	
	
	public void createCustomerTransaction(String userName, String password, int balance) throws SQLException {
		userName = userName.toLowerCase();
		password = password.toLowerCase();
		createCustomerStatement.clearParameters();
		createCustomerStatement.setString(1, userName);
		createCustomerStatement.setString(2, password);
		createCustomerStatement.setInt(3, balance);
		createCustomerStatement.executeUpdate();
		//conn.setAutoCommit(true);		
	}
	
	public boolean logInTransaction(String userName, String password) throws SQLException {
		logInStatement.clearParameters();
		logInStatement.setString(1, userName);
		logInStatement.setString(2, password);
		ResultSet results = logInStatement.executeQuery();
		//conn.setAutoCommit(true);
		return results.next();
	}
	
	public void makeReservationTransaction(int reservationID, int numFlights, int fid1, int fid2, int price, int isPaid, int day) throws SQLException {
		makeReservationStatement.clearParameters();
		makeReservationStatement.setInt(1, reservationID);
		makeReservationStatement.setString(2, this.username);
		makeReservationStatement.setInt(3, numFlights);
		makeReservationStatement.setInt(4, fid1);
		makeReservationStatement.setInt(5, fid2);
		makeReservationStatement.setInt(6, price);
		makeReservationStatement.setInt(7, isPaid);
		makeReservationStatement.setInt(8, day);
		makeReservationStatement.executeUpdate();
	}
	
	public boolean countTuplesTransaction() throws SQLException {
		ResultSet results = countTuplesStatement.executeQuery(); 
		return results.next(); //error here
	}
	
	public boolean checkDoubleBookTransaction(int day) throws SQLException { // If there is a search result, day already booked
		checkDoubleBookStatement.clearParameters();
		checkDoubleBookStatement.setInt(1, day);
		ResultSet results = checkDoubleBookStatement.executeQuery();
		return results.next();
	}
	
	public int payTransaction(int id) throws SQLException {
		payStatement.clearParameters();
		payStatement.setInt(1, id);
		ResultSet results = payStatement.executeQuery();
		return results.getInt("id");
	}
	
	public int checkBalanceTransaction() throws SQLException {
		checkBalanceStatement.clearParameters();
		checkBalanceStatement.setString(1, this.username);
		ResultSet results = checkBalanceStatement.executeQuery(); //error here
		results.next();
		int balance = results.getInt("balance"); 
		return balance;
	}
	
	public void isPaidTransaction(int isPaid, int reservationId) throws SQLException {
		isPaidStatement.clearParameters();
		isPaidStatement.setInt(1, isPaid);
		isPaidStatement.setInt(2, reservationId);
		isPaidStatement.executeUpdate();
	}
	
	public int lookupPriceTransaction(int reservationId) throws SQLException {
		lookupPriceStatement.clearParameters();
		lookupPriceStatement.setInt(1, reservationId);
		ResultSet results = lookupPriceStatement.executeQuery();
		results.next();
		int price = results.getInt("price"); 
		return price;
	}
	
	public ResultSet lookupFidTransaction() throws SQLException { // returns all info about initerary
		lookupFidStatement.clearParameters();
		ResultSet results = lookupFidStatement.executeQuery();
		return results;
	}
	
	public Flight findFlightTransaction(int fid) throws SQLException {
		findFlightStatement.clearParameters();
		findFlightStatement.setInt(1, fid);
		ResultSet results = findFlightStatement.executeQuery();
		results.next();
		int flightId = results.getInt("fid");
		int dayOfMonth = results.getInt("day_of_month");
		String carrierId = results.getString("carrier_id");
		String flightNum = results.getString("flight_num");
		String origin = results.getString("origin_city");
		String dest = results.getString("dest_city");
		int time = results.getInt("actual_time");
		int capacity = results.getInt("capacity");
		int price = results.getInt("price");
		return new Flight(flightId, dayOfMonth, carrierId, flightNum, origin, dest, time, capacity, price);
	}
	
	public ResultSet toBeCanceledTransaction(int reservationId) throws SQLException {
		toBeCanceledStatement.clearParameters();
		toBeCanceledStatement.setInt(1, reservationId);
		ResultSet results = toBeCanceledStatement.executeQuery();
		return results;
	}
	
	public void updateBalanceTransaction(int amount) throws SQLException {
		updateBalanceStatement.clearParameters();
		updateBalanceStatement.setInt(1, amount);
		updateBalanceStatement.executeUpdate();
	}
	
	public void cancelReservationTransaction(int reservationId) throws SQLException {
		cancelReservationStatement.clearParameters();
		cancelReservationStatement.setInt(1, reservationId);
		cancelReservationStatement.executeUpdate();
	}
	
	public int previousIDTransaction() throws SQLException {
		ResultSet results = previousIDStatement.executeQuery();
		return results.getInt("prev");
	}
	
	public int checkPaidStatusTransaction(int id) throws SQLException {
		checkPaidStatusStatement.clearParameters();
		checkPaidStatusStatement.setInt(1, id);
		ResultSet results = checkPaidStatusStatement.executeQuery();
		results.next();
		return results.getInt("isPaid");
		
	}
}
