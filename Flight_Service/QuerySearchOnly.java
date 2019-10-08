import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Runs queries against a back-end database. This class is responsible for
 * searching for flights.
 */
public class QuerySearchOnly {
	// `dbconn.properties` config file
	private String configFilename;
	
	// used to store itineraries for each search
	protected ArrayList<Itinerary> flightItineraries;

	// DB Connection
	protected Connection conn;

	// Canned queries
	private static final String CHECK_FLIGHT_CAPACITY = "SELECT capacity FROM Flights WHERE fid = ?";
	private static final String CHECK_FLIGHT_ORIGIN = "SELECT fid FROM Flights WHERE originCity = ?";
	private static final String CHECK_DIRECT_FLIGHT_ITINERARY = "SELECT TOP(?) fid, day_of_month, carrier_id, flight_num, origin_city, dest_city, actual_time, capacity, price "
			+ "FROM Flights WHERE origin_city = ? and dest_city = ? and day_of_month = ? and canceled = 0 "
			+ "ORDER BY actual_time ASC, fid ASC ";
	private static final String CHECK_INDIRECT_FLIGHT_ITINERARY = 
			"SELECT TOP (?) f1.fid as fid1, f1.day_of_month as day1, f1.carrier_id as carrier1, "
			+ "f1.flight_num as num1, f1.origin_city as origin1, f1.dest_city as dest1, " 
			+ "f1.actual_time as time1, f1.capacity as capacity1, f1.price as price1, "
			+ "f2.fid as fid2, f2.day_of_month as day2, f2.carrier_id as carrier2, " 
			+ "f2.flight_num as num2, f2.origin_city as origin2, f2.dest_city as dest2, "
			+ "f2.actual_time as time2, f2.capacity as capacity2, f2.price as price2 " 
			+ "FROM Flights as f1, Flights as f2 "
			+ "WHERE f1.origin_city = ? and f2.dest_city = ? and f1.dest_city = f2.origin_city "
			+ "and f1.day_of_month = ? and f1.day_of_month = f2.day_of_month and f1.canceled = 0 and f2.canceled = 0 "
			+ "ORDER BY f1.actual_time + f2.actual_time ASC ";
	protected PreparedStatement checkFlightCapacityStatement;
	protected PreparedStatement checkDirectFlightItineraryStatement;
	protected PreparedStatement checkIndirectFlightItineraryStatement;

	class Flight implements Comparable<Flight> {
		int fid;
		int dayOfMonth;
		String carrierId;
		String flightNum;
		String originCity;
		String destCity;
		int time;
		int capacity;
		int price;

		public Flight(int fid, int dayOfMonth, String carrierId, String flightNum, String originCity, String destCity,
				int time, int capacity, int price) {
			this.fid = fid;
			this.dayOfMonth = dayOfMonth;
			this.carrierId = carrierId;
			this.flightNum = flightNum;
			this.originCity = originCity;
			this.destCity = destCity;
			this.time = time;
			this.capacity = capacity;
			this.price = price;
		}

		@Override
		public String toString() {
			return "ID: " + fid + " Day: " + dayOfMonth + " Carrier: " + carrierId + " Number: " + flightNum
					+ " Origin: " + originCity + " Dest: " + destCity + " Duration: " + time + " Capacity: " + capacity
					+ " Price: " + price;
		}

		public int getTime() {
			return this.time;
		}

		public int getFid() {
			return this.fid;
		}
		
		public int getDayOfMonth() {
			return this.dayOfMonth;
		}

		public int compareTo(Flight other) {
			if (this.time == other.time) {
				return this.fid - other.fid;
			} else {
				return this.time - other.time;
			}
		}
	}
	
	class Itinerary implements Comparable<Itinerary> {
		int totalFlightTime;
		Flight f1;
		Flight f2;
		boolean isDirect;
		
		public Itinerary(Flight f1) {
			totalFlightTime = f1.time;
			this.f1 = f1;
			f2 = null;
			isDirect = true;
		}
		
		public Itinerary(Flight f1, Flight f2) {
			totalFlightTime = f1.time + f2.time;
			this.f1 = f1;
			this.f2 = f2;
			isDirect = false;
		}
		
