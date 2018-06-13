package database;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;

import com.mysql.cj.jdbc.MysqlDataSource;

import utilities.GeneralUtils;

public class DBUtils {

	private final static String configFilePath = System.getProperty("user.home") + "\\EclipseErrorRecorderConfig\\config.txt";
	private final static MysqlDataSource databaseDS = readConfigFileToDS();
	
	public static void addRecordToDB(String userMacAddress, String errorMessage, int errorTypeID) throws SQLException {
		if(!macAddressInDB(userMacAddress)) {
			addMacAddressToDB(userMacAddress);
		}
		if(!isErrorInDB(errorMessage, errorTypeID)) {
			addErrorToDB(errorMessage, errorTypeID);
		}
		
		int userID = getUserID(userMacAddress);
		int errorID = getErrorID(errorMessage, errorTypeID);
		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
		
		PreparedStatement pStmt = databaseDS.getConnection().prepareStatement("INSERT INTO `trecord`(`UserID`, `ErrorID`, `TimeOfRecording`) VALUES (?, ?, ?)");
		
		pStmt.setInt(1, userID);
		pStmt.setInt(2, errorID);
		pStmt.setTimestamp(3, currentTimestamp);
		
		pStmt.execute();
	}
	
	public static boolean isErrorInDB(String errorMsg, int errorTypeID) throws SQLException {
		boolean isErrorInDB = false;
		
		PreparedStatement pStmt = databaseDS.getConnection().prepareStatement("Select * From tError where Message = ? and ErrorTypeID = ?");
		pStmt.setString(1, errorMsg);
		pStmt.setInt(2, errorTypeID);
		
		ResultSet rs = pStmt.executeQuery();
		
		isErrorInDB = rs.isBeforeFirst();
		
		return isErrorInDB;
	}
	
	public static void addErrorToDB(String errorMsg, int errorTypeID) throws SQLException {
		PreparedStatement pStmt = databaseDS.getConnection().prepareStatement("INSERT INTO `tError`(`Message`, ErrorTypeID) VALUES (?, ?)");
		pStmt.setString(1, errorMsg);
		pStmt.setInt(2, errorTypeID);
		pStmt.execute();
	}
	
	public static int getErrorID(String errorMsg, int errorTypeID) throws SQLException {
		int firstColumn = 1;
		PreparedStatement pStmt = databaseDS.getConnection().prepareStatement("Select ErrorID From tError where Message = ? and ErrorTypeID = ?");
		pStmt.setString(1, errorMsg);
		pStmt.setInt(2, errorTypeID);
		ResultSet queryResults = pStmt.executeQuery();
		queryResults.next();
		return queryResults.getInt(firstColumn);
	}
	
	public static boolean macAddressInDB(String macAddress) throws SQLException {
		boolean macAddressInDB = false;
		
		PreparedStatement pStmt = databaseDS.getConnection().prepareStatement("Select * From tUser where MacAddress = ?");
		pStmt.setString(1, macAddress);
		
		ResultSet rs = pStmt.executeQuery();
		
		macAddressInDB = rs.isBeforeFirst();
		
		return macAddressInDB;
	}
	
	public static void addMacAddressToDB(String macAddress) throws SQLException {
		PreparedStatement pStmt = databaseDS.getConnection().prepareStatement("INSERT INTO `tUser`(`MacAddress`) VALUES (?)");
		pStmt.setString(1, macAddress);
		pStmt.execute();
	}
	
	public static int getUserID(String macAddress) throws SQLException {
		int firstColumn = 1;
		PreparedStatement pStmt = databaseDS.getConnection().prepareStatement("Select UserID From tUser where MacAddress = ?");
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
		
		databaseDS.setDatabaseName(databaseInfo.get("Database"));
		databaseDS.setServerName(databaseInfo.get("Server"));
		databaseDS.setUser(databaseInfo.get("Username"));
		databaseDS.setPassword(databaseInfo.get("Password"));
		
		return databaseDS;
	}
	
	public static void createConfigFile() throws IOException {
		File configFile = new File(configFilePath);
		
		configFile.getParentFile().mkdirs();
		configFile.createNewFile();
		
		GeneralUtils.writeToFile(configFile.getPath(), "Server=" + System.lineSeparator() + "Database=" + System.lineSeparator() + "Username=" + System.lineSeparator() + "Password=");
	}
	
	public static boolean configFileExists() {
		return new File(configFilePath).exists();
	}

}
