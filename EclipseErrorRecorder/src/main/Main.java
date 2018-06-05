/**
 * 
 */
package main;

import java.sql.SQLException;

import org.eclipse.ui.IStartup;

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
		database.DBSetup.buildDatabase();
		database.DBSetup.isDBSetup();
	}

}
