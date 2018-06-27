package database;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.cj.jdbc.MysqlDataSource;


public class DBSetup {
	
	private final static MysqlDataSource databaseDS = DBUtils.readConfigFileToDS();

	public static void buildDatabase() throws SQLException {		
		if(isSafeToBuildDB() && !databaseDS.getUser().equals("") && !databaseDS.getDatabaseName().equals("") && !databaseDS.getServerName().equals("")) {
			createDBTables();
		}
	}
	
	public static boolean isSafeToBuildDB() throws SQLException {
		boolean safeToBuildDB = false;
		
		
		ResultSet dbTables = databaseDS.getConnection().getMetaData().getTables(databaseDS.getDatabaseName(), null, "t%", null);
		 
		boolean tEventFound = false;
		boolean tEventTypeFound = false;
		boolean tUserFound = false;
		boolean tRecordingFound = false;
		 
		while(dbTables.next()) {
			String currentTableName = dbTables.getString("TABLE_NAME").toLowerCase();
			
			if(currentTableName.equals("tEventType".toLowerCase())) {
				tEventTypeFound = true;
			}
			if(currentTableName.equals("tEvent".toLowerCase())) {
				tEventFound = true;
			}
			if(currentTableName.equals("tUser".toLowerCase())) {
				tUserFound = true;
			}
			if(currentTableName.equals("tRecord".toLowerCase())) {
				tRecordingFound = true;
			}				
		}
		 
		safeToBuildDB = !(tEventTypeFound || tEventFound || tUserFound || tRecordingFound);
	 
		
		return safeToBuildDB;
	}
	
	public static void createDBTables() throws SQLException {
		
		String createUserTable = "CREATE TABLE `tUser` (\r\n" + 
				" `UserID` int(11) NOT NULL AUTO_INCREMENT,\r\n" + 
				" `MacAddress` varchar(30) NOT NULL,\r\n" + 
				" PRIMARY KEY (`UserID`),\r\n" + 
				" UNIQUE KEY `MacAddress` (`MacAddress`)\r\n" + 
				") ENGINE=InnoDB DEFAULT CHARSET=latin1";
		
		String createEventTypeTable = "CREATE TABLE `tEventType` (\r\n" + 
				" `EventTypeID` int(11) NOT NULL AUTO_INCREMENT,\r\n" + 
				" `EventType` varchar(20) NOT NULL,\r\n" + 
				" PRIMARY KEY (`EventTypeID`),\r\n" + 
				" UNIQUE KEY `EventType` (`EventType`)\r\n" + 
				") ENGINE=InnoDB DEFAULT CHARSET=latin1\r\n" + 
				"";
		String addEventTypes = "INSERT INTO tEventType (EventType)\r\n" + 
				"VALUES ('Runtime'), ('Syntax');";
		
		String createEventTable = "CREATE TABLE `tEvent` (\r\n" + 
				" `EventID` int(11) NOT NULL AUTO_INCREMENT,\r\n" + 
				" `Message` varchar(60) NOT NULL,\r\n" + 
				" `EventTypeID` int(11) NOT NULL,\r\n" + 
				" PRIMARY KEY (`EventID`),\r\n" + 
				" UNIQUE KEY `EventNK` (`Message`,`EventTypeID`) USING BTREE,\r\n" + 
				" KEY `EventTypeID_FK` (`EventTypeID`),\r\n" + 
				" CONSTRAINT `EventTypeID_FK` FOREIGN KEY (`EventTypeID`) REFERENCES `teventtype` (`EventTypeID`) ON DELETE CASCADE ON UPDATE CASCADE\r\n" + 
				") ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1";
		
		String createRecordsTable = "CREATE TABLE `tRecord` (\r\n" + 
				" `RecordID` int(11) NOT NULL AUTO_INCREMENT,\r\n" + 
				" `UserID` int(11) NOT NULL,\r\n" + 
				" `EventID` int(11) NOT NULL,\r\n" + 
				" `TimeOfRecording` TIMESTAMP NOT NULL,\r\n" +
				" PRIMARY KEY (`RecordID`),\r\n" + 
				" KEY `UserID_FK` (`UserID`),\r\n" + 
				" KEY `EventID_FK` (`EventID`),\r\n" + 
				" CONSTRAINT `EventID_FK` FOREIGN KEY (`EventID`) REFERENCES `tevent` (`EventID`) ON DELETE CASCADE ON UPDATE CASCADE,\r\n" + 
				" CONSTRAINT `UserID_FK` FOREIGN KEY (`UserID`) REFERENCES `tuser` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE\r\n" + 
				") ENGINE=InnoDB DEFAULT CHARSET=latin1";
		
		Statement stmt = databaseDS.getConnection().createStatement();
		
		stmt.execute(createEventTypeTable);
		stmt.execute(addEventTypes);
		stmt.execute(createUserTable);
		stmt.execute(createEventTable);
		stmt.execute(createRecordsTable);
	}

}