		public int compareTo(Itinerary other) {
			if (this.totalFlightTime == other.totalFlightTime) {
				if (this.f1.fid == other.f1.fid) {
					return this.f2.fid - other.f2.fid;
				} else {
					return this.f1.fid - other.f1.fid;
				}
			} else {
				return this.totalFlightTime - other.totalFlightTime;
			}
		}
		
	}

	public QuerySearchOnly(String configFilename) {
		this.configFilename = configFilename;
	}

	/** Open a connection to SQL Server in Microsoft Azure. */
	public void openConnection() throws Exception {
		Properties configProps = new Properties();
		configProps.load(new FileInputStream(configFilename));

		String jSQLDriver = configProps.getProperty("flightservice.jdbc_driver");
		String jSQLUrl = configProps.getProperty("flightservice.url");
		String jSQLUser = configProps.getProperty("flightservice.sqlazure_username");
		String jSQLPassword = configProps.getProperty("flightservice.sqlazure_password");

		/* load jdbc drivers */
		Class.forName(jSQLDriver).newInstance();

		/* open connections to the flights database */
		conn = DriverManager.getConnection(jSQLUrl, // database
				jSQLUser, // user
				jSQLPassword); // password

		conn.setAutoCommit(true); // by default automatically commit after each
									// statement
		/*
		 * In the full Query class, you will also want to appropriately set the
		 * transaction's isolation level: conn.setTransactionIsolation(...) See
		 * Connection class's JavaDoc for details.
		 */
	}

	public void closeConnection() throws Exception {
		conn.close();
	}

	/**
	 * prepare all the SQL statements in this method. "preparing" a statement is
	 * almost like compiling it. Note that the parameters (with ?) are still not
	 * filled in
	 */
	public void prepareStatements() throws Exception {
		checkFlightCapacityStatement = conn.prepareStatement(CHECK_FLIGHT_CAPACITY);
		checkDirectFlightItineraryStatement = conn.prepareStatement(CHECK_DIRECT_FLIGHT_ITINERARY);
		checkIndirectFlightItineraryStatement = conn.prepareStatement(CHECK_INDIRECT_FLIGHT_ITINERARY);
		/*
		 * add here more prepare statements for all the other queries you need
		 */
		/* . . . . . . */
	}

