/**
 * 
 */
package main;


import java.io.IOException;
import java.sql.SQLException;

import org.eclipse.ui.IStartup;

import database.DBSetup;
import database.DBUtils;
import utilities.EclipseTools;
import utilities.ProjectSetup;

/**
 * @author William
 *
 */
public class Main implements IStartup {

	@Override
	public void earlyStartup() {
		try {
			if(!DBUtils.configFileExists()) {
				DBUtils.createConfigFile();
			}
			DBSetup.buildDatabase();
			ProjectSetup.setupListeningForSyntaxEvents();
			ProjectSetup.setupListeningForRuntimeEvents();
		}
		catch(IOException | SQLException e) {
			e.printStackTrace();	
		}
	}
}
