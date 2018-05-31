package database;

import java.sql.SQLException;

import com.mysql.cj.jdbc.MysqlDataSource;

public class DBSetup {

	public static void buildDatabase() {
		/*try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		MysqlDataSource ds = new MysqlDataSource();
		ds.setDatabaseName("eclipseerrordb");
		ds.setServerName("localhost");
		ds.setUser("root");
		ds.setPassword("");
		
	}
	
	public static Object readConfigFile() {
		return new Object();
	}
	
	public static boolean isDBSetup() {
		return true;
	}

}
