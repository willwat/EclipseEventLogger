/**
 * 
 */
package main;


import java.sql.SQLException;

import org.eclipse.ui.IStartup;

import database.DBSetup;
import database.DBUtils;
import utilities.ProjectSetup;

/**
 * @author William
 *
 */
public class Main implements IStartup {

	@Override
	public void earlyStartup() {
		ProjectSetup.setupListeningForSyntaxErrors();
		ProjectSetup.setupListeningForRuntimeErrors();
		
		if(!DBUtils.configFileExists()) {
			DBUtils.createConfigFile();
		}
		else {
			DBSetup.buildDatabase();
		}
		
		try {
			DBUtils.addRecordToDB("AEEE-3FG5-444-AAAA", "Your cat is a dog", 1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
