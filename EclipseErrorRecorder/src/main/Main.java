/**
 * 
 */
package main;


import java.io.IOException;
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
		try {
			if(!DBUtils.configFileExists()) {
				DBUtils.createConfigFile();
			}
			else {
				DBSetup.buildDatabase();
				ProjectSetup.setupListeningForSyntaxErrors();
				ProjectSetup.setupListeningForRuntimeErrors();
			}
		}
		catch(IOException | SQLException e) {
			System.exit(0);
		}
	}
}