	/**
	 * Implement the search function.
	 *
	 * Searches for flights from the given origin city to the given destination
	 * city, on the given day of the month. If {@code directFlight} is true, it
	 * only searches for direct flights, otherwise it searches for direct
	 * flights and flights with two "hops." Only searches for up to the number
	 * of itineraries given by {@code numberOfItineraries}.
	 *
	 * The results are sorted based on total flight time.
	 *
	 * @param originCity
	 * @param destinationCity
	 * @param directFlight
	 *            if true, then only search for direct flights, otherwise
	 *            include indirect flights as well
	 * @param dayOfMonth
	 * @param numberOfItineraries
	 *            number of itineraries to return
	 *
	 * @return If no itineraries were found, return "No flights match your
	 *         selection\n". If an error occurs, then return "Failed to
	 *         search\n".
	 *
	 *         Otherwise, the sorted itineraries printed in the following
	 *         format:
	 *
	 *         Itinerary [itinerary number]: [number of flights] flight(s),
	 *         [total flight time] minutes\n [first flight in itinerary]\n ...
	 *         [last flight in itinerary]\n
	 *
	 *         Each flight should be printed using the same format as in the
	 *         {@code Flight} class. Itinerary numbers in each search should
	 *         always start from 0 and increase by 1.
	 *
	 * @see Flight#toString()
	 */
	private String transaction_search_safe(String originCity, String destinationCity, boolean directFlight,
			int dayOfMonth, int numberOfItineraries) {
		
		StringBuffer itineraries = new StringBuffer();
		
		try {			
			ResultSet resultsDirect = checkDirectFlightItinerary(originCity, destinationCity, dayOfMonth,
					numberOfItineraries);		
			flightItineraries = new ArrayList<Itinerary>();
			while (resultsDirect.next()) {
				int fid = resultsDirect.getInt("fid");
				int day = resultsDirect.getInt("day_of_month");
				String carrierId = resultsDirect.getString("carrier_id");
				String flightNum = resultsDirect.getString("flight_num");
				String origin = resultsDirect.getString("origin_city");
				String destCity = resultsDirect.getString("dest_city");
				int time = resultsDirect.getInt("actual_time");
				int capacity = resultsDirect.getInt("capacity");
				int price = resultsDirect.getInt("price");
				Flight flightData = new Flight(fid, day, carrierId, flightNum, origin, destCity, time, capacity, price);
				Itinerary temp = new Itinerary(flightData);
				flightItineraries.add(temp);
			}
			
			Collections.sort(flightItineraries);
			Iterator<Itinerary> iter1 = flightItineraries.iterator();
			int index = 0;
			while(iter1.hasNext()) {
				Itinerary next = iter1.next();
				
				index++;
			}
			
			if (directFlight == false) {
				ResultSet resultsIndirect = checkIndirectFlightItinerary(originCity, destinationCity, dayOfMonth,
						numberOfItineraries - index);
				ArrayList<Itinerary> indirectFlightItineraries = new ArrayList<Itinerary>();	
				while(resultsIndirect.next()){
					int fid1 = resultsIndirect.getInt("fid1");
					int fid2 = resultsIndirect.getInt("fid2");
					int day1 = resultsIndirect.getInt("day1");
					int day2 = resultsIndirect.getInt("day2");
					String carrierId1 = resultsIndirect.getString("carrier1");
					String carrierId2 = resultsIndirect.getString("carrier2");
					String flightNum1 = resultsIndirect.getString("num1");
					String flightNum2 = resultsIndirect.getString("num2");
					String origin1 = resultsIndirect.getString("origin1");
					String origin2 = resultsIndirect.getString("origin2");
					String destCity1 = resultsIndirect.getString("dest1");
					String destCity2 = resultsIndirect.getString("dest2");
					int time1 = resultsIndirect.getInt("time1");
					int time2 = resultsIndirect.getInt("time2");
					int capacity1 = resultsIndirect.getInt("capacity1");
					int capacity2 = resultsIndirect.getInt("capacity2");
					int price1 = resultsIndirect.getInt("price1");
					int price2 = resultsIndirect.getInt("price2");
					Flight flightData1 = new Flight(fid1, day1, carrierId1, flightNum1, origin1, destCity1, time1, capacity1, price1);
					Flight flightData2 = new Flight(fid2, day2, carrierId2, flightNum2, origin2, destCity2, time2, capacity2, price2);
					Itinerary indirectFlight = new Itinerary(flightData1, flightData2);
					
					indirectFlightItineraries.add(indirectFlight);
				}
				Collections.sort(indirectFlightItineraries);
				Iterator<Itinerary> iter2 = indirectFlightItineraries.iterator();
				while(iter2.hasNext() && index < numberOfItineraries) {
					Itinerary next = iter2.next();
					flightItineraries.add(next);
					
					index++;
				}
				
				resultsIndirect.close();
				
			}
			resultsDirect.close();
			Collections.sort(flightItineraries);
			Iterator<Itinerary> iter3 = flightItineraries.iterator();
			int index2 = 0;
			while(iter3.hasNext()) {
				Itinerary next = iter3.next();
				if(next.isDirect) {
					itineraries.append("Itinerary " + index2 + ": 1 flight(s), " + next.totalFlightTime + " minutes\n" + 
							next.f1.toString() + "\n");
				} else {
					itineraries.append("Itinerary " + index2 + ": 2 flight(s), " + next.totalFlightTime + " minutes\n" + 
								next.f1.toString() + "\n"
								+ next.f2.toString() + "\n");
				}
				
				index2++;
			}
			
			if (index == 0) {
				return "No flights match your selection\n";
			}
			
		} catch (SQLException e) {
			//e.printStackTrace();
			return "Failed to search\n";
		}
		return itineraries.toString();
	}

