/**
 * 
 */
package main;

import java.sql.SQLException;

import org.eclipse.ui.IStartup;

import database.DBSetup;
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
		
		if(!DBSetup.configFileExists()) {
			DBSetup.createConfigFile();
		}
		else {
			DBSetup.buildDatabase();
		}
	}

}
