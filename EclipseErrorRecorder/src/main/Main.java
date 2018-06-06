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
		DBSetup.buildDatabase();
		DBSetup.isDBSetup();
		
		if(!DBSetup.configFileExists()) {
			DBSetup.createConfigFile();
		}
		
		DBSetup.buildDatabase();
		
	}

}
