package database;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.cj.jdbc.MysqlDataSource;

public class DBSetup {

	public static void buildDatabase() {

		MysqlDataSource ds = new MysqlDataSource();
		ds.setDatabaseName("eclipseerrordb");
		ds.setServerName("localhost");
		ds.setUser("root");
		ds.setPassword("");
		
		
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
	
	public static Object readConfigFile() {
		return new Object();
	}
	
	public static boolean isDBSetup() {
		boolean result = false;
		
		MysqlDataSource ds = new MysqlDataSource();
		ds.setDatabaseName("eclipseerrordb");
		ds.setServerName("localhost");
		ds.setUser("root");
		ds.setPassword("");
		
		try {
			 ResultSet rs = ds.getConnection().getMetaData().getTables(null, null, "t%", null);
			 
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
			 
			 result = (tErrorTypeFound && tErrorFound && tUserFound && tRecordingFound) ? true : false;
		 
		} catch (SQLException e) {
			return false;
		}
		
		return result;
	}

}