	public String transaction_search(String originCity, String destinationCity, boolean directFlight, int dayOfMonth,
			int numberOfItineraries) {
		// Please implement your own (safe) version that uses prepared
		// statements rather than string concatenation.
		// You may use the `Flight` class (defined above).
		return transaction_search_safe(originCity, destinationCity, directFlight, dayOfMonth, numberOfItineraries);
	}

	/**
	 * Same as {@code transaction_search} except that it only performs single
	 * hop search and do it in an unsafe manner.
	 *
	 * @param originCity
	 * @param destinationCity
	 * @param directFlight
	 * @param dayOfMonth
	 * @param numberOfItineraries
	 *
	 * @return The search results. Note that this implementation *does not
	 *         conform* to the format required by {@code transaction_search}.
	 */
	private String transaction_search_unsafe(String originCity, String destinationCity, boolean directFlight,
			int dayOfMonth, int numberOfItineraries) {
		StringBuffer sb = new StringBuffer();

		try {
			// one hop itineraries
			String unsafeSearchSQL = "SELECT TOP (" + numberOfItineraries
					+ ") day_of_month,carrier_id,flight_num,origin_city,dest_city,actual_time,capacity,price "
					+ "FROM Flights " + "WHERE origin_city = \'" + originCity + "\' AND dest_city = \'"
					+ destinationCity + "\' AND day_of_month =  " + dayOfMonth + " " + "ORDER BY actual_time ASC";

			Statement searchStatement = conn.createStatement();
			ResultSet oneHopResults = searchStatement.executeQuery(unsafeSearchSQL);

			while (oneHopResults.next()) {
				int result_dayOfMonth = oneHopResults.getInt("day_of_month");
				String result_carrierId = oneHopResults.getString("carrier_id");
				String result_flightNum = oneHopResults.getString("flight_num");
				String result_originCity = oneHopResults.getString("origin_city");
				String result_destCity = oneHopResults.getString("dest_city");
				int result_time = oneHopResults.getInt("actual_time");
				int result_capacity = oneHopResults.getInt("capacity");
				int result_price = oneHopResults.getInt("price");

				sb.append("Day: ").append(result_dayOfMonth).append(" Carrier: ").append(result_carrierId)
						.append(" Number: ").append(result_flightNum).append(" Origin: ").append(result_originCity)
						.append(" Destination: ").append(result_destCity).append(" Duration: ").append(result_time)
						.append(" Capacity: ").append(result_capacity).append(" Price: ").append(result_price)
						.append('\n');
			}
			oneHopResults.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	/**
	 * Shows an example of using PreparedStatements after setting arguments. You
	 * don't need to use this method if you don't want to.
	 */
	private int checkFlightCapacity(int fid) throws SQLException {
		checkFlightCapacityStatement.clearParameters();
		checkFlightCapacityStatement.setInt(1, fid);
		ResultSet results = checkFlightCapacityStatement.executeQuery();
		results.next();
		int capacity = results.getInt("capacity");
		results.close();

		return capacity;
	}

	private ResultSet checkDirectFlightItinerary(String originCity, String destCity, int dayOfMonth,
			int numberOfItineraries) throws SQLException {
		checkDirectFlightItineraryStatement.clearParameters();
		checkDirectFlightItineraryStatement.setInt(1, numberOfItineraries);
		checkDirectFlightItineraryStatement.setNString(2, originCity);
		checkDirectFlightItineraryStatement.setNString(3, destCity);
		checkDirectFlightItineraryStatement.setInt(4, dayOfMonth);
		ResultSet results = checkDirectFlightItineraryStatement.executeQuery();
		return results;
	}

	private ResultSet checkIndirectFlightItinerary(String originCity, String destCity, int dayOfMonth,
			int numberOfItineraries) throws SQLException {
		checkIndirectFlightItineraryStatement.clearParameters();
		checkIndirectFlightItineraryStatement.setInt(1, numberOfItineraries);
		checkIndirectFlightItineraryStatement.setNString(2, originCity);
		checkIndirectFlightItineraryStatement.setNString(3, destCity);
		checkIndirectFlightItineraryStatement.setInt(4, dayOfMonth);
		ResultSet results = checkIndirectFlightItineraryStatement.executeQuery();
		return results;
	}
}
