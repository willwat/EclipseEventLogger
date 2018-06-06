package database;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.mysql.cj.jdbc.MysqlDataSource;

import utilities.GeneralUtils;

public class DBSetup {
	
	private final static String configFilePath = System.getProperty("user.home") + "\\EclipseErrorRecorderConfig\\config.txt";
	private static MysqlDataSource databaseDS;

	public static void buildDatabase() {

		readConfigFile();
		
		System.out.println(databaseDS.getServerName());
		System.out.println(databaseDS.getDatabaseName());
		System.out.println(databaseDS.getUser());

		
		String createUserTable = "CREATE TABLE `tUser` (\r\n" + 
				" `UserID` int(11) NOT NULL AUTO_INCREMENT,\r\n" + 
				" `MacAddress` varchar(30) NOT NULL,\r\n" + 
				" PRIMARY KEY (`UserID`),\r\n" + 
				" UNIQUE KEY `MacAddress` (`MacAddress`)\r\n" + 
				") ENGINE=InnoDB DEFAULT CHARSET=latin1";
		
		String createErrorTypeTable = "CREATE TABLE `tErrorType` (\r\n" + 
				" `ErrorTypeID` int(11) NOT NULL AUTO_INCREMENT,\r\n" + 
				" `ErrorType` varchar(20) NOT NULL,\r\n" + 
				" PRIMARY KEY (`ErrorTypeID`),\r\n" + 
				" UNIQUE KEY `ErrorType` (`ErrorType`)\r\n" + 
				") ENGINE=InnoDB DEFAULT CHARSET=latin1\r\n" + 
				"";
		String addErrorTypes = "INSERT INTO tErrorType (ErrorType)\r\n" + 
				"VALUES ('Runtime'), ('Syntax');";
		
		String createErrorTable = "CREATE TABLE `tError` (\r\n" + 
				" `ErrorID` int(11) NOT NULL AUTO_INCREMENT,\r\n" + 
				" `Message` varchar(60) NOT NULL,\r\n" + 
				" `ErrorTypeID` int(11) NOT NULL,\r\n" + 
				" PRIMARY KEY (`ErrorID`),\r\n" + 
				" UNIQUE KEY `ErrorNK` (`Message`,`ErrorTypeID`) USING BTREE,\r\n" + 
				" KEY `ErrorTypeID_FK` (`ErrorTypeID`),\r\n" + 
				" CONSTRAINT `ErrorTypeID_FK` FOREIGN KEY (`ErrorTypeID`) REFERENCES `terrortype` (`ErrorTypeID`) ON DELETE CASCADE ON UPDATE CASCADE\r\n" + 
				") ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1";
		
		String createRecordsTable = "CREATE TABLE `trecord` (\r\n" + 
				" `RecordID` int(11) NOT NULL AUTO_INCREMENT,\r\n" + 
				" `UserID` int(11) NOT NULL,\r\n" + 
				" `ErrorID` int(11) NOT NULL,\r\n" + 
				" PRIMARY KEY (`RecordID`),\r\n" + 
				" KEY `UserID_FK` (`UserID`),\r\n" + 
				" KEY `ErrorID_FK` (`ErrorID`),\r\n" + 
				" CONSTRAINT `ErrorID_FK` FOREIGN KEY (`ErrorID`) REFERENCES `terror` (`ErrorID`) ON DELETE CASCADE ON UPDATE CASCADE,\r\n" + 
				" CONSTRAINT `UserID_FK` FOREIGN KEY (`UserID`) REFERENCES `tuser` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE\r\n" + 
				") ENGINE=InnoDB DEFAULT CHARSET=latin1";
		
	}
	
	public static void readConfigFile() {
		databaseDS = new MysqlDataSource();
		String configFileText = GeneralUtils.getAllTextFromFile(configFilePath);
		String[] configFileLines = configFileText.split(System.lineSeparator());
		HashMap<String, String> databaseInfo = new HashMap<String,String>();
		int configFileLineKey = 0;
		int configFileLineValue = 1;
		
		for(String line : configFileLines) {
			String[] lineParts = line.split("=");
			databaseInfo.put(lineParts[configFileLineKey], lineParts[configFileLineValue]);
		}
		
		databaseDS.setDatabaseName(databaseInfo.get("Server"));
		databaseDS.setServerName(databaseInfo.get("Database"));
		databaseDS.setUser(databaseInfo.get("Username"));
		databaseDS.setPassword(databaseInfo.get("Password"));
	}
	
	public static void createConfigFile() {
		File configFile = new File(configFilePath);
		configFile.getParentFile().mkdirs();
		
		try {
			configFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		GeneralUtils.writeToFile(configFile.getPath(), "Server=" + System.lineSeparator() + "Database=" + System.lineSeparator() + "Username=" + System.lineSeparator() + "Password=");
	}
	
	public static boolean configFileExists() {
		return new File(configFilePath).exists();
	}
	
	public static boolean isDBSetup() {
		boolean result = false;
		
		try {
			 ResultSet rs = databaseDS.getConnection().getMetaData().getTables(null, null, "t%", null);
			 
			 boolean tErrorFound = false;
			 boolean tErrorTypeFound = false;
			 boolean tUserFound = false;
			 boolean tRecordingFound = false;
			 
			 while(rs.next()) {
				String currentTableName = rs.getString(3);
				
				if(currentTableName.equals("tErrorType")) {
					tErrorTypeFound = true;
				}
				if(currentTableName.equals("tError")) {
					tErrorFound = true;
				}
				if(currentTableName.equals("tUser")) {
					tUserFound = true;
				}
				if(currentTableName.equals("tRecording")) {
					tRecordingFound = true;
				}				
			 }
			 
			 result = (tErrorTypeFound && tErrorFound && tUserFound && tRecordingFound);
		 
		} catch (SQLException e) {
			return false;
		}
		
		return result;
	}

}
