package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import JavaMail.JavaMailUtil;
import logic.Instructor;
import logic.Order;
import logic.Subscriber;
import logic.Traveler;
import logic.Worker;

/**
 * @author Group 17
 * 
 * The class connects to the DB (MySQL) and uses the data
 *
 */
public class MysqlConnection
{
	private Connection conn;
	private static MysqlConnection instance = new MysqlConnection();
	final double PRICE = 50;

	/**
	 * the method calls the function connectToDB
	 */
	private MysqlConnection()
	{
		this.connectToDB();
	}

	/**
	 * the method returns the instance of the MySql connection
	 * @return instance of the MySql connection
	 */
	public static MysqlConnection getInstance()
	{
		return instance;
	}

	/**
	 * creates a connection to the DB of MySQL
	 */
	private void connectToDB()
	{
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			System.out.println("Driver definition succeed");
		} catch (Exception ex)
		{
			/* handle the error */
			System.out.println("Driver definition failed");
		}
		try
		{
			this.conn = DriverManager.getConnection("jdbc:mysql://localhost/gonature_db?serverTimezone=IST", "root",
					"Aa123456");
			System.out.println("SQL connection succeed");
		} catch (SQLException ex)
		{/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	/**
	 * the method search at the DB a worker with user name and password
	 * @param workerDetails the user name and the password of the worker
	 * @return the worker, if exists. else, null
	 */
	public Worker getWorker(String[] workerDetails)
	{
		Statement stmt;
		String userName = workerDetails[0], password = workerDetails[1];
		Worker w = new Worker();
		try
		{
			stmt = conn.createStatement(); // Statement for the correct worker
			w.setUserName(userName);
			String query = String.format("SELECT * FROM worker WHERE user_name=\"%s\" AND w_password=\"%s\"", userName,
					password);
			ResultSet rs = stmt.executeQuery(query);

			if (!rs.first()) // if we have no matching worker
				w.setUserName("Error");
			else
			{
				w.setfName(rs.getString(1));
				w.setlName(rs.getString(2));
				w.setEmail(rs.getString(3));
				w.setWorkerNumber(rs.getString(4));
				w.setOrganization(rs.getString(5));
				w.setUserName(rs.getString(6));
				w.setPassword(rs.getString(7));
				w.setRole(rs.getString(8));
			}
			rs.close();
		} catch (Exception e)
		{
			w.setUserName("Error");
			e.printStackTrace();
		}
		return w;
	}

	/**
	 * the method search at the DB a subscriber by subscriber number
	 * @param subNum the subscriber number
	 * @return the subscriber, if exists. else, null
	 */
	public Subscriber getTravelerBySubNum(String subNum)
	{
		Statement statement;
		Subscriber subscriber = new Subscriber();
		try
		{
			statement = conn.createStatement(); // Statement for the correct worker
			String query = String.format(
					"SELECT * FROM subscribers WHERE subscriber_number=\"%s\" OR id_number=\"%s\";", subNum, subNum);
			ResultSet rs = statement.executeQuery(query);
			if (!rs.first()) // if we have no matching worker
				return null;
			else
			{
				subscriber.setfName(rs.getString(1));
				subscriber.setlName(rs.getString(2));
				subscriber.setIdNumber(rs.getString(3));
				subscriber.setEmail(rs.getString(4));
				subscriber.setPhone(rs.getString(5));
				subscriber.setVisitorsNumber(Integer.parseInt(rs.getString(6)));
				subscriber.setSubscriberNumber(rs.getString(7));
				subscriber.setType("subscriber");
			}
			rs.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return subscriber;
	}

	/**
	 * the method return the setting of a specific park
	 * @param org organization
	 * @return settings of the park
	 */
	public String getParkSettings(String org)
	{
		StringBuilder str = new StringBuilder();
		Statement stmt;
		try
		{
			stmt = conn.createStatement();
			ResultSet rs = stmt
					.executeQuery(String.format("SELECT * FROM park_settings WHERE park_name = \"%s\";", org));
			// Print out the values
			while (rs.next())
			{
				str.append(rs.getString(1) + " ");
				str.append(rs.getString(2) + " ");
				str.append(rs.getString(3) + " ");
				str.append(rs.getString(4));
			}
			rs.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return str.toString();
	}

	/**
	 * the method return the setting status of a specific park
	 * @param org organization
	 * @return table of the setting status as String
	 */
	public String getSettingsStatus(String org)
	{
		StringBuilder str = new StringBuilder();
		Statement stmt;
		try
		{
			stmt = conn.createStatement();
			ResultSet rs = stmt
					.executeQuery(String.format("SELECT * FROM settings_status WHERE park_name=\"%s\";", org));
			while (rs.next())
			{
				// Print out the values
				str.append(rs.getString(1) + "/");
				str.append(rs.getString(2) + "/");
				str.append(rs.getString(3) + "/");
				str.append(rs.getString(4) + "/");
				str.append(rs.getString(5) + "/");
				str.append(rs.getString(6) + "/");
			}
			rs.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return str.toString();
	}

	/**
	 * the method adds to confirmation only if different value
	 * @param s array of all settings that we want to change
	 * @return true if change succeeded. else false
	 */
	public boolean checkIfSettingChanged(String[] s)
	{
		String settings = getParkSettings(s[24]);
		String[] str = new String[4];
		str = settings.split(" ");
		if (!str[0].equals(s[2]))
			if (!updateSettingsStatus(s, 0, s[24]) || !addToConfirmation(s, 0, s[24]))
				return false;
		if (!str[1].equals(s[8]))
			if (!updateSettingsStatus(s, 6, s[24]) || !addToConfirmation(s, 6, s[24]))
				return false;
		if (!str[2].equals(s[14]))
			if (!updateSettingsStatus(s, 12, s[24]) || !addToConfirmation(s, 12, s[24]))
				return false;
		if (!str[3].equals(s[20]))
			if (!updateSettingsStatus(s, 18, s[24]) || !addToConfirmation(s, 18, s[24]))
				return false;
		return true;
	}

	/**
	 * the method updates table setting_status
	 * @param s array of all settings that we want to change
	 * @param idx the first location in the array
	 * @param org organization
	 * @return true if change succeeded. else false
	 */
	public boolean updateSettingsStatus(String[] s, int idx, String org)
	{
		try
		{
			// create the java mysql update preparedstatement
			String query = "update settings_status set setting_before = ?, setting_after= ?, change_time= ?, setting_status= ?, response_time= ? where setting_name = ? AND park_name = ?";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, s[idx + 1]);
			preparedStmt.setString(2, s[idx + 2]);
			preparedStmt.setString(3, s[idx + 3]);
			preparedStmt.setString(4, s[idx + 4]);
			preparedStmt.setString(5, s[idx + 5]);
			preparedStmt.setString(6, s[idx]);
			preparedStmt.setString(7, org);
			preparedStmt.executeUpdate();
		} catch (Exception e)
		{
			System.out.println("Error occurred");
			return false;
		}
		return true;
	}

	/**
	 * the method adds a setting to confirmation table
	 * @param s array of all settings that we want to change
	 * @param idx the first location in the array
	 * @param org organization
	 * @return true if change succeeded. else false
	 */
	public boolean addToConfirmation(String[] s, int idx, String org)
	{
		try
		{
			// create the java mysql update preparedstatement
			String query = "DELETE FROM confirmation where park_name = ? AND setting_name = ?";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, org);
			preparedStmt.setString(2, s[idx]);
			preparedStmt.executeUpdate();
		} catch (Exception e)
		{
			System.out.println("Error occurred");
			return false;
		}
		try
		{
			// create the java mysql update preparedstatement
			String query = "INSERT INTO confirmation VALUES(?, ?, ?, ?, ?, ?)";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, org);
			preparedStmt.setString(2, s[idx]);
			preparedStmt.setString(3, s[idx + 1]);
			preparedStmt.setString(4, s[idx + 2]);
			preparedStmt.setString(5, s[idx + 3]);
			preparedStmt.setBoolean(6, false);
			preparedStmt.executeUpdate();
		} catch (Exception e)
		{
			System.out.println("Error occurred");
			return false;
		}
		return true;
	}

	/**
	 * the method return all the changes requests
	 * @return all the changes requests
	 */
	public String getConfirmationStatus()
	{
		StringBuilder str = new StringBuilder();
		Statement stmt;
		try
		{
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM confirmation;");
			while (rs.next())
			{
				// Print out the values
				str.append(rs.getString(1) + "/");
				str.append(rs.getString(2) + "/");
				str.append(rs.getString(3) + "/");
				str.append(rs.getString(4) + "/");
				str.append(rs.getString(5) + "/");
				str.append(rs.getString(6) + "/");
			}
			rs.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return str.toString();
	}

	/**
	 * the method removes the requests of a park manager
	 * @param s array of the requested setting
	 * @return true if change succeeded. else false
	 */
	public boolean deleteFromTable(String[] s)
	{
		for (int i = 0; i < s.length; i += 3)
			try
			{
				// create the java mysql update preparedstatement
				String query = "DELETE FROM confirmation where park_name = ? AND setting_name = ? AND setting_after = ?";
				PreparedStatement preparedStmt = conn.prepareStatement(query);
				preparedStmt.setString(1, s[i]);
				preparedStmt.setString(2, s[i + 1]);
				preparedStmt.setString(3, s[i + 2]);
				preparedStmt.executeUpdate();
			} catch (Exception e)
			{
				System.out.println("Error occurred");
				return false;
			}
		return true;
	}

	/**
	 * the method updates setting status to approved (after saving by department manager)
	 * @param str setting that have been changed
	 * @param org organization
	 */
	private void settingApproved(String str, String org) 
	{
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yy, HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		try
		{
			String query = "update settings_status set setting_status = \"Approved\", response_time = ? where setting_name = ? AND park_name = ?";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, dtf.format(now));
			preparedStmt.setString(2, str);
			preparedStmt.setString(3, org);
			preparedStmt.executeUpdate();
		} catch (Exception e)
		{
			System.out.println("Error occurred");
		}
	}

	/**
	 * the method updates setting status to denied (after saving by department manager)
	 * @param str setting that have been changed
	 * @return true if change succeeded. else false
	 */
	public boolean denyApproval(String[] str)
	{
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yy, HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		for (int i = 0; i < str.length; i += 3)
			try
			{
				String query = "update settings_status set setting_status = \"Denied\", response_time = ? where setting_name = ? AND setting_after = ? AND park_name = ?";
				PreparedStatement preparedStmt = conn.prepareStatement(query);
				preparedStmt.setString(1, dtf.format(now));
				preparedStmt.setString(2, str[i + 1]);
				preparedStmt.setString(3, str[i + 2]);
				preparedStmt.setString(4, str[i]); // park name
				preparedStmt.executeUpdate();
			} catch (Exception e)
			{
				System.out.println("Error occurred");
				return false;
			}
		if (!deleteFromTable(str))
			return false;
		return true;
	}

	/**
	 * the method registrates a subscriber at the DB
	 * @param s subscriber data
	 * @return "true" + the subscriber number, or false
	 */
	public String SubscriberRegistration(String[] s)
	{
		String str = CheckIfUserExist(s, "subscribers");
		if (!str.equals("OK"))
			return str;
		int subNum = getSubscriberNum();
		if (subNum == 0)
			return "false";
		try
		{
			// create the java mysql update preparedstatement
			String query = "INSERT INTO subscribers VALUES(?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, s[0]);
			preparedStmt.setString(2, s[1]);
			preparedStmt.setInt(3, Integer.parseInt(s[2]));
			preparedStmt.setString(4, s[4]);
			preparedStmt.setString(5, s[3]);
			preparedStmt.setInt(6, Integer.parseInt(s[5]));
			preparedStmt.setInt(7, subNum);
			preparedStmt.executeUpdate();
			query = "INSERT INTO travelers_with_order VALUES(?, ?, ?, ?, ?)";
			preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, s[0]);
			preparedStmt.setString(2, s[1]);
			preparedStmt.setString(3, s[4]);
			preparedStmt.setString(4, s[2]);
			preparedStmt.setString(5, s[3]);
			preparedStmt.executeUpdate();
		} catch (Exception e)
		{
			System.out.println("Error occurred");
			return "false";
		}
		return "true" + " " + subNum;
	}

