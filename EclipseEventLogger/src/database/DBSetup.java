package database;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.cj.jdbc.MysqlDataSource;


public class DBSetup {
	
	private final static MysqlDataSource databaseDS = DBUtils.readConfigFileToDS();
	private static Connection DBConnection;

	public static void buildDatabase() throws SQLException {
		final boolean DBInfoBlank = databaseDS.getUser().equals("") || databaseDS.getDatabaseName().equals("") || databaseDS.getServerName().equals("");
		
		if(!databaseExists() && !DBInfoBlank) {
			createDatabase();
		}
		if(isSafeToBuildDB() && !DBInfoBlank) {
			createDBTables();
		}
		
		DBUtils.setDBConnection(DBConnection);
	}
	
	public static boolean databaseExists() throws SQLException {
		boolean result = false;
		String databaseName = databaseDS.getDatabaseName();
		databaseDS.setDatabaseName("");
		DBConnection = databaseDS.getConnection();
		databaseDS.setDatabaseName(databaseName);
		
		ResultSet databases = DBConnection.getMetaData().getCatalogs();
		int databaseColumnNum = 1;
		
		while(databases.next()) {
			if(databases.getString(databaseColumnNum).toLowerCase().equals(databaseName.toLowerCase())) {
				result = true;
				DBConnection = databaseDS.getConnection();
				break;
			}
		}
		
		return result;
	}
	
	public static void createDatabase() throws SQLException {
		String databaseName = databaseDS.getDatabaseName();
		databaseDS.setDatabaseName("");
		DBConnection = databaseDS.getConnection();
		Statement stmt = DBConnection.createStatement();
		String createDatabaseQuery = "CREATE DATABASE " + databaseName;
		stmt.executeUpdate(createDatabaseQuery);
		databaseDS.setDatabaseName(databaseName);
		DBConnection = databaseDS.getConnection();
	}
	
	public static void createDBTables() throws SQLException {
		String createRecordDWTable = "CREATE TABLE `tRecordDW` (\r\n" + 
				"  `recordDWID` int(11) NOT NULL AUTO_INCREMENT,\r\n" + 
				"  `userMACAddress` varchar(20) NOT NULL,\r\n" + 
				"  `eventMessage` varchar(500) NOT NULL,\r\n" + 
				"  `timeOfRecording` datetime(3) NOT NULL,\r\n" + 
				"  `eventType` varchar(10) NOT NULL,\r\n" + 
				"  `fileName` varchar(500) NOT NULL,\r\n" + 
				"  `problemCode` varchar(500),\r\n" + 
				"  `lineNumber` int(5),\r\n" + 
				"  PRIMARY KEY (`recordDWID`),\r\n" + 
				"  UNIQUE KEY `RecordNK` (`userMACAddress`,`timeOfRecording`,`eventMessage`)\r\n" + 
				") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;";
		
		Statement stmt = DBConnection.createStatement();
		
		stmt.execute(createRecordDWTable);
	}
	
	public static boolean isSafeToBuildDB() throws SQLException {
		
		ResultSet dbTables = DBConnection.getMetaData().getTables(databaseDS.getDatabaseName(), null, "t%", null);
		 
		boolean tRecordDWFound = false;
		 
		while(dbTables.next()) {
			String currentTableName = dbTables.getString("TABLE_NAME").toLowerCase();
			
			if(currentTableName.equals("tRecordDW".toLowerCase())) {
				tRecordDWFound = true;
				break;
			}				
		}
		return !tRecordDWFound;
	}
}
