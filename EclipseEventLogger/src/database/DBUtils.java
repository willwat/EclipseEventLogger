package database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;

import com.mysql.cj.jdbc.MysqlDataSource;

import utilities.GeneralUtils;

public class DBUtils {

	private final static String configFilePath = System.getProperty("user.home") + "\\EclipseEventLogConfig\\config.txt";
	private static Connection DBConnection;
	
	public static void addRecordToDB(String userMacAddress, String eventMessage, int eventTypeID) throws SQLException {
		if(!macAddressInDB(userMacAddress)) {
			addMacAddressToDB(userMacAddress);
		}
		if(!isEventInDB(eventMessage, eventTypeID)) {
			addEventToDB(eventMessage, eventTypeID);
		}

		int userID = getUserID(userMacAddress);
		int eventID = getEventID(eventMessage, eventTypeID);
		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
		
		PreparedStatement pStmt = DBConnection.prepareStatement("INSERT INTO `tRecord`(`UserID`, `EventID`, `TimeOfRecording`) VALUES (?, ?, ?)");
		
		pStmt.setInt(1, userID);
		pStmt.setInt(2, eventID);
		pStmt.setTimestamp(3, currentTimestamp);
		
		pStmt.execute();
	}
	
	public static Connection getDBConnection() {
		return DBConnection;
	}
	
	public static void setDBConnection(Connection conn) {
		DBConnection = conn;
	}
	
	public static boolean isEventInDB(String eventMsg, int eventTypeID) throws SQLException {
		boolean isEventInDB = false;
		
		PreparedStatement pStmt = DBConnection.prepareStatement("Select * From tEvent where Message = ? and EventTypeID = ?");
		pStmt.setString(1, eventMsg);
		pStmt.setInt(2, eventTypeID);
		
		ResultSet rs = pStmt.executeQuery();
		
		isEventInDB = rs.isBeforeFirst();
		
		return isEventInDB;
	}
	
	public static void addEventToDB(String eventMsg, int eventTypeID) throws SQLException {
		PreparedStatement pStmt = DBConnection.prepareStatement("INSERT INTO `tEvent`(`Message`, EventTypeID) VALUES (?, ?)");
		pStmt.setString(1, eventMsg);
		pStmt.setInt(2, eventTypeID);
		pStmt.execute();
	}
	
	public static int getEventID(String eventMsg, int eventTypeID) throws SQLException {
		int firstColumn = 1;
		PreparedStatement pStmt = DBConnection.prepareStatement("Select EventID From tEvent where Message = ? and EventTypeID = ?");
		pStmt.setString(1, eventMsg);
		pStmt.setInt(2, eventTypeID);
		ResultSet queryResults = pStmt.executeQuery();
		queryResults.next();
		return queryResults.getInt(firstColumn);
	}
	
	public static boolean macAddressInDB(String macAddress) throws SQLException {
		boolean macAddressInDB = false;
		
		PreparedStatement pStmt = DBConnection.prepareStatement("Select * From tUser where MacAddress = ?");
		pStmt.setString(1, macAddress);
		
		ResultSet rs = pStmt.executeQuery();
		
		macAddressInDB = rs.isBeforeFirst();
		
		return macAddressInDB;
	}
	
	public static void addMacAddressToDB(String macAddress) throws SQLException {
		PreparedStatement pStmt = DBConnection.prepareStatement("INSERT INTO `tUser`(`MacAddress`) VALUES (?)");
		pStmt.setString(1, macAddress);
		pStmt.execute();
	}
	
	public static int getUserID(String macAddress) throws SQLException {
		int firstColumn = 1;
		PreparedStatement pStmt = DBConnection.prepareStatement("Select UserID From tUser where MacAddress = ?");
		pStmt.setString(1, macAddress);
		ResultSet queryResults = pStmt.executeQuery();
		queryResults.next();
		return queryResults.getInt(firstColumn);
	}
	
	public static MysqlDataSource readConfigFileToDS() {
		MysqlDataSource databaseDS = new MysqlDataSource();
		
		String configFileText = GeneralUtils.getAllTextFromFile(configFilePath);
		String[] configFileLines = configFileText.split(System.lineSeparator());
		HashMap<String, String> databaseInfo = new HashMap<String,String>();
		int configFileLineKey = 0;
		int configFileLineValue = 1;
		
		for(String line : configFileLines) {
			String[] lineParts = line.split("=");
			databaseInfo.put(lineParts[configFileLineKey], (lineParts.length > 1) ? lineParts[configFileLineValue] : "");
		}
		
		try {
			databaseDS.setUseSSL(false);
			databaseDS.setDatabaseName(databaseInfo.get("Database"));
			databaseDS.setServerName(databaseInfo.get("Server"));
			databaseDS.setUser(databaseInfo.get("Username"));
			databaseDS.setPassword(databaseInfo.get("Password"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return databaseDS;
	}
	
	public static void createConfigFile() throws IOException {
		File configFile = new File(configFilePath);
		
		configFile.getParentFile().mkdirs();
		configFile.createNewFile();
		
		GeneralUtils.writeToFile(configFile.getPath(), "Server=localhost" + System.lineSeparator() + "Database=EclipseEventLog" + System.lineSeparator() + "Username=EclipseEventLog" + System.lineSeparator() + "Password=P@ssword");
	}
	
	public static boolean configFileExists() {
		return new File(configFilePath).exists();
	}

}