	/**
	 * the method checks if the user already exist
	 * @param s data of the user
	 * @param tableName "subscriber" if the user is subscriber, "instructor"  if the user is instructor
	 * @return the matching field error, or "OK"
	 */
	@SuppressWarnings("resource")
	public String CheckIfUserExist(String[] s, String tableName)
	{
		Statement stmt;
		String email = s[4];
		try
		{
			stmt = conn.createStatement(); // Statement for the correct visitor
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName + " WHERE id_number=" + s[2] + ";");
			if (rs.first()) // if we have a match with the id
				return "ID";
			rs.close();
			if (tableName.equals("subscribers"))
				rs = stmt.executeQuery(String.format("SELECT * FROM subscribers WHERE email=\"%s\";", email));
			else
				rs = stmt.executeQuery(String.format("SELECT * FROM instructors WHERE email=\"%s\";", email));
			if (rs.first())
				return "Email";
			rs.close();
			rs = stmt.executeQuery("SELECT * FROM " + tableName + " WHERE phone_number=" + s[3] + ";");
			if (rs.first())
				return "Phone";
			rs.close();

		} catch (Exception e)
		{
			e.printStackTrace();
			return "false";
		}
		return "OK";
	}

	/**
	 * the method generates subscriber number and returns in
	 * @return subscriber number
	 */
	public int getSubscriberNum()
	{
		int num = 0;
		Statement stmt;
		try
		{
			stmt = conn.createStatement(); // Statement for the correct visitor
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS rowcount FROM subscribers");
			rs.next();
			num = rs.getInt("rowcount");
			rs.close();
		} catch (Exception e)
		{
			e.printStackTrace();
			num = -1;
		}
		return ++num;
	}

	/**
	 * the method registrates a instructor at the DB
	 * @param s instructor data
	 * @return the matching field error, or "true"
	 */
	public String InstructorRegistration(String[] s)
	{
		String str = CheckIfUserExist(s, "instructors");
		if (!str.equals("OK"))
			return str;

		try
		{
			// create the java mysql update preparedstatement
			String query = "INSERT INTO instructors VALUES(?, ?, ?, ?, ?)";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, s[0]);
			preparedStmt.setString(2, s[1]);
			preparedStmt.setString(3, s[2]);
			preparedStmt.setString(4, s[4]);
			preparedStmt.setString(5, s[3]);
			preparedStmt.executeUpdate();
			query = "INSERT INTO travelers_with_order VALUES(?, ?, ?, ?, ?)";
			preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, s[0]);
			preparedStmt.setString(2, s[1]);
			preparedStmt.setString(3, s[4]);
			preparedStmt.setString(4, s[2]);
			preparedStmt.setString(5, s[3]);
			preparedStmt.executeUpdate();
		} catch (Exception e)
		{
			System.out.println("Error occurred");
			return "false";
		}
		return "true";
	}

	/**
	 * the method inserts credit card data of a subscriber
	 * @param s credit card data
	 * @return true is succeeded, else false
	 */
	public boolean insertPaymentDetails(String[] s)
	{
		try
		{
			// create the java mysql update preparedstatement
			String query = "INSERT INTO payment VALUES(?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, s[0]);
			preparedStmt.setString(2, s[1]);
			preparedStmt.setString(3, s[2]);
			preparedStmt.setString(4, s[3]);
			preparedStmt.setString(5, s[4]);
			preparedStmt.setString(6, s[5]);
			preparedStmt.setInt(7, Integer.parseInt(s[6]));
			preparedStmt.executeUpdate();
		} catch (Exception e)
		{
			System.out.println("Error occurred");
			return false;
		}
		return true;
	}

	/**
	 * the method updates the entrance time and exit time of a visitor
	 * @param id order id
	 * @param action "Enter" or "Exit"
	 * @return true is succeeded, else false
	 */
	private boolean updateVisitingTime(String id, String action) // Enter: id = order_id, Exit = id = identification
	{
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
		LocalDateTime now = LocalDateTime.now();
		Statement stmt;
		ResultSet rs = null;
		PreparedStatement preparedStmt;
		if (action.equals("Enter"))
		{
			try
			{
				stmt = conn.createStatement(); // Statement for the correct visitor
				rs = stmt.executeQuery(String.format("SELECT * FROM orders WHERE order_id = \"%s\";", id));
				while (rs.next())
				{
					String query = "INSERT INTO visiting_time VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
					preparedStmt = conn.prepareStatement(query);
					preparedStmt.setString(1, rs.getString(2));// park_name
					preparedStmt.setString(2, id);// order_id
					preparedStmt.setString(3, rs.getString(3));// order_date
					preparedStmt.setString(4, rs.getString(4));// time_slot
					preparedStmt.setString(5, dtf.format(now));// enter time
					preparedStmt.setString(6, null);// time_slot
					preparedStmt.setInt(7, rs.getInt(5)); // visitors_number
					preparedStmt.setString(8, rs.getString(7)); // visitor_type
					preparedStmt.executeUpdate();
				}
				rs.close();
			} catch (Exception e)
			{
				e.printStackTrace();
				return false;
			}
		} else
		{
			try
			{
				stmt = conn.createStatement(); // Statement for the correct visitor
				rs = stmt.executeQuery(String.format(
						"SELECT order_id FROM orders WHERE (identification=\"%s\" OR sub_num=\"%s\") AND order_date=CURDATE();",
						id, id));
				while (rs.next())
				{
					String query = "update visiting_time set exit_time = ? where order_id = ?";
					preparedStmt = conn.prepareStatement(query);
					preparedStmt.setString(1, dtf.format(now));// exit_time
					preparedStmt.setString(2, rs.getString(1));// order_id
					preparedStmt.executeUpdate();
				}
				rs.close();
			} catch (Exception e)
			{
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	/**
	 * the method searches order and gets number of visitors and price
	 * @param s order id
	 * @return if order valid: number of visitors and price, else: "USED" or "IDNOTFOUND"
	 */
	public String getNumOfVisitorsInOrder(String s)
	{
		String[] str = s.split(" ");
		if(str[1].equals("Hai"))
			str[1] = "Hai Park";
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate now = LocalDate.now();
		String result;
		Statement stmt;
		try
		{
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM orders WHERE order_id=\"%s\" AND order_date=\"%s\" AND park_name=\"%s\";", str[0], dtf.format(now), str[1]));
			if (!rs.first())
				return "IDNOTFOUND";
			else if (rs.getString(8).equals("used"))
				return "USED";
			else if(rs.getString(8).equals("canceled"))
				return "CANCELED";
			result = rs.getInt(5) + " " + rs.getDouble(9);
			rs.close();

		} catch (Exception e)
		{
			e.printStackTrace();
			return "false";
		}
		return result;
	}

	/**
	 * the method get the number of visitors of specific park
	 * @param org organization
	 * @return number of visitors + number of spares
	 */
	public String getCurrentNumberOfVisitors(String org) // number of visitors: data for entrance worker
	{
		int sumVisitors = 0, sumSpares = 0, baniasVisitors = 0, hatzbaniVisitors = 0, haiParkVisitors = 0,
				baniasSpares = 0, hatzbaniSpares = 0, haiParkSpares = 0;
		Statement stmt;
		if (org.equals("TOTAL"))
		{
			try
			{
				stmt = conn.createStatement(); // Statement for the correct visitor
				ResultSet rs = stmt.executeQuery(String.format(
						"SELECT SUM(current_visitors) AS sumVisitors FROM card_reader WHERE park_name = \"%s\"",
						"Banias"));
				rs.next();
				baniasVisitors = rs.getInt("sumVisitors");
				rs.close();
				rs = stmt.executeQuery(String.format(
						"SELECT SUM(current_visitors) AS sumVisitors FROM card_reader WHERE spares=1 AND park_name = \"%s\"",
						"Banias"));
				rs.next();
				baniasSpares = rs.getInt("sumVisitors");
				rs.close();
				rs = stmt.executeQuery(String.format(
						"SELECT SUM(current_visitors) AS sumVisitors FROM card_reader WHERE park_name = \"%s\"",
						"Hatzbani"));
				rs.next();
				hatzbaniVisitors = rs.getInt("sumVisitors");
				rs.close();
				rs = stmt.executeQuery(String.format(
						"SELECT SUM(current_visitors) AS sumVisitors FROM card_reader WHERE spares=1 AND park_name = \"%s\"",
						"Hatzbani"));
				rs.next();
				hatzbaniSpares = rs.getInt("sumVisitors");
				rs.close();
				rs = stmt.executeQuery(String.format(
						"SELECT SUM(current_visitors) AS sumVisitors FROM card_reader WHERE park_name = \"%s\"",
						"Hai Park"));
				rs.next();
				haiParkVisitors = rs.getInt("sumVisitors");
				rs.close();
				rs = stmt.executeQuery(String.format(
						"SELECT SUM(current_visitors) AS sumVisitors FROM card_reader WHERE spares=1 AND park_name = \"%s\"",
						"Hai Park"));
				rs.next();
				haiParkSpares = rs.getInt("sumVisitors");
				rs.close();
			} catch (Exception e)
			{
				e.printStackTrace();
				return "false";
			}
			return baniasVisitors + " " + baniasSpares + " " + hatzbaniVisitors + " " + hatzbaniSpares + " "
					+ haiParkVisitors + " " + haiParkSpares;
		} else
		{
			try
			{
				stmt = conn.createStatement(); // Statement for the correct visitor
				ResultSet rs = stmt.executeQuery(String.format(
						"SELECT SUM(current_visitors) AS sumVisitors FROM card_reader WHERE park_name = \"%s\"", org));
				rs.next();
				sumVisitors = rs.getInt("sumVisitors");
				rs.close();
				rs = stmt.executeQuery(String.format(
						"SELECT SUM(current_visitors) AS sumVisitors FROM card_reader WHERE spares=1 AND park_name = \"%s\"",
						org));
				rs.next();
				sumSpares = rs.getInt("sumVisitors");
				rs.close();
			} catch (Exception e)
			{
				e.printStackTrace();
				return "false";
			}
			return sumVisitors + " " + sumSpares;
		}
	}

	/**
	 * the method checks if order was confirmed, or cancels it
	 * @param order the order that we want to check
	 * @param content content of the mail of the cancellation mail
	 */
	private void checkOrderConfirmed(Order order, String content)
	{
		Statement stmt;
		try
		{
			stmt = conn.createStatement(); // Statement for the correct worker
			String query = String.format(
					"SELECT order_status, order_id order_id FROM orders WHERE identification=\"%s\" AND park_name=\"%s\" AND order_date=\"%s\" AND time_slot=\"%s\";",
					order.getIdentification(), order.getParkName(), order.getDate().toString(), order.getTimeSlot());
			ResultSet rs = stmt.executeQuery(query);
			if (rs.first())
				if (rs.getString(1).equals("notYet"))
				{
					cancelOrder(rs.getString(2));
					rs.close();
					sendMail(order.getEmail(), "Order was canceled", content);
				}
		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * the method send an email to a client
	 * @param adress the email address of the client
	 * @param title the title of the email
	 * @param content the content of the email
	 */
	private void sendMail(String adress, String title, String content)
	{
		try
		{
			JavaMailUtil.sendMail(adress, title, content);
			System.out.println("An email was sent to " + adress + " with the content of: " + title);
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("something went wrong with the mail...");
		}
	}

	/**
	 * the method return a string of the type of the traveler (regular/subscriber/instructor) 
	 * @param identification the id of the traveler
	 * @return a string of the type of the traveler
	 */
	public String travelerType(String identification)
	{// identification = id
		if (isASubscriber(identification))
			return "subscriber";
		if (isAnInstructor(identification))
			return "instructor";
		return "regular";
	}

	/**
	 * the method returns true if the traveler is a subscriber
	 * @param identification the id of the traveler
	 * @return true if the traveler is a subscriber, else false
	 */
	private boolean isASubscriber(String identification)
	{
		Statement statement;
		try
		{
			statement = conn.createStatement(); // Statement for the correct worker
			String query = String.format(
					"SELECT * FROM subscribers WHERE subscriber_number=\"%s\" OR id_number=\"%s\";", identification,
					identification);
			ResultSet rs = statement.executeQuery(query);
			if (rs.first())
			{// if we have such subscriber
				rs.close();
				return true;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * the method returns true if the traveler is an instructor
	 * @param identification the id of the traveler
	 * @return true if the traveler is an instructor, else false
	 */
	private boolean isAnInstructor(String identification)
	{
		Statement statement;
		try
		{
			statement = conn.createStatement(); // Statement for the correct worker
			String query = String.format("SELECT * FROM instructors WHERE id_number=\"%s\";", identification);
			ResultSet rs = statement.executeQuery(query);
			if (rs.first())
			{// if we have such instructor
				rs.close();
				return true;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * the method returns the instructor, if he exists on DB
	 * @param id the instructor id
	 * @return the instructor if exists, else null
	 */
	public Instructor getInstructor(String id)
	{
		Statement statement;
		Instructor instructor = new Instructor();
		try
		{
			statement = conn.createStatement(); // Statement for the correct worker
			String query = String.format("SELECT * FROM instructors WHERE id_number=\"%s\"", id);
			ResultSet rs = statement.executeQuery(query);
			if (!rs.first()) // if we have no matching worker
				return null;
			else
			{
				instructor.setfName(rs.getString(1));
				instructor.setlName(rs.getString(2));
				instructor.setIdNumber(rs.getString(3));
				instructor.setEmail(rs.getString(4));
				instructor.setPhone(rs.getString(5));
				instructor.setType("instructor");
			}
			rs.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return instructor;
	}

	/**
	 * the method edits the new number of visitors in order
	 * @param number the new number of visitors in order
	 * @return the new price of the order
	 */
	public String editVisitorsInOrder(String number) // Edit
	{
		String[] s = number.split(" ");
		Statement stmt;
		ResultSet rs = null;
		Order o = new Order();
		String orderType = "";
		double price;
		System.out.println(s[0] + " " + s[1]);
		try
		{
			stmt = conn.createStatement();
			String query = "update orders set visitors_number = " + s[1] + " where order_id = ?";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, s[0]);
			preparedStmt.executeUpdate(); // Change number of visitors
			rs = stmt.executeQuery(
					String.format("SELECT * FROM gonature_db.orders WHERE order_id = \"%d\";", Integer.parseInt(s[0])));
			while (rs.next()) // Create an order to calculate its price.
			{
				o.setParkName(rs.getString(2)); // park name
				o.setNumberOfVisitors(rs.getInt(5)); // number of visitors
				o.setType(rs.getString(7)); // type
				o.setPrice(rs.getDouble(9)); // price
				orderType = rs.getString(10);
			}
			Traveler t = new Traveler();
			t.setType(o.getType());
			o.setTraveler(t);
			if (orderType.equals("occasional"))
				price = calculateOcassionalPrice(o);
			else
				price = calculatePreorderedPrice(o);
			query = "update orders set price = " + price + " where order_id = ?"; // update price in DB
			preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, s[0]);
			preparedStmt.executeUpdate(); // Change number of visitors
		} catch (Exception e)
		{
			e.printStackTrace();
			return "false";
		}
		return price + "";
	}

	/**
	 * the method gets data of a report and creates a report
	 * @param report array of strings: report name, month and year
	 * @return the report data
	 */
	public String[] departmentProduceAreport(String[] report)
	{
		if (report[0].equals("Visiting report"))
			return visitingReport(report[1], report[2]);
		return cancelingReport(report[1], report[2]);
	}

	/**
	 * the method creates visiting report (department manager)
	 * @param month month
	 * @param year year
	 * @return the report data
	 */
	private String[] visitingReport(String month, String year)
	{
		ZoneId z = ZoneId.of("Israel");
		LocalDate today = LocalDate.now(z);
		String m = String.valueOf(Month.valueOf(month.toUpperCase()).getValue());
		StringBuilder str = new StringBuilder();
		Statement stmt;
		try
		{
			stmt = conn.createStatement();
			ResultSet rsReg = null, rsSub = null, rsGrp = null, rsAvg = null;
			str.append("/gui/VisitorsReport.fxml" + "-" + month + "-" + year + "-");
			/* If the month is current month and year is current year */
			if (today.getMonth().toString().equals(month.toUpperCase()) && today.getYear() == Integer.parseInt(year))
			{
				rsReg = stmt.executeQuery(String.format(
						"SELECT visitor_type, HOUR(enter_time), COUNT(*), SUM(visitors_number) FROM gonature_db.visiting_time WHERE visitor_type = \"regular\" AND YEAR(order_date) = YEAR(CURRENT_DATE()) AND MONTH(order_date) = MONTH(CURRENT_DATE()) AND order_date < CURDATE() GROUP BY HOUR(enter_time) ORDER BY enter_time;"));
				while (rsReg.next())
				{
					str.append(rsReg.getString(2) + "-");// enter time
					str.append(rsReg.getString(1) + "-");// visitor_type
					str.append(rsReg.getString(4) + "-");// number of visitors
				}
				rsReg.close();
				rsSub = stmt.executeQuery(String.format(
						"SELECT visitor_type, HOUR(enter_time), COUNT(*), SUM(visitors_number) FROM gonature_db.visiting_time WHERE visitor_type = \"Subscriber\" AND YEAR(order_date) = YEAR(CURRENT_DATE()) AND MONTH(order_date) = MONTH(CURRENT_DATE()) AND order_date < CURDATE() GROUP BY HOUR(enter_time) ORDER BY enter_time;"));
				while (rsSub.next())
				{
					str.append(rsSub.getString(2) + "-");// enter time
					str.append(rsSub.getString(1) + "-");// visitor_type
					str.append(rsSub.getString(4) + "-");// number of visitors
				}
				rsSub.close();
				rsGrp = stmt.executeQuery(String.format(
						"SELECT visitor_type, HOUR(enter_time), COUNT(*), SUM(visitors_number) FROM gonature_db.visiting_time WHERE visitor_type = \"Instructor\" AND YEAR(order_date) = YEAR(CURRENT_DATE()) AND MONTH(order_date) = MONTH(CURRENT_DATE()) AND order_date < CURDATE() GROUP BY HOUR(enter_time) ORDER BY enter_time;"));
				while (rsGrp.next())
				{
					str.append(rsGrp.getString(2) + "-");// enter time
					str.append(rsGrp.getString(1) + "-");// visitor_type
					str.append(rsGrp.getString(4) + "-");// number of visitors
				}
				rsGrp.close();
				str.append("AVG" + "-");
				rsAvg = stmt.executeQuery(String.format(
						"SELECT *,SUBTIME(exit_time, enter_time) FROM gonature_db.visiting_time WHERE YEAR(order_date) = YEAR(CURRENT_DATE()) AND MONTH(order_date) = MONTH(CURRENT_DATE()) AND order_date < CURDATE();"));
				while (rsAvg.next())
				{
					str.append(rsAvg.getString(7) + "-");// number of visitors
					str.append(rsAvg.getString(8) + "-");// visitor type
					str.append(rsAvg.getString(9) + "-");// visit time
				}
				rsAvg.close();
			}
			/* Any other month and year that are not current month and current year */
			else
			{
				rsReg = stmt.executeQuery(String.format(
						"SELECT visitor_type, HOUR(enter_time), COUNT(*), SUM(visitors_number) FROM gonature_db.visiting_time WHERE visitor_type = \"regular\" AND YEAR(order_date) = \"%s\" AND MONTH(order_date) = \"%s\" GROUP BY HOUR(enter_time) ORDER BY enter_time;",
						year, m));
				while (rsReg.next())
				{
					str.append(rsReg.getString(2) + "-");// enter time
					str.append(rsReg.getString(1) + "-");// visitor_type
					str.append(rsReg.getString(4) + "-");// number of visitors
				}
				rsReg.close();
				rsSub = stmt.executeQuery(String.format(
						"SELECT visitor_type, HOUR(enter_time), COUNT(*), SUM(visitors_number) FROM gonature_db.visiting_time WHERE visitor_type = \"Subscriber\" AND YEAR(order_date) = \"%s\" AND MONTH(order_date) = \"%s\" GROUP BY HOUR(enter_time) ORDER BY enter_time;",
						year, m));
				while (rsSub.next())
				{
					str.append(rsSub.getString(2) + "-");// enter time
					str.append(rsSub.getString(1) + "-");// visitor_type
					str.append(rsSub.getString(4) + "-");// number of visitors
				}
				rsSub.close();
				rsGrp = stmt.executeQuery(String.format(
						"SELECT visitor_type, HOUR(enter_time), COUNT(*), SUM(visitors_number) FROM gonature_db.visiting_time WHERE visitor_type = \"Instructor\" AND YEAR(order_date) = \"%s\" AND MONTH(order_date) = \"%s\" GROUP BY HOUR(enter_time) ORDER BY enter_time;",
						year, m));
				while (rsGrp.next())
				{
					str.append(rsGrp.getString(2) + "-");// enter time
					str.append(rsGrp.getString(1) + "-");// visitor_type
					str.append(rsGrp.getString(4) + "-");// number of visitors
				}
				rsGrp.close();
				str.append("AVG" + "-");
				rsAvg = stmt.executeQuery(String.format(
						"SELECT *,SUBTIME(exit_time, enter_time) FROM gonature_db.visiting_time WHERE YEAR(order_date) = \"%s\" AND MONTH(order_date) = \"%s\";",
						year, m));
				while (rsAvg.next())
				{
					str.append(rsAvg.getString(7) + "-");// number of visitors
					str.append(rsAvg.getString(8) + "-");// visitor type
					str.append(rsAvg.getString(9) + "-");// visit time
				}
				rsAvg.close();
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		String[] s = str.toString().split("-");
		return s;
	}

	/**
	 * the method creates canceling report (department manager)
	 * @param month month
	 * @param year year
	 * @return the report data
	 */
	private String[] cancelingReport(String month, String year)
	{
		ZoneId z = ZoneId.of("Israel");
		LocalDate today = LocalDate.now(z);
		String m = String.valueOf(Month.valueOf(month.toUpperCase()).getValue());
		StringBuilder str = new StringBuilder();
		String[] s = new String[5];
		s[4] = "for month " + month + ", " + year;
		Statement stmt;
		try
		{
			stmt = conn.createStatement();
			ResultSet rs = null, rs2 = null, rs3 = null;
			/* If the month is current month and year is current year */
			if (today.getMonth().toString().equals(month.toUpperCase()) && today.getYear() == Integer.parseInt(year))
			{
				/* Query for unused orders group by park_name for current month */
				rs = stmt.executeQuery(
						"SELECT park_name, count(park_name) FROM gonature_db.orders WHERE (order_status = \"unused\" AND YEAR(order_date) = YEAR(CURRENT_DATE()) AND MONTH(order_date) = MONTH(CURRENT_DATE()) AND order_date < CURDATE()) GROUP BY park_name;");
				while (rs.next())
				{
					str.append(rs.getString(1) + "/");// park name
					str.append(rs.getString(2) + "/");// count(unused)
				}
				rs.close();
				s[1] = str.toString();
				str.delete(0, str.length());

				rs2 = stmt.executeQuery(
						"SELECT park_name, count(park_name) FROM gonature_db.orders WHERE (order_status=\"canceled\" AND YEAR(order_date) = YEAR(CURRENT_DATE()) AND MONTH(order_date) = MONTH(CURRENT_DATE()) AND order_date < CURDATE()) GROUP BY park_name;");
				while (rs2.next())
				{
					str.append(rs2.getString(1) + "/");// park name
					str.append(rs2.getString(2) + "/");// count(canceled)
				}
				rs2.close();
				s[2] = str.toString();
				str.delete(0, str.length());

				rs3 = stmt.executeQuery(
						"SELECT park_name, count(park_name) FROM gonature_db.orders WHERE (YEAR(order_date) = YEAR(CURRENT_DATE()) AND MONTH(order_date) = MONTH(CURRENT_DATE()) AND order_date < CURDATE()) GROUP BY park_name;");
				while (rs3.next())
				{
					str.append(rs3.getString(1) + "/");// park name
					str.append(rs3.getString(2) + "/");// total order for each park
				}
				rs3.close();
				s[3] = str.toString();
				str.delete(0, str.length());
				/* Any other month and year that are not current month and current year */
			} else
			{
				/* Query for unused orders group by park_name for specific month */
				rs = stmt.executeQuery(String.format(
						"SELECT park_name, count(park_name) FROM gonature_db.orders WHERE (order_status = \"unused\" AND MONTH(order_date) = \"%s\" AND YEAR(order_date) = \"%s\") GROUP BY park_name;",
						m, year));
				while (rs.next())
				{
					str.append(rs.getString(1) + "/");// park name
					str.append(rs.getString(2) + "/");// count(unused)
				}
				rs.close();
				s[1] = str.toString();
				str.delete(0, str.length());
				rs2 = stmt.executeQuery(String.format(
						"SELECT park_name, count(park_name) FROM gonature_db.orders WHERE (order_status=\"canceled\" AND MONTH(order_date) = \"%s\" AND YEAR(order_date) = \"%s\") GROUP BY park_name;",
						m, year));
				while (rs2.next())
				{
					str.append(rs2.getString(1) + "/");// park name
					str.append(rs2.getString(2) + "/");// count(canceled)
				}
				rs2.close();
				s[2] = str.toString();
				str.delete(0, str.length());

				rs3 = stmt.executeQuery(String.format(
						"SELECT park_name, count(park_name) FROM gonature_db.orders WHERE (MONTH(order_date) = \"%s\" AND YEAR(order_date) = \"%s\") GROUP BY park_name;",
						m, year));
				while (rs3.next())
				{
					str.append(rs3.getString(1) + "/");// park name
					str.append(rs3.getString(2) + "/");// total order for each park
				}
				rs3.close();
				s[3] = str.toString();
				str.delete(0, str.length());
			}

		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		s[0] = "/gui/cancelingReport.fxml";
		for (int i = 0; i < s.length; i++) {
			System.out.println(s[i]);
		}
		return s;
	}

	/**
	 * the method gets report name and date and returns the report of the date
	 * @param report report name and date
	 * @return the report data of the date
	 */
	public String[] parkManagerProduceAreport(String[] report)
	{
		if (report[0].equals("Total visitors"))
			return totalVisitorsReport(report[1], report[2], report[3]);
		else if (report[0].equals("Use report"))
			return useReport(report[1], report[2], report[3]);
		return incomeReport(report[1], report[2], report[3]);
	}

	/**
	 * the method creates visitors report for park manager
	 * @param month month
	 * @param year year
	 * @param org organization
	 * @return the data of the report
	 */
	private String[] totalVisitorsReport(String month, String year, String org)
	{
		ZoneId z = ZoneId.of("Israel");
		LocalDate today = LocalDate.now(z);
		String m = String.valueOf(Month.valueOf(month.toUpperCase()).getValue());
		StringBuilder str = new StringBuilder();
		Statement stmt;
		try
		{
			stmt = conn.createStatement();
			ResultSet rs = null;
			/* If the month is current month and year is current year */
			if (today.getMonth().toString().equals(month.toUpperCase()) && today.getYear() == Integer.parseInt(year))
				rs = stmt.executeQuery(String.format(
						"SELECT *,SUBTIME(exit_time, enter_time) FROM gonature_db.visiting_time WHERE park_name = \"%s\" AND YEAR(order_date) = YEAR(CURRENT_DATE()) AND MONTH(order_date) = MONTH(CURRENT_DATE()) AND order_date < CURDATE();",
						org));
			/* Any other month and year that are not current month and current year */
			else
			{
				rs = stmt.executeQuery(String.format(
						"SELECT *,SUBTIME(exit_time, enter_time) FROM gonature_db.visiting_time WHERE park_name = \"%s\" AND YEAR(order_date) = \"%s\" AND MONTH(order_date) = \"%s\";",
						org, year, m));
			}
			str.append("/gui/VisitsReport.fxml" + "-" + org + "-" + month + "-" + year + "-");
			while (rs.next())
			{
				str.append(rs.getString(7) + "-");// number of visitors
				str.append(rs.getString(8) + "-");// visitor type
				str.append(rs.getString(9) + "-");// visit time
			}
			rs.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		String[] s = str.toString().split("-");
		return s;
	}

	/**
	 * the method creates income report for park manager
	 * @param month month
	 * @param year year
	 * @param org organization
	 * @return the data of the report
	 */
	private String[] incomeReport(String month, String year, String org)
	{
		ZoneId z = ZoneId.of("Israel");
		LocalDate today = LocalDate.now(z);
		String m = String.valueOf(Month.valueOf(month.toUpperCase()).getValue());
		String[] str = new String[6];
		Statement stmt;
		try
		{
			stmt = conn.createStatement();
			ResultSet rs = null;
			/* If the month is current month and year is current year */
			if (today.getMonth().toString().equals(month.toUpperCase()) && today.getYear() == Integer.parseInt(year))
				rs = stmt.executeQuery(String.format(
						"SELECT park_name, sum(visitors_number), sum(price) FROM gonature_db.orders WHERE park_name=\"%s\" AND order_status = \"used\" AND YEAR(order_date) = YEAR(CURRENT_DATE()) AND MONTH(order_date) = MONTH(CURRENT_DATE()) AND order_date < CURDATE();",
						org));
			/* Any other month and year that are not current month and current year */
			else
			{
				rs = stmt.executeQuery(String.format(
						"SELECT park_name, sum(visitors_number), sum(price) FROM gonature_db.orders WHERE park_name=\"%s\" AND order_status = \"used\" AND MONTH(order_date) = \"%s\" AND YEAR(order_date) = \"%s\";",
						org, m, year));
			}
			while (rs.next())
			{
				str[2] = (rs.getString(2));// total number of visitors
				str[3] = (rs.getString(3));// total price of each day
			}
			rs.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		str[0] = "/gui/RevenueReport.fxml";
		str[1] = org;// park name
		str[4] = month;
		str[5] = year;
		return str;
	}

	/**
	 * the method passes the report from the park manager to the report table 
	 * @param reportDetails array of string of the report data
	 * @return true if succeeded, else false
	 */
	public boolean insertReport(String[] reportDetails)
	{
		PreparedStatement preparedStmt;
		Statement stmt;
		try
		{
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(String.format(
					"SELECT * FROM gonature_db.reports_table WHERE park_name = \"%s\" AND report_name = \"%s\" AND month = \"%s\" AND year = \"%s\";",
					reportDetails[0], reportDetails[1], reportDetails[2], reportDetails[3]));
			if (rs.first())
				return true;
			String query = "INSERT INTO reports_table VALUES(?, ?, ?, ?)";
			preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, reportDetails[0]);// park_name
			preparedStmt.setString(2, reportDetails[1]);// report name
			preparedStmt.setString(3, reportDetails[2]);// month
			preparedStmt.setString(4, reportDetails[3]);// year
			preparedStmt.executeUpdate();
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * the method loads all the reports to the report table
	 * @param s no need
	 * @return data of the report table
	 */
	public String getReports(String s)
	{
		StringBuilder str = new StringBuilder();
		Statement stmt;
		try
		{
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM reports_table;");
			while (rs.next())
			{
				// Print out the values
				str.append(rs.getString(1) + "/");
				str.append(rs.getString(2) + "/");
				str.append(rs.getString(3) + "/");
				str.append(rs.getString(4) + "/");
			}
			rs.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return str.toString();
	}

	/**
	 * the method calculates the price of the occasional order
	 * @param order the order that we want to calculate the price for
	 * @return the price after all discounts
	 */
	private double calculateOcassionalPrice(Order order)
	{
		String query = String.format("SELECT discount FROM park_settings WHERE park_name=\"%s\";", order.getParkName());
		double price = PRICE, generalDiscount, parkDiscount = 0;
		Statement stmt;
		// Traveler traveler = order.getTraveler();
		int visitorsNumber = order.getNumberOfVisitors();
		switch (order.getType())
		{
		case "instructor":
			generalDiscount = 0.9;
			break;
		case "subscriber":
			generalDiscount = 0.8;
			break;
		default: // regular
			generalDiscount = 1;
			break;
		}
		try
		{
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if (rs.first())
				parkDiscount = rs.getInt(1);
			rs.close();
		} catch (Exception e)
		{
			System.out.println("there's a problem with price query");
			e.printStackTrace();
		}
		return price * visitorsNumber * generalDiscount * (1 - parkDiscount * 0.01);
	}

	/**
	 * the method calculates the price of the preordered order
	 * @param order the order that we want to calculate the price for
	 * @return the price after all discounts
	 */
	private double calculatePreorderedPrice(Order order)
	{
		// preordered - 15% for regulars + subscribers
		String query = String.format("SELECT discount FROM park_settings WHERE park_name=\"%s\";", order.getParkName());
		Statement stmt;
		double price = PRICE, generalDiscount, parkDiscount = 0;
		// Traveler traveler = order.getTraveler();
		int visitorsNumber = order.getNumberOfVisitors();
		switch (order.getType())
		{
		case "instructor":
			visitorsNumber--;
			generalDiscount = 0.75;
			break;
		case "subscriber":
			generalDiscount = 0.85 * 0.8;
			break;
		default:
			generalDiscount = 0.85;
			break;
		}
		try
		{
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if (rs.first())
				parkDiscount = rs.getInt(1);
			rs.close();
		} catch (Exception e)
		{
			System.out.println("there's a problem with price query");
			e.printStackTrace();
		}
		return price * visitorsNumber * generalDiscount * (1 - parkDiscount * 0.01);
	}

	/**
	 * the method returns another available (alternative) dates for failed order
	 * @param order the failed order
	 * @return available slots, up to 3 days before, and up to 3 days after the date
	 */
	public String OtherAvailableSlots(Order order)
	{
		LocalDate fromDate = order.getDate().minusDays(4), temp = fromDate;
		StringBuilder str = new StringBuilder();
		Order[] orders = new Order[14];
		for (int i = 0; i < 14; i += 2)
		{ // -3 -2 -1 date 1 2 3
			temp = temp.plusDays(1);
			if (!temp.isBefore(LocalDate.now()))
			{
				Order morning = new Order(order, temp, "08:00 - 12:00");
				if (checkSlotForOrder(morning))
					orders[i] = morning;
				Order noon = new Order(order, temp, "12:00 - 16:00");
				if (checkSlotForOrder(noon))
					orders[i + 1] = noon;
			}
		}
		for (int i = 0; i < 14; i++)
		{
			if (orders[i] != null)
				str.append(orders[i].getDate() + "/" + orders[i].getTimeSlot() + "/");
		}
		return str.toString();
	}

	/**
	 * the method adds traveler to traveler list
	 * @param traveler the traveler that we want to add
	 * @return "updated", if the traveler already exist, else "added"
	 */
	public String addTravelerToTravelersList(Traveler traveler)
	{
		Statement stmt;
		String id = traveler.getIdNumber();
		try
		{
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM travelers_with_order WHERE id_number=\"" + id + "\";");
			if (rs.first())
			{
				rs.close();
				String query = "UPDATE travelers_with_order SET email= ?, phone= ? WHERE id_number = ?";
				PreparedStatement preparedStmt = conn.prepareStatement(query);
				preparedStmt.setString(1, traveler.getEmail());// email
				preparedStmt.setString(2, traveler.getPhone());// phone - from order
				preparedStmt.setString(3, id);// id
				preparedStmt.executeUpdate();
				preparedStmt.close();
				return "Updated";
			} else
			{
				rs.close();
				String query = "INSERT INTO travelers_with_order VALUES(?, ?, ?, ?, ?)";
				PreparedStatement preparedStmt = conn.prepareStatement(query);
				preparedStmt.setString(1, traveler.getfName());// firstName
				preparedStmt.setString(2, traveler.getlName());// lastName
				preparedStmt.setString(3, traveler.getEmail());// email
				preparedStmt.setString(4, id);// id
				preparedStmt.setString(5, traveler.getPhone());// phone - from order
				preparedStmt.executeUpdate();
				preparedStmt.close();
				return "Added";
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * the method makes all the confirmed passed orders to "unused"
	 * @return true if succeeded, else false
	 */
	public boolean changeNotYetOrdersToUnused()
	{// ************************
		try
		{
			String query = "UPDATE orders SET order_status= ? WHERE order_status= ? AND order_date < CURDATE();";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, "unused");
			preparedStmt.setString(2, "confirmed");
			preparedStmt.executeUpdate();
			preparedStmt.close();

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * the method adds order to waiting lists
	 * @param order the order that we want to add
	 * @return "Already" is exists, "Added" if added, else "false"
	 */
	public String addOrderToWaitingList(Order order)
	{
		if (waitingListExist(order))
			return "Already";
		String travelerId = order.getIdentification(), subNum = "";
		if (order.getType().equals("subscriber"))
		{
			Subscriber s = getTravelerBySubNum(travelerId);
			subNum = s.getSubscriberNumber();
			travelerId = order.getTraveler().getIdNumber();
		}
		try
		{
			String query = "INSERT INTO waiting_list VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, "0");// orderId - waiting list
			preparedStmt.setString(2, order.getParkName());
			preparedStmt.setString(3, order.getDate().toString());
			preparedStmt.setString(4, order.getTimeSlot());
			preparedStmt.setInt(5, order.getNumberOfVisitors());
			preparedStmt.setString(6, order.getEmail());
			preparedStmt.setString(7, order.getType());
			preparedStmt.setString(8, "notYet");
			preparedStmt.setDouble(9, order.getPrice());
			preparedStmt.setString(10, "preordered");
			preparedStmt.setString(11, travelerId);
			preparedStmt.setString(12, subNum);
			preparedStmt.setTimestamp(13, new Timestamp(System.currentTimeMillis()));
			preparedStmt.executeUpdate();
			preparedStmt.close();
			if (addTravelerToTravelersList(order.getTraveler()) != null)// *******
				return "Added";
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return "false";
	}

	/**
	 * the method gets the order list of a traveler
	 * @param traveler the traveler that we want to get order list for
	 * @return a string with the order list
	 */
	public String getOrderList(Traveler traveler)
	{
		StringBuilder str = new StringBuilder();
		String id = traveler.getIdNumber();
		Statement stmt;
		try
		{
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM orders WHERE identification=\"" + id + "\";");
			while (rs.next())
			{
				// Print out the values
				str.append(rs.getString(1) + "/");// order number
				str.append(rs.getString(2) + "/");// park name
				str.append(rs.getString(3) + "/");// date
				str.append(rs.getString(4) + "/");// time
				str.append(rs.getString(5) + "/");// number of visitors
				str.append(rs.getString(8) + "/");// status
			}
			rs.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return str.toString();
	}

	/**
	 * the method loads a specific day for visitor report
	 * @param report string array of: year, month, day
	 * @return the data of the visitor report
	 */
	public String[] reportSpecificDay(String[] report)
	{
		//ZoneId z = ZoneId.of("Israel");
		//LocalDate today = LocalDate.now(z);
		String m = String.valueOf(Month.valueOf(report[2].toUpperCase()).getValue());
		StringBuilder str = new StringBuilder();
		Statement stmt;
		try
		{
			stmt = conn.createStatement();
			ResultSet rsReg = null, rsSub = null, rsGrp = null, rsAvg = null;
			str.append("/gui/VisitorsReport.fxml" + "-" + report[2] + "-" + report[3] + "-");
			/* Any other month and year that are not current month and current year */

			rsReg = stmt.executeQuery(String.format(
					"SELECT visitor_type, HOUR(enter_time), COUNT(*), SUM(visitors_number) FROM gonature_db.visiting_time WHERE visitor_type = \"regular\" AND YEAR(order_date) = \"%s\" AND MONTH(order_date) = \"%s\" AND DAY(order_date) = \"%s\" GROUP BY HOUR(enter_time) ORDER BY enter_time;",
					report[3], m, report[1]));

			while (rsReg.next())
			{
				str.append(rsReg.getString(2) + "-");// enter time
				str.append(rsReg.getString(1) + "-");// visitor_type
				str.append(rsReg.getString(4) + "-");// number of visitors
			}
			rsReg.close();

			rsSub = stmt.executeQuery(String.format(
					"SELECT visitor_type, HOUR(enter_time), COUNT(*), SUM(visitors_number) FROM gonature_db.visiting_time WHERE visitor_type = \"Subscriber\" AND YEAR(order_date) = \"%s\" AND MONTH(order_date) = \"%s\" AND DAY(order_date) = \"%s\" GROUP BY HOUR(enter_time) ORDER BY enter_time;",
					report[3], m, report[1]));

			while (rsSub.next())
			{
				str.append(rsSub.getString(2) + "-");// enter time
				str.append(rsSub.getString(1) + "-");// visitor_type
				str.append(rsSub.getString(4) + "-");// number of visitors
			}
			rsSub.close();

			rsGrp = stmt.executeQuery(String.format(
					"SELECT visitor_type, HOUR(enter_time), COUNT(*), SUM(visitors_number) FROM gonature_db.visiting_time WHERE visitor_type = \"Instructor\" AND YEAR(order_date) = \"%s\" AND MONTH(order_date) = \"%s\" AND DAY(order_date) = \"%s\" GROUP BY HOUR(enter_time) ORDER BY enter_time;",
					report[3], m, report[1]));

			while (rsGrp.next())
			{
				str.append(rsGrp.getString(2) + "-");// enter time
				str.append(rsGrp.getString(1) + "-");// visitor_type
				str.append(rsGrp.getString(4) + "-");// number of visitors
			}
			rsGrp.close();
			str.append("AVG" + "-");
			rsAvg = stmt.executeQuery(String.format(
					"SELECT *,SUBTIME(exit_time, enter_time) FROM gonature_db.visiting_time WHERE YEAR(order_date) = \"%s\" AND MONTH(order_date) = \"%s\" AND DAY(order_date) = \"%s\";",
					report[3], m, report[1]));
			while (rsAvg.next())
			{
				str.append(rsAvg.getString(7) + "-");// number of visitors
				str.append(rsAvg.getString(8) + "-");// visitor type
				str.append(rsAvg.getString(9) + "-");// visit time
			}
			rsAvg.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		String[] s = str.toString().split("-");
		return s;
	}

	/**
	 * the method removes the order from the waiting list
	 * @param order the order that we want to remove
	 * @return true if succeeded, else false
	 */
	public boolean removeOrderFromWaitingList(Order order)
	{
		try
		{
			String query = "DELETE FROM waiting_list WHERE identification = ? AND park_name = ? AND order_date = ? AND time_slot = ?";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, order.getIdentification());
			preparedStmt.setString(2, order.getParkName());
			preparedStmt.setString(3, order.getDate().toString());
			preparedStmt.setString(4, order.getTimeSlot());
			preparedStmt.executeUpdate();
			preparedStmt.close();
			return true;
		} catch (Exception e)
		{
			System.out.println("Error occurred");
		}
		return false;
	}

	/**
	 * the method moves order from the waiting list to the order list
	 * @param order the order that we want to move
	 * @return the order id, or "false", if fails
	 */
	public String moveOrderFromWaiting(Order order)
	{
		order.setPrice(calculatePreorderedPrice(order));
		if (removeOrderFromWaitingList(order))
			return addOrderToOrdersList(order, "preorderd");
		return "false";
	}

	/**
	 * the method checks if the order already exists on order list
	 * @param order the order that we want to check
	 * @return true of the order exists, else false
	 */
	private boolean orderAlreadyExist(Order order)
	{
		Statement stmt;
		String query;
		query = String.format(
				"SELECT * FROM orders WHERE identification=\"%s\" AND order_date=\"%s\" AND park_name=\"%s\" AND time_slot=\"%s\" AND order_status!=\"canceled\";",
				order.getIdentification(), order.getDate().toString(), order.getParkName(), order.getTimeSlot());
		try
		{
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if (rs.first())
			{
				rs.close();
				return true;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * the method checks if the order already exists on waiting list
	 * @param order the order that we want to check
	 * @return true of the order exists, else false
	 */
	private boolean waitingListExist(Order order)
	{
		Statement stmt;
		String query;
		query = String.format(
				"SELECT * FROM waiting_list WHERE identification=\"%s\" AND order_date=\"%s\" AND park_name=\"%s\" AND time_slot=\"%s\";",
				order.getIdentification(), order.getDate().toString(), order.getParkName(), order.getTimeSlot());
		try
		{
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if (rs.first())
			{
				rs.close();
				return true;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * the method gets an occasional or preordered order and adds it to the order list if there's slot
	 * @param order the order that we want to add
	 * @return "AlreadyExist" if already exists, "DatePassed" if date passed, order id if the adding
	 * succeeded, or else- available alternative dates 
	 */
	public String addOrderIfSlotIsOK(Order order)
	{
		if (order.getTraveler() == null)
		{ // occasional order
			Traveler t = getTravelerById(order.getIdentification());
			if (t == null)
			{ // doesn't exist in db
				t = new Traveler();
				t.setIdNumber(order.getIdentification());
				t.setType("regular");
			}
			order.setTraveler(t);
			String travelerType = travelerType(order.getIdentification());// identification = traveler id number
			if (order.getType() == null)
			{// no at instructor
				if (travelerType.equals("instructor"))
					order.setType("regular");
				else
					order.setType(travelerType);
			}
			order.setPrice(calculateOcassionalPrice(order));
			return addOrderToOrdersList(order, "occasional");
		}
		// preorder - traveler wants to make an order
		if (orderAlreadyExist(order))
			return "AlreadyExist";
		if (orderDatePassed(order))
			return "DatePassed";
		if (checkSlotForOrder(order))
		{
			order.setPrice(calculatePreorderedPrice(order));
			return addOrderToOrdersList(order, "preordered");
		}
		return OtherAvailableSlots(order);// No Slot
	}

	/**
	 * the method checks if order date already passed
	 * @param order the order that we want to check
	 * @return true if the date passed, else false
	 */
	private boolean orderDatePassed(Order order)
	{
		if (order.getDate().equals(LocalDate.now()))
		{
			LocalTime now = LocalTime.now();
			String time = order.getTimeSlot();
			if ((time.equals("08:00 - 12:00") && now.isAfter(LocalTime.parse("12:00:00")))
					|| (time.equals("12:00 - 16:00") && now.isAfter(LocalTime.parse("16:00:00"))))
				return true;
		}
		return false;
	}

	/**
	 * the method gets all the waiting lists of a traveler
	 * @param traveler the traveler that want his waiting lists
	 * @return staring of all the waiting lists
	 */
	public String travelerWaitingList(Traveler traveler)
	{
		Statement stmt;
		StringBuilder str = new StringBuilder();
		try
		{
			stmt = conn.createStatement(); // Statement for the table
			String query1 = "DELETE FROM waiting_list "
					+ "WHERE "
					+ "	order_date < CURDATE() OR "
					+ "  ( "
					+ "		order_date=CURDATE() AND "
					+ "		( "
					+ "			(time_slot=\"08:00 - 12:00\" AND CURTIME()>TIME(\"08:00:00\")) OR "
					+ "         (time_slot=\"12:00 - 16:00\" AND CURTIME()>TIME(\"12:00:00\")) "
					+ "       )"
					+ "  );";
			PreparedStatement preparedStmt = conn.prepareStatement(query1);
			preparedStmt.executeUpdate();
			preparedStmt.close();

			String query2 = String.format("select *, case when (" + "	SELECT inwl.registration_time "
					+ "	FROM waiting_list as inwl "
					+ " INNER JOIN park_settings ON inwl.park_name=park_settings.park_name" + "	WHERE "
					+ "    inwl.park_name=wl.park_name and " + "    inwl.order_date=wl.order_date and "
					+ "    inwl.time_slot=wl.time_slot and " + "    visitors_number<=capacity-spares-( "
					+ "			SELECT IFNULL(sum(visitors_number), 0) as availables " + "			FROM orders "
					+ "			WHERE orders.park_name=inwl.park_name and "
					+ "            orders.order_date= inwl.order_date and "
					+ "            orders.time_slot=inwl.time_slot and "
					+ "            orders.order_status!=\"canceled\" " + "		) " + "	order by registration_time ASC "
					+ "	limit 1)=wl.registration_time then true else false end as bookable "
					+ "from waiting_list as wl " + "where identification=\"%s\";", traveler.getIdNumber());

			ResultSet rs2 = stmt.executeQuery(query2);
			while (rs2.next())
			{
				str.append(rs2.getString(2) + "/"); // park
				str.append(rs2.getString(3) + "/"); // date
				str.append(rs2.getString(4) + "/"); // time
				str.append(rs2.getString(5) + "/"); // visitors
				str.append(rs2.getString(14) + "/"); // bookable
			}
			rs2.close();
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("Error occurred");
		}
		return str.toString();
	}

	/**
	 * the method updates the order status to be confirmed
	 * @param orderId the order id
	 * @return true if succeeded, else false
	 */
	public boolean confirmOrder(String orderId)
	{
		try
		{
			String query = "UPDATE orders SET order_status= ? WHERE order_id= ?;";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, "confirmed");
			preparedStmt.setString(2, orderId);
			preparedStmt.executeUpdate();
			return true;
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("Error occurred");
		}
		return false;
	}

	/**
	 * the method updates the order status to be canceled
	 * @param orderId the order id
	 * @return true if succeeded, else false
	 */
	public boolean cancelOrder(String orderId)
	{
		try
		{
			String query = "UPDATE orders SET order_status= ? WHERE order_id= ?;";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, "canceled");
			preparedStmt.setString(2, orderId);
			preparedStmt.executeUpdate();
			preparedStmt.close();

			invokeWaitingList(orderId);

			return true;
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("Error occurred");
		}
		return false;
	}

	/**
	 * the method checks if there's traveler waiting for a date 
	 * if there is, sends him an email
	 * @param orderId the order id of the canceled order (for park, date, time)
	 */
	private void invokeWaitingList(String orderId)
	{
		Statement stmt;
		String content = "You can now log into the GoNature system and book your Order!\n"
				+ "Go to your \"waiting list page\" and press on the \"book\" button."
				+ "\nPlease hurry! if you wouldn't do so in a HOUR, the system will automatclly remove it.\n"
				+ "\n\nHope that you'll be able to make it on time,\nGoNature System.\n";
		Order order = new Order();
		try
		{
			String q = String.format("SELECT park_name, order_date, time_slot FROM orders WHERE order_id=\"%s\";",
					orderId);
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(q);
			if (rs.first())
			{
				order.setParkName(rs.getString(1));
				order.setDate(LocalDate.parse(rs.getString(2)));
				order.setTimeSlot(rs.getString(3));
			}
			rs.close();

			String q2 = String.format(
					"SELECT * FROM waiting_list WHERE park_name=\"%s\" AND order_date=\"%s\" AND time_slot=\"%s\" "
							+ "AND visitors_number<= ( " + "  SELECT capacity-spares-( "
							+ "	 SELECT IFNULL(sum(visitors_number), 0) AS b " + "	 FROM orders "
							+ "	 WHERE park_name=\"%s\" AND order_date=\"%s\" AND time_slot=\"%s\" AND order_status!=\"canceled\" "
							+ "	 ) AS a " + " FROM park_settings " + " WHERE park_name=\"%s\" " + " ) "
							+ "ORDER BY registration_time ASC;",
					order.getParkName(), order.getDate().toString(), order.getTimeSlot(), order.getParkName(),
					order.getDate().toString(), order.getTimeSlot(), order.getParkName());

			ResultSet rs2 = stmt.executeQuery(q2);
			if (rs2.first())
			{
				order.setEmail(rs2.getString(6));
				order.setIdentification(rs2.getString(11));
				rs2.close();
				sendMail(order.getEmail(), "Waiting list order at GoNature System", content);

				final LocalDateTime now = LocalDateTime.now();
				final LocalDateTime checking = now.plusHours(1);// plusMinutes(5);//plusHours(1)
				final ScheduledExecutorService ses2 = Executors.newScheduledThreadPool(1);
				ses2.schedule(() -> checkOrderBooked(order, orderId), now.until(checking, ChronoUnit.MILLIS),
						TimeUnit.MILLISECONDS);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("Error occurred");
		}
	}

	/**
	 * the method checks if the waiting traveler booked his order on time
	 * @param order the order that we want to check
	 * @param orderId the order id of the canceled order (for invoking waiting list)
	 */
	private void checkOrderBooked(Order order, String orderId)
	{
		Statement stmt;
		try
		{
			stmt = conn.createStatement();
			String query = String.format(
					"SELECT * FROM orders WHERE identification=\"%s\" AND park_name=\"%s\" AND order_date=\"%s\" AND time_slot=\"%s\";",
					order.getIdentification(), order.getParkName(), order.getDate().toString(), order.getTimeSlot());
			ResultSet rs = stmt.executeQuery(query);
			if (!rs.first())
			{// if the traveler didn't book his waiting order on time
				if (removeOrderFromWaitingList(order))
					invokeWaitingList(orderId);// don't care about the actual orderId, but about the park,date,time
			}

		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("Error occurred");
		}
	}

	/**
	 * the method checks if there's a slot for the order at the specific time slot 
	 * @param order the order that we want to check
	 * @return true if there's a slot, else false
	 */
	public boolean checkSlotForOrder(Order order)
	{
		Statement stmt;
		int countVisitors = 0, capacity = 0, spares = 0, numberOfVisitors = order.getNumberOfVisitors();
		String date = order.getDate().toString(), time = order.getTimeSlot();
		String parkName = order.getParkName();

		String query = String.format(
				"SELECT visitors_number FROM orders WHERE order_date=\"%s\" AND time_slot=\"%s\" AND park_name=\"%s\" AND (order_status=\"notYet\" OR order_status=\"confirmed\");",
				date, time, parkName);

		try
		{
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next())
			{ // start counting visitors number of the date
				countVisitors += rs.getInt(1); // after select there's only one column
			}
			rs.close();
			rs = stmt.executeQuery(String.format("SELECT * FROM park_settings WHERE park_name=\"%s\";", parkName));
			if (rs.first())
			{ // PRECONDITION - park setting are initialized
				capacity = Integer.parseInt(rs.getString(1));
				spares = Integer.parseInt(rs.getString(2));
			}
			rs.close();
			if ((countVisitors + numberOfVisitors) <= (capacity - spares))
			{
				return true;
			} else
				return false; // no slot

		} catch (Exception e)
		{
			System.out.println("there's a problem with checkSlotForOrder query");
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * the method creates use report for park manager
	 * @param month month
	 * @param year year
	 * @param org organization
	 * @return string array of the report data
	 */
	private String[] useReport(String month, String year, String org)
	{
		//ZoneId z = ZoneId.of("Israel");
		//LocalDate today = LocalDate.now(z);
		String m = String.valueOf(Month.valueOf(month.toUpperCase()).getValue());
		StringBuilder str = new StringBuilder();
		Statement stmt;
		try
		{
			stmt = conn.createStatement();
			ResultSet rs = null;
			/* If the month is current month and year is current year */
			rs = stmt.executeQuery(String.format(
					"SELECT DAY(date), from_time, to_time FROM gonature_db.use_report WHERE park_name = \"%s\" AND YEAR(date) = \"%s\" AND MONTH(date) = \"%s\" ORDER BY date;",
					org, year, m));
			str.append("/gui/UseReport.fxml" + "-" + org + "-" + month + "-" + year + "-");
			while (rs.next())
			{
				str.append(rs.getString(1) + "-");// day
				str.append(rs.getString(2) + "-");// from_time
				str.append(rs.getString(3) + "-");// to_time
			}
			rs.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		String[] s = str.toString().split("-");
		return s;
	}

	/**
	 * the method saves settings to park_settings table
	 * @param s string array of the setting data that we want to save
	 * @return true if succeeded, else false
	 */
	public boolean updateParkSettings(String[] s)
	{
		for (int i = 0; i < s.length; i += 3)
		{
			if (s[i + 1].equals("Park capacity"))
				if (!updateASetting("update park_settings set capacity = ? WHERE park_name = ?", s[i + 2],
						"Park capacity", s[i]))
					return false;
			if (s[i + 1].equals("Park spares"))
				if (!updateASetting("update park_settings set spares = ? WHERE park_name = ?", s[i + 2], "Park spares",
						s[i]))
					return false;
			if (s[i + 1].equals("Traveling time"))
				if (!updateASetting("update park_settings set travelingtime = ? WHERE park_name = ?", s[i + 2],
						"Traveling time", s[i]))
					return false;
			if (s[i + 1].equals("Discount"))
				if (!updateASetting("update park_settings set discount = ? WHERE park_name = ?", s[i + 2], "Discount",
						s[i]))
					return false;
		}
		if (!deleteFromTable(s))
			return false;
		return true;
	}

	/**
	 * the method updates a specific setting
	 * @param query the query of the park setting
	 * @param after the updated value - after changing
	 * @param approved the setting name
	 * @param org organization
	 * @return true if succeeded, else false
	 */
	private boolean updateASetting(String query, String after, String approved, String org)
	{
		try
		{// create the java mysql update preparedstatement
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, after);
			preparedStmt.setString(2, org);
			preparedStmt.executeUpdate();
			settingApproved(approved, org);
		} catch (Exception e)
		{
			System.out.println("Error occurred");
			return false;
		}

		return true;

	}

	/**
	 * the method checks if there's an order for a specific traveler identification
	 * and if so - updates the order status
	 * @param s traveler identification - id or subscriber number
	 * @return "USED" if used, "IDNOTFOUND" if not found, "EXIT" if the traveler is going out
	 * or match message
	 */
	public String currentVisitorsUpdate(String s) // Card reader
	{
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime now = LocalDateTime.now();
		LocalTime twelve = LocalTime.parse("12:00:00");
		String time;
		String[] newEnter = new String[4];
		Statement stmt;
		try
		{
			stmt = conn.createStatement(); // Statement for the correct visitor
			ResultSet rs = stmt.executeQuery("SELECT * FROM card_reader WHERE identification = " + s + ";");
			if (rs.first())
			{
				String query = "DELETE FROM card_reader where identification = " + s + "";
				PreparedStatement preparedStmt = conn.prepareStatement(query);
				preparedStmt.executeUpdate();
				if (!updateVisitingTime(s, "Exit"))
					return "false";
				checkIfMaxCapacityReached(rs.getString(4), false);
				return "EXIT";
			} else
			{
				rs = stmt.executeQuery(String.format("SELECT * FROM orders WHERE order_date=\"%s\" AND (identification=\"%s\" OR sub_num=\"%s\");", dtf.format(now), s, s));
				if (!rs.first())
					return "IDNOTFOUND";
				if (rs.getString(8).equals("used"))
					return "USED";
				if (rs.getString(8).equals("canceled"))
					return "CANCELED";
				time = rs.getString(3);
				rs.close();
				rs = stmt.executeQuery(String.format(
						"SELECT * FROM orders WHERE (identification=\"%s\" OR sub_num=\"%s\") AND order_status=\"confirmed\" AND order_date=\"%s\";",
						s, s, dtf.format(now)));
				if (!rs.first())
					return "WRONGTIME" + " " + time;
				String timeSlot = rs.getString(4);
				if((timeSlot.equals("08:00 - 12:00") && LocalTime.now().isAfter(twelve))
					|| (timeSlot.equals("12:00 - 16:00") && LocalTime.now().isBefore(twelve)))
					return "WRONGHOUR " + timeSlot;
				newEnter[0] = (rs.getInt(5)) + ""; // current visitors
				if (rs.getString(10).equals("occasional"))
					newEnter[1] = "1";
				else
					newEnter[1] = "0";
				newEnter[2] = rs.getString(1); // order_id
				newEnter[3] = rs.getString(2);
				rs.close();
				String query = "update orders set order_status = ? where identification = " + s + " OR sub_num=" + s
						+ " ";
				PreparedStatement preparedStmt = conn.prepareStatement(query);
				preparedStmt.setString(1, "used");
				preparedStmt.executeUpdate();
				query = "INSERT INTO card_reader VALUES(?, ?, ?, ?, ?)";
				preparedStmt = conn.prepareStatement(query);
				preparedStmt.setInt(1, Integer.parseInt(newEnter[0]));
				preparedStmt.setInt(2, Integer.parseInt(newEnter[1]));
				preparedStmt.setString(3, newEnter[2]);
				preparedStmt.setString(4, newEnter[3]);
				preparedStmt.setString(5, s);
				preparedStmt.executeUpdate();
				if (!updateVisitingTime(newEnter[2], "Enter"))
					return "false";
				checkIfMaxCapacityReached(newEnter[3], true);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			return "false";
		}
		return "ENTERED";
	}

	/**
	 * the method checks if the max capacity reached and updates the use report about that
	 * @param park_name organization
	 * @param b if enters - true, if exits - false
	 */
	private void checkIfMaxCapacityReached(String park_name, boolean b)
	{
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime date = LocalDateTime.now();
		DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("HH:mm");
		int capacity = 0;
		Statement stmt;
		ResultSet rs = null;
		PreparedStatement preparedStmt;
		try
		{
			stmt = conn.createStatement();
			if (b) // ENTER
			{
				rs = stmt.executeQuery(
						String.format("SELECT capacity FROM park_settings WHERE park_name = \"%s\";", park_name));
				if (rs.first())
					capacity = rs.getInt(1);
				rs.close();
				String current = getCurrentNumberOfVisitors(park_name);
				String[] visitorsAndSpares = current.split(" ");
				int currNum = Integer.parseInt(visitorsAndSpares[0]);
				if (currNum == capacity)
				{
					rs = stmt.executeQuery(
							String.format("SELECT * FROM use_report WHERE date = \"%s\" AND park_name = \"%s\";",
									dtf.format(date), park_name));
					if (!rs.first())
					{
						String query = "INSERT INTO use_report VALUES(?, ?, ?, ?)";
						preparedStmt = conn.prepareStatement(query);
						preparedStmt.setString(1, park_name);// park_name
						preparedStmt.setString(2, dtf.format(date));// date
						preparedStmt.setString(3, "08:00");// from_time
						preparedStmt.setString(4, dtf1.format(date));// to_time
						preparedStmt.executeUpdate();
						rs.close();
					} else
					{
						String query = String.format(
								"UPDATE use_report set to_time = ? WHERE to_time = \"16:00\" AND park_name = \"%s\"",
								park_name);
						preparedStmt = conn.prepareStatement(query);
						preparedStmt.setString(1, dtf1.format(date));// to_time
						preparedStmt.executeUpdate();
					}
				}
			} else // EXIT
			{
				String query = "INSERT INTO use_report VALUES(?, ?, ?, ?)";
				preparedStmt = conn.prepareStatement(query);
				preparedStmt.setString(1, park_name);// park_name
				preparedStmt.setString(2, dtf.format(date));// date
				preparedStmt.setString(3, dtf1.format(date));// from_time
				preparedStmt.setString(4, "16:00");// to_time
				preparedStmt.executeUpdate();
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * the method gets a traveler by id
	 * @param id id
	 * @return traveler if exits, else null
	 */
	public Traveler getTravelerById(String id)
	{
		Statement statement;
		String type = travelerType(id);
		if (type.equals("subscriber"))
			return getTravelerBySubNum(id);
		if (type.equals("instructor"))
			return getInstructor(id);
		Traveler traveler = new Traveler();
		try
		{
			statement = conn.createStatement(); // Statement for the correct worker
			String query = String.format("SELECT * FROM travelers_with_order WHERE id_number=\"%s\"", id);
			ResultSet rs = statement.executeQuery(query);
			if (!rs.first())
				return null;
			else
			{
				traveler.setfName(rs.getString(1));
				traveler.setlName(rs.getString(2));
				traveler.setEmail(rs.getString(3));
				traveler.setIdNumber(rs.getString(4));
				traveler.setType(type);
				traveler.setPhone(rs.getString(5));
			}
			rs.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return traveler;
	}

	/**
	 * the method returns a specific park's discount
	 * @param park park name
	 * @return the park discount
	 */
	public String getParkDiscount(String park)
	{
		if (park == null)
			return null;
		Statement stmt;
		String discount = null;
		try
		{
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT discount FROM park_settings WHERE park_name=\"" + park + "\";");
			if (rs.first())
				discount = rs.getInt(1) + "";
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return discount;
	}

	/**
	 * the method adds the order to the order list and sends an email
	 * @param order the order that we want to add
	 * @param oc_pre "occasional" if occasional, else "preordered"
	 * @return order id + price
	 */
	public String addOrderToOrdersList(Order order, String oc_pre)
	{
		String travelerId = order.getIdentification(), parkName = order.getParkName(),
				date = order.getDate().toString();
		String subNum = "", travelerType = order.getType(), status;
		long epoch = (System.currentTimeMillis() / 100) % 100000;
		String orderID = String.format("%05d",(int)epoch);
		if (travelerType.equals("subscriber"))
		{
			try
			{
				Subscriber s = getTravelerBySubNum(travelerId);
				subNum = s.getSubscriberNumber();
				travelerId = order.getTraveler().getIdNumber();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		if (oc_pre.equals("occasional") || order.getDate().equals(LocalDate.now())
				|| (order.getDate().minusDays(1).equals(LocalDate.now())
						&& LocalTime.now().isAfter(LocalTime.parse("18:00:00"))))
			status = "confirmed";
		else
			status = "notYet";
		// Statement stmt;
		try
		{
			String query = "INSERT INTO orders VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, orderID);
			preparedStmt.setString(2, parkName);
			preparedStmt.setString(3, date);
			// preparedStmt.setDate(3, java.sql.Date.valueOf(order.getDate()));
			preparedStmt.setString(4, order.getTimeSlot());
			preparedStmt.setInt(5, order.getNumberOfVisitors());
			preparedStmt.setString(6, order.getEmail());
			preparedStmt.setString(7, order.getType());
			preparedStmt.setString(8, status);
			preparedStmt.setDouble(9, order.getPrice());
			preparedStmt.setString(10, oc_pre);
			preparedStmt.setString(11, travelerId);
			preparedStmt.setString(12, subNum);
			preparedStmt.executeUpdate();
			preparedStmt.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		if (oc_pre.equals("preordered"))
			scheduleNotification(order, orderID, status);
		DecimalFormat df2 = new DecimalFormat(".##");
		return orderID + " " + df2.format(order.getPrice());
	}

	/**
	 * the method schedules emails for the specific situation
	 * @param order the order that we want to schedule for
	 * @param orderID the order id
	 * @param status "confirmed" if the order is already confirmed
	 */
	private void scheduleNotification(Order order, String orderID, String status)
	{
		String reminder = "Dear Traveler,\n please log in to the system and"
				+ " confirm your reservation for tomorrow.\n Please notice: if you wouldn't do so in 2 hours,\n"
				+ "the system will automatically cancel your order.\n\nHave a nice Day.\nGoNature System\n";

		String canceled = "Dear Traveler,\nsince you haven't confirmed your order at the requested time, "
				+ "the system automatically canceled your order.\nSorry, we will meet some other time...\n"
				+ "\n Have a nice day.\nGoNature System\n";

		String orderFinished = "Your order " + orderID + " is booked! We are waiting for you at:\n" + "\nPark:\t"
				+ order.getParkName() + "\nDate:\t" + order.getDate() + "\nTime slot:\t" + order.getTimeSlot()
				+ "\nVisitors number:\t" + order.getNumberOfVisitors() + "\nTotal cost:\t" + order.getPrice()
				+ "\nSee You SOON!\nPlease notice: A day before order date"
				+ " you will be asked to confirm your order. Don't worry, we will remind you to do so."
				+ "\n\nHave a nice day.\nGoNature System\n";

		sendMail(order.getEmail(), "Your order at GoNature is booked", orderFinished);

		if (!status.equals("confirmed"))
		{
			final LocalDateTime now = LocalDateTime.now();

			final LocalTime lc = LocalTime.parse("18:00:00");// "11:45:00"
			final LocalDateTime orderDate = LocalDateTime.of(order.getDate(), lc).minusDays(1);

			final ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
			ses.schedule(() -> sendMail(order.getEmail(), "Reminder of a visit for tomorrow", reminder),
					now.until(orderDate, ChronoUnit.MILLIS), TimeUnit.MILLISECONDS);

			final LocalDateTime checking = LocalDateTime.of(order.getDate(), lc.plusHours(2)).minusDays(1);
			// lc.plusMinutes(5) //plusHours(2)
			final ScheduledExecutorService ses2 = Executors.newScheduledThreadPool(1);
			ses2.schedule(() -> checkOrderConfirmed(order, canceled), now.until(checking, ChronoUnit.MILLIS),
					TimeUnit.MILLISECONDS);
		}

	}

}
